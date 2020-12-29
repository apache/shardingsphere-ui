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
    home: '主页',
    menuData: [
      {
        title: '分布式治理',
        child: [
          {
            title: '注册中心',
            href: '/registry-center'
          },
          {
            title: '配置管理',
            href: '/rule-config'
          },
          {
            title: '运行状态',
            href: '/runtime-status'
          }
        ]
      },
      {
        title: '数据扩容',
        href: '/data-scaling'
      }
    ],
    connect: '已连接',
    connection: '连接',
    del: '删除',
    notify: {
      title: '提示',
      addSucMessage: '添加成功',
      editSucMessage: '修改成功',
      conSucMessage: '连接成功',
      conFailMessage: '连接失败',
      delSucMessage: '删除成功',
      delFailMessage: '删除失败',
      updateCompletedMessage: '更新成功',
      updateFaildMessage: '更新失败',
      confirmDelOperator: '确认删除'
    },
    currentRegistryCenter: '已连接注册中心',
    loginOut: '退出登录',
    dropdownList: [
      {
        title: '中文',
        command: 'Chinese'
      },
      {
        title: 'English',
        command: 'English'
      }
    ]
  },
  login: {
    btnTxt: '登录',
    labelUserName: '用户名',
    labelPassword: '密码'
  },
  btn: {
    submit: '提交',
    reset: '重置',
    cancel: '取消',
    confirm: '确定'
  },
  input: {
    pUserName: '请输入用户名',
    pPaasword: '请输入密码'
  },
  registryCenter: {
    btnTxt: '添加',
    registDialog: {
      title: '添加注册中心',
      editTitle: '编辑注册中心',
      name: '注册中心名称',
      centerType: '注册中心类型',
      address: '注册中心地址',
      governanceName: '治理实例名称',
      namespaces: '命名空间',
      digest: '登录凭证',
      btnConfirmTxt: '确定',
      btnCancelTxt: '取消'
    },
    table: {
      operate: '操作',
      operateConnect: '连接',
      operateConnected: '已激活',
      operateDel: '删除',
      operateEdit: '编辑'
    },
    rules: {
      name: '请输入注册中心名称',
      centerType: '请选择注册中心类型',
      namespaces: '请输入命名空间',
      address: '请输入注册中心地址',
      additionalAddress: '请输入扩展配置中心地址',
      governanceName: '请输入治理实例名称',
      digest: '请输入登录凭证'
    }
  },
  runtimeStatus: {
    serviceNode: '服务节点',
    replicaDataSourceName: '从库信息',
    dataSource: {
      schema: '逻辑库名',
      masterDataSourceName: '主库名',
      replicaDataSourceName: '从库名'
    },
    instance: {
      instanceId: '节点标识',
      serverIp: '服务ip'
    },
    enabled: '是否启用'
  },
  ruleConfig: {
    form: {
      inputPlaceholder: '请输入内容'
    },
    schema: {
      title: '添加Schema',
      name: '名称',
      ruleConfig: '分片配置规则',
      dataSourceConfig: '数据源配置规则'
    },
    schemaRules: {
      name: '请输入名称',
      ruleConfig: '请输入数据分片配置规则',
      dataSourceConfig: '请输入数据源配置规则'
    },
    radioBtn: {
      schema: '数据源',
      authentication: '认证信息',
      props: '属性配置'
    }
  },
  dataScaling: {
    btnTxt: '添加',
    tableList: {
      jobId: '任务Id',
      jobName: '任务名称',
      status: '状态',
      operate: '操作',
      operateStop: '停止',
      operateSee: '查看'
    },
    registDialog: {
      title: '添加一个任务',
      source: '源',
      target: '目标',
      jobCount: '任务数量',
      jobCountPlaceholder: '请输入任务数量',
      username: '用户名',
      usernamePlaceholder: '请输入用户名',
      password: '密码',
      passwordPlaceholder: '请输入密码',
      url: '地址',
      urlPlaceholder: '请输入 url',
      btnConfirmTxt: '确认',
      btnCancelTxt: '取消'
    },
    rules: {
      source: '请选择注册中心的来源',
      target: '请选择目标',
      serviceUrl: '服务地址必填'
    },
    notify: {
      title: '提示',
      conSucMessage: '添加成功',
      conFailMessage: '添加失败',
      delSucMessage: '删除成功',
      delFailMessage: '删除失败'
    },
    serviceDialog: {
      title: '数据扩容配置',
      serviceName: '服务名称',
      serviceUrl: '服务地址',
      serviceNamePlaceholder: '请输入服务名称',
      serviceUrlPlaceholder: '请输入服务地址'
    },
    detail: {
      inventory: '存量',
      increment: '增量',
      taskId: '子任务Id',
      shardingItem: '分片项',
      delay: '延迟(秒)',
      total: '总任务数',
      finished: '已完成任务数'
    }
  }
}
