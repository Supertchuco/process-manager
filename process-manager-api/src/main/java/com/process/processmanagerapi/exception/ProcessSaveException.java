package com.process.processmanagerapi.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProcessSaveException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ProcessSaveException(final String message) {
        super(message);
    }
}

