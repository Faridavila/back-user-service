package com.asb.user.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "area")
@Getter
@Setter
public class EntityArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "area_id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;



    public EntityArea() {
    }

    public EntityArea(long id, String description) {
        this.id = id;
        this.description = description;
    }
}
