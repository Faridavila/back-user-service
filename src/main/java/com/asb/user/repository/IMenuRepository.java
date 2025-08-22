package com.asb.user.repository;

import com.asb.user.model.entity.EntityMenu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IMenuRepository extends JpaRepository<EntityMenu, Long> {


    Optional<EntityMenu> findByName(String name);


    @Override
    Page<EntityMenu> findAll(Pageable pageable);

    List<EntityMenu> findByStatus(String status);


    Page<EntityMenu> findByStatus(String status, Pageable pageable);

    Page<EntityMenu> findByIdOrNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrShortNameContainingIgnoreCaseOrMenuTypeId_DescriptionContainingIgnoreCaseAndFatherMenuIdAndStatus(
            Long id,
            String name,
            String description,
            String shortName,
            String menuTypeDescription,
            Long fatherMenuId,
            String status,
            Pageable pageable
    );

}
