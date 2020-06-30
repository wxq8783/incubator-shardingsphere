+++
title = "分布式治理"
weight = 6
+++

## 配置项说明

### 治理

*配置入口*

类名称：org.apache.shardingsphere.orchestration.center.config.OrchestrationConfiguration

可配置属性：

| *名称*                    | *数据类型*                           | *说明*                                                            |
| ------------------------ | ----------------------------------- | ----------------------------------------------------------------- |
| instanceConfigurationMap | Map\<String, CenterConfiguration\>  | 治理实例的配置 map，key 为名称，value 为治理实例 |

*治理实例配置*

类名称：org.apache.shardingsphere.orchestration.center.config.CenterConfiguration

可配置属性：

| *名称*             | *数据类型* | *说明*                                                                                         |
| ----------------- | ---------- | --------------------------------------------------------------------------------------------- |
| orchestrationType | String     | 治理类型，多个类型用逗号分隔，如 config_center,registry_center,metadata_center                |
| instanceType      | String     | 治理实例类型，如：zookeeper, etcd, apollo, nacos                                 |
| serverLists       | String     | 治理服务列表，包括 IP 地址和端口号，多个地址用逗号分隔，如: host1:2181,host2:2181 |
| namespace (?)     | String     | 治理命名空间                                                                    |
| props        | Properties | 配置本实例需要的其他参数，例如 ZooKeeper 的连接参数等                                               |

通用属性配置

| *名称*           | *数据类型* | *说明*                                                     | *默认值* |
| --------------- | --------- | ---------------------------------------------------------- | ------- |
| overwrite       | boolean   | 本地配置是否覆盖配置中心配置，如果可覆盖，每次启动都以本地配置为准 | false   |

ZooKeeper 属性配置

| *名称*                            | *数据类型* | *说明*                 | *默认值* |
| -------------------------------- | ---------- | --------------------- | ------- |
| digest (?)                       | String     | 连接注册中心的权限令牌   | 无需验证 |
| operationTimeoutMilliseconds (?) | int        | 操作超时的毫秒数        | 500 毫秒 |
| maxRetries (?)                   | int        | 连接失败后的最大重试次数 | 3 次     |
| retryIntervalMilliseconds (?)    | int        | 重试间隔毫秒数          | 500 毫秒 |
| timeToLiveSeconds (?)            | int        | 临时节点存活秒数        | 60 秒    |

Etcd 属性配置

| *名称*                 | *数据类型* | *说明*     | *默认值* |
| --------------------- | --------- | ---------- | ------- |
| timeToLiveSeconds (?) | long      | 数据存活秒数 | 30秒    |

Apollo 属性配置

| *名称*             | *数据类型* | *说明*               | *默认值*               |
| ------------------ | -------- | -------------------- | --------------------- |
| appId (?)          | String   | Apollo appId         | APOLLO_SHARDINGSPHERE |
| env (?)            | String   | Apollo env           | DEV                   |
| clusterName (?)    | String   | Apollo clusterName   | default               |
| administrator (?)  | String   | Apollo administrator | 空                    |
| token (?)          | String   | Apollo token         | 空                    |
| portalUrl (?)      | String   | Apollo portalUrl     | 空                    |
| connectTimeout (?) | int      | 连接超时毫秒数         | 1000 毫秒             |
| readTimeout (?)    | int      | 读取超时毫秒数         | 5000 毫秒             |

Nacos 属性配置

| *名称*       | *数据类型* | *说明*                 | *默认值*                      |
| ----------- | --------- | ---------------------- | ---------------------------- |
| group (?)   | String    | nacos group 配置       | SHARDING_SPHERE_DEFAULT_GROUP |
| timeout (?) | long      | nacos 获取数据超时毫秒数 | 3000 毫秒                     |

### 集群管理

*配置入口*

类名称：org.apache.shardingsphere.cluster.configuration.config.ClusterConfiguration

可配置属性：

| *名称*                    | *数据类型*                           | *说明*                                                            |
| ------------------------ | ----------------------------------- | ----------------------------------------------------------------- |
| heartbeat | HeartbeatConfiguration  | 心跳检测配置 |

*心跳检测配置*

类名称：org.apache.shardingsphere.orchestration.center.config.HeartbeatConfiguration

可配置属性：

| *名称*       | *数据类型* | *说明*                 |
| ----------- | --------- | ---------------------- |
| sql   | String    | 心跳检测 SQL       |
| interval | int      | 心跳检测间隔时间 (s) |
| threadCount   | int    | 心跳检测线程池大小       |
| retryEnable | Boolean      | 是否支持失败重试，可设置 true 或 false |
| retryMaximum(?)   | int    | 最大重试次数      |
| retryInterval(?) | int      | 重试间隔时间 (s) |
