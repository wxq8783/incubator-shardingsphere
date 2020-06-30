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

package org.apache.shardingsphere.cluster.configuration.swapper;

import org.apache.shardingsphere.cluster.configuration.config.ClusterConfiguration;
import org.apache.shardingsphere.cluster.configuration.config.HeartbeatConfiguration;
import org.apache.shardingsphere.cluster.configuration.yaml.YamlClusterConfiguration;
import org.apache.shardingsphere.cluster.configuration.yaml.YamlHeartbeatConfiguration;
import org.apache.shardingsphere.infra.yaml.swapper.YamlSwapper;

/**
 * Cluster configuration YAML swapper.
 */
public final class ClusterConfigurationYamlSwapper implements YamlSwapper<YamlClusterConfiguration, ClusterConfiguration> {
    
    @Override
    public YamlClusterConfiguration swapToYamlConfiguration(final ClusterConfiguration clusterConfiguration) {
        final YamlClusterConfiguration yamlClusterConfiguration = new YamlClusterConfiguration();
        final YamlHeartbeatConfiguration yamlHeartBeatConfiguration = new YamlHeartbeatConfiguration();
        HeartbeatConfiguration heartbeat = clusterConfiguration.getHeartbeat();
        yamlHeartBeatConfiguration.setSql(heartbeat.getSql());
        yamlHeartBeatConfiguration.setInterval(heartbeat.getInterval());
        yamlHeartBeatConfiguration.setRetryEnable(heartbeat.getRetryEnable());
        yamlHeartBeatConfiguration.setRetryMaximum(heartbeat.getRetryMaximum());
        yamlHeartBeatConfiguration.setRetryInterval(heartbeat.getRetryInterval());
        yamlHeartBeatConfiguration.setThreadCount(heartbeat.getThreadCount());
        yamlClusterConfiguration.setHeartbeat(yamlHeartBeatConfiguration);
        return yamlClusterConfiguration;
    }
    
    @Override
    public ClusterConfiguration swapToObject(final YamlClusterConfiguration yamlConfiguration) {
        final ClusterConfiguration clusterConfiguration = new ClusterConfiguration();
        final HeartbeatConfiguration heartBeatConfiguration = new HeartbeatConfiguration();
        YamlHeartbeatConfiguration heartbeat = yamlConfiguration.getHeartbeat();
        heartBeatConfiguration.setSql(heartbeat.getSql());
        heartBeatConfiguration.setInterval(heartbeat.getInterval());
        heartBeatConfiguration.setRetryEnable(heartbeat.getRetryEnable());
        heartBeatConfiguration.setRetryMaximum(heartbeat.getRetryMaximum());
        heartBeatConfiguration.setRetryInterval(heartbeat.getRetryInterval());
        heartBeatConfiguration.setThreadCount(null == heartbeat.getThreadCount() ? Runtime.getRuntime().availableProcessors() << 1 : heartbeat.getThreadCount());
        clusterConfiguration.setHeartbeat(heartBeatConfiguration);
        return clusterConfiguration;
    }
}
