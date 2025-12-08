package com.asb.user.service;

import com.asb.user.model.IN.LoginIn;
import com.asb.user.model.OUT.LoginOut;


public interface ILoginService {

    public LoginOut login(LoginIn loginIn);
}
