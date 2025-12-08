package com.asb.user.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GgpForgotPasswordDto {

    private String email;
}