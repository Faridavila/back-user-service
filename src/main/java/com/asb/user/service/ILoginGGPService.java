/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asb.user.service;

import com.asb.user.model.IN.LoginIn;
import com.asb.user.model.OUT.LoginOut;

/**
 *
 * @author manuelm
 */
public interface ILoginGGPService {

    public LoginOut login(LoginIn loginIn);
}
