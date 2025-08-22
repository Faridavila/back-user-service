package com.asb.user.repository;

import com.asb.user.model.entity.EntityArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IAreaRepository extends JpaRepository<EntityArea, Long> {


    Optional<EntityArea> findByDescription(String description);


    @Override
    Page<EntityArea> findAll(Pageable pageable);


    List<EntityArea> findByStatus(String status);


    Page<EntityArea> findByStatus(String status, Pageable pageable);


    Page<EntityArea> findByIdOrDescriptionContainingIgnoreCaseAndStatus(
            Long id,
            String description,
            String status,
            Pageable pageable
    );

}
