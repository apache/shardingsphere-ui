<!--
  - Licensed to the Apache Software Foundation (ASF) under one or more
  - contributor license agreements.  See the NOTICE file distributed with
  - this work for additional information regarding copyright ownership.
  - The ASF licenses this file to You under the Apache License, Version 2.0
  - (the "License"); you may not use this file except in compliance with
  - the License.  You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<template>
  <el-row class="box-card">
    <div class="btn-group">
      <el-button
        class="btn-plus"
        type="primary"
        icon="el-icon-plus"
        @click="add">{{ $t('dataScaling.btnTxt') }}</el-button>
      <span style="margin-left: 20px;">
        server: <samp style="color: #E17425;">{{ serviceForm.serviceName }}</samp>
        <i class="el-icon-edit" @click="showServerDialog"></i>
      </span>
    </div>
    <div class="table-wrap">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column
          v-for="(item, index) in column"
          :key="index"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
        />
        <el-table-column
          :label="$t('dataScaling.tableList.operate')"
          fixed="right"
          width="140"
        >
          <template slot-scope="scope">
            <el-tooltip
              :content="$t('dataScaling.tableList.operateSee')"
              class="item"
              effect="dark"
              placement="top"
            >
              <el-button
                size="small"
                type="primary"
                icon="el-icon-view"
                @click="showSyncTaskProgressDetail(scope.row)"
              />
            </el-tooltip>
            <el-tooltip
              :content="$t('dataScaling.tableList.operateStop')"
              class="item"
              effect="dark"
              placement="top"
            >
              <el-button
                :disabled="scope.row.status === 'STOPPED'"
                icon="el-icon-video-pause"
                size="small"
                type="danger"
                @click="handlerStop(scope.row)"
              />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          :total="total"
          :current-page="currentPage"
          background
          layout="prev, pager, next"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
    <!-- add Dialog -->
    <el-dialog
      :title="$t('dataScaling.registDialog.title')"
      :visible.sync="DataScalingDialogVisible"
      width="1010px"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="170px">
        <el-form-item
          :label="$t('dataScaling.registDialog.source')"
          prop="source"
        >
          <el-select
            v-model="form.source"
            :placeholder="$t('dataScaling.rules.source')"
            :disabled="!schemaData.length"
            @change="selectChange"
          >
            <el-option v-for="(item, index) in schemaData" :label="item" :value="item" :key="index"></el-option>
          </el-select>
          <span v-show="form.source" style="margin-left: 10px">
            <el-button type="primary" size="mini" @click="showDatasource">Datasource</el-button>
            <el-button type="primary" size="mini" @click="showRule">Rule</el-button>
          </span>
        </el-form-item>
        <el-form-item
          :label="$t('dataScaling.registDialog.target')"
          prop="target"
        >
          <el-radio-group v-model="form.target">
            <el-radio label="Proxy">Proxy</el-radio>
          </el-radio-group>
        </el-form-item>
        <div v-show="schemaData.length > 0">
          <el-form-item
            :label="$t('dataScaling.registDialog.username')"
            prop="username"
          >
            <el-input v-model="form.username" :placeholder="$t('dataScaling.registDialog.usernamePlaceholder')"></el-input>
          </el-form-item>
          <el-form-item
            :label="$t('dataScaling.registDialog.password')"
            prop="password"
          >
            <el-input v-model="form.password" :placeholder="$t('dataScaling.registDialog.passwordPlaceholder')"></el-input>
          </el-form-item>
          <el-form-item
            :label="$t('dataScaling.registDialog.url')"
            prop="url"
          >
            <el-input v-model="form.url" :placeholder="$t('dataScaling.registDialog.urlPlaceholder')"></el-input>
          </el-form-item>
          <el-form-item
            :label="$t('dataScaling.registDialog.jobCount')"
            prop="jobCount"
          >
            <el-input v-model="form.jobCount" :placeholder="$t('dataScaling.registDialog.jobCountPlaceholder')"></el-input>
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="DataScalingDialogVisible = false">{{
          $t('dataScaling.registDialog.btnCancelTxt')
        }}</el-button>
        <el-button type="primary" @click="onConfirm('form')">{{
          $t('dataScaling.registDialog.btnConfirmTxt')
        }}</el-button>
      </div>
    </el-dialog>
    <!-- showDatasource -->
    <el-dialog
      :visible.sync="DatasourceVisible"
      title="Result Datasource:"
      width="1010px"
    >
      <el-row>
        <el-col :span="24">
          <el-input
            :rows="20"
            v-model="textareaDatasourceCom"
            type="textarea"
            readonly
            class="show-text"
          />
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer"></div>
    </el-dialog>
    <!-- showRule -->
    <el-dialog
      :visible.sync="RuleVisible"
      title="Result Rule:"
      width="1010px"
    >
      <el-row>
        <el-col :span="24">
          <el-input
            :rows="20"
            v-model="textareaRuleCom"
            type="textarea"
            readonly
            class="show-text"
          />
        </el-col>
      </el-row>
    </el-dialog>
    <!-- syncTaskProgressDetail -->
    <el-dialog
      :visible.sync="DataScalingDialogSyncTaskProgressDetailVisible"
      width="1110px"
    >
      <el-form :inline="true">
        <el-form-item :label="this.$t('dataScaling').tableList.jobId">
          {{ job.jobId }}
        </el-form-item>
        <el-form-item :label="this.$t('dataScaling').tableList.jobName">
          {{ job.jobName }}
        </el-form-item>
        <el-form-item :label="this.$t('dataScaling').tableList.status">
          {{ job.status }}
        </el-form-item>
      </el-form>

      <el-tabs v-model="activeName">
        <el-tab-pane name="first">
          <div slot="label">{{ this.$t('dataScaling').detail.inventory }} ({{ percentageComputed }}%)</div>
          <el-table :data="jobDetail.inventoryTaskProgress">
            <el-table-column :label="this.$t('dataScaling').detail.shardingItem" prop="shardingItem"></el-table-column>
            <el-table-column :label="this.$t('dataScaling').detail.total" prop="total"></el-table-column>
            <el-table-column :label="this.$t('dataScaling').detail.finished" prop="finished"></el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane :label="this.$t('dataScaling').detail.increment" name="second">
          <el-table :data="jobDetail.incrementalTaskProgress">
            <el-table-column :label="this.$t('dataScaling').detail.shardingItem" prop="shardingItem"></el-table-column>
            <el-table-column :label="this.$t('dataScaling').detail.taskId" prop="id"></el-table-column>
            <el-table-column :label="this.$t('dataScaling').detail.delay" prop="delayMillisecond"></el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>

    </el-dialog>
    <el-dialog
      :visible.sync="serverDialogVisible"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
      :title="$t('dataScaling.serviceDialog.title')"
      width="480px"
      center>
      <el-form label-width="110px">
        <el-form-item
          :label="$t('dataScaling.serviceDialog.serviceName')">
          <el-input v-model="serviceForm.serviceName" :placeholder="$t('dataScaling.serviceDialog.serviceNamePlaceholder')"/>
        </el-form-item>
        <el-form-item
          :label="$t('dataScaling.serviceDialog.serviceUrl')">
          <el-input v-model="serviceForm.serviceUrl" :placeholder="$t('dataScaling.serviceDialog.serviceUrlPlaceholder')"/>
        </el-form-item>
        <el-form-item>
          <el-button @click="serverDialogVisible = false">{{
            $t('dataScaling.registDialog.btnCancelTxt')
          }}</el-button>
          <el-button type="primary" @click="setServer">
            {{ $t('dataScaling.registDialog.btnConfirmTxt') }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </el-row>
</template>
<script>
import yaml from 'js-yaml'
import Vue from 'vue'
import moment from 'moment'
import clone from 'lodash/clone'
import API from '../api'

Vue.prototype.$moment = moment

/**
 * 保留n位小数
 */
const nDecimal = (num = 0, n = 0) => {
  if (num === null) return '--'
  let f_x = parseFloat(num)
  if (isNaN(f_x)) {
    console.log('function:changeTwoDecimal->parameter error')
    return false
  }

  if (!n) return parseInt(f_x)

  f_x = Math.round(num * 100) / 100
  let s_x = f_x.toString()
  let pos_decimal = s_x.indexOf('.')
  if (pos_decimal < 0) {
    pos_decimal = s_x.length
    s_x += '.'
  }
  while (s_x.length <= pos_decimal + n) {
    s_x += '0'
  }
  return s_x
}

let timer = null

export default {
  name: 'DataScalingIndex',
  data() {
    return {
      DataScalingDialogVisible: false,
      DataScalingDialogSyncTaskProgressDetailVisible: false,
      DatasourceVisible: false,
      serverDialogVisible: false,
      RuleVisible: false,
      serviceForm: {
        serviceName: '',
        serviceType: 'ShardingScaling',
        serviceUrl: ''
      },
      schemaData: [],
      textareaDatasource: ``,
      textareaRule: ``,
      serverHost: '',
      column: [
        {
          label: this.$t('dataScaling').tableList.jobId,
          prop: 'jobId'
        },
        {
          label: this.$t('dataScaling').tableList.jobName,
          prop: 'jobName'
        },
        {
          label: this.$t('dataScaling').tableList.status,
          prop: 'status'
        }
      ],
      form: {
        source: '',
        target: 'Proxy',
        username: '',
        password: '',
        url: '',
        jobCount: '3'
      },
      job: {},
      jobDetail: {},
      activeName: 'first',
      rules: {
        source: [
          {
            required: true,
            message: this.$t('dataScaling').rules.source,
            trigger: 'change'
          }
        ],
        target: [
          {
            required: true,
            message: this.$t('dataScaling').rules.target,
            trigger: 'change'
          }
        ],
        username: [
          {
            required: true,
            message: this.$t('dataScaling').registDialog.usernamePlaceholder,
            trigger: 'change'
          }
        ],
        password: [
          {
            required: true,
            message: this.$t('dataScaling').registDialog.passwordPlaceholder,
            trigger: 'change'
          }
        ],
        url: [
          {
            required: true,
            message: this.$t('dataScaling').registDialog.urlPlaceholder,
            trigger: 'change'
          }
        ],
        jobCount: [
          {
            required: true,
            message: this.$t('dataScaling').registDialog.jobCountPlaceholder,
            trigger: 'change'
          }
        ]
      },
      tableData: [],
      cloneTableData: [],
      currentPage: 1,
      pageSize: 10,
      total: null
    }
  },
  computed: {
    textareaDatasourceCom() {
      const DS_SCHEMA = yaml.Schema.create([])
      return JSON.stringify(
        yaml.load(this.textareaDatasource, { schema: DS_SCHEMA }),
        null,
        '\t'
      )
    },
    textareaRuleCom() {
      const shardingYamlType = new yaml.Type(
        '!SHARDING',
        {
          kind: 'mapping',
          construct(data) {
            return data !== null ? data : {}
          }
        }
      )
      const encryptYamlType = new yaml.Type(
        '!ENCRYPT',
        {
          kind: 'mapping',
          construct(data) {
            return data !== null ? data : {}
          }
        }
      )
      const masterSlaveYamlType = new yaml.Type(
        '!PRIMARY_REPLICA_REPLICATION',
        {
          kind: 'mapping',
          construct(data) {
            return data !== null ? data : {}
          }
        }
      )
      const shadowYamlType = new yaml.Type(
        '!SHADOW',
        {
          kind: 'mapping',
          construct(data) {
            return data !== null ? data : {}
          }
        }
      )
      const DS_SCHEMA = yaml.Schema.create([shardingYamlType, encryptYamlType, masterSlaveYamlType, shadowYamlType])
      return JSON.stringify(
        yaml.load(this.textareaRule, { schema: DS_SCHEMA }),
        null,
        '\t'
      )
    },
    percentageComputed() {
      if (!this.jobDetail.inventoryTaskProgress) {
        return
      }
      let total = 0
      let finished = 0
      for (const v of this.jobDetail.inventoryTaskProgress) {
        total += v.total
        finished += v.finished
      }
      return total ? nDecimal(finished / total * 100, 0) : 100
    }
  },
  created() {
    this.getJobServer()
  },
  methods: {
    dateFormat(row, column) {
      if (row.delayMillisecond === -1) {
        return -1
      }
      return this.$moment(
        row.delayMillisecond
      ).format('s')
    },
    showServerDialog() {
      this.serverDialogVisible = true
    },
    setServer() {
      if (this.serviceForm.serviceUrl) {
        API.postJobServer(this.serviceForm).then(res => {
          this.$notify({
            title: this.$t('dataScaling').notify.title,
            message: 'Set up successfully！',
            type: 'success'
          })
          this.serverDialogVisible = false
        }, () => {
          this.$notify({
            title: this.$t('dataScaling').notify.title,
            message: 'Setup failed！',
            type: 'error'
          })
        })
      } else {
        this.$notify({
          title: this.$t('dataScaling').notify.title,
          message: this.$t('dataScaling').rules.serviceUrl,
          type: 'error'
        })
      }
    },
    getJobServer() {
      API.getJobServer().then(res => {
        const { model } = res
        if (model) {
          const { serviceName, serviceType, serviceUrl } = model
          this.serviceForm = {
            serviceName,
            serviceType,
            serviceUrl
          }
          this.getJobList()
        } else {
          this.serverDialogVisible = true
        }
      }, () => {
        this.serverDialogVisible = true
      })
    },
    getOption(obj) {
      let data = 0
      if (obj.estimatedRows) {
        data = obj.syncedRows / obj.estimatedRows
      }
      const option = {
        series: [
          {
            type: 'liquidFill',
            radius: '90%',
            data: [data],
            outline: {
              show: false
            },
            label: {
              fontSize: 20
            }
          }
        ]
      }
      return option
    },
    selectChange(item) {
      this.getSchemaDataSource(item)
      this.getSchemaRule(item)
    },
    getSchemaDataSource(schemaName) {
      API.getSchemaDataSource(schemaName).then(res => {
        const { model } = res
        if (Object.prototype.toString.call(model) === '[object String]') {
          this.textareaDatasource = model
        } else {
          this.textareaDatasource = JSON.stringify(model, null, '\t')
        }
      })
    },
    getSchemaRule(schemaName) {
      API.getSchemaRule(schemaName).then(res => {
        const { model } = res
        if (Object.prototype.toString.call(model) === '[object String]') {
          this.textareaRule = model
        } else {
          this.textareaRule = JSON.stringify(model, null, '\t')
        }
      })
    },
    getSchema() {
      API.getSchema().then(res => {
        this.schemaData = res.model
      })
    },
    handleCurrentChange(val) {
      const data = clone(this.cloneTableData)
      this.tableData = data.splice((val - 1) * this.pageSize, this.pageSize)
    },
    getJobList() {
      API.getJobList().then(res => {
        const data = res.model
        this.total = data.length
        this.cloneTableData = clone(res.model)
        this.tableData = data.splice(0, this.pageSize)
      })
    },
    handlerStop(row) {
      API.getJobStop(row.jobId).then(res => {
        this.$notify({
          title: this.$t('dataScaling').notify.title,
          message: this.$t('dataScaling').notify.delSucMessage,
          type: 'success'
        })
        this.getJobList()
      })
    },
    getJobProgress(row) {
      const { jobId, status } = row
      API.getJobProgress(jobId).then(res => {
        const { model } = res
        this.jobDetail = model
        clearTimeout(timer)
        if (status !== 'STOPPED') {
          timer = setTimeout(() => {
            this.getJobProgress(row)
            clearTimeout(timer)
          }, 2000)
        }
      })
    },
    showSyncTaskProgressDetail(item) {
      this.job = item
      this.DataScalingDialogSyncTaskProgressDetailVisible = true
      this.getJobProgress(item)
    },
    onConfirm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          const { username, password, url, jobCount } = this.form
          const params = {
            ruleConfiguration: {
              source: {
                type: 'shardingSphereJdbc',
                parameter: {
                  dataSource: this.textareaDatasource,
                  rule: this.textareaRule
                }
              },
              target: {
                type: 'jdbc',
                parameter: {
                  username: username,
                  password: password,
                  jdbcUrl: url
                }
              }
            },
            jobConfiguration: {
              concurrency: jobCount
            }
          }
          API.getJobStart(params).then(res => {
            this.DataScalingDialogVisible = false
            this.$notify({
              title: this.$t('dataScaling').notify.title,
              message: this.$t('dataScaling').notify.conSucMessage,
              type: 'success'
            })
            this.clearForm()
            this.getJobList()
          })
        } else {
          return false
        }
      })
    },
    clearForm() {
      this.form = {
        source: '',
        target: 'Proxy',
        username: '',
        password: '',
        url: '',
        jobCount: '3'
      }
    },
    add() {
      this.DataScalingDialogVisible = true
      this.getSchema()
    },
    showDatasource() {
      this.DatasourceVisible = true
    },
    showRule() {
      this.RuleVisible = true
    },
    close() {
      clearTimeout(timer)
    }
  }
}
</script>
<style lang="scss">
  .el-icon-edit {
    cursor: pointer;
  }
  .btn-group {
    margin-bottom: 20px;
  }
  .pagination {
    float: right;
    margin: 10px -10px 10px 0;
  }
  .collapse-row {
    width: 100%;
    .collapse-progress {
      margin-top: 15px;
    }
  }
  .progress-item {
    height: 48px;
    line-height: 48px;
    .collapse-progress {
      margin-top: 15px;
      float: right;
    }
    .collapse-active {
      .el-progress-bar__inner:before {
        content: '';
        opacity: 0;
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: #fff;
        border-radius: 10px;
        animation: progress-active 2s ease-in-out infinite;
      }
    }
  }
  @keyframes progress-active {
    0% {
      opacity: 0.3;
      width: 0;
    }
    to {
      opacity: 0;
      width: 100%;
    }
  }
  .echarts {
    width: 300px;
    height: 200px;
  }
</style>
