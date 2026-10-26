package com.example.weblog.jwt.exception;

import org.springframework.security.core.AuthenticationException;

public class CaptchaVerificationFailedException extends AuthenticationException {
    public CaptchaVerificationFailedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CaptchaVerificationFailedException(String msg) {
        super(msg);
    }
}
