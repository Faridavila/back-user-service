package com.asb.user.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "position")
@Getter
@Setter
public class EntityPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;


    public EntityPosition() {
    }

    public EntityPosition(long id, String description) {
        this.id = id;
        this.description = description;
    }
}
