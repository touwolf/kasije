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

import com.kasije.core.RequestContext;
import com.kasije.core.WebPage;
import com.kasije.core.WebSite;
import com.kasije.core.auth.AuthUser;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

/**
 *
 */
public class TemplateDataBuilder
{
    public static TemplateData parse(RequestContext reqCtx, WebSite site, WebPage page)
    {
        AuthUser user = null;
        if (site.isAdmin())
        {
            user = reqCtx.get(AuthUser.class);
        }

        File parent = site.getFile();
        String path = page.getRelativePath();

        File file = new File(parent, getPathWithExt(path, "xml"));
        if (file.exists() && file.canRead())
        {
            return parseXML(file, user);
        }

        file = new File(parent, getPathWithExt(path, "json"));
        if (file.exists() && file.canRead())
        {
            return parseJSON(file, user);
        }

        return null;
    }

    private static TemplateData parseXML(File file, AuthUser user)
    {
        try
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(file);
            Element docElement = doc.getDocumentElement();
            docElement.normalize();

            TemplateData data = parseXMLElement(doc.getDocumentElement());
            if (data != null && user != null)
            {
                data.addChild(user.toTemplateData());
            }

            return data;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private static TemplateData parseXMLElement(Element element)
    {
        TemplateData data = new TemplateData();
        data.setName(element.getNodeName());
        data.setText(element.getTextContent());

        NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++)
        {
            Node node = attrs.item(i);
            data.setAttribute(node.getNodeName(), node.getNodeValue());
        }

        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++)
        {
            Node node = children.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            Element child = (Element) node;
            data.addChild(parseXMLElement(child));
        }

        if (data.getTagChildren() != null)
        {
            data.setText(null);
        }

        return data;
    }

    private static TemplateData parseJSON(File file, AuthUser user)
    {
        //TODO
        return null;
    }

    private static String getPathWithExt(String path, String ext)
    {
        if (path.endsWith("." + ext))
        {
            return path;
        }

        if (!path.endsWith("."))
        {
            path += ".";
        }

        if (ext.startsWith("."))
        {
            ext = ext.substring(1);
        }

        return path + ext;
    }
}
