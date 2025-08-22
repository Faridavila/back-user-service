package com.asb.user.repository;

import com.asb.user.model.entity.EntityCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ICompanyRepository extends JpaRepository<EntityCompany, Long> {



    @Override
    Page<EntityCompany> findAll(Pageable pageable);

    List<EntityCompany> findByStatus(String status);

    Page<EntityCompany> findByIdOrCompanyNameContainingIgnoreCaseAndStatus(Long id, String companyName, String status, Pageable pageable);
    Page<EntityCompany> findByStatus(String status, Pageable pageable);


}
