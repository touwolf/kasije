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

package com.kasije.core.tpl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class TemplateData
{
    private String name;

    private String text;

    private Map<String, String> attributes;

    private Map<String, List<TemplateData>> tagChildren;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getAttribute(String key)
    {
        if (attributes == null || !attributes.containsKey(key))
        {
            return null;
        }

        return attributes.get(key);
    }

    public void setAttribute(String key, String value)
    {
        if (attributes == null)
        {
            attributes = new HashMap<>();
        }

        attributes.put(key, value);
    }

    public List<TemplateData> getChildren(String tag)
    {
        if (tagChildren == null || !tagChildren.containsKey(tag))
        {
            return null;
        }

        return tagChildren.get(tag);
    }

    public void addChild(TemplateData child)
    {
        String tag = child.getName();
        if (tagChildren == null)
        {
            tagChildren = new HashMap<>();
        }
        if (!tagChildren.containsKey(tag))
        {
            tagChildren.put(tag, new LinkedList<>());
        }

        tagChildren.get(tag).add(child);
    }

    public boolean isEmpty()
    {
        return text != null && tagChildren != null && attributes == null;
    }

    public int size()
    {
        int size = 0;
        if (tagChildren != null)
        {
            size += tagChildren.size();
        }

        if (attributes != null)
        {
            size += attributes.size();
        }

        return size;
    }

    public Map<String, String> getAttributes()
    {
        return attributes;
    }

    public Map<String, List<TemplateData>> getTagChildren()
    {
        return tagChildren;
    }
}
