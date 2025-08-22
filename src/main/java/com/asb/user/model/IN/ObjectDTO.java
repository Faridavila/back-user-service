package com.asb.user.model.IN;


import lombok.Data;

import java.util.List;

@Data
public class ObjectDTO {
    private String token;
    private List<CompanyDTO> companies;
    private String sessionExpiresIn;
}
