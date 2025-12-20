package com.asb.user.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "company")
@Getter
@Setter
public class EntityCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    @Column(name = "name_", nullable = false)
    private String companyName;

    @Column(name = "nit", nullable = false, unique = true)
    private String nit;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "economic_activity_id")
    private Long economicActivityId;

    @Column(name = "image")
    private String  image;

    @Column(name = "status")
    private String  status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public EntityCompany() {
    }

    public EntityCompany(long id, String companyName) {
        this.id = id;
        this.companyName = companyName;

    }
}
