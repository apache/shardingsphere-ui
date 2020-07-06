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

import API from '@/utils/api'
import ruleConfigApi from '@/views/rule-config/api'

const { getSchema, getSchemaDataSource, getSchemaRule, getSchemaMetadata } = ruleConfigApi

export default {
  getJobServer: (params = {}) => API.get(`/api/shardingscaling`, params),
  postJobServer: (params = {}) => API.post(`/api/shardingscaling`, params),
  getJobStart: (params = {}) =>
    API.post(`/api/shardingscaling/job/start`, params),
  getJobList: (params = {}) =>
    API.get(`/api/shardingscaling/job/list`, params),
  getJobProgress: jobId =>
    API.get(`/api/shardingscaling/job/progress/${jobId}`),
  postJobStop: (params = {}) =>
    API.post(`/api/shardingscaling/job/stop`, params),
  getSchema,
  getSchemaDataSource,
  getSchemaRule,
  getSchemaMetadata
}
