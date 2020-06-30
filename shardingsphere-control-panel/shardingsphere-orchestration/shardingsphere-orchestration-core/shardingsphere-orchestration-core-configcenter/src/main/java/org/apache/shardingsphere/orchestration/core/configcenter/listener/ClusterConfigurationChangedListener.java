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

package org.apache.shardingsphere.orchestration.core.configcenter.listener;

import org.apache.shardingsphere.cluster.configuration.swapper.ClusterConfigurationYamlSwapper;
import org.apache.shardingsphere.cluster.configuration.yaml.YamlClusterConfiguration;
import org.apache.shardingsphere.infra.yaml.engine.YamlEngine;
import org.apache.shardingsphere.orchestration.center.ConfigCenterRepository;
import org.apache.shardingsphere.orchestration.center.listener.DataChangedEvent;
import org.apache.shardingsphere.orchestration.core.common.event.ClusterConfigurationChangedEvent;
import org.apache.shardingsphere.orchestration.core.common.listener.PostShardingCenterRepositoryEventListener;
import org.apache.shardingsphere.orchestration.core.configcenter.ConfigCenterNode;

import java.util.Collections;

/**
 * Cluster configuration changed listener.
 */
public final class ClusterConfigurationChangedListener extends PostShardingCenterRepositoryEventListener {
    
    public ClusterConfigurationChangedListener(final String name, final ConfigCenterRepository configCenterRepository) {
        super(configCenterRepository, Collections.singletonList(new ConfigCenterNode(name).getClusterPath()));
    }
    
    @Override
    protected ClusterConfigurationChangedEvent createShardingOrchestrationEvent(final DataChangedEvent event) {
        return new ClusterConfigurationChangedEvent(new ClusterConfigurationYamlSwapper()
                .swapToObject(YamlEngine.unmarshal(event.getValue(), YamlClusterConfiguration.class)));
    }
}
