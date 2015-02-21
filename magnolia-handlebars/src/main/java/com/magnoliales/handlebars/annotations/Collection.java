package com.magnoliales.handlebars.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Collection {
    public enum Scope { CHILDREN, SUBTREE, ALL };
    Scope scope() default Scope.CHILDREN;
}
