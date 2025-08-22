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
    private EntityRol rol;
    private Long rolId;
    private Long companyId;
    private Long positionId;
    private Long areaId;
    private List<AbilityDto> ability;

    private Date tokenDateExpired;
    private String token;
    private String status;

    public UserDto() {
    }

}