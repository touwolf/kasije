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

package com.kasije.core.auth;

import com.kasije.core.tpl.TemplateData;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class AuthUser
{
    private String name;

    private String email;

    private String session;

    private AuthVendor authVendor;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getSession()
    {
        return session;
    }

    public void setSession(String session)
    {
        this.session = session;
    }

    public AuthVendor getAuthVendor()
    {
        return authVendor;
    }

    public void setAuthVendor(AuthVendor authVendor)
    {
        this.authVendor = authVendor;
    }

    public TemplateData toTemplateData()
    {
        TemplateData data = new TemplateData();
        data.setName("__user");
        data.setText(name);
        data.setAttribute("name", name);
        data.setAttribute("email", email);
        data.setAttribute("gravatarHash", md5Hex(email));

        List<String> roles = Arrays.asList("admin", "pages");//TODO: from storage
        roles.forEach(role ->
        {
            TemplateData roleData = new TemplateData();
            roleData.setName("role");
            roleData.setText(role);

            data.addChild(roleData);
        });

        return data;
    }

    private static String md5Hex (String message)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");

            return hex (md.digest(message.getBytes("CP1252")));
        }
        catch (NoSuchAlgorithmException  | UnsupportedEncodingException e) {}

        return null;
    }

    private static String hex(byte[] array)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i)
        {
            String hex = Integer
                    .toHexString((array[i] & 0xFF) | 0x100)
                    .substring(1, 3);

            sb.append(hex);
        }

        return sb.toString();
    }
}
