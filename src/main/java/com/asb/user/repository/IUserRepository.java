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


    @Query("SELECT u FROM EntityUser u WHERE LOWER(:input) IN (LOWER(u.email), LOWER(u.login), LOWER(u.phone))")
    Optional<EntityUser> findByEmailOrLoginOrPhone(@Param("input") String input);
    @Override
    Page<EntityUser> findAll(Pageable pageable);


    @Query(value = "SELECT new com.asb.user.model.dto.GgpUserGetAllDto(u.id, u.name, u.login,u.password, u.email, u.rolId, r.name, u.positionId, p.description,u.companyId, c.companyName, u.areaId, a.description,u.phone,u.status) " +
            "FROM EntityUser u " +
            "INNER JOIN EntityRol r ON u.rolId = r.id " +
            "INNER JOIN EntityPosition p ON u.positionId = p.id " +
            "INNER JOIN EntityCompany c ON u.companyId = c.id " +
            "INNER JOIN EntityArea a ON u.areaId = a.id " +
            "WHERE u.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM EntityUser u " +
                    "INNER JOIN EntityRol r ON u.rolId = r.id " +
                    "INNER JOIN EntityPosition p ON u.positionId = p.id " +
                    "INNER JOIN EntityCompany c ON u.companyId = c.id " +
                    "INNER JOIN EntityArea a ON u.areaId = a.id " +
                    "WHERE u.status = 'ACTIVE'")
    Page<GgpUserGetAllDto> getStatus(Pageable pageable);




    Page<EntityUser> findByStatus(String status, Pageable pageable);


    List<EntityUser> findByStatusAndRolId(String status, Long rolId);

    List<EntityUser> findByStatus(String status);

    @Query(value = """
    SELECT new com.asb.user.model.dto.UserResponsiveDto(
        u.id, u.name, u.login, u.password, u.email,
        r.name, p1.description, c1.companyName, a1.description, u.phone,u.status
    )
    FROM EntityUser u
    INNER JOIN EntityRol r ON u.rolId = r.id
    INNER JOIN EntityPosition p1 ON u.positionId = p1.id
    INNER JOIN EntityCompany c1 ON u.companyId = c1.id
    INNER JOIN EntityArea a1 ON u.areaId = a1.id
    WHERE u.status = 'ACTIVE'
      AND (:id IS NULL OR CAST(u.id AS string) LIKE :id)
      AND (:userName IS NULL OR UPPER(u.name) LIKE UPPER(:userName))
      AND (:email IS NULL OR UPPER(u.email) LIKE UPPER(:email))
      AND (:login IS NULL OR UPPER(u.login) LIKE UPPER(:login))
      AND (:phone IS NULL OR UPPER(u.phone) LIKE UPPER(:phone))
      AND (:companyName IS NULL OR UPPER(c1.companyName) LIKE UPPER(:companyName))
      AND (:positionDescription IS NULL OR UPPER(p1.description) LIKE UPPER(:positionDescription))
      AND (:areaDescription IS NULL OR UPPER(a1.description) LIKE UPPER(:areaDescription))
      AND (:rolName IS NULL OR UPPER(r.name) LIKE UPPER(:rolName))
      AND u.status = :status
    """,
            countQuery = """
    SELECT COUNT(u)
    FROM EntityUser u
    INNER JOIN EntityRol r ON u.rolId = r.id
    INNER JOIN EntityPosition p1 ON u.positionId = p1.id
    INNER JOIN EntityCompany c1 ON u.companyId = c1.id
    INNER JOIN EntityArea a1 ON u.areaId = a1.id
    WHERE u.status = 'ACTIVE'
      AND (:id IS NULL OR CAST(u.id AS string) LIKE :id)
      AND (:userName IS NULL OR UPPER(u.name) LIKE UPPER(:userName))
      AND (:email IS NULL OR UPPER(u.email) LIKE UPPER(:email))
      AND (:login IS NULL OR UPPER(u.login) LIKE UPPER(:login))
      AND (:phone IS NULL OR UPPER(u.phone) LIKE UPPER(:phone))
      AND (:companyName IS NULL OR UPPER(c1.companyName) LIKE UPPER(:companyName))
      AND (:positionDescription IS NULL OR UPPER(p1.description) LIKE UPPER(:positionDescription))
      AND (:areaDescription IS NULL OR UPPER(a1.description) LIKE UPPER(:areaDescription))
      AND (:rolName IS NULL OR UPPER(r.name) LIKE UPPER(:rolName))
      AND u.status = :status
    """)
    Page<UserResponsiveDto> searchFiltered(
            @Param("id") String id,
            @Param("userName") String userName,
            @Param("email") String email,
            @Param("login") String login,
            @Param("phone") String phone,
            @Param("companyName") String companyName,
            @Param("positionDescription") String positionDescription,
            @Param("areaDescription") String areaDescription,
            @Param("rolName") String rolName,
            @Param("status") String status,
            Pageable pageable);
}
