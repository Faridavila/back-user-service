package com.asb.user.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityMenu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @Column(name = "name_", nullable = false, length = 255)
    private String name;

    @Column(name = "description", unique = true, nullable = false, length = 255)
    private String description;

    @Column(name = "short_name", nullable = false, length = 150)
    private String shortName;

    @ManyToOne
    @JoinColumn(name="menu_type_id", nullable=false)
    private EntityMenuType menuTypeId;

    @Column(name = "father_menu_id")
    private Long fatherMenuId;

    @Column(name = "status", nullable = false, length = 255)
    private String status;

}
