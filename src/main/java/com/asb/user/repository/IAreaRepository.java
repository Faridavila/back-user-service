package com.asb.user.repository;

import com.asb.user.model.dto.AreaGetAllDto;
import com.asb.user.model.entity.EntityArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IAreaRepository extends JpaRepository<EntityArea, Long> {


    Optional<EntityArea> findByDescription(String description);


    @Override
    Page<EntityArea> findAll(Pageable pageable);


    List<EntityArea> findByStatus(String status);
    @Query(value = "SELECT new com.asb.user.model.dto.AreaGetAllDto(a.id, a.description, a.status) " +
            "FROM EntityArea a " +
            "WHERE (:id IS NULL OR CAST(a.id AS string) LIKE :id) " +
            "AND (:description IS NULL OR UPPER(a.description) LIKE UPPER(:description)) " +
            "AND (:status IS NULL OR UPPER(a.status) LIKE UPPER(:status))")
    Page<AreaGetAllDto> searchFiltered(@Param("id") String id,
                                       @Param("description") String description,
                                       @Param("status") String status,
                                       Pageable pageable);


    Page<EntityArea> findByStatus(String status, Pageable pageable);
}
