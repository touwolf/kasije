[![Build Status](https://travis-ci.org/touwolf/kasije.svg?branch=master)](https://travis-ci.org/touwolf/kasije)
[![Release](https://img.shields.io/github/release/touwolf/kasije.svg)](https://github.com/touwolf/kasije/releases/)

[![Koding Hackathon Badge](/koding_hackathon_badge.png?raw=true "Koding Hackathon Badge")](https://koding.com/Hackathon)

# kasije

Kasije lightweight cms, evolve the productivity.

> Develop and host multiple websites has never been easier.

### Features

Efficiently develop multiple websites from a single location with shared resources and templates. Define the content of text files in various formats (XML, JSON, YAML, etc.) and update it at any time. Themes can be edited in multiple templating engines (freemarker, velocity, etc.).

**Preprocessing and compression of static resources**

Static resources that require preprocessing (Sass, typescript, CoffeeScript, etc) are processed without your intervention, all you need to do is include them as they are, and Kasije will process them appropriately by their extension. Also the resources that support compression (JS and CSS) are compressed by the system and cached until they return to be modified. (All this features are used on this site you are reading ;)

**Separation of content and presentation**

It is a good design philosophy that we made concrete. You will preserve the semantics of the content, your content will be a piece of structured information. With features such as Kasije theme sharing, you can define and share themes among your sites and still keep all aesthetic details separated from the subject, allowing your information schema to be always readable. Everybody benefits from the separation: the content supplier does not have to be a good designer and vice versa.

**Easy virtual hosting**

Virtual Host refers to running multiples websites on a single physical server, which is not apparent to the end user. Kasije leaves no room for complicated configurations or server restarts, simply add content in the folder with the name of the virtual domain and the virtual host will be ready (as we have done with this website).

**Extensible by plugins**

The CMS supports the implementation of plugins to extend its core features, provide more options for working with templates (engines alternatives to freemarker or velocity), support formats for content editing (alternatives to XML or Json) and support other resource kinds preprocessing.

**Command line interface**   
 
Kasije has multiple command line tools integrated to facilitate the work on the server, its Java based nature makes it run on most platforms and allow you to create new sites, configure and monitor them in the most natural way.              
  
### Dependencies

Kasije depends on the following projects:

 - [Bridje]: A project providing Java API for building Fast, Roboust Java Aplications with Maven.
 - [Jetty]: Jetty provides a Web server and javax.servlet container.
 - [Freemarker]: Apache FreeMarker is a template engine: a Java library to generate text output.
 - [Google Closure]: The Closure Compiler is a tool for making JavaScript download and run faster.
 - [JSass]: jsass is a Java sass compiler using libsass.

[Bridje]: <https://github.com/bridje/bridje-framework>
[Jetty]: <https://eclipse.org/jetty/>
[Freemarker]: <http://freemarker.incubator.apache.org/>
[Google Closure]: <https://developers.google.com/closure/compiler/>
[JSass]: <http://jsass.readthedocs.org/en/latest/>

### Deploy

**Download and install**

Download the TAR file of the latest release (available at 0.0.1), extract it in the designated server, and you will have the following structure:

```sh
- kasije-root
  - lib
  - bin
  - log
  - sites
    - themes
      - default
        - page.ftl
    - localhost
      - etc
      - pages
        - index.xml
```

Inside the "sites" folder you will place your websites, grouped by domain name, with a predefined structure. The TAR contains an example for a simple starting web site, using Freemarker as template engine and XML to define the content. As the example site domain is localhost, you can browse it locally without any further configuration.

**Start and explore**

Inside the "bin" folder are several scripts to control the server:

```sh
- kasije-root
   ...
   - bin
     - start.sh
     - stop.sh
     - restart.sh
   ...
```

Invoke the start command via terminal:

```sh
$ sh bin/start.sh
   > :INFO::main: Logging initialized @168ms
   > :INFO:oejs.Server:main: jetty-9.3.7.v20160115
   > :INFO:oejs.ServerConnector:main: Started ServerConnector@5c30a9b0{HTTP/1.1,[http/1.1]}{0.0.0.0:8080}
   > :INFO:oejs.Server:main: Started @481ms
```

Now navigate to "localhost:8080" on the browser. Voila! We got a web site (dummy of course).

### Developer

**Java based framework**

Kasije is a Java based framework for the web. Its implementations relies in other pure Java components and libraries, providing great multi platform support, performance, maintainability and scalability.

**How it is designed**

Kasije is developed using the chain of responsibility pattern, in order to make it easy for other users to extend or add features. Each request is processed through the chain where a group of handles performs specific actions adding information to the final result.

To extend the system, simply add handlers with the desired functionalities implementing the following interface:
```java
public interface RequestHandler
{
    boolean handle(RequestContext reqCtx) throws IOException;
}
```

Kasije serves a variety of components and content. Among the current available functionalities are to serve sites, static resources, themes and templates.

By simple configurations the service can be tailored, such as adjusting a site route, an alias or the servlet protocol. For example, the following snippet shows how to configure the route for the www.kasije.com site:

```xml
<routerConfig>
   <routers>
       <!--router uri="www.kasije.com" path="/var/www/"></router-->
   </routers>
</routerConfig>
```
To configure an alias, for example, see the code below:

```xml
<aliasConfig>
   <alias path="/home" realpath="/index" />
</aliasConfig>
```

[Bridje]: <https://github.com/bridje/bridje-framework>
