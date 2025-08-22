package com.asb.user.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MenuDto {

    private Long id;
    private String name;
    private String description;
    private String shortName;
    private Long menuTypeId;
    private Long fatherMenuId;
    private String status;
}
