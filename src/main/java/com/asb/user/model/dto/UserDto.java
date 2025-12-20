package com.asb.user.model.dto;

import com.asb.user.model.entity.EntityRol;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Data
public class UserDto {

    private Long id;
    private String name;
    private String login;
    private String password;
    private String email;
    private Long rolId;
    private String rolName;
    private Long positionId;
    private String positionName;
    private Long companyId;
    private String companyName;
    private String imageCompany;
    private Long areaId;
    private String areaName;
    private String phone;
    private List<AbilityDto> ability;
    private Date tokenDateExpired;
    private String token;
    private String status;

    public UserDto() {
    }

}