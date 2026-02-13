package com.asb.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RolGetAllDto {

    private long id;
    private String name;
    private String status;
    private List<PermissionListDto> permissions;
    private Integer numberPermissions;
}
