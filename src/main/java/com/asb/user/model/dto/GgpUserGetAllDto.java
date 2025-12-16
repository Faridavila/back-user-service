package com.asb.user.model.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GgpUserGetAllDto {

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
    private Long areaId;
    private String areaName;
    private String phone;
    private String status;


}