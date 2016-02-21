package com.kasije.core.config;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "serverConfig")
public class ServerConfig
{
    @XmlElementWrapper(name = "connectorConfigs")
    @XmlElements(
    {
            @XmlElement(name = "connectorConfig", type = ConnectorConfig.class)
    })
    private List<ConnectorConfig> connectorConfigs;

    private Integer sessionExp;

    public List<ConnectorConfig> getConnectorConfigs()
    {
        return connectorConfigs;
    }

    public void setConnectorConfigs(List<ConnectorConfig> connectorConfigs)
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
}
