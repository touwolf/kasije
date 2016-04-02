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
public class TemplateListModel implements TemplateSequenceModel, TemplateCollectionModel, TemplateModelIterator
{
    private int cursor = 0;

    private final List<TemplateData> children;

    private final ObjectWrapper wrapper;

    public TemplateListModel(List<TemplateData> children, ObjectWrapper wrapper)
    {
        this.children = children;
        this.wrapper = wrapper;
    }

    @Override
    public TemplateModel get(int index) throws TemplateModelException
    {
        return new TemplateDataModel(children.get(index), wrapper);
    }

    @Override
    public int size() throws TemplateModelException
    {
        return children.size();
    }

    @Override
    public TemplateModelIterator iterator() throws TemplateModelException
    {
        return this;
    }

    @Override
    public TemplateModel next() throws TemplateModelException
    {
        if (cursor < children.size())
        {
            cursor += 1;
            return get(cursor - 1);
        }

        return null;
    }

    @Override
    public boolean hasNext() throws TemplateModelException
    {
        return cursor < children.size();
    }
}
