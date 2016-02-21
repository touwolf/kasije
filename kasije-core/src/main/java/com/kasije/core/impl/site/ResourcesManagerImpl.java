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

package com.kasije.core.impl.site;

import com.kasije.core.ResourcesManager;
import io.bit3.jsass.CompilationException;
import io.bit3.jsass.Compiler;
import io.bit3.jsass.Options;
import io.bit3.jsass.Output;
import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.bridje.ioc.Component;

/**
 *
 */
@Component
public class ResourcesManagerImpl implements ResourcesManager
{
    private static final Logger LOG = Logger.getLogger(ResourcesManagerImpl.class.getName());

    private static final String SASS_SUFFIX = ".scss";

    private static final String CSS_SUFFIX = ".css";

    private static final String JS_SUFFIX = ".js";

    private static final String MIN_SUFFIX = ".js";

    @Override
    public String getMime(String resourceName)
    {
        String ext = getExtension(resourceName);
        if (ext == null || ext.isEmpty())
        {
            return "text/plain";
        }

        switch (ext)
        {
            case JS_SUFFIX:
            {
                return "text/javascript";
            }
            case SASS_SUFFIX:
            case CSS_SUFFIX:
            {
                return "text/css";
            }
            case ".html":
            {
                return "text/html";
            }
            case ".png":
            {
                return "image/png";
            }
            case ".jpg":
            {
                return "image/jpg";
            }
            case ".ico":
            {
                return "image/ico";
            }
        }

        return "text/plain";
    }

    @Override
    public void processResource(File source, OutputStream outStream) throws IOException
    {
        String sourceName = source.getName();

        if (sourceName.endsWith(SASS_SUFFIX))
        {
            source = sassToCss(source);
            if (source == null)
            {
                throw new IOException("Could not process: " + sourceName);
            }
        }

        if (allowMinify(sourceName) && !isMinified(sourceName))
        {
            source = compress(source);
        }

        IOUtils.copy(new FileInputStream(source), outStream);
    }

    private boolean isMinified(String sourceName)
    {
        String ext = getExtension(sourceName);
        return sourceName.endsWith(MIN_SUFFIX + ext);
    }

    private String getExtension(String sourceName)
    {
        int dotIndex = sourceName.lastIndexOf(".");
        if (dotIndex < 0)
        {
            return "";
        }

        return sourceName.substring(dotIndex);
    }

    private boolean allowMinify(String sourceName)
    {
        return sourceName.endsWith(CSS_SUFFIX) || sourceName.endsWith(JS_SUFFIX);
    }

    private File sassToCss(File source) throws IOException
    {
        String sourceName = source.getName();

        int lastDotIndex = sourceName.lastIndexOf(SASS_SUFFIX);
        String cssName = sourceName.substring(0, lastDotIndex) + CSS_SUFFIX;
        File cssFile = new File(source.getParentFile(), cssName);

        //ONLY FROM CACHE IF WAS MODIFIED AFTER SASS FILE
        if (cssFile.exists() && source.lastModified() <= cssFile.lastModified())
        {
            List<String> lines = IOUtils.readLines(new FileInputStream(cssFile));
            if (lines != null && !lines.isEmpty())
            {
                return cssFile;
            }
        }

        if (!cssFile.exists())
        {
            if (!cssFile.createNewFile())
            {
                LOG.log(Level.SEVERE, "Could not create: {0} to compile sass", cssFile.getAbsolutePath());
                return null;
            }
        }

        Compiler compiler = new Compiler();
        Options options = new Options();//TODO: options
        try
        {
            Output output = compiler.compileFile(source.toURI(), cssFile.toURI(), options);
            IOUtils.copy(new StringReader(output.getCss()), new FileOutputStream(cssFile));

            return cssFile;
        }
        catch (CompilationException ex)
        {
            LOG.log(Level.SEVERE, "Error compiling SASS: " + ex.getMessage());
            return null;
        }
    }

    private File compress(File source)
    {
        //TODO
        return source;
    }
}
