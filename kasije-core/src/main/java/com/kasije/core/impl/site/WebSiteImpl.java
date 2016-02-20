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

import com.kasije.core.WebPage;
import com.kasije.core.WebSite;

import java.io.File;

/**
 *
 */
class WebSiteImpl implements WebSite
{
    private final String name;
    
    private final File siteFolder;

    public WebSiteImpl(String absolutePath, String name)
    {
        this.name = name;
        siteFolder = new File(absolutePath + File.separator + name);
        if(!siteFolder.exists() || !siteFolder.isDirectory())
        {
            throw new IllegalArgumentException("Web Site " + this.name + " does not exists.");
        }
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
    public WebPage findPage(String pagePath)
    {
        File pageFile = new File(siteFolder.getAbsoluteFile() + "/" + pagePath + ".xml");
        if(pageFile.exists() && pageFile.isFile())
        {
            //return new WebPageImpl();
        }
        return null;
    }
}
