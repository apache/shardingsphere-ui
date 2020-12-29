# shardingsphere-ui backend API

## 1.User authentication related interfaces

### 1.1 User login

POST /api/login

#### Request

| Parameter | Field type | Essential | Describe      |
| --------- | ---------- | --------- | ------------- |
| username  | String     | Y         | User name     |
| password  | String     | Y         | User password |

#### Example

```
curl -X GET http://localhost:8088/api/login
```

#### Response

| Parameter   | Field type | Describe                  |
| ----------- | ---------- | ------------------------- |
| success     | Boolean    | Is the request successful |
| errorCode   | Integer    | Error code                |
| errorMsg    | String     | Wrong description         |
| accessToken | String     | Access credentials        |
| username    | String     | User name                 |

```
{
    "success": true,
    "errorCode": 0,
    "errorMsg": null,
    "model": {"accessToken":"string","username":"string"}
}
```

**Note: accessToken is obtained. All subsequent requests need to carry this voucher in request headers: `Access-Token: accessToken`**

## 2.Relevant interfaces of Registration Center

### 2.1 Get all registry configurations

GET /api/reg-center

#### Example

```
curl -X GET http://localhost:8088/api/reg-center
```

#### Response

| Parameter          | Field type | Describe                                    |
| ------------------ | ---------- | ------------------------------------------- |
| success            | Boolean    | Is the request successful                   |
| errorCode          | Integer    | Error code                                  |
| errorMsg           | String     | Wrong description                           |
| name               | String     | Name of Registration Center                 |
| registryCenterType | String     | Registry type: "zookeeper" / "etcd"         |
| serverLists        | String     | Service address of Registration Center      |
| governanceName  | String     | Governance instance name               |
| digest             | String     | Permission token to connect to the registry |
| additionalConfigCenterType | String     | Additional config center type: "zookeeper" / "etcd"         |
| additionalConfigCenterServerList        | String     | Service address of additional config center      |
| additionalDigest             | String     | Permission token to connect to the additional config center |
| activated          | Boolean    | Is it active                                |

```
{
  "success": true,
  "errorCode": 0,
  "errorMsg": null,
  "model": [
    {
      "name": "string",
      "registryCenterType": "Zookeeper",
      "serverLists": "string",
      "governanceName": "string",
      "digest": "string",
      "additionalConfigCenterType": "Zookeeper",
      "additionalConfigCenterServerList": "string",
      "additionalDigest": "string",
      "activated": true
    },
    {
      "name": "string",
      "registryCenterType": "Etcd",
      "serverLists": "string",
      "governanceName": "string",
      "digest": "string",
      "additionalConfigCenterType": "Zookeeper",
      "additionalConfigCenterServerList": "string",
      "additionalDigest": "string",
      "activated": false
    }
  ]
}
```

### 2.2 New registry configuration

POST /api/reg-center

#### Example

```
curl -X POST http://localhost:8088/api/reg-center
```

#### Request

| Parameter          | Field type | Essential | Describe                                    |
| ------------------ | ---------- | --------- | ------------------------------------------- |
| name               | String     | Y         | Name of Registration Center                 |
| governanceName  | String     | Y         | Data governance instance name               |
| registryCenterType | String     | Y         | Registry type: "zookeeper" / "etcd"         |
| serverLists        | String     | Y         | Service address of Registration Center      |
| digest             | String     | N         | Permission token to connect to the registry |
| additionalConfigCenterType | String     | N         | Additional config center type: "zookeeper" / "etcd"         |
| additionalConfigCenterServerList        | String     | N         | Service address of additional config center      |
| additionalDigest             | String     | N         | Permission token to connect to the additional config center |

```
{
  "name": "string",
  "governanceName": "string",
  "registryCenterType": "Zookeeper",
  "serverLists": "string"
  "additionalConfigCenterType": "Zookeeper",
  "additionalConfigCenterServerList": "string"
}
```

#### Response

