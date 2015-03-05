# magnolia-handlebars

Handlebars rendering for Magnolia CMS

## Pages

### @Page

Declares the class as a page that can be created through the Magnolia Pages app. You must provide a `templateScript` which is the name of the handlebars template file used to render the page.
```
@Page(templateScript = "home-page")
public class HomePage {
}
```

#### parents

To extend another page, extend the Java class in the usual manner, and also supply the parent class as `parents` in within the `@Page` annotation
``` 
@Page(templateScript = "campaign-home-page", parents = HomePage.class)
public class CampaignHomePage extends HomePage {
}
```

#### singleton

If only one instance of the page should ever be created, use the singleton parameter.
```
@Page(templateScript = "home-page", singleton = true)
public class HomePage {
}
```

### @Field

Declares that the class member is mapped to an input field in the Magnolia Pages dialog. A `definition` class, which extends `ConfiguredFieldDefinition` is required so Magnolia knows how to render the field in the dialog. Simple definitions can be configured using the `settings` field. The following example configures a `title` field, which will produce a 2 row text field, or maximum length 100 characters

Fields should always be `private` class members.
```
@Page(templateScript = "home-page")
public class HomePage {
    @Field(definition = TextFieldDefinition.class, settings = "{ rows : 2, maxLength : 100 }")
    private String title;
}
```

#### settings

A JSON string which is used to configure the provided `ConfiguredFieldDefinition` . This can be used for basic configuration. If more advanced configuration is required then `factory` should be used instead of `definition` and `settings`. The following example configures a `title` field, which will produce a 2 row text field, or maximum length 100 characters.
The `settings` JSON is parsed and then mapped to the new instance of the `definition` class.
```
@Page(templateScript = "home-page")
public class HomePage {
    @Field(definition = TextFieldDefinition.class, settings = "{ rows : 2, maxLength : 100 }")
    private String title;
}
``` 

#### inherits

If the page extends another page and the configuration for the field should be the same as in the parent class then `inherits` should be set to true. Using this functionality means the configuration does not need to be repeated.
  
``` 
@Page(templateScript = "campaign-home-page", parents = HomePage.class)
public class CampaignHomePage extends HomePage {
    @Field(inherits = true)
        private String title;
}
```

#### reader

The `reader` property can be set as a class which extends `PropertyReader`. The JCR property will be passed to this reader, enabling it to converted the property before being set as the member value. This is useful when the value stored in the JCR needs conversion before it is useful. For example a link to a DAM node can be converted to an `Asset` using the `AssetReader`
```
@Page(templateScript = "home-page")
public class HomePage {
    @Field(factory = AssetLinkFieldDefinitionFactory.class, reader = AssetReader.class)
    private Asset image;
}
```




### @Value @Query

The `@Query` and `@Value` annotations accept use JSR-245 (http://download.oracle.com/otndocs/jcp/jsp-2.1-fr-eval-spec-oth-JSpec/)
as implemented by JUEL to interpolate values. The following variables are defined:
- `this` - current object
- `node` - current node
- `properties` - the magnolia settings
- `parameters` - query parameters

The following example will store the JCR node path to `path` , and  @todo - query `children`

```
public class HomePage {

    @Value("${node.path}")
    private String path;

    @Query("SELECT * FROM [mgnl:page] AS page WHERE CONTAINS(page.*, '\"${parameters.query}\"~10')")
    private DetailsPage[] children;
}
```


## Dialogs

@todo

## Areas
 
### @Area

The `@Area` annotation is used to define a class as being a Magnolia area. Areas structure the page and control what components editors can place inside them. https://documentation.magnolia-cms.com/display/DOCS/Areas

A class annotated with `@Area` must be a `final` class, as areas cannot be extended.
 
Similar to `@Page`, `@Area` requires a `templateScript`

Areas support the same @Field annotations as Pages. @todo - Value and query support?
```
@Area(templateScript = "areas/menu")
public final class Menu {

    @Field(definition = TextFieldDefinition.class)
    private String title;

    public String getTitle() {
        return title;
    }
}
```

## Components

### @Component

The `@Component` annotation is used to define a class as being a Magnolia component. Components can be placed inside areas, or directly on pages. @todo - is this true?  https://documentation.magnolia-cms.com/display/DOCS/Components

A class annotated with `@Component` must be a `final` class, as components cannot be extended.

Components support the same @Field annotations as Pages. @todo - Value and query support?
```
@Component(templateScript = "components/menu-item")
public final class MenuItem {
    @Field(definition = TextFieldDefinition.class)
    private String title;
}
```


## Using Pages, Areas and Components

@todo


## Inheritance
 
@todo


## Template Helpers

Template helpers available within handlebars
 
### {{init}}

Embeds the JavaScript and CSS needed to edit pages on the author instance of Magnolia CMS  
```
<html>
    <head>
        {{{init}}}
        ...
    </head>
    <body>
        ....
    </body>
</html>
```

### {{area}}

Renders an area. The area name is the node that contains the area definition such as header, footer.
```
<body>
    <header>
        {{{area name="header"}}}
    </header>
    <footer>
        {{{area name="footer"}}}
    </footer>
</body>
```

### {{component}}

Renders a component. The component rendered will be the component at index `@index`
```
<ul>
    {{#each items}}
        <li>{{{component}}}</li>
    {{/each}}
</ul>
```
### {{img}}

@todo

### {{if_mod2}} {{if_mod3}} {{if_mod4}} 

@todo

### {{debug}}

@todo


## Installation 

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

## The expression language


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