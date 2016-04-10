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

package com.kasije.freemarker;

import com.kasije.core.RequestContext;
import com.kasije.core.WebPage;
import com.kasije.core.WebSite;
import com.kasije.core.tpl.TemplateContext;
import com.kasije.core.tpl.TemplateData;
import com.kasije.core.tpl.TemplateDataBuilder;
import com.kasije.freemarker.model.TemplateDataModel;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;

public class FreemarkerTemplateContext implements TemplateContext
{
    private static final Logger LOG = Logger.getLogger(FreemarkerTemplateContext.class.getName());

    private final static Version VERSION = Configuration.VERSION_2_3_23;

    private final Configuration config;

    public FreemarkerTemplateContext(File path)
    {
        config = new Configuration(VERSION);
        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

        try
        {
            //Web site location resources
            FileTemplateLoader fileLoader = new FileTemplateLoader(path);
            //Core common resources
            ClassTemplateLoader classLoader = new ClassTemplateLoader(WebSite.class, "/");

            config.setTemplateLoader(new MultiTemplateLoader(new TemplateLoader[]
            {
                fileLoader, classLoader
            }));
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Override
    public boolean render(RequestContext reqCtx) throws IOException
    {
        WebPage webPage = reqCtx.get(WebPage.class);
        if (webPage == null)
        {
            return false;
        }

        WebSite webSite = reqCtx.get(WebSite.class);
        if (webSite == null)
        {
            return false;
        }

        Template template = config.getTemplate("page.ftl");//TODO: improve this wiring
        PrintWriter writer = reqCtx.get(HttpServletResponse.class).getWriter();

        TemplateData data = TemplateDataBuilder.parse(reqCtx, webSite, webPage);
        ObjectWrapper wrapper = new DefaultObjectWrapper(VERSION);
        TemplateDataModel dataModel = new TemplateDataModel(data, wrapper);

        try
        {
            template.process(dataModel, writer, wrapper);
            return true;
        }
        catch (TemplateException e)
        {
            throw new IOException(e);
        }
    }
}
