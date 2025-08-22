package com.asb.user.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Data
public class DataLovDto {

    private long id;
    private String descripcion;
    private long padreId;

    public DataLovDto() {
    }

    @Override
    public String toString() {
        return "DataLovDto{" + "id=" + id + ", descripcion=" + descripcion + ", padreId=" + padreId + '}';
    }

}
