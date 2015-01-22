package com.magnoliales.handlebars.annotations;

import com.magnoliales.handlebars.dialogs.processors.PageProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Processable(processor = PageProcessor.class)
public @interface Page {

    String templateScript();
    boolean singleton() default false;
}
