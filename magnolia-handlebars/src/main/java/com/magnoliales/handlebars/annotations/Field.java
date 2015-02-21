package com.magnoliales.handlebars.annotations;

import com.magnoliales.handlebars.utils.FieldDefinitionFactory;
import info.magnolia.ui.form.field.definition.ConfiguredFieldDefinition;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.IllegalFormatException;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field {
    Class<? extends ConfiguredFieldDefinition> definition() default ConfiguredFieldDefinition.class;
    Class<? extends FieldDefinitionFactory> factory() default FieldDefinitionFactory.class;
    String settings() default "";
    boolean inherits() default false;
    Class<? extends Reader> reader() default Reader.class;

    public static interface Reader {
        Object read(Property property) throws IllegalFormatException, RepositoryException;
    }
}
