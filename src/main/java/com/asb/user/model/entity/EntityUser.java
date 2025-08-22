package com.asb.user.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "user_app")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(name = "name_", nullable = false, length = 255)
    private String name;

    @Column(name = "login", unique = true, nullable = false, length = 255)
    private String login;

    @Column(name = "password", nullable = false, length = 150)
    private String password;

    @Column(name = "email", unique = true, nullable = false, length = 255)
    private String email;

    @ManyToOne
    @JoinColumn(name="rol_id", nullable=false)
    private EntityRol rol;

    @ManyToOne
    @JoinColumn(name="position_id", nullable=false)
    private EntityPosition position;

    @ManyToOne
    @JoinColumn(name="company_id", nullable=false)
    private EntityCompany company;

    @ManyToOne
    @JoinColumn(name="area_id", nullable=false)
    private EntityArea area;

    @Column(name = "status", nullable = false, length = 255)
    private String status;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;


}
