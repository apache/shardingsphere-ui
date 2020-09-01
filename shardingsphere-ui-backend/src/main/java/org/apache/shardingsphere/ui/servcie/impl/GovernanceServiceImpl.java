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

package org.apache.shardingsphere.ui.servcie.impl;

import com.google.common.base.Joiner;
import org.apache.shardingsphere.governance.core.registry.RegistryCenterNodeStatus;
import org.apache.shardingsphere.infra.config.RuleConfiguration;
import org.apache.shardingsphere.masterslave.api.config.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.masterslave.api.config.rule.MasterSlaveDataSourceRuleConfiguration;
import org.apache.shardingsphere.ui.common.dto.InstanceDTO;
import org.apache.shardingsphere.ui.common.dto.SlaveDataSourceDTO;
import org.apache.shardingsphere.ui.servcie.GovernanceService;
import org.apache.shardingsphere.ui.servcie.RegistryCenterService;
import org.apache.shardingsphere.ui.servcie.ShardingSchemaService;
import org.apache.shardingsphere.ui.util.ConfigurationYamlConverter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of governance operation service.
 */
@Service
public final class GovernanceServiceImpl implements GovernanceService {
    
    @Resource
    private RegistryCenterService registryCenterService;
    
    @Resource
    private ShardingSchemaService shardingSchemaService;
    
    @Override
    public Collection<InstanceDTO> getALLInstance() {
        List<String> instanceIds = registryCenterService.getActivatedRegistryCenter().getChildrenKeys(getInstancesNodeFullRootPath());
        Collection<InstanceDTO> result = new ArrayList<>(instanceIds.size());
        for (String instanceId : instanceIds) {
            String value = registryCenterService.getActivatedRegistryCenter().get(registryCenterService.getActivatedStateNode().getInstancesNodeFullPath(instanceId));
            result.add(new InstanceDTO(instanceId, !RegistryCenterNodeStatus.DISABLED.toString().equalsIgnoreCase(value)));
        }
        return result;
    }
    
    @Override
    public void updateInstanceStatus(final String instanceId, final boolean enabled) {
        String value = enabled ? "" : RegistryCenterNodeStatus.DISABLED.toString();
        registryCenterService.getActivatedRegistryCenter().persist(registryCenterService.getActivatedStateNode().getInstancesNodeFullPath(instanceId), value);
    }
    
    @Override
    public Collection<SlaveDataSourceDTO> getAllSlaveDataSource() {
        Collection<SlaveDataSourceDTO> result = new ArrayList<>();
        for (String schemaName : shardingSchemaService.getAllSchemaNames()) {
            String configData = shardingSchemaService.getRuleConfiguration(schemaName);
            if (configData.contains("!SHARDING")) {
                handleShardingRuleConfiguration(result, configData, schemaName);
            } else if (configData.contains("!MASTER_SLAVE")) {
                handleMasterSlaveRuleConfiguration(result, configData, schemaName);
            }
        }
        return result;
    }
    
    @Override
    public void updateSlaveDataSourceStatus(final String schemaNames, final String slaveDataSourceName, final boolean enabled) {
        String value = enabled ? "" : RegistryCenterNodeStatus.DISABLED.toString();
        registryCenterService.getActivatedRegistryCenter().persist(registryCenterService.getActivatedStateNode().getDataSourcesNodeDataSourcePath(schemaNames, slaveDataSourceName), value);
    }
    
    private String getInstancesNodeFullRootPath() {
        String result = registryCenterService.getActivatedStateNode().getInstancesNodeFullPath("");
        return result.substring(0, result.length() - 1);
    }
    
    private void handleShardingRuleConfiguration(final Collection<SlaveDataSourceDTO> slaveDataSourceDTOS, final String configData, final String schemaName) {
        Collection<RuleConfiguration> configurations = ConfigurationYamlConverter.loadRuleConfigurations(configData);
        Collection<MasterSlaveRuleConfiguration> masterSlaveRuleConfigs = configurations.stream().filter(
            config -> config instanceof MasterSlaveRuleConfiguration).map(config -> (MasterSlaveRuleConfiguration) config).collect(Collectors.toList());
        for (MasterSlaveRuleConfiguration masterSlaveRuleConfiguration : masterSlaveRuleConfigs) {
            addSlaveDataSource(slaveDataSourceDTOS, masterSlaveRuleConfiguration, schemaName);
        }
    }
    
    private void handleMasterSlaveRuleConfiguration(final Collection<SlaveDataSourceDTO> slaveDataSourceDTOS, final String configData, final String schemaName) {
        MasterSlaveRuleConfiguration masterSlaveRuleConfiguration = ConfigurationYamlConverter.loadMasterSlaveRuleConfiguration(configData);
        addSlaveDataSource(slaveDataSourceDTOS, masterSlaveRuleConfiguration, schemaName);
    }
    
    private void addSlaveDataSource(final Collection<SlaveDataSourceDTO> slaveDataSourceDTOS, final MasterSlaveRuleConfiguration masterSlaveRuleConfiguration, final String schemaName) {
        Collection<String> disabledSchemaDataSourceNames = getDisabledSchemaDataSourceNames();
        for (MasterSlaveDataSourceRuleConfiguration each : masterSlaveRuleConfiguration.getDataSources()) {
            slaveDataSourceDTOS.addAll(getSlaveDataSourceDTOS(schemaName, disabledSchemaDataSourceNames, each));
        }
    }
    
    private Collection<SlaveDataSourceDTO> getSlaveDataSourceDTOS(final String schemaName, final Collection<String> disabledSchemaDataSourceNames, final MasterSlaveDataSourceRuleConfiguration group) {
        Collection<SlaveDataSourceDTO> result = new LinkedList<>();
        for (String each : group.getSlaveDataSourceNames()) {
            result.add(new SlaveDataSourceDTO(schemaName, group.getMasterDataSourceName(), each, !disabledSchemaDataSourceNames.contains(schemaName + "." + each)));
        }
        return result;
    }
    
    private Collection<String> getDisabledSchemaDataSourceNames() {
        List<String> result = new ArrayList<>();
        List<String> schemaNames = registryCenterService.getActivatedRegistryCenter().getChildrenKeys(registryCenterService.getActivatedStateNode().getDataSourcesNodeFullRootPath());
        for (String schemaName : schemaNames) {
            List<String> dataSourceNames = registryCenterService.getActivatedRegistryCenter().getChildrenKeys(registryCenterService.getActivatedStateNode().getDataSourcesNodeSchemaPath(schemaName));
            for (String dataSourceName : dataSourceNames) {
                String value = registryCenterService.getActivatedRegistryCenter().get(registryCenterService.getActivatedStateNode().getDataSourcesNodeDataSourcePath(schemaName, dataSourceName));
                if (RegistryCenterNodeStatus.DISABLED.toString().equalsIgnoreCase(value)) {
                    result.add(Joiner.on(".").join(schemaName, dataSourceName));
                }
            }
        }
        return result;
    }
}
