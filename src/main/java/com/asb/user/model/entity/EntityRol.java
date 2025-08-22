package com.asb.user.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "rol")
@Getter
@Setter
public class EntityRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private long id;

    @Column(name = "name_")
    private String name;

    @Column(name = "status")
    private String status;


    public EntityRol() {
    }

    public EntityRol(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
