package com.asb.user.repository;

import com.asb.user.model.entity.EntityArea;
import com.asb.user.model.entity.EntityRol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface IRolRepository extends JpaRepository<EntityRol, Long> {


    Optional<EntityRol> findByName(String name);
    @Override
    Page<EntityRol> findAll(Pageable pageable);

    List<EntityRol> findByStatus(String status);


    Page<EntityRol> findByStatus(String status, Pageable pageable);

    Page<EntityRol> findByIdOrNameContainingIgnoreCaseAndStatus(
            Long id,
            String description,
            String status,
            Pageable pageable
    );
}
