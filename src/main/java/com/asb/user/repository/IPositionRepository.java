package com.asb.user.repository;

import com.asb.user.model.entity.EntityPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IPositionRepository extends JpaRepository<EntityPosition, Long> {

    Optional<EntityPosition> findByDescription(String description);

    @Override
    Page<EntityPosition> findAll(Pageable pageable);

    List<EntityPosition> findByStatus(String status);

    Page<EntityPosition> findByStatus(String status, Pageable pageable);


    @Query(value = "SELECT * FROM position " +
            "WHERE (:id IS NULL OR CAST(position_id AS CHAR) LIKE CONCAT(:id, '%')) " +
            "AND (:description IS NULL OR UPPER(description) LIKE UPPER(CONCAT('%', :description, '%'))) " +
            "AND (:status IS NULL OR UPPER(status) = UPPER(:status) OR status = 'ACTIVE') " +
            "ORDER BY id ASC", // Cambia de 'id' a 'position_id'
            countQuery = "SELECT COUNT(*) FROM position " +
                    "WHERE (:id IS NULL OR CAST(position_id AS CHAR) LIKE CONCAT(:id, '%')) " +
                    "AND (:description IS NULL OR UPPER(description) LIKE UPPER(CONCAT('%', :description, '%'))) " +
                    "AND (:status IS NULL OR UPPER(status) = UPPER(:status) OR status = 'ACTIVE')",
            nativeQuery = true)
    Page<EntityPosition> findByIdOrDescriptionContainingIgnoreCaseAndStatus(
            @Param("id") String id,
            @Param("description") String description,
            @Param("status") String status,
            Pageable pageable
    );



}
