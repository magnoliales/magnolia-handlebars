package com.magnoliales.handlebars.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Validator {
    Class<? extends Annotation> value();
}
