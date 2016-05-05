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

import com.kasije.core.config.server.RouterConfig;
import com.kasije.core.config.sites.SiteConfig;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.vfs.FileVfsSource;
import org.bridje.vfs.Path;
import org.bridje.vfs.VfsService;
import org.bridje.vfs.VirtualFile;

/**
 *
 */
@Component
public class ConfigProvider
{
    private static final Logger LOG = Logger.getLogger(ConfigProvider.class.getName());

    @Inject
    private VfsService vfsServ;

    private RouterConfig routerConfig;

    private final Map<String, Object> configs = new HashMap<>();

    private final Map<String, SiteConfig> siteConfigs = new HashMap<>();

    public RouterConfig getRouterConfig()
    {
        try
        {
            if (null == routerConfig)
            {
                VirtualFile confFile = vfsServ.findFile("/etc/routerConfig.xml");//TODO: rename
                routerConfig = readConfig(RouterConfig.class, confFile);
            }
        }
        catch (Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return routerConfig;
    }

    public SiteConfig getSiteConfig(String siteName, String absolutePath)
    {
        SiteConfig siteConfig = siteConfigs.get(siteName);
        try
        {
            if(null == siteConfig)
            {
                vfsServ.mount(new Path(absolutePath + "/etc"), new FileVfsSource(new File(absolutePath + "/etc")));

                VirtualFile confFile = vfsServ.findFile("/etc/siteConfig.xml");//TODO: rename
                siteConfig = readConfig(SiteConfig.class, confFile);

                siteConfigs.put(siteName, siteConfig);
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
        Object configObj = configs.get(key);
        T config = null;
        if (configObj != null && classConfig.isAssignableFrom(configObj.getClass()))
        {
            config = classConfig.cast(configObj);
        }

        try
        {
            if (null == config)
            {
                vfsServ.mount(new Path(path), new FileVfsSource(new File(path)));

                VirtualFile confFile = vfsServ.findFile(path);//TODO: rename
                config = readConfig(classConfig, confFile);

                configs.put(key, config);
            }
        }
        catch (Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return config;
    }

    private <T> T readConfig(Class<T> cls, VirtualFile vFile)
    {
        if (vFile == null)
        {
            return null;
        }

        try (InputStream is = vFile.open())
        {
            JAXBContext ctx = JAXBContext.newInstance(cls);
            Unmarshaller unm = ctx.createUnmarshaller();

            return (T) unm.unmarshal(is);
        }
        catch(IOException | JAXBException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return null;
    }
}
