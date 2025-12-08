package com.asb.user.service.impl;

import com.asb.user.exception.CustomErrorException;
import com.asb.user.model.IN.LoginIn;
import com.asb.user.model.OUT.LoginOut;
import com.asb.user.model.dto.AbilityDto;
import com.asb.user.model.dto.UserDto;
import com.asb.user.model.entity.EntityUser;
import com.asb.user.repository.IUserRepository;
import com.asb.user.security.JwtUtil;
import com.asb.user.service.ILoginGGPService;
import com.asb.user.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LoginGGPServiceImpl implements ILoginGGPService {

    private final IUserRepository iRepository;

    private final JwtUtil jwtUtil;

    private final Utils utils;

    private final long EXPIRATION_TIME_LONG = 100_000_000;

    @Override
    public LoginOut login(LoginIn loginIn) {
        LoginOut loginOut = new LoginOut();
        Boolean isCorrect = false;
        String token = "";

        Optional<EntityUser> objectOptional = iRepository.findByLogin(loginIn.getUsername());
        UserDto objectDtoVo = null;
        if (objectOptional.isPresent()) {
            objectDtoVo = mapUserDto(objectOptional);

            isCorrect = utils.doPasswordsMatch(loginIn.getPassword(), objectDtoVo.getPassword());

            List<AbilityDto> ability = new ArrayList<AbilityDto>();
            ability.add(new AbilityDto("manage", "all"));

            objectDtoVo.setAbility(ability);

            if (isCorrect) {
                objectDtoVo.setTokenDateExpired(new Date(System.currentTimeMillis() + EXPIRATION_TIME_LONG));
                token = jwtUtil.generateToken(loginIn.getUsername());

                objectDtoVo.setToken(token);

                objectDtoVo.setPassword("******");

                loginOut.setData(objectDtoVo);
                loginOut.setStatusCode(HttpStatus.OK.value());
                loginOut.setMessage("success");
            } else {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Error[Credenciales incorrectas]");
            }
        }

        return loginOut;
    }

    private UserDto mapUserDto(Optional<EntityUser> objectUser) {
        UserDto objectDtoVo = new UserDto();
        BeanUtils.copyProperties(objectUser.get(), objectDtoVo);
        objectDtoVo.setRolId(objectUser.get().getRol().getId());
        return objectDtoVo;
    }

}
