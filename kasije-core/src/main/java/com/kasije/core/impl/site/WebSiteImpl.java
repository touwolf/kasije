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

import com.kasije.core.KasijeConfigRepo;
import com.kasije.core.WebFile;
import com.kasije.core.WebPage;
import com.kasije.core.WebSite;
import com.kasije.core.config.Alias;
import com.kasije.core.config.SiteConfig;
import com.kasije.core.impl.files.WebFileImpl;
import com.kasije.core.impl.page.WebPageImpl;
import java.io.File;
import java.util.logging.Logger;
import org.bridje.ioc.Ioc;

/**
 *
 */
class WebSiteImpl implements WebSite
{
    private static final Logger LOG = Logger.getLogger(WebSiteImpl.class.getName());

    private final String name;

    private final File siteFolder;

    private final SiteConfig config;

    public WebSiteImpl(String absolutePath)
    {
        siteFolder = new File(absolutePath);
        if(!siteFolder.exists() || !siteFolder.isDirectory())
        {
            throw new IllegalArgumentException("Web Site " + siteFolder.getName() + " does not exists.");
        }
        this.name = siteFolder.getName();

        KasijeConfigRepo configRepo = Ioc.context().find(KasijeConfigRepo.class);
        SiteConfig siteConfig = configRepo.findConfig(absolutePath, SiteConfig.class);
        if (siteConfig == null)
        {
            siteConfig = new SiteConfig();
        }
        config = siteConfig;
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
    public String getTheme()
    {
        return config.getTheme();
    }

    @Override
    public WebPage findPage(String pagePath)
    {
        File pageFile = new File(siteFolder.getAbsoluteFile() + "/pages/" + pagePath + ".xml");
        if(pageFile.exists() && pageFile.isFile())
        {
            return new WebPageImpl(this, pagePath);
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
}
