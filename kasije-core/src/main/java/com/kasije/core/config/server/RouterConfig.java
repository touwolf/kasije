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

package com.kasije.core.config.server;

import com.kasije.core.config.server.model.Router;
import com.kasije.core.config.server.model.Virtual;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "routerConfig")
public class RouterConfig
{
    @XmlElementWrapper(name = "routers")
    @XmlElements(
    {
        @XmlElement(name = "router", type = Router.class)
    })
    private List<Router> routers;

    @XmlElementWrapper(name = "virtuals")
    @XmlElements(
    {
        @XmlElement(name = "virtual", type = Virtual.class)
    })
    private List<Virtual> virtuals;

    @XmlElement(name = "adminPath", type = String.class)
    private String adminPath;

    public List<Router> getRouters()
    {
        if(null == routers)
        {
            routers = new LinkedList<>();
        }

        return routers;
    }

    public void setRouters(List<Router> routers)
    {
        this.routers = routers;
    }

    public List<Virtual> getVirtuals() {
        if(null == virtuals)
        {
            virtuals = new LinkedList<>();
        }

        return virtuals;
    }

    public void setVirtuals(List<Virtual> virtuals) {
        this.virtuals = virtuals;
    }

    public String getAdminPath()
    {
        return adminPath;
    }

    public void setAdminPath(String adminPath)
    {
        this.adminPath = adminPath;
    }
}
