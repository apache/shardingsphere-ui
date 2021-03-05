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
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.apache.shardingsphere.governance.core.yaml.config.YamlConfigurationConverter;
import org.apache.shardingsphere.governance.repository.api.RegistryRepository;
import org.apache.shardingsphere.infra.config.datasource.DataSourceConfiguration;
import org.apache.shardingsphere.ui.servcie.RegistryCenterService;
import org.apache.shardingsphere.ui.servcie.ShardingSchemaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Implementation of sharding schema service.
 */
@Service
public final class ShardingSchemaServiceImpl implements ShardingSchemaService {

    @Resource
    private RegistryCenterService registryCenterService;

    @Override
    public Collection<String> getAllSchemaNames() {
        return registryCenterService.getActivatedRegistryCenter().getChildrenKeys(registryCenterService.getActivatedStateNode().getMetadataNodePath());
    }
    
    @Override
    public String getRuleConfiguration(final String schemaName) {
        return registryCenterService.getActivatedRegistryCenter().get(registryCenterService.getActivatedStateNode().getRulePath(schemaName));
    }
    
    @Override
    public String getDataSourceConfiguration(final String schemaName) {
        return registryCenterService.getActivatedRegistryCenter().get(registryCenterService.getActivatedStateNode().getMetadataDataSourcePath(schemaName));
    }
    
    @Override
    public void updateRuleConfiguration(final String schemaName, final String configData) {
        checkRuleConfiguration(configData);
        persistRuleConfiguration(schemaName, configData);
    }
    
    @Override
    public void updateDataSourceConfiguration(final String schemaName, final String configData) {
        checkDataSourceConfiguration(configData);
        persistDataSourceConfiguration(schemaName, configData);
    }
    
    @Override
    public void addSchemaConfiguration(final String schemaName, final String ruleConfiguration, final String dataSourceConfiguration) {
        checkSchemaName(schemaName, getAllSchemaNames());
        checkRuleConfiguration(ruleConfiguration);
        checkDataSourceConfiguration(dataSourceConfiguration);
        persistRuleConfiguration(schemaName, ruleConfiguration);
        persistDataSourceConfiguration(schemaName, dataSourceConfiguration);
        persistSchemaName(schemaName);
    }
    
    @Override
    public void deleteSchemaConfiguration(final String schemaName) {
        RegistryRepository registryRepository = registryCenterService.getActivatedRegistryCenter();
        String schemaNamePath = registryCenterService.getActivatedStateNode().getSchemaNamePath(schemaName);
        registryRepository.delete(schemaNamePath);
        String schemaNames = registryCenterService.getActivatedRegistryCenter().get(registryCenterService.getActivatedStateNode().getMetadataNodePath());
        List<String> schemaNameList = new ArrayList<>(Splitter.on(",").splitToList(schemaNames));
        schemaNameList.remove(schemaName);
        registryRepository.persist(registryCenterService.getActivatedStateNode().getMetadataNodePath(), Joiner.on(",").join(schemaNameList));
    }

    @Override
    public String getMetadataConfiguration(final String schemaName) {
        return registryCenterService.getActivatedRegistryCenter().get(
                registryCenterService.getActivatedStateNode().getSchemaPath(schemaName));
    }

    private void checkRuleConfiguration(final String configData) {
        try {
            YamlConfigurationConverter.convertRuleConfigurations(configData);
            // CHECKSTYLE:OFF
        } catch (final Exception ex) {
            // CHECKSTYLE:ON
            throw new IllegalArgumentException("rule configuration is invalid.", ex);
        }
    }
    
    private void persistRuleConfiguration(final String schemaName, final String ruleConfiguration) {
        registryCenterService.getActivatedRegistryCenter().persist(registryCenterService.getActivatedStateNode().getRulePath(schemaName), ruleConfiguration);
    }
    
    private void checkDataSourceConfiguration(final String configData) {
        try {
            Map<String, DataSourceConfiguration> dataSourceConfigs = YamlConfigurationConverter.convertDataSourceConfigurations(configData);
            Preconditions.checkState(!dataSourceConfigs.isEmpty(), "data source configuration is invalid.");
            // CHECKSTYLE:OFF
        } catch (final Exception ex) {
            // CHECKSTYLE:ON
            throw new IllegalArgumentException("data source configuration is invalid.", ex);
        }
    }
    
    private void persistDataSourceConfiguration(final String schemaName, final String dataSourceConfiguration) {
        registryCenterService.getActivatedRegistryCenter().persist(registryCenterService.getActivatedStateNode().getMetadataDataSourcePath(schemaName), dataSourceConfiguration);
    }
    
    private void checkSchemaName(final String schemaName, final Collection<String> existedSchemaNames) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(schemaName), "schema name is invalid.");
        Preconditions.checkArgument(!existedSchemaNames.contains(schemaName), "schema name already exists.");
    }
    
    private void persistSchemaName(final String schemaName) {
        RegistryRepository registryRepository = registryCenterService.getActivatedRegistryCenter();
        String schemaPath = registryCenterService.getActivatedStateNode().getMetadataNodePath();
        String schemaNames = registryRepository.get(schemaPath);
        List<String> schemaNameList = Strings.isNullOrEmpty(schemaNames) ? new ArrayList<>() : new ArrayList<>(Splitter.on(",").splitToList(schemaNames));
        if (!schemaNameList.contains(schemaName)) {
            schemaNameList.add(schemaName);
            registryRepository.persist(schemaPath, Joiner.on(",").join(schemaNameList));
        }
    }
}
