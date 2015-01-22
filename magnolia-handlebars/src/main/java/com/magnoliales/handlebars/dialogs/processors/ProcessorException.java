package com.magnoliales.handlebars.dialogs.processors;

public class ProcessorException extends RuntimeException {

    public ProcessorException(String message) {
        super(message);
    }

    public ProcessorException(String message, Throwable cause) {
        super(message, cause);
    }
}
