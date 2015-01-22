package com.magnoliales.handlebars.annotations;

import com.magnoliales.handlebars.dialogs.processors.Processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Processable annotations mark classes that contain dialog definitions
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Processable {
    Class<? extends Processor> processor();
}
