package com.kasije.freemarker;

import com.kasije.core.RequestContext;
import com.kasije.core.tpl.TemplateEngine;
import com.kasije.core.WebSite;
import com.kasije.freemarker.data.DataTemplateModel;
import freemarker.template.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import org.bridje.ioc.Component;

@Component
public class FreemarkerTemplateEngine implements TemplateEngine
{
    public final static Version VERSION = Configuration.VERSION_2_3_23;

    private final Configuration config;

    public FreemarkerTemplateEngine()
    {
        config = new Configuration(VERSION);
        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    @Override
    public boolean render(RequestContext reqCtx) throws IOException
    {
        WebSite webSite = reqCtx.get(WebSite.class);
        if (webSite == null)
        {
            return false;
        }

        config.setDirectoryForTemplateLoading(webSite.getFile());

        String page = "index";

        Template template = config.getTemplate(page + ".ftl");
        PrintWriter writer = reqCtx.get(HttpServletResponse.class).getWriter();

        DataTemplateModel dataModel = DataTemplateModel.getDataModel(webSite.getFile(), page);

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
