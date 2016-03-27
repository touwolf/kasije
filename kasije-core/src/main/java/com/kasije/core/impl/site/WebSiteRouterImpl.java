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

package com.kasije.core.impl.site;

import com.kasije.core.WebSite;
import com.kasije.core.WebSiteRouter;
import com.kasije.core.config.ConfigProvider;
import com.kasije.core.config.server.AdminConfig;
import com.kasije.core.config.server.model.Router;
import com.kasije.core.config.sites.SiteConfig;
import java.io.File;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.Priority;

@Component
@Priority(value = Integer.MAX_VALUE)
public class WebSiteRouterImpl implements WebSiteRouter
{
    private static final String ADMIN_PREFIX = "/admin";

    private static final Logger LOG = Logger.getLogger(WebSiteRouterImpl.class.getName());

    @Inject
    private ConfigProvider config;

    @Override
    public WebSite findWebSite(String serverName)
    {
        Router router = config.getRouterConfig().getRouters().stream()
                .filter(r -> serverName.equals(r.getUri()))
                .findAny()
                .orElse(null);

        /* by default in the site is into de ./sites/ */
        String relativePath = "./sites";

        /* try to resolved the location where it is hosting by configuration */
        if (null != router && StringUtils.isNotBlank(router.getPath()))
        {
            relativePath = router.getPath();
        }

        String path = relativePath + "/" + serverName;

        SiteConfig siteConfig = config.getSiteConfig(path);
        return new WebSiteImpl(path, siteConfig);
    }

    @Override
    public WebSite findAdminWebSite(String pathInfo)
    {
        if (pathInfo.startsWith(ADMIN_PREFIX))
        {
            WebSite adminSite = findAdminSite();
            if (adminSite != null)
            {
                return adminSite;
            }
        }

        return null;
    }

    private WebSite findAdminSite()
    {
        AdminConfig adminConfig = config.getRouterConfig().getAdminConfig();
        if (adminConfig == null)
        {
            return null;
        }

        String path = adminConfig.getPath() + "/" + adminConfig.getName();
        File file = new File(path);
        if (!file.exists() || !file.canRead())
        {
            return null;
        }

        SiteConfig siteConfig = config.getSiteConfig(path);
        WebSiteImpl webSite = new WebSiteImpl(path, siteConfig);
        webSite.setAdmin(true);

        return webSite;
    }

    @Override
    public String findPathInfo(WebSite site, String pathInfo)
    {
        if (site.isAdmin() && pathInfo.startsWith(ADMIN_PREFIX))
        {
            pathInfo = pathInfo.substring(ADMIN_PREFIX.length());
        }

        return pathInfo;
    }
}
