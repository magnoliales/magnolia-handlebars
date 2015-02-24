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

The expression language
-----------------------

The `@Query` and `@Value` annotations accept use JSR-245 (http://download.oracle.com/otndocs/jcp/jsp-2.1-fr-eval-spec-oth-JSpec/)
as implemented by JUEL to interpolate values. The following variables are defined:
- `this` - current object
- `node` - current node
- `properties` - the magnolia settings
- `parameters` - query parameters


`@Collection` and `@Query` post filter the results, only accepting mapped classes of compatible type.
For example the following annotation would result in collecting only the child nodes that
are mapped either to `DetailsPage` or one of its subclasses

```
@Collection
DetailPage[] details;
```



To Do
-----

- Put all interfaces and super classes into mixin set of all nodes created by magnolia
- Test references and reference resolution
- Add parent resolution