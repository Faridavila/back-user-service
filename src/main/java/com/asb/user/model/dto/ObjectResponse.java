package com.asb.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ObjectResponse {
    private int statusCode;
    private  String message;
}
