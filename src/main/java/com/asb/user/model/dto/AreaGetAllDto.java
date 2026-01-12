package com.asb.user.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AreaGetAllDto {

    private Long id;
    private String description;
    private String status;

}
