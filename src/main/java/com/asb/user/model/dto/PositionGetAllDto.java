package com.asb.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PositionGetAllDto {

    private Long id;
    private String description;
    private String status;

}
