package com.kasije.freemarker.data.impl;

import com.kasije.freemarker.data.TemplateModelBuilder;
import freemarker.ext.dom.NodeModel;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import java.io.File;
import org.bridje.ioc.Component;

@Component
public class XmlTemplateModel implements TemplateHashModel, TemplateModelBuilder
{
    private final NodeModel nodeModel;

    public XmlTemplateModel()
    {
        nodeModel = null;
    }

    public XmlTemplateModel(NodeModel nodeModel)
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
