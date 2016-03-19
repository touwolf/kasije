package com.kasije.core.config.sites;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.*;

import com.kasije.core.config.sites.model.Alias;
import com.kasije.core.config.sites.model.Theme;
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

    @XmlElement(name = "theme", type = Theme.class)
    private Theme theme;

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

    public Theme getTheme()
    {
        if (theme == null)
        {
            theme = new Theme();
            theme.setName("default");
        }

        return theme;
    }

    public void setTheme(Theme theme)
    {
        this.theme = theme;
    }
}
