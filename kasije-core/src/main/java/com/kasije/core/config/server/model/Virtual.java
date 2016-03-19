package com.kasije.core.config.server.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Virtual
{
    @XmlAttribute
    private String uri;

    @XmlAttribute
    private String real;

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getReal() {
        return real;
    }

    public void setReal(String real) {
        this.real = real;
    }
}
