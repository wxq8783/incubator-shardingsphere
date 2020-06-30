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

package org.apache.shardingsphere.sharding.spring.boot;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import org.apache.shardingsphere.infra.config.RuleConfiguration;
import org.apache.shardingsphere.sharding.algorithm.config.AlgorithmProvidedShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.spi.KeyGenerateAlgorithm;
import org.apache.shardingsphere.sharding.spi.ShardingAlgorithm;
import org.apache.shardingsphere.sharding.spring.boot.condition.ShardingSpringBootCondition;
import org.apache.shardingsphere.sharding.yaml.config.YamlShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.yaml.swapper.ShardingRuleAlgorithmProviderConfigurationYamlSwapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Sharding rule configuration for spring boot.
 */
@Configuration
@ConditionalOnClass(YamlShardingRuleConfiguration.class)
@Conditional(ShardingSpringBootCondition.class)
public class ShardingRuleSpringBootConfiguration {
    
    private final ShardingRuleAlgorithmProviderConfigurationYamlSwapper swapper = new ShardingRuleAlgorithmProviderConfigurationYamlSwapper();
    
    /**
     * Sharding YAML rule spring boot configuration.
     *
     * @return YAML rule configuration
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.shardingsphere.rules.sharding")
    public YamlShardingRuleConfiguration shardingConfig() {
        return new YamlShardingRuleConfiguration();
    }
    
    /**
     * Sharding rule configuration.
     *
     * @param yamlShardingRuleConfiguration YAML sharding rule configuration
     * @param shardingAlgorithmProvider sharding algorithm provider
     * @param keyGenerateAlgorithmProvider key generate algorithm provider
     * @return the rule configuration
     */
    @Bean
    public RuleConfiguration shardingRuleConfiguration(final YamlShardingRuleConfiguration yamlShardingRuleConfiguration,
                                                       final ObjectProvider<Map<String, ShardingAlgorithm>> shardingAlgorithmProvider,
                                                       final ObjectProvider<Map<String, KeyGenerateAlgorithm>> keyGenerateAlgorithmProvider) {
        Map<String, ShardingAlgorithm> shardingAlgorithmMap = Optional.ofNullable(shardingAlgorithmProvider.getIfAvailable()).orElse(Collections.emptyMap());
        Map<String, KeyGenerateAlgorithm> keyGenerateAlgorithmMap = Optional.ofNullable(keyGenerateAlgorithmProvider.getIfAvailable()).orElse(Collections.emptyMap());
        AlgorithmProvidedShardingRuleConfiguration ruleConfiguration = swapper.swapToObject(yamlShardingRuleConfiguration);
        ruleConfiguration.setShardingAlgorithms(shardingAlgorithmMap);
        ruleConfiguration.setKeyGenerators(keyGenerateAlgorithmMap);
        return ruleConfiguration;
    }
    
    /**
     * Sharding algorithm provided bean registry.
     *
     * @param environment environment
     * @return sharding algorithm provided bean registry
     */
    @Bean
    public static ShardingAlgorithmProvidedBeanRegistry shardingAlgorithmProvidedBeanRegistry(final Environment environment) {
        return new ShardingAlgorithmProvidedBeanRegistry(environment);
    }
}
