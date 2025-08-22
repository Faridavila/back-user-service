/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asb.user.exception;

/**
 *
 * @author manuelm
 */
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CustomErrorException extends RuntimeException {
    private HttpStatus status = null;

    private Object data = null;

    public CustomErrorException() {
        super();
    }

    public CustomErrorException(
            String message
    ) {
        super(message);
    }

    public CustomErrorException(
            HttpStatus status,
            String message
    ) {
        this(message);
        this.status = status;
    }

    public CustomErrorException(
            HttpStatus status,
            String message,
            Object data
    ) {
        this(
                status,
                message
        );
        this.data = data;
    }
}