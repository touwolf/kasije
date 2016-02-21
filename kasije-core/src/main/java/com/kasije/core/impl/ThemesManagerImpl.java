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

package com.kasije.core.impl;

import com.kasije.core.ThemesManager;
import com.kasije.core.WebSiteTheme;
import com.kasije.core.tpl.TemplateEngine;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;

/**
 *
 */
@Component
public class ThemesManagerImpl implements ThemesManager
{
    @Inject
    private TemplateEngine[] tplEngines;

    private final Map<String,WebSiteTheme> themesMap;

    public ThemesManagerImpl()
    {
        themesMap = new ConcurrentHashMap<>();
    }
    
    @Override
    public WebSiteTheme findTheme(String themeName)
    {
        WebSiteTheme theme = themesMap.get(themeName);
        if(theme == null)
        {
            theme = new WebSiteThemeImpl(themeName, tplEngines);
            themesMap.put(themeName, theme);
        }
        return theme;
    }
}
