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

package org.apache.shardingsphere.spring.namespace.orchestration.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Cluster bean definition tag.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClusterBeanDefinitionTag {
    
    public static final String ROOT_TAG = "heartbeat";
    
    public static final String SQL_TAG = "sql";
    
    public static final String THREAD_COUNT_TAG = "threadCount";
    
    public static final String INTERVAL_TAG = "interval";
    
    public static final String RETRY_MAXIMUM_TAG = "retryMaximum";
    
    public static final String RETRY_INTERVAL_TAG = "retryInterval";
    
    public static final String RETRY_ENABLE_TAG = "retryEnable";
}
