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

package com.kasije.core.impl.themes;

import com.kasije.core.RequestContext;
import com.kasije.core.WebSite;
import com.kasije.core.WebSiteTheme;
import com.kasije.core.config.sites.model.Theme;
import com.kasije.core.tpl.TemplateContext;
import com.kasije.core.tpl.TemplateEngine;
import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;

/**
 *
 */
public class WebSiteThemeImpl implements WebSiteTheme
{
    private final String name;

    private final TemplateEngine[] tplEngines;

    private final File file;

    public WebSiteThemeImpl(WebSite webSite, TemplateEngine[] tplEngines)
    {
        this.name = webSite.getTheme().getName();
        this.tplEngines = tplEngines;
        this.file = findThemeFile(webSite);
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

    private File findThemeFile(WebSite webSite)
    {
        Theme themeConfig = webSite.getTheme();
        String name = themeConfig.getName();

        if (StringUtils.isNotBlank(themeConfig.getPath()))
        {
            File file = new File(themeConfig.getPath() + "/" + name);
            if (file.exists() && file.isDirectory())
            {
                return file;
            }
        }

        File file = new File("./themes/" + name);
        if (file.exists() && file.isDirectory())
        {
            return file;
        }

        file = new File(webSite.getFile().getParent(), "themes/" + name);
        if (file.exists())
        {
            return file;
        }

        file = new File(webSite.getFile().getParent(), name);
        if (file.exists())
        {
            return file;
        }

        file = new File(webSite.getFile(), "themes/" + name);
        if (file.exists())
        {
            return file;
        }

        file = new File(webSite.getFile(), name);
        if (file.exists())
        {
            return file;
        }

        return null;
    }
}
