package com.asb.user.repository;

import com.asb.user.model.dto.RolGetAllDto;
import com.asb.user.model.entity.EntityArea;
import com.asb.user.model.entity.EntityRol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface IRolRepository extends JpaRepository<EntityRol, Long> {


    Optional<EntityRol> findByName(String name);
    @Override
    Page<EntityRol> findAll(Pageable pageable);

    List<EntityRol> findByStatus(String status);


    Page<EntityRol> findByStatus(String status, Pageable pageable);

    @Query(value = "SELECT new com.asb.user.model.dto.RolGetAllDto(r.id, r.name, r.status) " +
            "FROM EntityRol r " +
            "WHERE (:id IS NULL OR CAST(r.id AS string) LIKE :id) " +
            "AND (:name IS NULL OR UPPER(r.name) LIKE UPPER(:name)) " +
            "AND (:status IS NULL OR UPPER(r.status) LIKE UPPER(:status))")
    Page<RolGetAllDto> searchFiltered(@Param("id") String id,
                                      @Param("name") String name,
                                      @Param("status") String status,
                                      Pageable pageable);

}
