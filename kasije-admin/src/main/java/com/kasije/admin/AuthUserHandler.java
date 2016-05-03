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

import com.kasije.admin.auth.AuthUserProvider;
import com.kasije.admin.config.AuthConfig;
import com.kasije.admin.config.AuthUserConfig;
import com.kasije.core.WebSite;
import com.kasije.core.auth.AuthUser;
import com.kasije.core.config.ConfigProvider;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import org.bridje.http.HttpCookie;
import org.bridje.http.HttpServerContext;
import org.bridje.http.HttpServerHandler;
import org.bridje.http.HttpServerRequest;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;

/**
 *
 */
@Component
@Priority(Integer.MIN_VALUE + 270)
public class AuthUserHandler implements HttpServerHandler
{
    @InjectNext
    private HttpServerHandler handler;

    @Inject
    private AuthUserProvider[] providers;

    @Inject
    private ConfigProvider configProvider;

    @Override
    public boolean handle(HttpServerContext reqCtx) throws IOException
    {
        WebSite site = reqCtx.get(WebSite.class);
        if (site != null && site.isAdmin())
        {
            Map<String, HttpCookie> cookies = reqCtx.get(HttpServerRequest.class).getCookies();

            if (cookies != null && !cookies.isEmpty() && providers.length > 0)
            {
                for (AuthUserProvider provider : providers)
                {
                    AuthUser user = provider.fetchUser(cookies.values());
                    if (user != null)
                    {
                        String path = new File(".").getAbsolutePath() + "/etc";
                        AuthConfig authConfig = configProvider.getConfig(AuthConfig.class, path);
                        if (authConfig != null)
                        {
                            AuthUserConfig userConfig = authConfig.findUserConfig(user.getEmail());
                            if (userConfig != null)
                            {
                                user.setRoles(Arrays.asList(userConfig.getRoles().split(",")));
                            }
                        }

                        reqCtx.set(AuthUser.class, user);
                        break;
                    }
                }
            }
        }

        return handler != null && handler.handle(reqCtx);
    }
}
