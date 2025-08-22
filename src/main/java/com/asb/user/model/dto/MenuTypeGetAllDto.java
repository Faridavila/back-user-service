package com.asb.user.model.dto;

import com.asb.user.model.entity.EntityMenuType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MenuTypeGetAllDto {

    private long id;
    private String description;
    private String status;

}
