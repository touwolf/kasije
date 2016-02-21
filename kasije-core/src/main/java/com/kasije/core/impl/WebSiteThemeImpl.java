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

import com.kasije.core.RequestContext;
import com.kasije.core.WebSiteTheme;
import com.kasije.core.tpl.TemplateContext;
import com.kasije.core.tpl.TemplateEngine;
import java.io.File;
import java.io.IOException;

/**
 *
 */
public class WebSiteThemeImpl implements WebSiteTheme
{
    private final String name;

    private final TemplateEngine[] tplEngines;

    private final File file;

    public WebSiteThemeImpl(String name, TemplateEngine[] tplEngines)
    {
        this.name = name;
        this.tplEngines = tplEngines;
        this.file = new File("./sites/themes/" + name);
    }

    @Override
    public String getName()
    {
        return name;
    }
    
    @Override
    public File getFile()
    {
        return file;
    }

    @Override
    public boolean render(RequestContext reqCtx) throws IOException
    {
        for (TemplateEngine engine : tplEngines)
        {
            TemplateContext context = engine.createContext(file);
            if (context.render(reqCtx))
            {
                return true;
            }
        }
        return false;
    }
}
