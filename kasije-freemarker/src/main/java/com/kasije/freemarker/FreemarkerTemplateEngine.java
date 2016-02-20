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

package com.kasije.freemarker;

import com.kasije.core.tpl.TemplateContext;
import com.kasije.core.tpl.TemplateEngine;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.bridje.ioc.Component;

@Component
public class FreemarkerTemplateEngine implements TemplateEngine
{
    private Map<File, TemplateContext> contextMap = new HashMap();

    @Override
    public TemplateContext createContext(File path)
    {
        if (!contextMap.containsKey(path))
        {
            contextMap.put(path, new FreemarkerTemplateContext(path));
        }

        return contextMap.get(path);
    }
}
