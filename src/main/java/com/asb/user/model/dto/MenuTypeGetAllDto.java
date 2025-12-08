package com.asb.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MenuTypeGetAllDto {

    private long id;
    private String description;
    private String status;

}
