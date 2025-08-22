package com.asb.user.service;

import com.asb.user.model.IN.LoginIn;
import com.asb.user.model.OUT.LoginOut;
import com.asb.user.util.LoginMode;

public interface LoginExecutor {

    LoginOut processLogin(LoginMode loginMode, LoginIn loginIn) ;
}