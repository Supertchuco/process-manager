package com.process.processmanagerapi.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotAuthorizedAddProcessOpinionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserNotAuthorizedAddProcessOpinionException(final String message) {
        super(message);
    }
}

