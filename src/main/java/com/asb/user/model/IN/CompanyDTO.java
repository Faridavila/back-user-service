package com.asb.user.model.IN;

import lombok.Data;

@Data
public class CompanyDTO {
    private int id;
    private int idusuario;
    private int idempresa;
    private String descripcion;
    private String key;
}
