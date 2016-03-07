package com.kasije.core.config;

import com.kasije.core.config.sites.Alias;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.*;
import org.apache.commons.lang.StringUtils;
import org.bridje.cfg.Configuration;
import org.bridje.cfg.adapter.XmlConfigAdapter;

@Configuration(XmlConfigAdapter.class)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "siteConfig")
public class SiteConfig
{
    @XmlElements(
    {
        @XmlElement(name = "alias", type = Alias.class)
    })
    private List<Alias> alias;

    private String theme;

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

    public String getTheme()
    {
        if (StringUtils.isBlank(theme))
        {
            theme = "default";
        }

        return theme;
    }

    public void setTheme(String theme)
    {
        this.theme = theme;
    }
}