| Parameter | Field type | Describe                  |
| --------- | ---------- | ------------------------- |
| success   | Boolean    | Is the request successful |
| errorCode | Integer    | Error code                |
| errorMsg  | String     | Wrong description         |

```
{ "success": true, "errorCode": 0, "errorMsg": null, "model": null }
```

### 2.3 Delete registry configuration

DELETE /api/reg-center

#### Request

| Parameter | Field type | Essential | Describe                    |
| --------- | ---------- | --------- | --------------------------- |
| name      | String     | Y         | Name of Registration Center |

```
{"name":"string"}
```

#### Response

| Parameter | Field type | Describe                  |
| --------- | ---------- | ------------------------- |
| success   | Boolean    | Is the request successful |
| errorCode | Integer    | Error code                |
| errorMsg  | String     | Wrong description         |

```
{"success":true,"errorCode":0,"errorMsg":null,"model":null}
```

### 2.4 Activate And Connect TheRegistry

POST /api/reg-center/connect

#### Request

| Parameter | Field type | Essential | Describe                    |
| --------- | ---------- | --------- | --------------------------- |
| name      | String     | Y         | Name of Registration Center |

```
{"name":"string"}
```

#### Response

| Parameter | Field type | Describe                  |
| --------- | ---------- | ------------------------- |
| success   | Boolean    | Is the request successful |
| errorCode | Integer    | Error code                |
| errorMsg  | String     | Wrong description         |

```
{"success":true,"errorCode":0,"errorMsg":null,"model":null}
```

**Note: after the registration center is activated successfully, subsequent configuration and governance operations can be performed. Only one active registry is allowed for the service.**

### 2.5 Get activated registry

GET /api/reg-center/activated

#### Request

无

#### Response

| Parameter          | Field type | 描述                                        |
| ------------------ | ---------- | ------------------------------------------- |
| success            | Boolean    | Is the request successful                   |
| errorCode          | Integer    | Error code                                  |
| errorMsg           | String     | Wrong description                           |
| name               | String     | Name of Registration Center                 |
| registryCenterType | String     | Registry type: "zookeeper" / "etcd"         |
| serverLists        | String     | Service address of Registration Center      |
| governanceName     | String     | Data governance instance name               |
| digest             | String     | Permission token to connect to the registry |
| additionalConfigCenterType | String     | Additional config center type: "zookeeper" / "etcd"         |
| additionalConfigCenterServerList        | String     | Service address of additional config center      |
| additionalDigest             | String     | Permission token to connect to the additional config center |
| activated          | Boolean    | Is it active                                |

```
{
  "success": true,
  "errorCode": 0,
  "errorMsg": null,
  "model": {
    "name": "string",
    "registryCenterType": "Zookeeper",
    "serverLists": "string",
    "governanceName": "string",
    "digest": "string",
    "additionalConfigCenterType": "Zookeeper",
    "additionalConfigCenterServerList": "string",
    "additionalDigest": "string",
    "activated": true
  }
}
```

## 3.Configuration center schema configuration related interfaces

### 3.1 Get all schema names

`GET /api/schema`

#### Request

无

#### Response

| Parameter | Field type | Describe                  |
| --------- | ---------- | ------------------------- |
| success   | Boolean    | Is the request successful |
| errorCode | Integer    | Error code                |
| errorMsg  | String     | Wrong description         |
| model     | Collection | schema name list          |

```
{
  "success": true,
  "errorCode": 0,
  "errorMsg": null,
  "model": ["sharding_order", "sharding_db", "primary_replica_db"]
}
```

### 3.2 Get data fragmentation rules of schema

`GET /api/schema/rule/{schemaName}`

#### Request

| Parameter  | Field type | Essential | Describe    |
| ---------- | ---------- | --------- | ----------- |
| schemaName | String     | Y         | schema name |

#### Response

| Parameter | Field type | Describe                  |
| --------- | ---------- | ------------------------- |
| success   | Boolean    | Is the request successful |
| errorCode | Integer    | Error code                |
| errorMsg  | String     | Wrong description         |
| model     | String     | yaml string               |

