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
import com.kasije.core.impl.page.WebPageImpl;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 */
class WebSiteImpl implements WebSite
{
    private static final Logger LOG = Logger.getLogger(WebSiteImpl.class.getName());

    private final String name;
    
    private final File siteFolder;

    public WebSiteImpl(String absolutePath)
    {
        siteFolder = new File(absolutePath);
        if(!siteFolder.exists() || !siteFolder.isDirectory())
        {
            throw new IllegalArgumentException("Web Site " + siteFolder.getName() + " does not exists.");
        }
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
    public WebPage findPage(String pagePath)
    {
        File pageFile = new File(siteFolder.getAbsoluteFile() + "/" + pagePath + ".xml");
        if(pageFile.exists() && pageFile.isFile())
        {
            return new WebPageImpl(this, pagePath);
        }
        return null;
    }

    @Override
    public <T> T findConfig(Class<T> cls)
    {
        try
        {
            //XmlRootElement.name() puede ser ##default coger el nombre de la clase inicial minuscula en ese caso.
            String name = cls.getAnnotation(XmlRootElement.class).name();
            File configFile = new File(siteFolder.getPath() + "/etc/" + name + ".xml");
            //verificar si el fichero exists y es un fichero yno una carpeta.
            JAXBContext ctx = JAXBContext.newInstance(cls);
            Unmarshaller unm = ctx.createUnmarshaller();
            //crear un cache del jaxbcontext por cada clase de forma tal que no sea necesario crearlo cada vez
            return (T)unm.unmarshal(configFile);
            //o tambien seria bueno que este objeto hiciera un cache de las configuraciones
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String getTheme()
    {
        return "default";
    }
}
