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
    private String positionDescription;
    private String companyName;
    private String areaDescription;
    private String status;
}