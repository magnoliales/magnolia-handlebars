# magnolia-handlebars

Handlebars rendering for Magnolia CMS

## Pages

All class members of a page should be `private` and should have a getter to return the value.

### @Page

Declares the class as a page that can be created through the Magnolia Pages app. You must provide a `templateScript` which is the name of the handlebars template file used to render the page.
```java
@Page(templateScript = "home-page")
public class HomePage {

}
```

#### parents

To restrict where a page template is available to editors specify `parents` . The page template will only be available to an editor when the page being created is a direct descendant ( in the JCR / pages app ) of a page class specified in `parents`

```java
@Page(templateScript = "sub-home-page", parents = HomePage.class)
public class SubHomePage {

}
```

#### singleton

If only one instance of the page should ever be created, use the singleton parameter.

```java
@Page(templateScript = "home-page", singleton = true)
public class HomePage {

}
```

### @Field

Declares that the class member is mapped to an input field in the Magnolia Pages dialog. A `definition` class, which extends `ConfiguredFieldDefinition` is required so Magnolia knows how to render the field in the dialog. Simple definitions can be configured using the `settings` field. The following example configures a `title` field, which will produce a 2 row text field, or maximum length 100 characters

Fields should always be `private` class members.

```java
@Page(templateScript = "home-page")
public class HomePage {

    @Field(definition = TextFieldDefinition.class, settings = "{ rows : 2, maxLength : 100 }")
    private String title;

    public String getTitle() {
        return title;
    }
}
```

#### settings

A JSON string which is used to configure the provided `ConfiguredFieldDefinition` . This can be used for basic configuration. If more advanced configuration is required then `factory` should be used instead of `definition` and `settings`. The following example configures a `title` field, which will produce a 2 row text field, or maximum length 100 characters.
The `settings` JSON is parsed and then mapped to the new instance of the `definition` class.

```java
@Page(templateScript = "home-page")
public class HomePage {

    @Field(definition = TextFieldDefinition.class, settings = "{ rows : 2, maxLength : 100 }")
    private String title;

    public String getTitle() {
        return title;
    }
}
```

#### inherits

If the page extends another page and the configuration for the field should be the same as in the parent class then `inherits` should be set to true. Using this functionality means the configuration does not need to be repeated.

```java
@Page(templateScript = "campaign-home-page")
public class CampaignHomePage extends HomePage {
    
    @Field(inherits = true)
    private String title;

    public String getTitle() {
        return title;
    }
}
```

#### reader

The `reader` property can be set as a class which extends `PropertyReader`. The JCR property will be passed to this reader, enabling it to converted the property before being set as the member value. This is useful when the value stored in the JCR needs conversion before it is useful. For example a link to a DAM node can be converted to an `Asset` using the `AssetReader`

```java
@Page(templateScript = "home-page")
public class HomePage {
    
    @Field(factory = AssetLinkFieldDefinitionFactory.class, reader = AssetReader.class)
    private Asset image;

    public Asset getImage() {
        return image;
    }
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

```java
@Page(templateScript = "home-page")
public class HomePage {

    @Value("${node.path}")
    private String path;

    public String getPath() {
        return path
    }

    @Query("SELECT * FROM [mgnl:page] AS page WHERE CONTAINS(page.*, '\"${parameters.query}\"~10')")
    private DetailsPage[] children;

    public DetailsPage[] getChildren() {
        return children;
    }
}
```


## Dialogs

By default all fields in the page placed in the 'main' tab of the edit dialog. To create new tabs add a private member which is a class that contains fields. For example, to create a meta tab first create a `Meta` class.

```java
public final class Meta {

    @Field(definition = TextFieldDefinition.class)
    private String description;

    @Field(definition = TextFieldDefinition.class)
    private String keywords;

    public String getDescription() {
        return description;
    }

    public String getKeywords() {
        return keywords;
    }
}
```

Next add the `Meta` class as a private member of the page class

```java
@Page(templateScript = "home-page")
public class HomePage {

    @Field(definition = TextFieldDefinition.class, settings = "{ rows : 2, maxLength : 100 }")
    private String title;

    public String getTitle() {
        return title;
    }

    private Meta meta;

    public Meta getMeta() {
        return meta;
    }

}
```

The dialog for a page of class `HomePage` will now have two tabs, a main tab with an input box for `title`, and a meta tab with input boxes for `description` and `keywords`



## Areas

All class members of an area should be `private` and should have a getter to return the value.

### @Area

The `@Area` annotation is used to define a class as being a Magnolia area. Areas structure the page and control what components editors can place inside them. https://documentation.magnolia-cms.com/display/DOCS/Areas

A class annotated with `@Area` must be a `final` class, as areas cannot be extended.

Similar to `@Page`, `@Area` requires a `templateScript`

Areas support the same @Field annotations as Pages. @todo - Value and query support?

```java
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

All class members of a component should be `private` and should have a getter to return the value.

### @Component

The `@Component` annotation is used to define a class as being a Magnolia component. Components area placed inside areas. https://documentation.magnolia-cms.com/display/DOCS/Components

A class annotated with `@Component` must be a `final` class, as components cannot be extended.

Components support the same @Field annotations as Pages. @todo - Value and query support?

```java
@Component(templateScript = "components/menu-item")
public final class MenuItem {

    @Field(definition = TextFieldDefinition.class)
    private String title;
}
```


## Using Pages, Areas and Components

To add an area to a page add a private member of the area class. In this example the `Menu` class must be annotated with the `@Area` annotation

```java
@Page(templateScript = "home-page")
public class HomePage {

    private Menu menu;

    public Menu getMenu() {
        return menu;
    }
}
```

```java
@Area(templateScript = "areas/menu")
public final class Menu {

}
```

In `home-page.hbs` this area must be rendered using the `{{area}}` helper.

```html
<html>
    <head>
        {{{init}}}
    </head>
    <body>
        {{{area name="menu"}}}
    </body>
</html>
```

To enable components to be added to an area, add a private member array of the component class, and annotate it with the `@Collection` annotation. The component class must be annotated with the `@Component` annotation.

```java
@Area(templateScript = "areas/menu")
public final class Menu {

    @Collection
    private MenuItem[] menuItems

    public MenuItem[] String getMenuItems() {
        return menuItems;
    }
}
```

```java
@Component(templateScript = "components/menu-item")
public final class MenuItem {

    @Field(definition = TextFieldDefinition.class)
    private String title;

    public String getTitle() {
        return title;
    }
}
```

In `menu.hbs` the components must be rendered using the `{{component}}` helper.

```html
<ul>
    {{#each menuItems}}
        <li>{{{component}}}</li>
    {{/each}}
</ul>
```


## Inheritance

@todo


## Template Helpers

Template helpers available within handlebars

### {{init}}

Embeds the JavaScript and CSS needed to edit pages on the author instance of Magnolia CMS

```html
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

```html
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

```html
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