```
{"success":true,"errorCode":0,"errorMsg":null,"model":"yaml string"}
```

### 3.3 Modify data fragmentation rules of schema

`PUT /api/schema/rule/{schemaName}`

#### Request

| Parameter  | Field type | Essential | Describe    |
| ---------- | ---------- | --------- | ----------- |
| schemaName | String     | Y         | schema name |
| ruleConfig | String     | Y         | yaml string |

```java
{"ruleConfig":"yaml string"}
```

#### Response

| Parameter | Field type | Describe                  |
| --------- | ---------- | ------------------------- |
| success   | Boolean    | Is the request successful |
| errorCode | Integer    | Error code                |
| errorMsg  | String     | Wrong description         |

```
{"success":true,"errorCode":0,"errorMsg":null,"model":null}
```

### 3.4 Get the data source configuration of schema

`GET /api/schema/datasource/{schemaName}`

#### Request

| Parameter  | Field type | Essential | Describe    |
| ---------- | ---------- | --------- | ----------- |
| schemaName | String     | Y         | schema name |

#### Response

| Parameter | Field type | Describe                  |
| --------- | ---------- | ------------------------- |
| success   | Boolean    | Is the request successful |
| errorCode | Integer    | Error code                |
| errorMsg  | String     | Wrong description         |
| model     | String     | yaml string               |

```
{"success":true,"errorCode":0,"errorMsg":null,"model":"yaml string"}
```

### 3.5 Modify the data source configuration of schema

`PUT /api/schema/datasource/{schemaName}`

#### Request

| Parameter  | Field type | Essential | Describe    |
| ---------- | ---------- | --------- | ----------- |
| schemaName | String     | Y         | schema name |

| Parameter        | Field type | Essential | Describe    |
| ---------------- | ---------- | --------- | ----------- |
| dataSourceConfig | String     | Y         | yaml string |

```
{"dataSourceConfig":"yaml string"}
```

#### Response

| Parameter | Field type | Describe                  |
| --------- | ---------- | ------------------------- |
| success   | Boolean    | Is the request successful |
| errorCode | Integer    | Error code                |
| errorMsg  | String     | Wrong description         |

```
{"success":true,"errorCode":0,"errorMsg":null,"model":null}
```

## 4.ShardingSphere-Proxy Authentication

### 4.1 Get the authentication configuration of sharding proxy

`GET /api/authentication`

#### Request

无

#### Response

| Parameter | Field type | Describe                  |
| --------- | ---------- | ------------------------- |
| success   | Boolean    | Is the request successful |
| errorCode | Integer    | Error code                |
| errorMsg  | String     | Wrong description         |
| username  | String     | Login user name           |
| password  | String     | Login password            |

```
{
  "success": true,
  "errorCode": 0,
  "errorMsg": null,
  "model": { "username": "string", "password": "string" }
}
```

### 4.2 Modify the authentication configuration of sharding proxy

`PUT /api/authentication`

#### Request

| Parameter | Field type | Essential | Describe        |
| --------- | ---------- | --------- | --------------- |
| username  | String     | Y         | Login user name |
| password  | String     | Y         | Login password  |

```
{"username":"string","password":"string"}
```

#### Response

| Parameter | Field type | Describe                  |
| --------- | ---------- | ------------------------- |
| success   | Boolean    | Is the request successful |
| errorCode | Integer    | Error code                |
| errorMsg  | String     | Wrong description         |

```
{"success":true,"errorCode":0,"errorMsg":null,"model":null}
```

## 5.Configuration center properties configuration related interfaces

### 5.1 Get property configuration

`GET /api/properties`

#### Request

无

#### Response

| Parameter | Field type | Describe                  |
| --------- | ---------- | ------------------------- |
| success   | Boolean    | Is the request successful |
| errorCode | Integer    | Error code                |
| errorMsg  | String     | Wrong description         |
| model     | String     | yaml string               |

