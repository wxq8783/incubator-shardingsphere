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

package org.apache.shardingsphere.encrypt.spring.boot;

import javax.annotation.Resource;
import org.apache.shardingsphere.encrypt.spring.boot.fixture.TestEncryptAlgorithm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EncryptSpringBootAlgorithmProviderBeanTest.class)
@SpringBootApplication
@ActiveProfiles("encrypt")
public class EncryptSpringBootAlgorithmProviderBeanTest {
    
    @Resource
    private ApplicationContext applicationContext;
    
    @Test
    public void assertAlgorithmProviderBean() {
        Object algorithmBean = applicationContext.getBean("aes_encryptor");
        assertThat(algorithmBean.getClass().getName(), is(TestEncryptAlgorithm.class.getName()));
        TestEncryptAlgorithm algorithmFixture = (TestEncryptAlgorithm) algorithmBean;
        assertNotNull(algorithmFixture.getEnvironment());
    }
}
