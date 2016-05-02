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

import com.kasije.core.WebSiteVirtual;
import com.kasije.core.config.ConfigProvider;
import com.kasije.core.config.server.model.Virtual;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.Priority;

@Component
@Priority(value = Integer.MAX_VALUE)
public class WebSiteVirtualImpl implements WebSiteVirtual
{
    @Inject
    private ConfigProvider config;

    @Override
    public String findRealSiteName(String serverName)
    {
        Virtual virtual = config.getRouterConfig().getVirtuals().stream()
                .filter(r -> serverName.equals(r.getUri()))
                .findAny()
                .orElse(null);

        return null == virtual ? serverName : virtual.getReal();
    }
}
