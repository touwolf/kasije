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

package com.kasije.admin;

import com.kasije.core.ResourcesManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;

/**
 *
 */
public class ResourcesHelper
{
    public static Resource buildResource(File baseFile, File file)
    {
        List<String> lines;
        try
        {
            lines = IOUtils.readLines(new FileInputStream(file));
        }
        catch (IOException e)
        {
            return null;
        }
        String content = String.join("\n", lines);

        String name = file.getName();
        int lastDotIndex = name.lastIndexOf(".");
        String ext = name.substring(lastDotIndex + 1);

        String path = file.getAbsolutePath();
        path = path.substring(baseFile.getAbsolutePath().length());
        path = path.substring(0, path.length() - name.length());

        Resource resource = new Resource(path, name, findType(ext), content);
        resource.setTags(findTags(ext, lines));

        return resource;
    }

    public static Resource buildImage(File baseFile, File file)
    {
        String path = file.getAbsolutePath();
        path = path.substring(baseFile.getAbsolutePath().length());
        path = path.replaceAll("\\\\", "/");
        if (!path.startsWith("/"))
        {
            path = "/" + path;
        }

        String fileName = file.getName();
        String ext = ResourcesManager.findFileExtension(file.getName(), true);
        int lastDotIndex = fileName.lastIndexOf(ext);
        String minName = fileName.substring(0, lastDotIndex) + ".min" + ext;
        File minFile = new File(file.getParentFile(), minName);
        long srcLastModified = file.lastModified();
        long minLastModified = minFile.lastModified();
        if (!minFile.exists() || srcLastModified < minLastModified)
        {
            minifyImage(file, minFile);
        }

        String minPath = minFile.getAbsolutePath();
        minPath = minPath.substring(baseFile.getAbsolutePath().length());
        minPath = minPath.replaceAll("\\\\", "/");
        if (!minPath.startsWith("/"))
        {
            minPath = "/" + minPath;
        }

        String reducedName = fileName.substring(0, fileName.length() - ext.length());
        if (reducedName.length() > 15)
        {
            reducedName = reducedName.substring(0, 15);
        }

        return new Resource(path, path, minPath, reducedName);
    }

    private static void minifyImage(File file, File minFile)
    {
        if (minFile.exists())
        {
            minFile.delete();
        }

        try
        {
            int scaledWidth = 275;
            int scaledHeight = 185;

            BufferedImage input = ImageIO.read(file);
            BufferedImage output = new BufferedImage(scaledWidth, scaledHeight, input.getType());

            Graphics2D g2d = output.createGraphics();
            g2d.drawImage(input, 0, 0, scaledWidth, scaledHeight, null);
            g2d.dispose();

            String ext = ResourcesManager.findFileExtension(minFile.getName(), false);

            ImageIO.write(output, ext, minFile);
        }
        catch (IOException e)
        {
        }
    }

    private static String findType(String ext)
    {
        if ("js".equalsIgnoreCase(ext))
        {
            return "javascript";
        }

        if ("scss".equalsIgnoreCase(ext))
        {
            return "sass";
        }

        return ext;
    }

    public static List<String> findTags(String ext, List<String> lines)
    {
        if ("xml".equalsIgnoreCase(ext))
        {
            Pattern tagPattern = Pattern.compile("\\w+");

            List<String> tags = new LinkedList<>();
            lines.parallelStream().forEach(line ->
            {
                int startIndex = line.indexOf("<");
                if (startIndex < 0)
                {
                    return;
                }

                int spaceIndex = line.indexOf(" ", startIndex);
                if (spaceIndex < 0)
                {
                    spaceIndex = line.length();
                }

                int closeIndex = line.indexOf(">", startIndex);
                if (closeIndex < 0)
                {
                    closeIndex = line.length();
                }

                int endIndex = Math.min(spaceIndex, closeIndex);
                String tag = line.substring(startIndex + 1, endIndex);

                if (!tag.isEmpty() && !tags.contains(tag))
                {
                    Matcher matcher = tagPattern.matcher(tag);

                    if (matcher.matches())
                    {
                        tags.add(tag);
                    }
                }
            });

            return tags;
        }

        return null;
    }

