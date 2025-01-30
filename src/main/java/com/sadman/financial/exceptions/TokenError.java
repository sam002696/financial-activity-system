package com.sadman.financial.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class TokenError extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 14L;


    public TokenError(String s) {
        super(s);
    }

}
