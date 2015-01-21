package com.magnoliales.handlebars.annotations;

import info.magnolia.ui.form.field.definition.ConfiguredFieldDefinition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Value {
    Class<? extends ConfiguredFieldDefinition> fieldDefinition() default ConfiguredFieldDefinition.class;
    String settings() default "";
    boolean inherits() default false;
}
