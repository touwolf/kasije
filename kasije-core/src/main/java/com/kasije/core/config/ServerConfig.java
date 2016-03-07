package com.kasije.core.config;

import com.kasije.core.config.global.Connector;
import java.util.List;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "serverConfig")
public class ServerConfig
{
    @XmlElementWrapper(name = "connectorConfigs")
    @XmlElements(
    {
        @XmlElement(name = "connectorConfig", type = Connector.class)
    })
    private List<Connector> connectorConfigs;

    private Integer sessionExp;

    private Boolean development;

    public List<Connector> getConnectorConfigs()
    {
        return connectorConfigs;
    }

    public void setConnectorConfigs(List<Connector> connectorConfigs)
    {
        this.connectorConfigs = connectorConfigs;
    }

    public Integer getSessionExp()
    {
        return sessionExp;
    }

    public void setSessionExp(Integer sessionExp)
    {
        this.sessionExp = sessionExp;
    }

    public Boolean getDevelopment()
    {
        if (development == null)
        {
            development = false;
        }

        return development;
    }

    public void setDevelopment(Boolean development)
    {
        this.development = development;
    }
}
