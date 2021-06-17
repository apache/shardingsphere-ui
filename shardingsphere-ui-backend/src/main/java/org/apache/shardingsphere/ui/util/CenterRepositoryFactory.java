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
import org.apache.shardingsphere.governance.repository.api.config.RegistryCenterConfiguration;
import org.apache.shardingsphere.governance.repository.etcd.EtcdRepository;
import org.apache.shardingsphere.governance.repository.spi.RegistryCenterRepository;
import org.apache.shardingsphere.governance.repository.zookeeper.CuratorZookeeperRepository;
import org.apache.shardingsphere.ui.common.constant.InstanceType;
import org.apache.shardingsphere.ui.common.domain.CenterConfig;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Center factory.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CenterRepositoryFactory {
    
    private static final ConcurrentHashMap<String, RegistryCenterRepository> REGISTRY_REPOSITORY_MAP = new ConcurrentHashMap<>();
    
    /**
     * Create registry center repository.
     *
     * @param config center config
     * @return registry repository
     */
    public static RegistryCenterRepository createRegistryRepository(final CenterConfig config) {
        RegistryCenterRepository result = REGISTRY_REPOSITORY_MAP.get(config.getName());
        if (null != result) {
            return result;
        }
        result = createGovernanceRepository(config.getInstanceType());
        result.init(config.getGovernanceName(), convert(config));
        REGISTRY_REPOSITORY_MAP.put(config.getName(), result);
        return result;
    }
    
    private static RegistryCenterConfiguration convert(final CenterConfig config) {
        RegistryCenterConfiguration result = new RegistryCenterConfiguration(config.getInstanceType(), config.getServerLists(), new Properties());
        result.getProps().put("digest", config.getDigest());
        return result;
    }
    
    private static RegistryCenterRepository createGovernanceRepository(final String instanceType) {
        RegistryCenterRepository result;
        InstanceType type = InstanceType.nameOf(instanceType);
        switch (type) {
            case ZOOKEEPER:
                result = new CuratorZookeeperRepository();
                break;
            case ETCD:
                EtcdRepository etcdCenterRepository = new EtcdRepository();
                etcdCenterRepository.setProps(new Properties());
                result = etcdCenterRepository;
                break;
            default:
                throw new UnsupportedOperationException(instanceType);
        }
        return result;
    }
}
