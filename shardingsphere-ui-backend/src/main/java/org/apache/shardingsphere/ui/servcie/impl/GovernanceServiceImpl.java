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
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.shardingsphere.governance.core.registry.state.ResourceState;
import org.apache.shardingsphere.governance.core.registry.state.node.StatesNode;
import org.apache.shardingsphere.infra.config.RuleConfiguration;
import org.apache.shardingsphere.infra.yaml.config.YamlRuleConfiguration;
import org.apache.shardingsphere.infra.yaml.engine.YamlEngine;
import org.apache.shardingsphere.infra.yaml.swapper.YamlRuleConfigurationSwapperEngine;
import org.apache.shardingsphere.readwritesplitting.api.ReadwriteSplittingRuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.rule.ReadwriteSplittingDataSourceRuleConfiguration;
import org.apache.shardingsphere.ui.common.dto.InstanceDTO;
import org.apache.shardingsphere.ui.common.dto.ReadDataSourceDTO;
import org.apache.shardingsphere.ui.servcie.GovernanceService;
import org.apache.shardingsphere.ui.servcie.RegistryCenterService;
import org.apache.shardingsphere.ui.servcie.ShardingSchemaService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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
            String value = registryCenterService.getActivatedRegistryCenter().get(StatesNode.getProxyNodePath(instanceId));
            result.add(new InstanceDTO(instanceId, !ResourceState.DISABLED.toString().equalsIgnoreCase(value)));
        }
        return result;
    }
    
    @Override
    public void updateInstanceStatus(final String instanceId, final boolean enabled) {
        String value = enabled ? "" : ResourceState.DISABLED.toString();
        registryCenterService.getActivatedRegistryCenter().persist(StatesNode.getProxyNodePath(instanceId), value);
    }
    
    @Override
    public Collection<ReadDataSourceDTO> getAllReadDataSource() {
        Collection<ReadDataSourceDTO> result = new ArrayList<>();
        for (String schemaName : shardingSchemaService.getAllSchemaNames()) {
            String configData = shardingSchemaService.getRuleConfiguration(schemaName);
            if (StringUtils.isEmpty(configData)) {
                continue;
            }
            if (configData.contains("!SHARDING")) {
                handleShardingRuleConfiguration(result, configData, schemaName);
            } else if (configData.contains("!READWRITE_SPLITTING")) {
                handleMasterSlaveRuleConfiguration(result, configData, schemaName);
            }
        }
        return result;
    }
    
    @Override
    public void updateReadDataSourceStatus(final String schemaNames, final String readDataSourceName, final boolean enabled) {
        String value = enabled ? "" : ResourceState.DISABLED.toString();
        registryCenterService.getActivatedRegistryCenter().persist(StatesNode.getDataSourcePath(schemaNames, readDataSourceName), value);
    }
    
    private String getInstancesNodeFullRootPath() {
        String result = StatesNode.getProxyNodePath("");
        return result.substring(0, result.length() - 1);
    }
    
    private void handleShardingRuleConfiguration(final Collection<ReadDataSourceDTO> readDataSourceDTOS, final String configData, final String schemaName) {
        Collection<RuleConfiguration> configurations = getRuleConfigurations(configData);
        Collection<ReadwriteSplittingRuleConfiguration> readWriteSplittingRuleConfigurations = configurations.stream().filter(
            config -> config instanceof ReadwriteSplittingRuleConfiguration).map(config -> (ReadwriteSplittingRuleConfiguration) config).collect(Collectors.toList());
        for (ReadwriteSplittingRuleConfiguration readwriteSplittingRuleConfiguration : readWriteSplittingRuleConfigurations) {
            addSlaveDataSource(readDataSourceDTOS, readwriteSplittingRuleConfiguration, schemaName);
        }
    }
    
    private void handleMasterSlaveRuleConfiguration(final Collection<ReadDataSourceDTO> readDataSourceDTOS, final String configData, final String schemaName) {
        ReadwriteSplittingRuleConfiguration readwriteSplittingRuleConfiguration = loadReadwriteSplittingRuleConfiguration(configData);
        addSlaveDataSource(readDataSourceDTOS, readwriteSplittingRuleConfiguration, schemaName);
    }
    
    private ReadwriteSplittingRuleConfiguration loadReadwriteSplittingRuleConfiguration(final String configData) {
        Collection<RuleConfiguration> ruleConfigurations = getRuleConfigurations(configData);
        Optional<ReadwriteSplittingRuleConfiguration> result = ruleConfigurations.stream().filter(
                each -> each instanceof ReadwriteSplittingRuleConfiguration).map(ruleConfiguration -> (ReadwriteSplittingRuleConfiguration) ruleConfiguration).findFirst();
        Preconditions.checkState(result.isPresent());
        return result.get();
    }
    
    private void addSlaveDataSource(final Collection<ReadDataSourceDTO> readDataSourceDTOS, final ReadwriteSplittingRuleConfiguration readwriteSplittingRuleConfiguration, final String schemaName) {
        Collection<String> disabledSchemaDataSourceNames = getDisabledSchemaDataSourceNames();
        for (ReadwriteSplittingDataSourceRuleConfiguration each : readwriteSplittingRuleConfiguration.getDataSources()) {
            readDataSourceDTOS.addAll(getReadDataSourceDTOS(schemaName, disabledSchemaDataSourceNames, each));
        }
    }
    
    private Collection<ReadDataSourceDTO> getReadDataSourceDTOS(final String schemaName, final Collection<String> disabledSchemaDataSourceNames, 
                                                                final ReadwriteSplittingDataSourceRuleConfiguration group) {
        Collection<ReadDataSourceDTO> result = new LinkedList<>();
        for (String each : group.getReadDataSourceNames()) {
            result.add(new ReadDataSourceDTO(schemaName, group.getWriteDataSourceName(), each, !disabledSchemaDataSourceNames.contains(schemaName + "." + each)));
        }
        return result;
    }

    private Collection<RuleConfiguration> getRuleConfigurations(final String yamlContent) {
        Collection<YamlRuleConfiguration> rules = Strings.isNullOrEmpty(yamlContent)
                ? new LinkedList<>() : YamlEngine.unmarshal(yamlContent, Collection.class);
        return new YamlRuleConfigurationSwapperEngine().swapToRuleConfigurations(rules);
    }
    
    private Collection<String> getDisabledSchemaDataSourceNames() {
        List<String> result = new ArrayList<>();
        List<String> schemaNames = registryCenterService.getActivatedRegistryCenter().getChildrenKeys(StatesNode.getDataNodesPath());
        for (String schemaName : schemaNames) {
            List<String> dataSourceNames = registryCenterService.getActivatedRegistryCenter().getChildrenKeys(StatesNode.getSchemaPath(schemaName));
            for (String dataSourceName : dataSourceNames) {
                String value = registryCenterService.getActivatedRegistryCenter().get(StatesNode.getDataSourcePath(schemaName, dataSourceName));
                if (ResourceState.DISABLED.toString().equalsIgnoreCase(value)) {
                    result.add(Joiner.on(".").join(schemaName, dataSourceName));
                }
            }
        }
        return result;
    }
}
