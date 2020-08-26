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

package org.apache.shardingsphere.ui.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.orchestration.repository.api.ConfigurationRepository;
import org.apache.shardingsphere.orchestration.repository.api.RegistryRepository;
import org.apache.shardingsphere.orchestration.repository.api.config.OrchestrationCenterConfiguration;
import org.apache.shardingsphere.orchestration.repository.etcd.EtcdRepository;
import org.apache.shardingsphere.orchestration.repository.zookeeper.CuratorZookeeperRepository;
import org.apache.shardingsphere.ui.common.constant.InstanceType;
import org.apache.shardingsphere.ui.common.domain.CenterConfig;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Center factory.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CenterRepositoryFactory {
    
    private static final ConcurrentHashMap<String, RegistryRepository> REGISTRY_CENTER_MAP = new ConcurrentHashMap<>();
    
    private static final ConcurrentHashMap<String, ConfigurationRepository> CONFIG_CENTER_MAP = new ConcurrentHashMap<>();
    
    /**
     * Create registry center instance.
     *
     * @param config registry center config
     * @return registry center
     */
    public static RegistryRepository createRegistryCenter(final CenterConfig config) {
        RegistryRepository result = REGISTRY_CENTER_MAP.get(config.getName());
        if (null != result) {
            return result;
        }
        InstanceType instanceType = InstanceType.nameOf(config.getInstanceType());
        switch (instanceType) {
            case ZOOKEEPER:
                result = new CuratorZookeeperRepository();
                break;
            case ETCD:
                EtcdRepository etcdCenterRepository = new EtcdRepository();
                etcdCenterRepository.setProps(new Properties());
                result = etcdCenterRepository;
                break;
            default:
                throw new UnsupportedOperationException(config.getName());
        }
        result.init(config.getName(), convert(config));
        REGISTRY_CENTER_MAP.put(config.getName(), result);
        return result;
    }
    
    /**
     * Create config center instance.
     * 
     * @param config config center config
     * @return config center
     */
    public static ConfigurationRepository createConfigCenter(final CenterConfig config) {
        ConfigurationRepository result = CONFIG_CENTER_MAP.get(config.getName());
        if (null != result) {
            return result;
        }
        InstanceType instanceType = InstanceType.nameOf(config.getInstanceType());
        switch (instanceType) {
            case ZOOKEEPER:
                result = new CuratorZookeeperRepository();
                break;
            case ETCD:
                EtcdRepository etcdCenterRepository = new EtcdRepository();
                etcdCenterRepository.setProps(new Properties());
                result = etcdCenterRepository;
                break;
            default:
                throw new UnsupportedOperationException(config.getName());
        }
        result.init(config.getName(), convert(config));
        CONFIG_CENTER_MAP.put(config.getName(), result);
        return result;
    }
    
    private static OrchestrationCenterConfiguration convert(final CenterConfig config) {
        OrchestrationCenterConfiguration result = new OrchestrationCenterConfiguration(config.getInstanceType(), config.getServerLists(), new Properties());
        result.getProps().put("digest", config.getDigest());
        return result;
    }
}
