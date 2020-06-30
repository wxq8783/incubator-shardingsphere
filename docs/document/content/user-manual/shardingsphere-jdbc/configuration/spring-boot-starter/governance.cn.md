+++
title = "分布式治理"
weight = 6
+++

## 配置项说明

### 治理

```properties
spring.shardingsphere.orchestration.spring_boot_ds_sharding.orchestration-type= #治理类型，多个类型用逗号分隔，例如 config_center, registry_center, metadata_center
spring.shardingsphere.orchestration.spring_boot_ds_sharding.instance-type= #治理实例类型。如：zookeeper, etcd, apollo, nacos
spring.shardingsphere.orchestration.spring_boot_ds_sharding.server-lists= #治理服务列表。包括 IP 地址和端口号。多个地址用逗号分隔。如: host1:2181,host2:2181
spring.shardingsphere.orchestration.spring_boot_ds_sharding.namespace= #治理命名空间
spring.shardingsphere.orchestration.spring_boot_ds_sharding.props.overwrite= #本地配置是否覆盖配置中心配置。如果可覆盖，每次启动都以本地配置为准
```

### 集群管理

```properties
spring.shardingsphere.cluster.heartbeat.sql= #心跳检测 SQL
spring.shardingsphere.cluster.heartbeat.interval= #心跳检测间隔时间 (s)
spring.shardingsphere.cluster.heartbeat.threadCount= #心跳检测线程池大小
spring.shardingsphere.cluster.heartbeat.retryEnable= #是否支持失败重试，可设置 true 或 false
spring.shardingsphere.cluster.heartbeat.retryInterval= #重试间隔时间 (s)
spring.shardingsphere.cluster.heartbeat.retryMaximum= #最大重试次数
```
