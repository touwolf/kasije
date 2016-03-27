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

package com.kasije.freemarker.data.impl;

import com.kasije.freemarker.data.TemplateModelBuilder;
import freemarker.ext.dom.NodeModel;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import java.io.File;
import org.bridje.ioc.Component;

/**
 *
 */
@Component
public class XmlTemplateModel implements TemplateHashModel, TemplateModelBuilder
{
    private final NodeModel nodeModel;

    public XmlTemplateModel()
    {
        nodeModel = null;
    }

    private XmlTemplateModel(NodeModel nodeModel)
    {
        this.nodeModel = nodeModel;
    }

    @Override
    public TemplateModel get(String key) throws TemplateModelException
    {
        if (nodeModel != null)
        {
            return nodeModel.get(key);
        }

        return null;
    }

    @Override
    public boolean isEmpty() throws TemplateModelException
    {
        return nodeModel == null || nodeModel.isEmpty();
    }

    @Override
    public TemplateHashModel parse(File path, String name)
    {
        try
        {
            NodeModel nodeModel = NodeModel.parse(new File(path, name + ".xml"));
            return new XmlTemplateModel(nodeModel);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
