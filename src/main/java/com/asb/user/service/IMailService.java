package com.asb.user.service;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.http.ResponseEntity;

public interface IMailService {

    ResponseEntity forgotPassword(String mailTarget, String password) throws UnirestException;
}
