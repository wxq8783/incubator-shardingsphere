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

package org.apache.shardingsphere.proxy.backend.util;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.infra.config.DataSourceConfiguration;
import org.apache.shardingsphere.kernel.context.schema.DataSourceParameter;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Data source parameter converter.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataSourceConverter {
    
    /**
     * Get data source map.
     *
     * @param dataSourceConfigurationMap data source configuration map
     * @return data source parameter map
     */
    public static Map<String, DataSourceParameter> getDataSourceParameterMap(final Map<String, DataSourceConfiguration> dataSourceConfigurationMap) {
        return dataSourceConfigurationMap.entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey, entry -> createDataSourceParameter(entry.getValue()), (oldVal, currVal) -> oldVal, LinkedHashMap::new));
    }
    
    private static DataSourceParameter createDataSourceParameter(final DataSourceConfiguration dataSourceConfiguration) {
        bindAlias(dataSourceConfiguration);
        DataSourceParameter result = new DataSourceParameter();
        for (Field each : result.getClass().getDeclaredFields()) {
            try {
                each.setAccessible(true);
                if (dataSourceConfiguration.getProps().containsKey(each.getName())) {
                    each.set(result, dataSourceConfiguration.getProps().get(each.getName()));
                }
            } catch (final ReflectiveOperationException ignored) {
            }
        }
        return result;
    }

    private static void bindAlias(final DataSourceConfiguration dataSourceConfiguration) {
        dataSourceConfiguration.addAlias("url", "jdbcUrl");
        dataSourceConfiguration.addAlias("user", "username");
        dataSourceConfiguration.addAlias("connectionTimeout", "connectionTimeoutMilliseconds");
        dataSourceConfiguration.addAlias("maxLifetime", "maxLifetimeMilliseconds");
        dataSourceConfiguration.addAlias("idleTimeout", "idleTimeoutMilliseconds");
    }
    
    /**
     * Get data source configuration map.
     *
     * @param dataSourceParameterMap data source map
     * @return data source configuration map
     */
    public static Map<String, DataSourceConfiguration> getDataSourceConfigurationMap(final Map<String, DataSourceParameter> dataSourceParameterMap) {
        return dataSourceParameterMap.entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey, entry -> createDataSourceConfiguration(entry.getValue()), (oldVal, currVal) -> oldVal, LinkedHashMap::new));
    }
    
    private static DataSourceConfiguration createDataSourceConfiguration(final DataSourceParameter dataSourceParameter) {
        DataSourceConfiguration result = new DataSourceConfiguration(HikariDataSource.class.getName());
        result.getProps().put("jdbcUrl", dataSourceParameter.getUrl());
        result.getProps().put("username", dataSourceParameter.getUsername());
        result.getProps().put("password", dataSourceParameter.getPassword());
        result.getProps().put("connectionTimeout", dataSourceParameter.getConnectionTimeoutMilliseconds());
        result.getProps().put("idleTimeout", dataSourceParameter.getIdleTimeoutMilliseconds());
        result.getProps().put("maxLifetime", dataSourceParameter.getMaxLifetimeMilliseconds());
        result.getProps().put("maxPoolSize", dataSourceParameter.getMaxPoolSize());
        result.getProps().put("minPoolSize", dataSourceParameter.getMinPoolSize());
        result.getProps().put("maintenanceIntervalMilliseconds", dataSourceParameter.getMaintenanceIntervalMilliseconds());
        result.getProps().put("readOnly", dataSourceParameter.isReadOnly());
        return result;
    }
}
