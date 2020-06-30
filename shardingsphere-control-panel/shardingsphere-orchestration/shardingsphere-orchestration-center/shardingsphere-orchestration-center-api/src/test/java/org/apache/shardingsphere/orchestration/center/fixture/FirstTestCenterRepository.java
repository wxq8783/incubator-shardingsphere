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

package org.apache.shardingsphere.orchestration.center.fixture;

import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.orchestration.center.ConfigCenterRepository;
import org.apache.shardingsphere.orchestration.center.config.CenterConfiguration;
import org.apache.shardingsphere.orchestration.center.listener.DataChangedEvent;
import org.apache.shardingsphere.orchestration.center.listener.DataChangedEventListener;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Getter
@Setter
public final class FirstTestCenterRepository implements ConfigCenterRepository {
    
    private Properties props = new Properties();
    
    @Override
    public void init(final CenterConfiguration config) {
    }
    
    @Override
    public String get(final String key) {
        return "";
    }
    
    @Override
    public List<String> getChildrenKeys(final String key) {
        return Collections.emptyList();
    }
    
    @Override
    public void persist(final String key, final String value) {
    }
    
    @Override
    public void watch(final String key, final DataChangedEventListener dataChangedEventListener) {
        DataChangedEvent.ChangedType type = DataChangedEvent.ChangedType.valueOf(key);
        DataChangedEvent event = new DataChangedEvent(type.name(), type.name(), type);
        dataChangedEventListener.onChange(event);
    }
    
    @Override
    public void delete(final String key) {
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public String getType() {
        return "FirstTestConfigCenter";
    }
}
