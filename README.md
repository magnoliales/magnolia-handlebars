magnolia-handlebars
===================

Handlebars rendering for Magnolia CMS

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

    <import resource="classpath:/base-handlebars-context.xml" />
    <context:component-scan base-package="templates,components,dialogs"/>

</beans>
```

Templating
----------

Node content is available through `content` variable. You can navigate up the supplier chain with `content.supplier`.

