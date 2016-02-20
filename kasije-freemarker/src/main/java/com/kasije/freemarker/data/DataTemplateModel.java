package com.kasije.freemarker.data;

import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bridje.ioc.Ioc;

public class DataTemplateModel implements TemplateHashModel
{
    public static DataTemplateModel getDataModel(File path, String name)
    {
        TemplateModelBuilder[] modelBuilders = Ioc.context().findAll(TemplateModelBuilder.class);

        DataTemplateModel model = new DataTemplateModel();
        for (TemplateModelBuilder builder : modelBuilders)
        {
            TemplateHashModel templateModel = builder.parse(path, name);
            if (templateModel != null)
            {
                model.models.add(templateModel);
            }
        }

        return model;
    }

    private List<TemplateHashModel> models = new ArrayList<>();

    @Override
    public TemplateModel get(String key) throws TemplateModelException
    {
        for (TemplateHashModel model : models)
        {
            TemplateModel tplModel = model.get(key);
            if (tplModel != null)
            {
                return tplModel;
            }
        }

        return null;
    }

    @Override
    public boolean isEmpty() throws TemplateModelException
    {
        for (TemplateHashModel model : models)
        {
            if (!model.isEmpty())
            {
                return false;
            }
        }

        return true;
    }
}
