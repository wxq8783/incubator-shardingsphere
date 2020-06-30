/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.cluster.heartbeat.detect;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.cluster.configuration.config.HeartbeatConfiguration;
import org.apache.shardingsphere.cluster.heartbeat.response.HeartbeatResponse;
import org.apache.shardingsphere.cluster.heartbeat.response.HeartbeatResult;
import org.apache.shardingsphere.kernel.context.SchemaContext;
import org.apache.shardingsphere.orchestration.core.facade.ShardingOrchestrationFacade;
import org.apache.shardingsphere.orchestration.core.registrycenter.RegistryCenterNodeStatus;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;


/**
 * Heartbeat handler.
 */
@Slf4j
public final class HeartbeatHandler {
    
    private static final int FUTURE_GET_TIME_OUT_MILLISECONDS = 5000;
    
    private HeartbeatConfiguration configuration;
    
    private Collection<String> disabledDataSources = Collections.emptyList();
    
    private ExecutorService executorService;
    
    /**
     * Init heartbeat handler.
     *
     * @param configuration heartbeat configuration
     */
    public void init(final HeartbeatConfiguration configuration) {
        Preconditions.checkNotNull(configuration, "heartbeat configuration can not be null.");
        this.configuration = configuration;
    }
    
    /**
     * Get heartbeat handler instance.
     *
     * @return heartbeat handler instance
     */
    public static HeartbeatHandler getInstance() {
        return HeartbeatHandlerHolder.INSTANCE;
    }
    
    /**
     * Handle heartbeat detect event.
     *
     * @param schemaContexts schema contexts
     * @param disabledDataSources collection of disabled data sources
     * @return heartbeat response
     */
    public HeartbeatResponse handle(final Map<String, SchemaContext> schemaContexts, final Collection<String> disabledDataSources) {
        this.disabledDataSources = disabledDataSources;
        executorService = Executors.newFixedThreadPool(configuration.getThreadCount());
        List<Future<Map<String, HeartbeatResult>>> futureTasks = new ArrayList<>();
        schemaContexts.forEach((key, value) -> value.getSchema().getDataSources().forEach((innerKey, innerValue) -> {
            futureTasks.add(executorService.submit(new HeartbeatDetect(key, innerKey, innerValue, configuration, isDisabled(key, innerKey))));
        }));
        HeartbeatResponse heartbeatResponse = buildHeartbeatResponse(futureTasks);
        closeExecutor(executorService);
        return heartbeatResponse;
    }
    
    /**
     * Close heartbeat handler.
     */
    public void close() {
        closeExecutor(executorService);
    }
    
    private HeartbeatResponse buildHeartbeatResponse(final List<Future<Map<String, HeartbeatResult>>> futureTasks) {
        Map<String, Collection<HeartbeatResult>> heartbeatResultMap = futureTasks.stream().map(e -> {
            try {
                return e.get(FUTURE_GET_TIME_OUT_MILLISECONDS, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                log.error("Heartbeat report error", ex);
                return new HashMap<String, HeartbeatResult>();
            }
        }).flatMap(map -> map.entrySet().stream()).collect(Collectors.groupingBy(Map.Entry::getKey, HashMap::new, Collectors.mapping(Map.Entry::getValue, Collectors.toCollection(ArrayList::new))));
        return new HeartbeatResponse(heartbeatResultMap);
    }
    
    private void closeExecutor(final ExecutorService executorService) {
        if (null != executorService && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
    
    private boolean isDisabled(final String schemaName, final String dataSourceName) {
        return disabledDataSources.isEmpty() ? Boolean.FALSE : isDisabled(Joiner.on(".").join(schemaName, dataSourceName));
    }
    
    private boolean isDisabled(final String schemaDataSourceName) {
        return disabledDataSources.contains(schemaDataSourceName) && RegistryCenterNodeStatus.DISABLED.toString()
                .equals(ShardingOrchestrationFacade.getInstance().getRegistryCenter().getDataSourcesNodeData(schemaDataSourceName));
    }
    
    private static final class HeartbeatHandlerHolder {
        
        public static final HeartbeatHandler INSTANCE = new HeartbeatHandler();
    }
}
