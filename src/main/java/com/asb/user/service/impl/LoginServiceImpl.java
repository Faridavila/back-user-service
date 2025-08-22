package com.asb.user.service.impl;

import com.asb.user.model.IN.LoginIn;
import com.asb.user.model.OUT.LoginOut;
import com.asb.user.service.ILoginService;
import com.asb.user.service.LoginExecutor;
import com.asb.user.util.LoginMode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author manuelm
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LoginServiceImpl implements ILoginService {

    private final LoginExecutor loginExecutor;

    @Override
    public LoginOut login(LoginIn loginIn) {
        return loginExecutor.processLogin(Objects.isNull(loginIn.getLoginMode())
                ? LoginMode.GGP_LOGIN: loginIn.getLoginMode(),loginIn);
    }

}
