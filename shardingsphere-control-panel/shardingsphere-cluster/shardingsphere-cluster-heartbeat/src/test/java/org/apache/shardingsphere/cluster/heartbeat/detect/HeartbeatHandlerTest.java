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

import lombok.SneakyThrows;
import org.apache.shardingsphere.cluster.configuration.config.HeartbeatConfiguration;
import org.apache.shardingsphere.cluster.heartbeat.response.HeartbeatResponse;
import org.apache.shardingsphere.cluster.heartbeat.response.HeartbeatResult;
import org.apache.shardingsphere.kernel.context.SchemaContext;
import org.apache.shardingsphere.kernel.context.schema.ShardingSphereSchema;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;

public final class HeartbeatHandlerTest {
    
    private static final String DETECT_SQL = "select 1";
    
    private static final String SCHEMA_NAME = "sharding_db";
    
    private static final String DATA_SOURCE_0 = "ds_0";
    
    private static final String DATA_SOURCE_1 = "ds_1";
    
    private static Boolean enableExecuteQuery;
    
    private static Boolean multipleDataSource;
    
    private HeartbeatHandler handler;
    
    @Before
    public void init() {
        handler = HeartbeatHandler.getInstance();
        enableExecuteQuery = Boolean.TRUE;
        multipleDataSource = Boolean.FALSE;
    }
    
    @Test
    public void assertHandleWithoutRetry() {
        handler.init(getHeartbeatConfiguration(Boolean.FALSE));
        HeartbeatResponse response = handler.handle(getSchemaContext(), Collections.emptyList());
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getHeartbeatResultMap());
        assertTrue(response.getHeartbeatResultMap().keySet().contains(SCHEMA_NAME));
        assertThat(response.getHeartbeatResultMap().get(SCHEMA_NAME).size(), is(1));
        HeartbeatResult heartbeatResult = response.getHeartbeatResultMap().get(SCHEMA_NAME).iterator().next();
        assertNotNull(heartbeatResult);
        assertThat(heartbeatResult.getDataSourceName(), is(DATA_SOURCE_0));
        assertTrue(heartbeatResult.getEnable());
    }
    
    @Test
    public void assertHandleWhenDetectExceptionWithoutRetry() {
        enableExecuteQuery = Boolean.FALSE;
        handler.init(getHeartbeatConfiguration(Boolean.FALSE));
        HeartbeatResponse response = handler.handle(getSchemaContext(), Collections.emptyList());
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getHeartbeatResultMap());
        assertTrue(response.getHeartbeatResultMap().keySet().contains(SCHEMA_NAME));
        assertThat(response.getHeartbeatResultMap().get(SCHEMA_NAME).size(), is(1));
        HeartbeatResult heartbeatResult = response.getHeartbeatResultMap().get(SCHEMA_NAME).iterator().next();
        assertNotNull(heartbeatResult);
        assertThat(heartbeatResult.getDataSourceName(), is(DATA_SOURCE_0));
        assertFalse(heartbeatResult.getEnable());
    }
    
    @Test
    public void assertHandleWithRetry() {
        handler.init(getHeartbeatConfiguration(Boolean.TRUE));
        HeartbeatResponse response = handler.handle(getSchemaContext(), Collections.emptyList());
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getHeartbeatResultMap());
        assertTrue(response.getHeartbeatResultMap().keySet().contains(SCHEMA_NAME));
        assertThat(response.getHeartbeatResultMap().get(SCHEMA_NAME).size(), is(1));
        HeartbeatResult heartbeatResult = response.getHeartbeatResultMap().get(SCHEMA_NAME).iterator().next();
        assertNotNull(heartbeatResult);
        assertThat(heartbeatResult.getDataSourceName(), is(DATA_SOURCE_0));
        assertTrue(heartbeatResult.getEnable());
    }
    
    @Test
    public void assertHandleWhenDetectExceptionWithRetry() {
        enableExecuteQuery = Boolean.FALSE;
        handler.init(getHeartbeatConfiguration(Boolean.TRUE));
        HeartbeatResponse response = handler.handle(getSchemaContext(), Collections.emptyList());
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getHeartbeatResultMap());
        assertTrue(response.getHeartbeatResultMap().keySet().contains(SCHEMA_NAME));
        assertThat(response.getHeartbeatResultMap().get(SCHEMA_NAME).size(), is(1));
        HeartbeatResult heartbeatResult = response.getHeartbeatResultMap().get(SCHEMA_NAME).iterator().next();
        assertNotNull(heartbeatResult);
        assertThat(heartbeatResult.getDataSourceName(), is(DATA_SOURCE_0));
        assertFalse(heartbeatResult.getEnable());
    }
    
    @Test
    public void assertMultipleDataSource() {
        multipleDataSource = Boolean.TRUE;
        handler.init(getHeartbeatConfiguration(Boolean.FALSE));
        HeartbeatResponse response = handler.handle(getSchemaContext(), Collections.emptyList());
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getHeartbeatResultMap());
        assertTrue(response.getHeartbeatResultMap().keySet().contains(SCHEMA_NAME));
        assertThat(response.getHeartbeatResultMap().get(SCHEMA_NAME).size(), is(2));
        assertTrue(response.getHeartbeatResultMap().get(SCHEMA_NAME).stream().map(each -> each.getDataSourceName())
                .collect(Collectors.toList()).containsAll(Arrays.asList(DATA_SOURCE_0, DATA_SOURCE_1)));
        response.getHeartbeatResultMap().get(SCHEMA_NAME).iterator().forEachRemaining(each -> assertTrue(each.getEnable()));
    }
    
    private HeartbeatConfiguration getHeartbeatConfiguration(final Boolean retry) {
        HeartbeatConfiguration configuration = new HeartbeatConfiguration();
        configuration.setSql(DETECT_SQL);
        configuration.setThreadCount(50);
        configuration.setInterval(10);
        configuration.setRetryEnable(retry);
        configuration.setRetryMaximum(3);
        configuration.setRetryInterval(1);
        return configuration;
    }
    
    private Map<String, SchemaContext> getSchemaContext() {
        Map<String, SchemaContext> schemaContexts = new HashMap<>();
        SchemaContext schemaContext = mock(SchemaContext.class);
        ShardingSphereSchema schema = mock(ShardingSphereSchema.class);
        when(schemaContext.getSchema()).thenReturn(schema);
        Map<String, DataSource> dataSources = getDataSources();
        when(schema.getDataSources()).thenReturn(dataSources);
        schemaContexts.put(SCHEMA_NAME, schemaContext);
        return schemaContexts;
    }
    
    private Map<String, DataSource> getDataSources() {
        Map<String, DataSource> dataSources = new HashMap<>();
        dataSources.put(DATA_SOURCE_0, getDataSource());
        if (multipleDataSource) {
            dataSources.put(DATA_SOURCE_1, getDataSource());
        }
        return dataSources;
    }
    
    @SneakyThrows(SQLException.class)
    private DataSource getDataSource() {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = getStatement();
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(DETECT_SQL)).thenReturn(preparedStatement);
        return dataSource;
    }
    
    @SneakyThrows(SQLException.class)
    private PreparedStatement getStatement() {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        if (enableExecuteQuery) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
        } else {
            doThrow(SQLException.class).when(preparedStatement).executeQuery();
        }
        return preparedStatement;
    }
}
