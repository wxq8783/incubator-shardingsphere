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

package org.apache.shardingsphere.cluster.heartbeat.response;

import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public final class HeartbeatResponseTest {
    
    @Test
    public void getHeartbeatResultMap() {
        Map<String, Collection<HeartbeatResult>> heartbeatResultMap = new HashMap<>();
        heartbeatResultMap.put("sharding_db", Collections.singleton(new HeartbeatResult("ds_1", true, 123L, Boolean.FALSE)));
        HeartbeatResponse heartbeatResponse = new HeartbeatResponse(heartbeatResultMap);
        assertThat(heartbeatResponse.getHeartbeatResultMap(), is(heartbeatResultMap));
        assertTrue(heartbeatResponse.getHeartbeatResultMap().keySet().contains("sharding_db"));
        assertThat(heartbeatResponse.getHeartbeatResultMap().get("sharding_db").size(), is(1));
        HeartbeatResult heartbeatResult = heartbeatResultMap.values().iterator().next().stream().findFirst().get();
        assertNotNull(heartbeatResult);
        assertThat(heartbeatResult.getDataSourceName(), is("ds_1"));
        assertTrue(heartbeatResult.getEnable());
        assertThat(heartbeatResult.getDetectTimeStamp(), is(123L));
    }
}
