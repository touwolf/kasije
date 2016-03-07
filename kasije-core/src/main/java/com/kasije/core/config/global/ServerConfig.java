package com.kasije.core.config.global;

import org.bridje.cfg.Configuration;
import org.bridje.cfg.adapter.XmlConfigAdapter;

import javax.xml.bind.annotation.*;
import java.util.List;

@Configuration(XmlConfigAdapter.class)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "serverConfig")
public class ServerConfig
{
    @XmlElementWrapper(name = "connectors")
    @XmlElements(
    {
            @XmlElement(name = "connector", type = Connector.class)
    })
    private List<Connector> connectors;

    private Integer sessionExp;

    public List<Connector> getConnectors()
    {
        return connectors;
    }

    public void setConnectors(List<Connector> connectors)
    {
        this.connectors = connectors;
    }

    public Integer getSessionExp()
    {
        return sessionExp;
    }

    public void setSessionExp(Integer sessionExp)
    {
        this.sessionExp = sessionExp;
    }
}
