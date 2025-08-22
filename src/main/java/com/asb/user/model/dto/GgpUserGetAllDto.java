package com.asb.user.model.dto;

import com.asb.user.model.entity.EntityArea;
import com.asb.user.model.entity.EntityCompany;
import com.asb.user.model.entity.EntityPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class GgpUserGetAllDto {

    private long id;
    private String name;
    private String login;
    private String password;
    private String email;
    private RolDto rol;
    private long rolId;
    private String rolName;
    private EntityPosition position;
    private EntityCompany company;
    private EntityArea Area;
    private List<AbilityDto> ability;

    private Date tokenDateExpired;
    private String token;
    private String status;

    public GgpUserGetAllDto() {
    }

}