```
{"success":true,"errorCode":0,"errorMsg":null,"model":"yaml string"}
```

### 5.2 Modify property configuration

`PUT /api/properties`

#### Request

| Parameter | Field type | Essential | Describe    |
| --------- | ---------- | --------- | ----------- |
| properties     | String     | Y         | yaml string |

```
{"properties":"yaml string"}
```

#### Response

| Parameter | Field type | Describe                  |
| --------- | ---------- | ------------------------- |
| success   | Boolean    | Is the request successful |
| errorCode | Integer    | Error code                |
| errorMsg  | String     | Wrong description         |

```
{"success":true,"errorCode":0,"errorMsg":null,"model":null}
```

## 6.Arrange interfaces related to governance

### 6.1 Get running instance information

`GET /api/governance/instance`

#### Request

无

#### Response

_Response Body_: (`io.shardingsphere.shardingui.web.response.ResponseResult<java.util.Collection<io.shardingsphere.shardingui.common.dto.InstanceDTO>>`)

| Parameter  | Field type | Describe                  |
| ---------- | ---------- | ------------------------- |
| success    | Boolean    | Is the request successful |
| errorCode  | Integer    | Error code                |
| errorMsg   | String     | Wrong description         |
| serverIp   | String     | IP                        |
| instanceId | String     | Instance ID               |
| enabled    | Boolean    | Instance ID enable status |

```
{
  "success": true,
  "errorCode": 0,
  "errorMsg": null,
  "model": [{ "serverIp": "string", "instanceId": "string", "enabled": true }]
}
```

### 6.2 Modify running instance status

`PUT /api/governance/instance`

#### Request

| Parameter  | Field type | Essential | Describe                                                    |
| ---------- | ---------- | --------- | ----------------------------------------------------------- |
| instanceId | String     | Y         | Running instance ID                                         |
| enabled    | Boolean    | Y         | Instance ID enabling status, true: enabled, false: Disabled |

```
{"instanceId":"string","enabled":false}
```

#### Response

| Parameter | Field type | Describe                  |
| --------- | ---------- | ------------------------- |
| success   | Boolean    | Is the request successful |
| errorCode | Integer    | Error code                |
| errorMsg  | String     | Wrong description         |

```java
{"success":true,"errorCode":0,"errorMsg":null,"model":null}
```

### 6.3 Get replica data source information

`GET /api/governance/datasource`

#### Request

无

#### Response

_Response Body_: (`io.shardingsphere.shardingui.web.response.ResponseResult<java.util.Collection<io.shardingsphere.shardingui.common.dto.ReplicaDataSourceDTO>>`)

| Parameter            | Field type | Describe                                                          |
| -------------------- | ---------- | ----------------------------------------------------------------- |
| success              | Boolean    | Is the request successful                                         |
| errorCode            | Integer    | Error code                                                        |
| errorMsg             | String     | Wrong description                                                 |
| schema               | String     | Schema name                                  |
| primaryDataSourceName | String     | The name of primary data source |
| replicaDataSourceName  | String     | The name of replica data source                                                 |
| enabled              | Boolean    | Enable status of replica data source                                        |

```
{
  "success": true,
  "errorCode": 0,
  "errorMsg": null,
  "model": [
    {
      "schema": "primary_replica_db",
      "primaryDataSourceName": "primary_ds",
      "replicaDataSourceName": "replica_ds_0",
      "enabled": true
    },
    {
      "schema": "primary_replica_db",
      "primaryDataSourceName": "primary_ds",
      "replicaDataSourceName": "replica_ds_1",
      "enabled": true
    }
  ]
}
```

### 6.4 Modify replica status

`PUT /api/governance/datasource`

#### Request

| Parameter            | Field type | Essential                                                         | Describe |
| -------------------- | ---------- | ----------------------------------------------------------------- | -------- |
| schema               | String     | Schema name                                  |
| primaryDataSourceName | String     | The name of primary data source |
| replicaDataSourceName  | String     | The name of replica data source                                                 |
| enabled              | Boolean    | Enable status of replica data source                                        |

