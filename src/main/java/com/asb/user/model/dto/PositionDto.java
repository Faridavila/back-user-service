package com.asb.user.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PositionDto {

    private Long id;
    private String description;
    private String status;
}
