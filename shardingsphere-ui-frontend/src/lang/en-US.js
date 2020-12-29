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

export default {
  common: {
    home: 'Home',
    menuData: [
      {
        title: 'Governance',
        child: [
          {
            title: 'Registry Center',
            href: '/registry-center'
          },
          {
            title: 'Rule Config',
            href: '/rule-config'
          },
          {
            title: 'Runtime Status',
            href: '/runtime-status'
          }
        ]
      },
      {
        title: 'Data scaling',
        href: '/data-scaling'
      }
    ],
    connected: 'Connected',
    connection: 'Connection',
    del: 'Delete',
    notify: {
      title: 'Prompt',
      addSucMessage: 'Add Succeeded',
      editSucMessage: 'Edit Succeeded',
      conSucMessage: 'Connection Succeeded',
      conFailMessage: 'Connection Failed',
      delSucMessage: 'Delete Succeeded',
      delFailMessage: 'Delete Failed',
      updateCompletedMessage: 'Update Completed',
      updateFaildMessage: 'Update Faild',
      confirmDelOperator: 'Confirm delete'
    },
    currentRegistryCenter: 'Connected Registry Center',
    loginOut: 'Sign Out',
    dropdownList: [
      {
        title: '中文',
        command: 'zh-CN'
      },
      {
        title: 'English',
        command: 'en-US'
      }
    ]
  },
  login: {
    btnTxt: 'Login',
    labelUserName: 'Username',
    labelPassword: 'Password'
  },
  btn: {
    submit: 'Submit',
    reset: 'Reset',
    cancel: 'Cancel',
    confirm: 'Confirm'
  },
  input: {
    pUserName: 'Please enter user name',
    pPaasword: 'Please enter your password'
  },
  registryCenter: {
    btnTxt: 'ADD',
    registDialog: {
      title: 'Add a registry center',
      editTitle: 'Edit registry center',
      name: 'Name',
      centerType: 'Type',
      address: 'Address',
      governanceName: 'Governance Name',
      namespaces: 'Namespace',
      digest: 'Digest',
      btnConfirmTxt: 'Confirm',
      btnCancelTxt: 'Cancel'
    },
    table: {
      operate: 'Operation',
      operateConnect: 'Connect',
      operateConnected: 'Connected',
      operateDel: 'Delete',
      operateEdit: 'Edit'
    },
    rules: {
      name: 'Please enter the name of the registration center',
      address: 'Please enter the registration center address',
      additionalAddress: 'Please enter additional config center address',
      namespaces: 'Please enter a namespace',
      centerType: 'Please select a center type',
      governanceName: 'Please enter a governance name',
      digest: 'Please enter a digest'
    }
  },
  runtimeStatus: {
    serviceNode: 'Service Node',
    replicaDataSourceName: 'Replica DataSource Info',
    dataSource: {
      schema: 'Schema',
      masterDataSourceName: 'Primary DataSource Name',
      replicaDataSourceName: 'Replica DataSource Name'
    },
    instance: {
      instanceId: 'Instance Id',
      serverIp: 'Server Ip'
    },
    enabled: 'Enabled'
  },
  ruleConfig: {
    form: {
      inputPlaceholder: 'Please enter content'
    },
    schema: {
      title: 'Add Schema',
      name: 'Name',
      ruleConfig: 'Rule Config',
      dataSourceConfig: 'Data Source Config',
      metadataConfig: 'Metadata Config'
    },
    schemaRules: {
      name: 'Please enter the name of the schema',
      ruleConfig: 'Please enter the rule config of the schema',
      dataSourceConfig: 'Please enter the data source config of the schema',
      metadataConfig: 'Please enter the metadata config of the schema'
    },
    radioBtn: {
      schema: 'Schema',
      authentication: 'Authentication',
      props: 'Props'
    }
  },
  dataScaling: {
    btnTxt: 'ADD',
    tableList: {
      jobId: 'Job Id',
      jobName: 'Job Name',
      status: 'Status',
      operate: 'Operation',
      operateStop: 'stop',
      operateSee: 'see'
    },
    registDialog: {
      title: 'Add a job',
      source: 'Source',
      target: 'Target',
      jobCount: 'JobCount',
      jobCountPlaceholder: 'Please enter jobCount',
      username: 'Username',
      usernamePlaceholder: 'Please enter username',
      password: 'Password',
      passwordPlaceholder: 'Please enter password',
      url: 'Url',
      urlPlaceholder: 'Please enter url',
      btnConfirmTxt: 'Confirm',
      btnCancelTxt: 'Cancel'
    },
    rules: {
      source: 'Please select the source of the registration center',
      target: 'Please select a target',
      serviceUrl: 'ServiceUrl must fill'
    },
    notify: {
      title: 'Prompt',
      conSucMessage: 'Add Succeeded',
      conFailMessage: 'Add Failed',
      delSucMessage: 'Delete Succeeded',
      delFailMessage: 'Delete Failed'
    },
    serviceDialog: {
      title: 'Data Scaling Setting',
      serviceName: 'Service Name',
      serviceUrl: 'Service Url',
      serviceNamePlaceholder: 'Please enter serviceName',
      serviceUrlPlaceholder: 'Please enter serviceUrl'
    },
    detail: {
      inventory: 'Inventory',
      increment: 'Increment',
      taskId: 'Sub Task Id',
      shardingItem: 'Sharding Item',
      delay: 'Delay(seconds)',
      total: 'Total Tasks',
      finished: 'Finished Tasks'
    }
  }
}