    public static List<File> findFiles(File parent, List<String> extensions)
    {
        if (!parent.exists() || !parent.canRead())
        {
            return Collections.EMPTY_LIST;
        }

        List<File> files = new LinkedList<>();

        File[] children = parent.listFiles(file ->
        {
            return file.isDirectory();
        });
        for (File child : children)
        {
            files.addAll(findFiles(child, extensions));
        }

        children = parent.listFiles(file ->
        {
            String name = file.getName();
            String extension = ResourcesManager.findFileExtension(name, false);
            if (extension.isEmpty() || name.endsWith(".min." + extension))
            {
                return false;
            }

            return extensions.contains(extension);
        });
        files.addAll(Arrays.asList(children));

        return files;
    }

    public static List<Resource> handleSaveThemeResponse(File themeFolder, String resource, Map<String, String[]> params) throws IOException
    {
        if (!params.containsKey("text"))
        {
            return null;
        }

        File resourceFile = new File(themeFolder, resource);
        return handleSaveFileResponse(resourceFile, params.get("text")[0]);
    }

    public static List<Resource> handleSavePageResponse(File pagesFolder, String pageName, Map<String, String[]> params) throws IOException
    {
        if (!params.containsKey("text"))
        {
            return null;
        }

        File pageFile = new File(pagesFolder, pageName);
        return handleSaveFileResponse(pageFile, params.get("text")[0]);
    }

    private static List<Resource> handleSaveFileResponse(File file, String content) throws IOException
    {
        if (!file.exists() || !file.canRead())
        {
            return null;
        }

        FileOutputStream fileOutStream = new FileOutputStream(file);

        IOUtils.write(content, fileOutStream);

        return Collections.emptyList();
    }

    public static Resource createResource(File parent, Map<String, String[]> params) throws Exception
    {
        if (!params.containsKey("fileName") || !params.containsKey("fileType")
            || parent == null || !parent.exists() || !parent.canWrite())
        {
            throw new Exception("Cannot create resource.");
        }

        String name = params.get("fileName")[0];
        String type = params.get("fileType")[0].toLowerCase();
        String extension = findExtension(type);

        String path = "";
        if (params.containsKey("filePath"))
        {
            path = params.get("filePath")[0];
        }

        File parentResource = getResourceParent(parent, path);

        if (!name.endsWith("." + extension.toLowerCase()))
        {
            name += "." + extension.toLowerCase();
        }

        File file = new File(parentResource, name);
        if (!file.exists())
        {
            file.createNewFile();
        }

        FileOutputStream fileOutStream = new FileOutputStream(file);
        String text = getTextContent(type);
        IOUtils.write(text, fileOutStream);

        path = file.getAbsolutePath();
        path = path.substring(parent.getAbsolutePath().length());
        path = path.substring(0, path.length() - name.length());

        return new Resource(path, name, type, text);
    }

    private static String findExtension(String type)
    {
        if ("javascript".equalsIgnoreCase(type))
        {
            return "js";
        }

        if ("sass".equalsIgnoreCase(type))
        {
            return "scss";
        }

        return type;
    }

    private static File getResourceParent(File parent, String path) throws Exception
    {
        File parentResource = parent;
        if (!path.isEmpty())
        {
            parentResource = new File(parent, path);
            if (parentResource.exists() && !parentResource.isDirectory())
            {
                throw new Exception("Cannot create resource.");
            }

            parentResource.mkdirs();
        }

        return parentResource;
    }

    private static String getTextContent(String type)
    {
        if ("ftl".equals(type))
        {
            return "<#ftl encoding=\"UTF-8\">\n<#-- Add content -->";
        }

        if ("xml".equals(type))
        {
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!-- Add content -->";
        }

        return "/* Add content */";
    }
}
