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

package com.kasije.core.impl.page;

import com.kasije.core.WebPage;
import com.kasije.core.WebSite;

/**
 * 
 */
public class WebPageImpl implements WebPage
{
    private final WebSite site;
    
    private final String name;

    public WebPageImpl(WebSite site, String name)
    {
        this.site = site;
        this.name = name;
    }

    @Override
    public WebSite getSite()
    {
        return site;
    }

    @Override
    public String getName()
    {
        return name;
    }
}
