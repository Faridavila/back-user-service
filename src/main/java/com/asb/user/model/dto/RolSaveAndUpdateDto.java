package com.asb.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RolSaveAndUpdateDto {

    private Long id;
    private String name;
    private String status;
    private List<PermissionListDto> permissions;


}
