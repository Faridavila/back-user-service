package com.asb.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RolSaveAndUpdateDto {

    private Long id;
    private String name;
    private String status;

}
