+++
title = "使用 Java API"
weight = 1
+++

## 引入 Maven 依赖

```xml
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-jdbc-orchestration</artifactId>
    <version>${shardingsphere.version}</version>
</dependency>

<!-- 使用 ZooKeeper 时，需要引入此模块 -->
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-orchestration-center-zookeeper-curator</artifactId>
    <version>${shardingsphere.version}</version>
</dependency>

<!-- 使用 Etcd 时，需要引入此模块 -->
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-orchestration-center-etcd</artifactId>
    <version>${shardingsphere.version}</version>
</dependency>
```

## 规则配置

以下示例将 ZooKeeper 作为配置中心和注册中心。

```java
// 省略配置数据源以及规则
// ...

// 配置配置/注册/元数据中心
Properties props = new Properties();
props.setProperty("overwrite", overwrite);
CenterConfiguration centerConfiguration = new CenterConfiguration("zookeeper", props);
centerConfiguration.setServerLists("localhost:2181");
centerConfiguration.setNamespace("shardingsphere-orchestration");
centerConfiguration.setOrchestrationType("registry_center,config_center,metadata_center");

// 配置治理
Map<String, CenterConfiguration> instanceConfigurationMap = new HashMap<String, CenterConfiguration>();
instanceConfigurationMap.put("orchestration-shardingsphere-data-source", centerConfiguration);

// 创建 OrchestrationShardingSphereDataSource
DataSource dataSource = OrchestrationShardingSphereDataSourceFactory.createDataSource(
        createDataSourceMap(), createShardingRuleConfig(), new HashMap<String, Object>(), new Properties(), new OrchestrationConfiguration(instanceConfigurationMap));
```

## 使用 OrchestrationShardingSphereDataSource

通过 OrchestrationShardingSphereDataSourceFactory 工厂创建的 OrchestrationShardingSphereDataSource 实现自 JDBC 的标准接口 DataSource。
可通过 DataSource 选择使用原生 JDBC，或JPA， MyBatis 等 ORM 框架。

以原生 JDBC 使用方式为例：

```java
DataSource dataSource = OrchestrationShardingSphereDataSourceFactory.createDataSource(
        createDataSourceMap(), createShardingRuleConfig(), new HashMap<String, Object>(), new Properties(), new OrchestrationConfiguration(instanceConfigurationMap));
String sql = "SELECT i.* FROM t_order o JOIN t_order_item i ON o.order_id=i.order_id WHERE o.user_id=? AND o.order_id=?";
try (
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
    ps.setInt(1, 10);
    ps.setInt(2, 1000);
    try (ResultSet rs = preparedStatement.executeQuery()) {
        while(rs.next()) {
            // ...
        }
    }
}
```
