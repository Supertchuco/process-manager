package com.process.processmanagerapi.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotAuthorizedToIncludeProcessOpnionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserNotAuthorizedToIncludeProcessOpnionException(final String message) {
        super(message);
    }
}

