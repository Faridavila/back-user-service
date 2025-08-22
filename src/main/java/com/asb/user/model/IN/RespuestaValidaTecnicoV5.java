package com.asb.user.model.IN;

import lombok.Data;

@Data
public class RespuestaValidaTecnicoV5 {

    private long code;
    private String msg;
    private ObjectDTO object;
}
