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

package com.kasije.admin.config;

import java.util.List;
import javax.xml.bind.annotation.*;
import org.bridje.cfg.Configuration;
import org.bridje.cfg.adapter.XmlConfigAdapter;

/**
 *
 */
@Configuration(XmlConfigAdapter.class)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "authConfig")
public class AuthConfig
{
    @XmlElements(
        @XmlElement(name = "user", type = AuthUserConfig.class)
    )
    private List<AuthUserConfig> userConfigs;

    public List<AuthUserConfig> getUserConfigs()
    {
        return userConfigs;
    }

    public void setUserConfigs(List<AuthUserConfig> userConfigs)
    {
        this.userConfigs = userConfigs;
    }

    public AuthUserConfig findUserConfig(String email)
    {
        if (userConfigs == null || email == null)
        {
            return null;
        }

        for (AuthUserConfig userConfig : userConfigs)
        {
            if (email.equals(userConfig.getEmail()))
            {
                return userConfig;
            }
        }

        return null;
    }
}
