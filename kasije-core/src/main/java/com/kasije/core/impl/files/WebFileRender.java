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

package com.kasije.core.impl.files;

import com.kasije.core.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.bridje.ioc.Component;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;

/**
 *
 */
@Component
@Priority(Integer.MIN_VALUE + 900)
class WebFileRender implements RequestHandler
{
    @InjectNext
    private RequestHandler handler;

    @Override
    public boolean handle(RequestContext reqCtx) throws IOException
    {
        WebFile webFile = reqCtx.get(WebFile.class);
        if (webFile != null)
        {
            HttpServletResponse resp = reqCtx.get(HttpServletResponse.class);
            IOUtils.copy(new FileInputStream(new File(webFile.getSite().getFile().getAbsolutePath() + "/" + webFile.getRelativePath())), resp.getOutputStream());
            return true;
        }

        return handler != null && handler.handle(reqCtx);
    }
}
