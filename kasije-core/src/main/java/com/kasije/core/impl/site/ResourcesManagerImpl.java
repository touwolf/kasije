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

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;
import com.kasije.core.ResourcesManager;
import com.kasije.core.config.ConfigProvider;
import com.yahoo.platform.yui.compressor.CssCompressor;
import io.bit3.jsass.CompilationException;
import io.bit3.jsass.Compiler;
import io.bit3.jsass.Options;
import io.bit3.jsass.Output;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.mozilla.javascript.EvaluatorException;

/**
 *
 */
@Component
public class ResourcesManagerImpl implements ResourcesManager
{
    private static final Logger LOG = Logger.getLogger(ResourcesManagerImpl.class.getName());

    @Inject
    private ConfigProvider config;

    private static final String SASS_SUFFIX = ".scss";

    private static final String CSS_SUFFIX = ".css";

    private static final String JS_SUFFIX = ".js";

    private static final String MIN_SUFFIX = ".min";

    @Override
    public String getMime(String resourceName)
    {
        String ext = ResourcesManager.findFileExtension(resourceName, true);
        if (ext.isEmpty())
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
            default:
            {
                return "text/plain";
            }
        }
    }

    @Override
    public void processResource(File src, OutputStream outStream) throws IOException
    {
        String sourceName = src.getName();
        File source = src;

        if (sourceName.endsWith(SASS_SUFFIX))
        {
            source = cssFromSass(source);
            if (source == null)
            {
                throw new IOException("Could not process: " + sourceName);
            }
            sourceName = source.getName();
        }

        if (allowMinify(sourceName) && !isMinified(sourceName))
        {
            source = compress(source);
        }

        IOUtils.copy(new FileInputStream(source), outStream);
    }

    private boolean isMinified(String sourceName)
    {
        String ext = ResourcesManager.findFileExtension(sourceName, true);
        return sourceName.endsWith(MIN_SUFFIX + ext);
    }

    private boolean allowMinify(String sourceName)
    {
        Boolean development = config.getServerConfig().getDevelopment();
        return !development && (sourceName.endsWith(CSS_SUFFIX) || sourceName.endsWith(JS_SUFFIX));
    }

    private File cssFromSass(File source) throws IOException
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

        if (!cssFile.exists() && !cssFile.createNewFile())
        {
            LOG.log(Level.SEVERE, "Could not create: {0} to compile sass", cssFile.getAbsolutePath());
            return null;
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
            LOG.log(Level.SEVERE, "Error compiling SASS: {0}", ex.getMessage());
            return null;
        }
    }

    private File compress(File source)
    {
        String sourceName = source.getName();
        String ext = ResourcesManager.findFileExtension(sourceName, true);

        int lastDotIndex = sourceName.lastIndexOf(ext);
        String minName = sourceName.substring(0, lastDotIndex) + MIN_SUFFIX + ext;
        File minFile = new File(source.getParentFile(), minName);

        try
        {
            //ONLY FROM CACHE IF WAS MODIFIED AFTER SOURCE FILE
            if (minFile.exists())
            {
                long srcLastModified = source.lastModified();
                long minLastModified = minFile.lastModified();
                if (srcLastModified < minLastModified)
                {
                    List<String> lines = IOUtils.readLines(new FileInputStream(minFile));
                    if (lines != null && !lines.isEmpty())
                    {
                        return minFile;
                    }
                }
            }

            if (!minFile.exists() && !minFile.createNewFile())
            {
                LOG.log(Level.SEVERE, "Could not create: {0} to compress resource", minFile.getAbsolutePath());
                return source;
            }

            OutputStream out = new BufferedOutputStream(new FileOutputStream(minFile));

            if (sourceName.endsWith(CSS_SUFFIX))
            {
                Reader reader = new FileReader(source);
                compressCss(reader, out);
            }
            else if (sourceName.endsWith(JS_SUFFIX))
            {
                InputStream in = new BufferedInputStream(new FileInputStream(source));
                compressJs(sourceName, in, out);
            }

            return minFile;
        }
        catch (Exception ex)
        {
            LOG.log(Level.WARNING, "Could not compress: {0}", sourceName);
            return source;
        }
    }

    private void compressCss(Reader reader, OutputStream out) throws IOException
    {
        CssCompressor compressor = new CssCompressor(reader);
        StringWriter writer = new StringWriter();

        compressor.compress(writer, -1);

        String compressed = writer.toString();
        IOUtils.copy(new StringReader(compressed), out);
    }

    private void compressJs(String sourceName, InputStream in, OutputStream out) throws IOException
    {
        CompilerOptions options = new CompilerOptions();
        CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);

        SourceFile input = SourceFile.fromInputStream(sourceName, in, Charset.forName("UTF-8"));

        com.google.javascript.jscomp.Compiler compiler = new com.google.javascript.jscomp.Compiler();
        compiler.compile(Collections.EMPTY_LIST, Collections.singletonList(input), options);

        if (compiler.hasErrors())
        {
            throw new EvaluatorException(compiler.getErrors()[0].description);
        }

        String compressed = compiler.toSource();
        IOUtils.copy(new StringReader(compressed), out);
    }
}
