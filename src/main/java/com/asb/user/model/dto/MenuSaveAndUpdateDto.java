package com.asb.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MenuSaveAndUpdateDto {

    private Long id;
    private String name;
    private String description;
    private String shortName;
    private Long menuTypeId;
    private Long fatherMenuId;
    private String status;

}
