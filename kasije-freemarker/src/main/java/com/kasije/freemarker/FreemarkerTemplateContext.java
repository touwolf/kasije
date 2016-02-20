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
import com.kasije.core.WebSite;
import com.kasije.core.tpl.TemplateContext;
import com.kasije.freemarker.data.DataTemplateModel;
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

    public final static Version VERSION = Configuration.VERSION_2_3_23;

    private final Configuration config;

    public FreemarkerTemplateContext(File path)
    {
        config = new Configuration(VERSION);
        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        try
        {
            config.setDirectoryForTemplateLoading(path);
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Override
    public boolean render(RequestContext reqCtx) throws IOException
    {
        WebSite webSite = reqCtx.get(WebSite.class);
        if (webSite == null)
        {
            return false;
        }

        String page = "index";//FIXME: from WebPage

        Template template = config.getTemplate(page + ".ftl");
        PrintWriter writer = reqCtx.get(HttpServletResponse.class).getWriter();

        DataTemplateModel dataModel = DataTemplateModel.getDataModel(webSite.getFile(), page);//FIXME: from WebPage

        try
        {
            template.process(dataModel, writer, new DefaultObjectWrapper(VERSION));
            return true;
        }
        catch (TemplateException e)
        {
            throw new IOException(e);
        }
    }
}