```
{
  "schema": "primary_replica_db",
  "primaryDataSourceName": "primary_ds",
  "replicaDataSourceName": "replica_ds_0",
  "enabled": true
}
```

#### Response

| Parameter | Field type | Describe                  |
| --------- | ---------- | ------------------------- |
| success   | Boolean    | Is the request successful |
| errorCode | Integer    | Error code                |
| errorMsg  | String     | Wrong description         |

```
{"success":true,"errorCode":0,"errorMsg":null,"model":null}
```

## 7. shardingscaling

### 7.1 Get sharding scaling service

GET /api/shardingscaling

#### Example

```

curl -X GET http://localhost:8088/api/shardingscaling

```

#### Response

```

{
"success": true,
"errorCode": 0,
"errorMsg": null,
"model": {
"serviceName": "scaling",
"serviceType": "ShardingScaling",
"serviceUrl": "localhost:8084"
}
}

OR

{
"success": false,
"errorCode": 0,
"errorMsg": "No configured sharding scaling services",
"model": null
}

```

### 7.2 Add sharding scaling service

POST /api/shardingscaling

#### Body

| Parameter   | Describe                      |
| ----------- | ----------------------------- |
| serviceName | user defined name of service  |
| serviceType | Fixed value `ShardingScaling` |
| serviceUrl   | user defined url of service   |

#### Example

```

curl -X POST \
 http://localhost:8088/api/shardingscaling \
 -H 'content-type: application/json' \
 -d '{
"serviceName": "scaling",
"serviceType": "ShardingScaling",
"serviceUrl": "localhost:8084"
}'

```

#### Response

```

{
"success": true,
"errorCode": 0,
"errorMsg": null,
"model": null
}

```

### 7.3 Delete sharding scaling service

DELETE /api/shardingscaling

#### Example

```

curl -X DELETE http://localhost:8088/api/shardingscaling

```

#### Response

```

{
"success": true,
"errorCode": 0,
"errorMsg": null,
"model": null
}

```

### 7.4 Start scaling job

POST /api/shardingscaling/job/start

#### Body

| Parameter                                         | Describe                                                     |
| ------------------------------------------------- | ------------------------------------------------------------ |
| ruleConfiguration.source                          | source data source configuration                             |
| ruleConfiguration.target                          | target data source configuration                             |
| jobConfiguration.concurrency                      | sync task proposed concurrency                               |

Data source configuration:

| Parameter                                         | Describe                                                     |
| ------------------------------------------------- | ------------------------------------------------------------ |
| type                                              | data source type(available parameters:shardingSphereJdbc,jdbc)|
| parameter                                         | data source parameter                                        |

Parameter configuration:

type = shardingSphereJdbc 

| Parameter                                         | Describe                                                     |
| ------------------------------------------------- | ------------------------------------------------------------ |
| dataSource                                        | sharding sphere data source configuration                    |
| rule                                              | sharding sphere data source table rule                       |

type = jdbc 

| Parameter                                         | Describe                                                     |
| ------------------------------------------------- | ------------------------------------------------------------ |
| name                                              | jdbc name                                                    |
| ruleConfiguration.targetDataSources.jdbcUrl           | jdbc url                                                     |
| ruleConfiguration.targetDataSources.username      | jdbc username                                                |
| ruleConfiguration.targetDataSources.password      | jdbc password                                                |

#### Example

