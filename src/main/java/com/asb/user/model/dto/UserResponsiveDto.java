package com.asb.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponsiveDto {

    private Long id;
    private String name;
    private String login;
    private String password;
    private String email;
    private String rolName;
    private String positionName;
    private String companyName;
    private String areaName;
    private String phone;
    private String status;
}


