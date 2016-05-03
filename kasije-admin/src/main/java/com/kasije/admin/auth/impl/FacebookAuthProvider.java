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

package com.kasije.admin.auth.impl;

import com.kasije.admin.auth.AuthUserProvider;
import com.kasije.core.auth.AuthUser;
import com.kasije.core.auth.AuthVendor;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.exception.FacebookException;
import com.restfb.types.User;
import java.util.Collection;
import org.bridje.http.HttpCookie;
import org.bridje.ioc.Component;

/**
 *
 */
@Component
public class FacebookAuthProvider implements AuthUserProvider
{
    private static final String COOKIE_ID = "atfb";

    @Override
    public AuthUser fetchUser(Collection<HttpCookie> cookies)
    {
        if (cookies == null || cookies.isEmpty())
        {
            return null;
        }

        for (HttpCookie cookie : cookies)
        {
            if (COOKIE_ID.equals(cookie.getName()))
            {
                return fetchFacebookUser(cookie.getValue());
            }
        }

        return null;
    }

    private AuthUser fetchFacebookUser(String accessToken)
    {
        //TODO: this takes too long, maybe cache the access token for a configurable time
        FacebookClient client = new DefaultFacebookClient(accessToken, Version.VERSION_2_5);
        try
        {
            User user = client.fetchObject("me", User.class, Parameter.with("fields", "name,email"));
            if (user != null)
            {
                AuthUser authUser = new AuthUser();
                authUser.setName(user.getName());
                authUser.setEmail(user.getEmail());
                authUser.setAuthVendor(AuthVendor.FACEBOOK);
                authUser.setSession(accessToken);

                return authUser;
            }
        }
        catch (FacebookException ex)
        {
        }

        return null;
    }
}
