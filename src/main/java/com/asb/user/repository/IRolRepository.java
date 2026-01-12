package com.asb.user.repository;

import com.asb.user.model.dto.RolGetAllDto;
import com.asb.user.model.entity.EntityArea;
import com.asb.user.model.entity.EntityRol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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


    Page<EntityRol> findAll(Specification<EntityRol> spec, Pageable pagingSort);
}
