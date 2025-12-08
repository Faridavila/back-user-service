package com.asb.user.repository;

import com.asb.user.model.dto.GgpUserGetAllDto;
import com.asb.user.model.dto.UserResponsiveDto;
import com.asb.user.model.entity.EntityUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface IUserRepository extends JpaRepository<EntityUser, Long> {

    public Optional<EntityUser> findByEmail(String email);

    public Optional<EntityUser> findByEmailOrLogin(String email, String login);
    @Query("from EntityUser where (email=:email or login=:login) and id not in (:userIds)")
    public Page<EntityUser> findByEmailOrLoginAndIdNotIn(String email, String login, Collection<Long> userIds, Pageable pageable);

    public Optional<EntityUser> findByLogin(String login);

    @Override
    Page<EntityUser> findAll(Pageable pageable);


    @Query(value = "SELECT new com.asb.user.model.dto.GgpUserGetAllDto(u.id, u.name, u.login,u.password, u.email, r.id, r.name, u.status) " +
            "FROM EntityUser u " +
            "INNER JOIN u.rol r " +
            "WHERE u.status = 'ACTIVO'",
            countQuery = "SELECT COUNT(u) " +
                    "FROM EntityUser u " +
                    "INNER JOIN u.rol r " +
                    "WHERE u.status = 'ACTIVO'")
    Page<GgpUserGetAllDto> getStatus(Pageable pageable);

    Page<EntityUser> findByStatus(String status, Pageable pageable);

    List<EntityUser> findByStatus(String status);

    @Query(value = "SELECT new com.asb.user.model.dto.UserResponsiveDto(u.id, u.name, u.login, u.password, u.email, r.name, p1.description, c1.companyName, a1.description, u.status) " +
            "FROM EntityUser u " +
            "INNER JOIN u.rol r " +
            "LEFT JOIN u.company c1 " +
            "LEFT JOIN u.position p1 " +
            "LEFT JOIN u.area a1 " +
            "WHERE (:id IS NULL OR CAST(u.id AS string) LIKE :id) " +
            "AND (:name IS NULL OR UPPER(u.name) LIKE UPPER(:name)) " +
            "AND (:email IS NULL OR UPPER(u.email) LIKE UPPER(:email)) " +
            "AND (:login IS NULL OR UPPER(u.login) LIKE UPPER(:login)) " +
            "AND (:company IS NULL OR UPPER(c1.companyName) LIKE UPPER(:company)) " +
            "AND (:position IS NULL OR UPPER(p1.description) LIKE UPPER(:position)) " +
            "AND (:area IS NULL OR UPPER(a1.description) LIKE UPPER(:area)) " +
            "AND (:rol IS NULL OR UPPER(r.name) LIKE UPPER(:rol)) " +
            "AND u.status = :status",
            countQuery = "SELECT COUNT(u) " +
                    "FROM EntityUser u " +
                    "INNER JOIN u.rol r " +
                    "LEFT JOIN u.company c1 " +
                    "LEFT JOIN u.position p1 " +
                    "LEFT JOIN u.area a1 " +
                    "WHERE (:id IS NULL OR CAST(u.id AS string) LIKE :id) " +
                    "AND (:name IS NULL OR UPPER(u.name) LIKE UPPER(:name)) " +
                    "AND (:email IS NULL OR UPPER(u.email) LIKE UPPER(:email)) " +
                    "AND (:login IS NULL OR UPPER(u.login) LIKE UPPER(:login)) " +
                    "AND (:company IS NULL OR UPPER(c1.companyName) LIKE UPPER(:company)) " +
                    "AND (:position IS NULL OR UPPER(p1.description) LIKE UPPER(:position)) " +
                    "AND (:area IS NULL OR UPPER(a1.description) LIKE UPPER(:area)) " +
                    "AND (:rol IS NULL OR UPPER(r.name) LIKE UPPER(:rol)) " +
                    "AND u.status = :status")
    Page<UserResponsiveDto> searchFiltered(@Param("id") String id,
                                           @Param("name") String name, @Param("email") String email, @Param("login") String login,
                                           @Param("company") String company, @Param("position") String position,
                                           @Param("area") String area, @Param("rol") String rol,
                                           @Param("status") String status, Pageable pageable);
}
