package com.kasije.core.config;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.*;

import com.kasije.core.config.server.Router;
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
}
