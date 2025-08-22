/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asb.user.model.OUT;

/**
 *
 * @author manuelm
 */
public class GenericResponseOut {

    private String message;
    private int statusCode;

    public GenericResponseOut() {
    }

    public GenericResponseOut(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    

}
