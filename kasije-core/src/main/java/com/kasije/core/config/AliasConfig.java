package com.kasije.core.config;

import javax.xml.bind.annotation.*;
import java.util.LinkedList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "aliasConfig")
public class AliasConfig
{
    @XmlElements(
    {
        @XmlElement(name = "alias", type = Alias.class)
    })
    private List<Alias> alias;

    public List<Alias> getAlias()
    {
        if(null == alias)
        {
            alias = new LinkedList<>();
        }

        return alias;
    }

    public void setAlias(List<Alias> alias)
    {
        this.alias = alias;
    }
}
