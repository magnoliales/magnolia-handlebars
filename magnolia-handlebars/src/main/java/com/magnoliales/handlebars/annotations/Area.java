package com.magnoliales.handlebars.annotations;

import com.magnoliales.handlebars.dialogs.processors.AreaProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
@Processable(processor = AreaProcessor.class)
public @interface Area {

    String template();
}
