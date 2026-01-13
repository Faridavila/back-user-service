package com.asb.user.repository;

import com.asb.user.model.dto.GgpUserGetAllDto;
import com.asb.user.model.dto.UserResponsiveDto;
import com.asb.user.model.entity.EntityUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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



    @Query(value = """
    SELECT new com.asb.user.model.dto.GgpUserGetAllDto(
        u.id, u.name, u.login, u.password, u.email, 
        u.rolId, r.name, 
        u.positionId, p.description, 
        u.companyId, c.companyName, 
        u.areaId, a.description, 
        u.phone, u.status
    )
    FROM EntityUser u
    INNER JOIN EntityRol r ON u.rolId = r.id
    INNER JOIN EntityPosition p ON u.positionId = p.id
    INNER JOIN EntityCompany c ON u.companyId = c.id
    INNER JOIN EntityArea a ON u.areaId = a.id
    WHERE u.status = :status
    AND (
        u.id NOT IN (
            SELECT td.userId 
            FROM EntityTerminalDetails td
        )
        OR (:terminalId IS NOT NULL AND u.id IN (
            SELECT td.userId 
            FROM EntityTerminalDetails td
            WHERE td.terminalId = :terminalId
        ))
    )
    """)
    List<GgpUserGetAllDto> findUsersWithoutTerminal(
            @Param("status") String status,
            @Param("terminalId") Long terminalId
    );

    @Query(value = "SELECT new com.asb.user.model.dto.UserResponsiveDto(" +
            "u.id, u.name, u.login, u.password, u.email, r.name, p.description, " +
            "c.companyName, a.description, u.phone, u.status) " +
            "FROM EntityUser u " +
            "JOIN EntityRol r ON u.rolId = r.id " +
            "JOIN EntityPosition p ON u.positionId = p.id " +
            "JOIN EntityCompany c ON u.companyId = c.id " +
            "JOIN EntityArea a ON u.areaId = a.id " +
            "WHERE u.status = 'ACTIVE' " +
            "AND (CAST(u.id AS string) LIKE :id " +
            "OR UPPER(u.name) LIKE UPPER(:userName) " +
            "OR UPPER(u.email) LIKE UPPER(:email) " +
            "OR UPPER(u.login) LIKE UPPER(:login) " +
            "OR u.phone LIKE :phone " +
            "OR UPPER(c.companyName) LIKE UPPER(:companyName) " +
            "OR UPPER(p.description) LIKE UPPER(:positionDescription) " +
            "OR UPPER(a.description) LIKE UPPER(:areaDescription) " +
            "OR UPPER(r.name) LIKE UPPER(:rolName))",
            countQuery = "SELECT COUNT(u) " +
                    "FROM EntityUser u " +
                    "JOIN EntityRol r ON u.rolId = r.id " +
                    "JOIN EntityPosition p ON u.positionId = p.id " +
                    "JOIN EntityCompany c ON u.companyId = c.id " +
                    "JOIN EntityArea a ON u.areaId = a.id " +
                    "WHERE u.status = 'ACTIVE' " +
                    "AND (CAST(u.id AS string) LIKE :id " +
                    "OR UPPER(u.name) LIKE UPPER(:userName) " +
                    "OR UPPER(u.email) LIKE UPPER(:email) " +
                    "OR UPPER(u.login) LIKE UPPER(:login) " +
                    "OR u.phone LIKE :phone " +
                    "OR UPPER(c.companyName) LIKE UPPER(:companyName) " +
                    "OR UPPER(p.description) LIKE UPPER(:positionDescription) " +
                    "OR UPPER(a.description) LIKE UPPER(:areaDescription) " +
                    "OR UPPER(r.name) LIKE UPPER(:rolName))")
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
            Pageable pageable);

    Page<EntityUser> findByStatus(String status, Pageable pageable);


    List<EntityUser> findByStatusAndRolId(String status, Long rolId);

    List<EntityUser> findByStatus(String status);


    Page<EntityUser> findAll(Specification<EntityUser> spec, Pageable pagingSort);
}
