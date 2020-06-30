+++
title = "Governance"
weight = 6
+++

## Configuration Item Explanation

### Management

*Configuration Entrance*

Class name: org.apache.shardingsphere.orchestration.center.config.OrchestrationConfiguration

Attributes:

| *Name*                   | *Data Type*                         | *Description*                                                                                                         |
| ------------------------ | ----------------------------------- | --------------------------------------------------------------------------------------------------------------------- |
| instanceConfigurationMap | Map\<String, CenterConfiguration\>  | Config map of config-center&registry-center, the key is orchestration name, the value is the orchestration instance |

*Orchestration Instance Configuration*

Class name: org.apache.shardingsphere.orchestration.center.config.CenterConfiguration

Attributes:

| *Name*            | *Data Type* | *Description*                                                                                                                               |
| ----------------- | ----------- | ------------------------------------------------------------------------------------------------------------------------------------------- |
| orchestrationType | String      | Orchestration type, use commas to separate, such as: config_center,registry_center,metadata_center               |
| instanceType      | String      | Orchestration instance type, such as: zookeeper, etcd, apollo, nacos                                                                                    |
| serverLists       | String      | The list of servers that connect to orchestration instance, including IP and port number, use commas to separate, such as: host1:2181,host2:2181 |
| namespace (?)     | String      | Orchestration namespace                                                                                                               |
| props        | Properties      | Properties for center instance config, such as options of zookeeper                                                                         |

Common Properties Configuration

| *Name*          | *Data Type* | *Description*                                                                                                                             | *Default Value* |
| --------------- | ----------- | ----------------------------------------------------------------------------------------------------------------------------------------- | --------------- |
| overwrite       | boolean     | Local configurations overwrite config center configurations or not; if they overwrite, each start takes reference of local configurations | false           |

ZooKeeper Properties Configuration

| *Name*                           | *Data Type* | *Description*                                  | *Default Value*       |
| -------------------------------- | ----------- | ---------------------------------------------- | --------------------- |
| digest (?)                       | String      | Connect to authority tokens in registry center | No need for authority |
| operationTimeoutMilliseconds (?) | int         | The operation timeout milliseconds             | 500 milliseconds      |
| maxRetries (?)                   | int         | The maximum retry count                        | 3                     |
| retryIntervalMilliseconds (?)    | int         | The retry interval milliseconds                | 500 milliseconds      |
| timeToLiveSeconds (?)            | int         | Time to live seconds for ephemeral nodes       | 60 seconds            |


Etcd Properties Configuration

| *Name*                | *Data Type* | *Description*                         | *Default Value* |
| --------------------- | ----------- | ------------------------------------- | --------------- |
| timeToLiveSeconds (?) | long        | Time to live seconds for data persist | 30 seconds      |

Apollo Properties Configuration

| *Name*             | *Data Type* | *Description*                | *Default Value*       |
| ------------------ | ----------- | ---------------------------- | --------------------- |
| appId (?)          | String      | Apollo appId                 | APOLLO_SHARDINGSPHERE |
| env (?)            | String      | Apollo env                   | DEV                   |
| clusterName (?)    | String      | Apollo clusterName           | default               |
| administrator (?)  | String      | Apollo administrator         | Empty                 |
| token (?)          | String      | Apollo token                 | Empty                 |
| portalUrl (?)      | String      | Apollo portalUrl             | Empty                 |
| connectTimeout (?) | int         | Connect timeout milliseconds | 1000 milliseconds     |
| readTimeout (?)    | int         | Read timeout milliseconds    | 5000 milliseconds     |

Nacos Properties Configuration

| *Name*      | *Data Type* | *Description* | *Default Value*               |
| ----------- | ----------- | ------------- | ----------------------------- |
| group (?)   | String      | group         | SHARDING_SPHERE_DEFAULT_GROUP |
| timeout (?) | long        | timeout       | 3000 milliseconds             |

### Cluster

*Configuration Entrance*

Class name：org.apache.shardingsphere.cluster.configuration.config.ClusterConfiguration

Attributes：

| *Name*                    | *Data Type*                           | *Description*                                                            |
| ------------------------ | ----------------------------------- | ----------------------------------------------------------------- |
| heartbeat | HeartbeatConfiguration  | heartbeat detection configuration |

*Heartbeat Detection Configuration*

Class name：org.apache.shardingsphere.orchestration.center.config.HeartbeatConfiguration

Attributes：

| *Name*       | *Data Type* | *Description*                 |
| ----------- | --------- | ---------------------- |
| sql   | String    | Heartbeat detection SQL       |
| interval | int      | Heartbeat detection task interval (s) |
| threadCount   | int    | Thread pool size       |
| retryEnable | Boolean      | Whether to enable retry, set true or false |
| retryMaximum(?)   | int    | Maximum number of retry, effective when retryEnable is true      |
| retryInterval(?) | int      | Retry interval (s), effective when retryEnable is true |
