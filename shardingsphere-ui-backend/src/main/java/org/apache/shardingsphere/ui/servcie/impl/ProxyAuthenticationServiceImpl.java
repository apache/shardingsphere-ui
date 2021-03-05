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

import org.apache.shardingsphere.governance.core.yaml.config.YamlConfigurationConverter;
import org.apache.shardingsphere.ui.servcie.ProxyAuthenticationService;
import org.apache.shardingsphere.ui.servcie.RegistryCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of sharding proxy authentication service.
 */
@Service
public final class ProxyAuthenticationServiceImpl implements ProxyAuthenticationService {
    
    @Autowired
    private RegistryCenterService registryCenterService;
    
    @Override
    public String getAuthentication() {
        return registryCenterService.getActivatedRegistryCenter().get(registryCenterService.getActivatedStateNode().getAuthenticationPath());
    }
    
    @Override
    public void updateAuthentication(final String authentication) {
        checkAuthenticationConfiguration(authentication);
        registryCenterService.getActivatedRegistryCenter()
                .persist(registryCenterService.getActivatedStateNode().getAuthenticationPath(), authentication);
    }
    
    private void checkAuthenticationConfiguration(final String data) {
        try {
            YamlConfigurationConverter.convertUserRule(data);
            // CHECKSTYLE:OFF
        } catch (final Exception ex) {
            // CHECKSTYLE:ON
            throw new IllegalArgumentException("authentication configuration is invalid.");
        }
    }
}
