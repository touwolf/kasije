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

import com.kasije.core.WebFile;
import com.kasije.core.WebPage;
import com.kasije.core.WebSite;
import com.kasije.core.config.sites.SiteConfig;
import com.kasije.core.config.sites.model.Alias;
import com.kasije.core.config.sites.model.Theme;
import com.kasije.core.impl.files.WebFileImpl;
import com.kasije.core.impl.page.WebPageImpl;
import java.io.File;

/**
 *
 */
class WebSiteImpl implements WebSite
{
    private final String name;

    private final File siteFolder;

    private SiteConfig config;

    private boolean admin = false;

    public WebSiteImpl(String absolutePath, SiteConfig config)
    {
        this.siteFolder = new File(absolutePath);
        if(!siteFolder.exists() || !siteFolder.isDirectory())
        {
            throw new IllegalArgumentException("Web Site " + siteFolder.getName() + " does not exists.");
        }

        this.config = config;
        this.name = siteFolder.getName();
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public File getFile()
    {
        return siteFolder;
    }

    @Override
    public Theme getTheme()
    {
        return config.getTheme();
    }

    @Override
    public WebPage findPage(String pPath)
    {
        String pagePath = pPath;
        if (!pagePath.startsWith("/"))
        {
            pagePath = "/" + pagePath;
        }

        File pageFile = new File(siteFolder.getAbsoluteFile() + "/pages" + pagePath + ".xml");//FIXME: only XML?
        if(pageFile.exists() && pageFile.isFile())
        {
            return new WebPageImpl(this, pagePath);
        }

        if (isAdmin() && pagePath.startsWith("/admin/"))
        {
            pagePath = pagePath.substring("/admin".length());
            pageFile = new File(siteFolder.getAbsoluteFile() + "/pages/" + pagePath + ".xml");//FIXME: only XML?
            if(pageFile.exists() && pageFile.isFile())
            {
                return new WebPageImpl(this, pagePath);
            }
        }

        return null;
    }

    @Override
    public WebFile findFile(String filePath)
    {
        File file = new File(siteFolder.getAbsoluteFile() + "/pages/" + filePath);
        if(file.exists() && file.isFile())
        {
            return new WebFileImpl(this, filePath);
        }
        return null;
    }

    @Override
    public Alias findAlias(String path)
    {
        return config.getAlias().parallelStream()
            .filter(a -> path.equals(a.getPath()))
            .findFirst()
            .orElse(null);
    }

    @Override
    public boolean isAdmin()
    {
        return admin;
    }

    public void setAdmin(boolean admin)
    {
        this.admin = admin;
    }
}
