package com.asb.user.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "user_app")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "rol_id")
    private Long rolId;

    @Column(name = "position_id")
    private Long positionId;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "area_id")
    private Long areaId;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;


}
