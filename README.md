magnolia-handlebars
===================

### Current Versions

 - magnolia-handlebars-parent - `1.0.0`


 - magnolia-handlebars - `1.0.0`
 - magnolia-handlebars-example - `1.0.0`
 - magnolia-handlebars-helpers - `1.0.0`
 
### Changelog

 - Created version 1.0.0
 - Added a buildNumber.properties for later use in development builds
 

Installation
------------

Handlebars rendering for Magnolia CMS

For the compilation add the following repositories to your a profile in `settings.xml`
```xml
<settings>
    ...
    <profiles>
        <profile>
            <id>magnolia</id>
            <repositories>
                <repository>
                    <id>magnolia.public.releases</id>
                    <url>https://nexus.magnolia-cms.com/content/repositories/magnolia.public.releases</url>
                </repository>
                <repository>
                    <id>thirdparty</id>
                    <url>https://nexus.magnolia-cms.com/content/repositories/thirdparty</url>
                </repository>
                <repository>
                    <id>thirdparty.customized</id>
                    <url>https://nexus.magnolia-cms.com/content/repositories/thirdparty.customized</url>
                </repository>
                <repository>
                    <id>vaadin-addons</id>
                    <url>http://maven.vaadin.com/vaadin-addons</url>
                </repository>
                <repository>
                    <id>maven.central</id>
                    <url>http://repo1.maven.org/maven2</url>
                </repository>
            </repositories>
        </profile>
    </profiles>
    ...
</settings>
```

Handlebars templates need to be in `classpath:/templates`
 
To enable scanning of your templates, dialogs and components use the following `classpath:/handlebars-context.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
           http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="templates,components,dialogs"/>

</beans>
```

Templating
----------

Node content is available through `content` variable. You can navigate up the supplier chain with `content.supplier`.


Todo
----
* Remove Spring
* Configure templates using OCM annotations