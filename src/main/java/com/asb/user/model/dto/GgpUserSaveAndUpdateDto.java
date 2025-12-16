package com.asb.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GgpUserSaveAndUpdateDto {

    private Long id;
    private String name;
    private String login;
    private String password;
    private String email;
    private Long rolId;
    private Long positionId;
    private Long companyId;
    private Long areaId;
    private String phone;
    private String status;
}