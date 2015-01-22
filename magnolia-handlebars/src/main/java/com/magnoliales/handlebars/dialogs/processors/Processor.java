package com.magnoliales.handlebars.dialogs.processors;

import info.magnolia.ui.form.definition.ConfiguredTabDefinition;

import java.util.Map;

public interface Processor {

    Map<String, ConfiguredTabDefinition> getTabs(Class<?> type) throws ProcessorException;
}
