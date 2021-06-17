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

import org.apache.shardingsphere.governance.core.registry.config.node.GlobalNode;
import org.apache.shardingsphere.infra.config.properties.ConfigurationProperties;
import org.apache.shardingsphere.infra.yaml.engine.YamlEngine;
import org.apache.shardingsphere.ui.servcie.RegistryCenterService;
import org.apache.shardingsphere.ui.servcie.ShardingPropertiesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * Implementation of sharding properties service.
 */
@Service
public final class ShardingPropertiesServiceImpl implements ShardingPropertiesService {
    
    @Resource
    private RegistryCenterService registryCenterService;
    
    @Override
    public String loadShardingProperties() {
        return registryCenterService.getActivatedRegistryCenter().get(GlobalNode.getPropsPath());
    }
    
    @Override
    public void updateShardingProperties(final String configData) {
        checkShardingProperties(configData);
        registryCenterService.getActivatedRegistryCenter().persist(GlobalNode.getPropsPath(), configData);
    }
    
    private void checkShardingProperties(final String configData) {
        try {
            Properties props = YamlEngine.unmarshal(configData, Properties.class);
            new ConfigurationProperties(props);
            // CHECKSTYLE:OFF
        } catch (final Exception ex) {
            // CHECKSTYLE:ON
            throw new IllegalArgumentException("Sharding properties is invalid.", ex);
        }
    }
}
