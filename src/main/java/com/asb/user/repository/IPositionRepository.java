package com.asb.user.repository;

import com.asb.user.model.dto.PositionGetAllDto;
import com.asb.user.model.entity.EntityArea;
import com.asb.user.model.entity.EntityPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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


    Page<EntityPosition> findAll(Specification<EntityPosition> spec, Pageable pagingSort);
}
