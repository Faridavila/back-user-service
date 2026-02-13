package com.asb.user.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Data
public class RolDto {

    private long id;
    private String name;
    private String status;
    private List<PermissionListDto> permissions;

    public RolDto() {
    }

}