```
curl -X POST \
  http://localhost:8088/api/shardingscaling/job/start \
  -H 'content-type: application/json' \
  -d '{
        "ruleConfiguration": {
          "source": {
            "type": "shardingSphereJdbc",
            "parameter": {
              "dataSource":"
                dataSources:
                  ds_0:
                    dataSourceClassName: com.zaxxer.hikari.HikariDataSource
                    props:
                      driverClassName: com.mysql.jdbc.Driver
                      jdbcUrl: jdbc:mysql://127.0.0.1:3306/scaling_0?useSSL=false
                      username: scaling
                      password: scaling
                  ds_1:
                    dataSourceClassName: com.zaxxer.hikari.HikariDataSource
                    props:
                      driverClassName: com.mysql.jdbc.Driver
                      jdbcUrl: jdbc:mysql://127.0.0.1:3306/scaling_1?useSSL=false
                      username: scaling
                      password: scaling
                ",
              "rule":"
                rules:
                - !SHARDING
                  tables:
                    t_order:
                      actualDataNodes: ds_$->{0..1}.t_order_$->{0..1}
                      databaseStrategy:
                        standard:
                          shardingColumn: order_id
                          shardingAlgorithmName: t_order_db_algorith
                      logicTable: t_order
                      tableStrategy:
                        standard:
                          shardingColumn: user_id
                          shardingAlgorithmName: t_order_tbl_algorith
                  shardingAlgorithms:
                    t_order_db_algorith:
                      type: INLINE
                      props:
                        algorithm-expression: ds_$->{order_id % 2}
                    t_order_tbl_algorith:
                      type: INLINE
                      props:
                        algorithm-expression: t_order_$->{user_id % 2}
                "
            }
          },
          "target": {
              "type": "jdbc",
              "parameter": {
                "username": "root",
                "password": "root",
                "jdbcUrl": "jdbc:mysql://127.0.0.1:3307/sharding_db?serverTimezone=UTC&useSSL=false"
              }
          }
        },
        "jobConfiguration":{
          "concurrency":"3"
        }
      }'
```

#### Response

```

{
"success": true,
"errorCode": 0,
"errorMsg": null,
"model": null
}

```

### 7.5 Get scaling progress

GET /api/shardingscaling/job/progress/{jobId}

#### Example

```

curl -X GET \
 http://localhost:8088/api/shardingscaling/job/progress/1

```

#### Response

```

{
"success": true,
"errorCode": 0,
"errorMsg": null,
"model": {
"id": 1,
"jobName": "Local Sharding Scaling Job",
"status": "RUNNING/STOPPED"
"syncTaskProgress": [{
"id": "127.0.0.1-3306-test",
"status": "PREPARING/MIGRATE_HISTORY_DATA/SYNCHRONIZE_REALTIME_DATA/STOPPING/STOPPED",
"historySyncTaskProgress": [{
"id": "history-test-t1#0",
"estimatedRows": 41147,
"syncedRows": 41147
}, {
"id": "history-test-t1#1",
"estimatedRows": 42917,
"syncedRows": 42917
}, {
"id": "history-test-t1#2",
"estimatedRows": 43543,
"syncedRows": 43543
}, {
"id": "history-test-t2#0",
"estimatedRows": 39679,
"syncedRows": 39679
}, {
"id": "history-test-t2#1",
"estimatedRows": 41483,
"syncedRows": 41483
}, {
"id": "history-test-t2#2",
"estimatedRows": 42107,
"syncedRows": 42107
}],
"realTimeSyncTaskProgress": {
"id": "realtime-test",
"delayMillisecond": 1576563771372,
"logPosition": {
"filename": "ON.000007",
"position": 177532875,
"serverId": 0
}
}
}]
}
}

```

### 7.6 List scaling jobs

GET /api/shardingscaling/job/list

#### Example

```

curl -X GET \
 http://localhost:8088/api/shardingscaling/job/list

```

#### Response

```

{
"success": true,
"errorCode": 0,
"model": [
{
"jobId": 1,
"jobName": "Local Sharding Scaling Job",
"status": "RUNNING"
}
]
}

```

### 7.7 Stop scaling job

GET /api/shardingscaling/job/stop

#### Body

| Parameter | Describe |
| --------- | -------- |
| jobId     | job id   |

#### Example

```
curl -X GET \
  http://localhost:8888/scaling/job/stop/1
```

#### Response

```

{
"success": true,
"errorCode": 0,
"errorMsg": null,
"model": null
}

```

```

```
