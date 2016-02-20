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

package com.kasije.freemarker.data;

import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bridje.ioc.Ioc;

/**
 *
 */
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
