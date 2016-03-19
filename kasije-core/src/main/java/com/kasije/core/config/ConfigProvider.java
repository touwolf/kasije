/*
 * Copyright 2016 Kasije Framework.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kasije.core.config;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.kasije.core.config.server.RouterConfig;
import com.kasije.core.config.server.ServerConfig;
import com.kasije.core.config.sites.SiteConfig;
import org.bridje.cfg.ConfigRepositoryContext;
import org.bridje.cfg.ConfigService;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;

/**
 *
 */
@Component
public class ConfigProvider
{
    private static final Logger LOG = Logger.getLogger(ConfigProvider.class.getName());

    @Inject
    private ConfigService configService;

    private ServerConfig serverConfig;

    private RouterConfig routerConfig;

    private Map<String, Object> configs = new HashMap<>();

    private Map<String, SiteConfig> siteConfigs = new HashMap<>();

    public RouterConfig getRouterConfig()
    {
        try
        {
            if (null == routerConfig)
            {
                ConfigRepositoryContext configContext = configService.createRepoContext("server");
                routerConfig = configContext.findConfig(RouterConfig.class);
            }
        }
        catch (Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return routerConfig;
    }

    public ServerConfig getServerConfig()
    {
        try
        {
            if (null == serverConfig)
            {
                ConfigRepositoryContext configContext = configService.createRepoContext("server");
                serverConfig = configContext.findConfig(ServerConfig.class);
            }
        }
        catch (Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return serverConfig;
    }

    public SiteConfig getSiteConfig(String absolutePath)
    {
        SiteConfig siteConfig = siteConfigs.get(absolutePath);
        try
        {
            if(null == siteConfig)
            {
                ConfigRepositoryContext configContext = configService.createRepoContext(absolutePath + "/etc/");
                siteConfig = configContext.findConfig(SiteConfig.class);
                siteConfigs.put(absolutePath, siteConfig);
            }
        }
        catch (Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return siteConfig == null ? new SiteConfig() : siteConfig;
    }

    public <T> T getConfig(Class<T> classConfig, String path)
    {
        String key = classConfig.getName() + path;
        T config = (T)configs.get(key);
        try
        {
            if(null == config)
            {
                ConfigRepositoryContext configContext = configService.createRepoContext(path);
                config = configContext.findConfig(classConfig);
                configs.put(key, config);
            }
        }
        catch (Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return config;
    }
}
