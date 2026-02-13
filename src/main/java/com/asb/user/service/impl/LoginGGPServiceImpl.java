package com.asb.user.service.impl;

import com.asb.user.exception.CustomErrorException;
import com.asb.user.model.IN.LoginIn;
import com.asb.user.model.OUT.LoginOut;
import com.asb.user.model.dto.AbilityDto;
import com.asb.user.model.dto.PermissionListDto;
import com.asb.user.model.dto.UserDto;
import com.asb.user.model.entity.*;
import com.asb.user.repository.*;
import com.asb.user.security.JwtUtil;
import com.asb.user.service.ILoginGGPService;
import com.asb.user.util.LoginMode;
import com.asb.user.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LoginGGPServiceImpl implements ILoginGGPService {

    private final IUserRepository iRepository;
    private final IRolRepository iRolRepository;
    private final ICompanyRepository iCompanyRepository;
    private final IAreaRepository iAreaRepository;
    private final IPositionRepository iPositionRepository;
    private final JwtUtil jwtUtil;
    private final Utils utils;

    private final long EXPIRATION_TIME_LONG = 100_000_000;

    @Override
    public LoginOut login(LoginIn loginIn) {

        if (loginIn.getLoginMode() == LoginMode.BUSSINES_SUITE_LOGIN) {
            throw new CustomErrorException(HttpStatus.NOT_IMPLEMENTED, "Login Business Suite no implementado");
        }

        String input = loginIn.getUsername().trim();
        Optional<EntityUser> userOpt = iRepository.findByEmailOrLoginOrPhone(input);

        if (userOpt.isEmpty()) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Usuario no encontrado");
        }

        EntityUser user = userOpt.get();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(loginIn.getPassword(), user.getPassword())) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Credenciales incorrectas");
        }

        UserDto userDto = mapUserDto(Optional.of(user));

        String token = jwtUtil.generateToken(user.getLogin());
        userDto.setToken(token);
        userDto.setTokenDateExpired(new Date(System.currentTimeMillis() + EXPIRATION_TIME_LONG));
        userDto.setPassword("******");

        List<AbilityDto> abilities = new ArrayList<>();
        abilities.add(new AbilityDto("manage", "all"));
        userDto.setAbility(abilities);

        LoginOut loginOut = new LoginOut();
        loginOut.setData(userDto);
        loginOut.setStatusCode(HttpStatus.OK.value());
        loginOut.setMessage("Login exitoso");
        return loginOut;
    }

    private UserDto mapUserDto(Optional<EntityUser> objectOptional) {
        EntityUser user = objectOptional.get();

        // 🔥 OBTENER EL ROL COMPLETO CON PERMISOS
        EntityRol rol = iRolRepository.findById(user.getRolId())
                .orElse(null);

        String rolName = rol != null ? rol.getName() : "Sin rol";

        String positionName = iPositionRepository.findById(user.getPositionId())
                .map(EntityPosition::getDescription)
                .orElse("Sin cargo");

        String companyName = iCompanyRepository.findById(user.getCompanyId())
                .map(EntityCompany::getCompanyName)
                .orElse("Sin empresa");

        String companyImage = iCompanyRepository.findById(user.getCompanyId())
                .map(EntityCompany::getImage)
                .orElse("");

        String areaName = iAreaRepository.findById(user.getAreaId())
                .map(EntityArea::getDescription)
                .orElse("Sin área");

        // 🔥 MAPEAR PERMISOS DEL ROL
        List<PermissionListDto> permissions = new ArrayList<>();
        if (rol != null && rol.getPermissions() != null) {
            permissions = rol.getPermissions().stream()
                    .map(perm -> PermissionListDto.builder()
                            .permissionId(perm.getId())
                            .permissionName(perm.getName())
                            .permissionPath(perm.getPath())
                            .build())
                    .collect(Collectors.toList());
        }

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .login(user.getLogin())
                .email(user.getEmail())
                .password(user.getPassword())
                .rolId(user.getRolId())
                .rolName(rolName)
                .positionId(user.getPositionId())
                .positionName(positionName)
                .companyId(user.getCompanyId())
                .companyName(companyName)
                .imageCompany(companyImage)
                .areaId(user.getAreaId())
                .areaName(areaName)
                .phone(user.getPhone())
                .status(user.getStatus())
                .permissions(permissions)
                .build();
    }
}