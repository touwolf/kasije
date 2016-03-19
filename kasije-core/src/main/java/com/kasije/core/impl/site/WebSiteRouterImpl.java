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
import com.kasije.core.impl.ConfigCache;
import com.kasije.core.config.RouterConfig;
import com.kasije.core.config.SiteConfig;
import com.kasije.core.config.server.Router;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.Priority;

@Component
@Priority(value = Integer.MAX_VALUE)
public class WebSiteRouterImpl implements WebSiteRouter
{
    private static final Logger LOG = Logger.getLogger(WebSiteRouterImpl.class.getName());

    @Inject
    private ConfigCache config;

    @Override
    public WebSite findWebSite(String serverName) throws IOException
    {
        Router router = config.getRouterConfig().getRouters().stream()
                .filter(r -> serverName.equals(r.getUri()))
                .findAny()
                .orElse(null);

         /* by default in the site is into de ./sites/ */
        String relativePath = "./sites/";

        /* i try to resolved the location where it is hosting by configuration */
        if(null != router && StringUtils.isNotBlank(router.getPath()))
        {
            relativePath = router.getPath();
        }

        SiteConfig siteConfig = config.getSiteConfig(relativePath + serverName);
        return new WebSiteImpl(relativePath + serverName, siteConfig);
    }
}
