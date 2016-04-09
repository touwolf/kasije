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

import com.kasije.admin.config.AuthConfig;
import com.kasije.admin.config.AuthUserConfig;
import com.kasije.core.auth.AuthUser;
import com.kasije.admin.auth.AuthUserProvider;
import com.kasije.core.RequestContext;
import com.kasije.core.RequestHandler;
import com.kasije.core.WebSite;
import com.kasije.core.config.ConfigProvider;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;

/**
 *
 */
@Component
@Priority(Integer.MIN_VALUE + 270)
public class AuthUserHandler implements RequestHandler
{
    @InjectNext
    private RequestHandler handler;

    @Inject
    private AuthUserProvider[] providers;

    @Inject
    private ConfigProvider configProvider;

    @Override
    public boolean handle(RequestContext reqCtx) throws IOException
    {
        WebSite site = reqCtx.get(WebSite.class);
        if (site != null && site.isAdmin())
        {
            Cookie[] cookies = reqCtx.get(HttpServletRequest.class).getCookies();
            if (cookies != null && cookies.length > 0 && providers.length > 0)
            {
                for (AuthUserProvider provider : providers)
                {
                    AuthUser user = provider.fetchUser(cookies);
                    if (user != null)
                    {
                        String path = site.getFile().getAbsolutePath() + "/etc";
                        AuthConfig authConfig = configProvider.getConfig(AuthConfig.class, path);
                        if (authConfig != null)
                        {
                            AuthUserConfig userConfig = authConfig.findUserConfig(user.getEmail());
                            if (userConfig != null)
                            {
                                user.setRoles(Arrays.asList(userConfig.getRoles().split(",")));
                            }
                        }

                        reqCtx.put(AuthUser.class, user);
                        break;
                    }
                }
            }
        }

        return handler != null && handler.handle(reqCtx);
    }
}
