package com.asb.user.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Data
public class AbilityDto {

    private String action;
    private String subject;

    public AbilityDto() {
    }

}