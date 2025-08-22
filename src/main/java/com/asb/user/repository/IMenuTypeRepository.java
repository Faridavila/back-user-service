package com.asb.user.repository;

import com.asb.user.model.entity.EntityMenuType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IMenuTypeRepository extends JpaRepository<EntityMenuType, Long> {


    Optional<EntityMenuType> findByDescription(String description);


    @Override
    Page<EntityMenuType> findAll(Pageable pageable);


    List<EntityMenuType> findByStatus(String status);


    Page<EntityMenuType> findByStatus(String status, Pageable pageable);


    Page<EntityMenuType> findByIdOrDescriptionContainingIgnoreCaseAndStatus(
            Long id,
            String description,
            String status,
            Pageable pageable
    );
}
