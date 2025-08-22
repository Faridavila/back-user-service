package com.asb.user.service.impl;

import com.asb.user.model.IN.LoginIn;
import com.asb.user.model.OUT.LoginOut;
import com.asb.user.service.ILoginGGPService;
import com.asb.user.service.LoginStrategy;
import com.asb.user.util.LoginMode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.asb.user.service.impl.LoginExecutorImpl.addLoginStrategy;
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GGPLoginStrategy implements LoginStrategy {

    private final ILoginGGPService iLoginGGPService;

    @PostConstruct
    @Override
    public void apply() {
        addLoginStrategy(LoginMode.GGP_LOGIN,this);
    }

    @Override
    public LoginOut login(LoginIn loginIn) {
        return iLoginGGPService.login(loginIn);
    }
}
