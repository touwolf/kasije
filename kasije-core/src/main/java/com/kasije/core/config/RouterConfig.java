package com.kasije.core.config;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.*;

import com.kasije.core.config.server.Router;
import com.kasije.core.config.server.Virtual;
import org.bridje.cfg.Configuration;
import org.bridje.cfg.adapter.XmlConfigAdapter;

@Configuration(XmlConfigAdapter.class)
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
}
