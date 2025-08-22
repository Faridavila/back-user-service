package com.asb.user.model.dto;

import com.asb.user.model.entity.EntityMenuType;
import com.asb.user.model.entity.EntityPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MenuGetAllDto {

    private long id;
    private String name;
    private String description;
    private String shortName;
    private EntityMenuType menuTypeId;
    private Long fatherMenuId;
    private String status;


}
