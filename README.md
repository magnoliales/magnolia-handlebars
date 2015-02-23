magnolia-handlebars
===================

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
 
To Do
-----

- Put all interfaces and super classes into mixin set of all nodes created by magnolia
- Add automatic mapping to @Collection fetched my mixin type only (need to fix it).
- Add scope, order, offset and limit parameters
- Add query parameter to @Collection to use custom queries,
  use some kind of expression language
- Test references and reference resolution
- Add parent resolution, and maybe xpath
- Add simple availability for hierarchies
- Add @Value mapper to map things from node