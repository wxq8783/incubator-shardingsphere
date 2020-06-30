+++
title = "Use Spring Namespace"
weight = 4
+++

## Import Maven Dependency

```xml
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-jdbc-orchestration-spring-namespace</artifactId>
    <version>${shardingsphere.version}</version>
</dependency>

<!-- import if using ZooKeeper -->
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-orchestration-center-zookeeper-curator</artifactId>
    <version>${shardingsphere.version}</version>
</dependency>

<!-- import if using Etcd -->
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-orchestration-center-etcd</artifactId>
    <version>${shardingsphere.version}</version>
</dependency>
```

## Configure Rule

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:orchestration="http://shardingsphere.apache.org/schema/shardingsphere/orchestration"
       xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://shardingsphere.apache.org/schema/shardingsphere/orchestration
                           http://shardingsphere.apache.org/schema/shardingsphere/orchestration/orchestration.xsd">
     <util:properties id="instance-properties">
         <prop key="max-retries">3</prop>
         <prop key="operation-timeout-milliseconds">3000</prop>
     </util:properties>
     <orchestration:instance id="regCenter" orchestration-type="registry_center,config_center,metadata_center" instance-type="zookeeper" server-lists="localhost:2181" namespace="orchestration-spring-namespace-demo" properties-ref="instance-properties" />
     <orchestration:data-source id="shardingDatabasesTablesDataSource" data-source-ref="realShardingDatabasesTablesDataSource" instance-ref="regCenter" overwrite="true" />
     <orchestration:slave-data-source id="masterSlaveDataSource" data-source-ref="realMasterSlaveDataSource" instance-ref="regCenter" overwrite="true" />
     <orchestration:data-source id="encryptDataSource" data-source-ref="realEncryptDataSource" instance-ref="regCenter" overwrite="true" />
</beans>
```

## Use OrchestrationShardingSphereDataSource in Spring

OrchestrationShardingSphereDataSource can be used directly by injection; 
or configure OrchestrationShardingSphereDataSource in ORM frameworks such as JPA or MyBatis.

```java
@Resource
private DataSource dataSource;
```
