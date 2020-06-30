+++
title = "Use Spring Boot Starter"
weight = 3
+++

## Import Maven Dependency

```xml
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-jdbc-orchestration-spring-boot-starter</artifactId>
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

```properties
spring.shardingsphere.orchestration.spring_boot_ds.orchestration-type=registry_center,config_center,metadata_center
spring.shardingsphere.orchestration.spring_boot_ds.instance-type=zookeeper
spring.shardingsphere.orchestration.spring_boot_ds.server-lists=localhost:2181
spring.shardingsphere.orchestration.spring_boot_ds.namespace=orchestration-spring-boot-shardingsphere-test
spring.shardingsphere.orchestration.spring_boot_ds.properties.overwrite=true
```

## Use OrchestrationShardingSphereDataSource in Spring

OrchestrationShardingSphereDataSource can be used directly by injection; 
or configure OrchestrationShardingSphereDataSource in ORM frameworks such as JPA or MyBatis.

```java
@Resource
private DataSource dataSource;
```
