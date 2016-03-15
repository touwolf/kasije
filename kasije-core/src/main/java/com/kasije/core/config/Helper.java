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

import java.io.IOException;
import org.bridje.cfg.ConfigRepositoryContext;
import org.bridje.cfg.ConfigService;
import org.bridje.ioc.Ioc;

/**
 *
 */
public class Helper
{
    private static ConfigService configService;

    private static ConfigService getConfigService()
    {
        if (configService == null)
        {
            configService = Ioc.context().find(ConfigService.class);
        }

        return configService;
    }

    public static <T> T findConfig(String path, Class<T> configClass) throws IOException
    {
        ConfigRepositoryContext configContext = getConfigService().createRepoContext(path);
        return configContext.findConfig(configClass);
    }
}
