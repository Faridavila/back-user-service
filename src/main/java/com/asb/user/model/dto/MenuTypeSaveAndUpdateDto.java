package com.asb.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MenuTypeSaveAndUpdateDto {

    private Long id;
    private String description;
    private String status;

}
