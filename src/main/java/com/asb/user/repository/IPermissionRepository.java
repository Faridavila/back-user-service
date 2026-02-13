package com.asb.user.repository;

import com.asb.user.model.entity.EntityPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPermissionRepository extends JpaRepository<EntityPermission, Long> {

    List<EntityPermission> findByStatus(String status);

    List<EntityPermission> findAllById(Iterable<Long> ids);
}