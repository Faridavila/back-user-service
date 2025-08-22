package com.asb.user.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Data
public class RolDto {

    private long id;
    private String name;

    public RolDto() {
    }

}
