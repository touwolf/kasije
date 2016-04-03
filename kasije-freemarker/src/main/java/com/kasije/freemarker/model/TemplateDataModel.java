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

package com.kasije.freemarker.model;

import com.kasije.core.tpl.TemplateData;
import freemarker.template.*;
import java.util.List;

/**
 *
 */
public class TemplateDataModel implements TemplateHashModel, TemplateScalarModel, TemplateSequenceModel,
                                          TemplateCollectionModel, TemplateModelIterator
{
    private final TemplateData data;

    private final ObjectWrapper wrapper;

    private boolean hasIterated;

    public TemplateDataModel(TemplateData data, ObjectWrapper wrapper)
    {
        this.data = data;
        this.wrapper = wrapper;
        hasIterated = false;
    }

    @Override
    public TemplateModel get(String key) throws TemplateModelException
    {
        if (key.startsWith("@"))
        {
            return wrapper.wrap(data.getAttribute(key.substring(1)));
        }

        if (key.equals(data.getName()))
        {
            return this;
        }

        List<TemplateData> children = data.getChildren(key);
        if (children != null)
        {
            if (children.size() == 1)
            {
                return new TemplateDataModel(children.get(0), wrapper);
            }

            return new TemplateListModel(children, wrapper);
        }

        //TODO: check if exception should be throw
        return null;
    }

    @Override
    public boolean isEmpty() throws TemplateModelException
    {
        return data.isEmpty();
    }

    @Override
    public String getAsString() throws TemplateModelException
    {
        return data.getText();
    }

    @Override
    public TemplateModel get(int index) throws TemplateModelException
    {
        return this;
    }

    @Override
    public int size() throws TemplateModelException
    {
        return 1;
    }

    @Override
    public TemplateModelIterator iterator() throws TemplateModelException
    {
        return this;
    }

    @Override
    public TemplateModel next() throws TemplateModelException
    {
        if (hasIterated)
        {
            throw new TemplateModelException("No more elements");
        }

        hasIterated = true;
        return this;
    }

    @Override
    public boolean hasNext() throws TemplateModelException
    {
        return !hasIterated;
    }
}
