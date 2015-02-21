package com.magnoliales.handlebars.annotations;

import com.magnoliales.handlebars.utils.FieldDefinitionFactory;
import com.magnoliales.handlebars.utils.PropertyReader;
import info.magnolia.ui.form.field.definition.ConfiguredFieldDefinition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field {
    Class<? extends ConfiguredFieldDefinition> definition() default ConfiguredFieldDefinition.class;
    Class<? extends FieldDefinitionFactory> factory() default FieldDefinitionFactory.class;
    String settings() default "";
    boolean inherits() default false;
    Class<? extends PropertyReader> reader() default PropertyReader.class;
}
