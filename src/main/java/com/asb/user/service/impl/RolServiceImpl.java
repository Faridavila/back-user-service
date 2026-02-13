package com.asb.user.service.impl;

import com.asb.user.exception.CustomErrorException;
import com.asb.user.model.dto.PermissionListDto;
import com.asb.user.model.dto.RolDto;
import com.asb.user.model.dto.RolGetAllDto;
import com.asb.user.model.dto.RolSaveAndUpdateDto;
import com.asb.user.model.entity.EntityPermission;
import com.asb.user.model.entity.EntityRol;
import com.asb.user.repository.IPermissionRepository;
import com.asb.user.repository.IRolRepository;
import com.asb.user.service.IRolService;
import com.asb.user.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RolServiceImpl implements IRolService {

    private final IRolRepository rolRepository;
    private final IPermissionRepository permissionRepository;

    @Override
    @Transactional
    public RolDto save(RolSaveAndUpdateDto rolDto) {

        log.info("💾 Guardando rol: {}", rolDto.getName());

        // Validar que no exista
        if (rolRepository.findByName(rolDto.getName()).isPresent()) {
            throw new CustomErrorException(
                    HttpStatus.BAD_REQUEST,
                    "El rol ya existe"
            );
        }

        // Crear rol
        EntityRol entityRol = new EntityRol();
        entityRol.setName(rolDto.getName());
        entityRol.setStatus(
                rolDto.getStatus() != null
                        ? rolDto.getStatus()
                        : Constants.ACTIVE_STATUS
        );

        // ✅ Asignar permisos
        entityRol.setPermissions(getPermissionsFromDto(rolDto.getPermissions()));

        // Guardar
        EntityRol savedRol = rolRepository.save(entityRol);

        log.info("✅ Rol guardado con ID {} y {} permisos",
                savedRol.getId(),
                savedRol.getPermissions().size()
        );

        return mapRolDto(savedRol);
    }

    // ======================================================
    // ✅ UPDATE
    // ======================================================
    @Override
    @Transactional
    public RolDto update(Long rolId, RolSaveAndUpdateDto rolDto) {

        log.info("🔄 Actualizando rol ID: {}", rolId);

        EntityRol entityRol = rolRepository.findById(rolId)
                .orElseThrow(() -> new CustomErrorException(
                        HttpStatus.BAD_REQUEST,
                        "El rol no existe"
                ));

        // Actualizar datos básicos
        entityRol.setName(rolDto.getName());
        entityRol.setStatus(rolDto.getStatus());

        // ✅ Reemplazar permisos completamente
        Set<EntityPermission> newPermissions =
                getPermissionsFromDto(rolDto.getPermissions());

        entityRol.getPermissions().clear();
        entityRol.getPermissions().addAll(newPermissions);

        EntityRol updatedRol = rolRepository.save(entityRol);

        log.info("✅ Rol actualizado con {} permisos",
                updatedRol.getPermissions().size()
        );

        return mapRolDto(updatedRol);
    }
    @Override
    @Transactional
    public boolean delete(Long rolId) {
        log.info("🗑️ Iniciando eliminación lógica de rol ID: {}", rolId);

        Optional<EntityRol> rolOptional = rolRepository.findById(rolId);
        if (rolOptional.isPresent()) {
            EntityRol entityRol = rolOptional.get();
            entityRol.setStatus(Constants.INACTIVE_STATUS);
            rolRepository.save(entityRol);

            log.info("✅ Rol '{}' marcado como INACTIVO", entityRol.getName());
            return true;
        }

        log.warn("⚠️ Rol con ID {} no encontrado", rolId);
        return false;
    }

    @Override
    public RolDto get(long id) {
        log.info("🔍 Buscando rol ID: {}", id);

        Optional<EntityRol> rolOptional = rolRepository.findById(id);
        if (rolOptional.isPresent()) {
            EntityRol rol = rolOptional.get();
            log.info("✅ Rol encontrado: {} con {} permisos",
                    rol.getName(), rol.getPermissions().size());
            return mapRolDto(rol);
        } else {
            log.error("❌ Rol con ID {} no existe", id);
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El rol no existe");
        }
    }

    @Override
    public Page<RolGetAllDto> getAll(Map<String, String> customQuery) {
        // Valores por defecto
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 5;
        String status = Constants.ACTIVE_STATUS;

        // Extraer parámetros del query
        if (customQuery.containsKey("status")) {
            status = customQuery.get("status");
        }
        if (customQuery.containsKey("orders")) {
            orders = customQuery.get("orders");
        }
        if (customQuery.containsKey("sortBy")) {
            sortBy = customQuery.get("sortBy");
        }
        if (customQuery.containsKey("page")) {
            page = Integer.parseInt(customQuery.get("page"));
        }
        if (customQuery.containsKey("size")) {
            size = Integer.parseInt(customQuery.get("size"));
        }

        log.info("📄 Obteniendo roles - Página: {}, Tamaño: {}, Estado: {}", page, size, status);

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<RolGetAllDto> result = mapPageRolDto(
                rolRepository.findByStatus(status, pagingSort),
                pagingSort
        );

        log.info("✅ Se encontraron {} roles", result.getTotalElements());
        return result;
    }

    @Override
    public Page<RolGetAllDto> getAll(int page, int size, String orders, String sortBy) {
        log.info("📄 Obteniendo roles activos - Página: {}, Tamaño: {}", page, size);

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<RolGetAllDto> result = mapPageRolDto(
                rolRepository.findByStatus(Constants.ACTIVE_STATUS, pagingSort),
                pagingSort
        );

        log.info("✅ Se encontraron {} roles activos", result.getTotalElements());
        return result;
    }

    @Override
    public List<RolGetAllDto> getAllWithOutPage(Map<String, String> customQuery) {
        String status = Constants.ACTIVE_STATUS;

        if (customQuery.containsKey("status")) {
            status = customQuery.get("status");
        }

        log.info("📋 Obteniendo todos los roles sin paginación - Estado: {}", status);

        List<RolGetAllDto> result = rolRepository.findByStatus(status).stream()
                .map(this::mapRolGetAllDto)
                .collect(Collectors.toList());

        log.info("✅ Se encontraron {} roles", result.size());
        return result;
    }

    @Override
    public Page<RolGetAllDto> searchCustom(Map<String, String> customQuery) {
        // Valores por defecto
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 5;
        String status = Constants.ACTIVE_STATUS;
        String id = null;
        String name = null;

        // Extraer parámetros
        if (customQuery.containsKey("orders")) {
            orders = customQuery.get("orders");
        }
        if (customQuery.containsKey("sortBy")) {
            sortBy = customQuery.get("sortBy");
        }
        if (customQuery.containsKey("page")) {
            page = Integer.parseInt(customQuery.get("page"));
        }
        if (customQuery.containsKey("size")) {
            size = Integer.parseInt(customQuery.get("size"));
        }
        if (customQuery.containsKey("status") && !customQuery.get("status").isEmpty()) {
            status = customQuery.get("status");
        }
        if (customQuery.containsKey("id") && !customQuery.get("id").isEmpty()) {
            id = customQuery.get("id");
        }
        if (customQuery.containsKey("name") && !customQuery.get("name").isEmpty()) {
            name = customQuery.get("name");
        }

        log.info("🔍 Búsqueda personalizada - ID: {}, Nombre: {}, Estado: {}", id, name, status);

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));

        // Construir especificación
        Specification<EntityRol> spec = Specification.where(null);

        // Filtro por status
        final String statusParam = status;
        spec = spec.and((root, query, cb) ->
                cb.equal(root.get("status"), statusParam));

        // Filtro por ID (búsqueda parcial)
        if (id != null) {
            final String idParam = id;
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("id").as(String.class), "%" + idParam + "%"));
        }

        // Filtro por nombre (búsqueda parcial, case-insensitive)
        if (name != null) {
            final String nameParam = name;
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.upper(root.get("name")), "%" + nameParam.toUpperCase() + "%"));
        }

        Page<EntityRol> entityPage = rolRepository.findAll(spec, pagingSort);

        log.info("✅ Búsqueda completada - {} resultados encontrados", entityPage.getTotalElements());

        return entityPage.map(this::mapRolGetAllDto);
    }

    // ========================================
    // 🔥 MÉTODOS DE MAPEO PRIVADOS
    // ========================================

    /**
     * Mapea EntityRol a RolDto (respuesta completa con permisos)
     */
    private RolDto mapRolDto(EntityRol entityRol) {
        return RolDto.builder()
                .id(entityRol.getId())
                .name(entityRol.getName())
                .status(entityRol.getStatus())
                .permissions(mapPermissions(entityRol.getPermissions()))
                .build();
    }

    /**
     * Mapea EntityRol a RolGetAllDto (incluye contador de permisos)
     */
    private RolGetAllDto mapRolGetAllDto(EntityRol entityRol) {
        List<PermissionListDto> permissions = mapPermissions(entityRol.getPermissions());
        return RolGetAllDto.builder()
                .id(entityRol.getId())
                .name(entityRol.getName())
                .status(entityRol.getStatus())
                .permissions(permissions)
                .numberPermissions(permissions.size())
                .build();
    }


    private Set<EntityPermission> getPermissionsFromDto(
            List<PermissionListDto> permissionDtos
    ) {

        if (permissionDtos == null || permissionDtos.isEmpty()) {
            return new HashSet<>();
        }

        // ✅ Extraer IDs
        List<Long> ids = permissionDtos.stream()
                .map(PermissionListDto::getPermissionId)
                .collect(Collectors.toList());

        // Buscar en DB
        Set<EntityPermission> permissions =
                new HashSet<>(permissionRepository.findAllById(ids));

        // Validación
        if (permissions.size() != ids.size()) {
            throw new CustomErrorException(
                    HttpStatus.BAD_REQUEST,
                    "Algunos permisos no existen en la base de datos"
            );
        }

        return permissions;
    }
    /**
     * Mapea Page de EntityRol a Page de RolGetAllDto
     */
    private Page<RolGetAllDto> mapPageRolDto(Page<EntityRol> entityPage, Pageable pagingSort) {
        int totalElements = (int) entityPage.getTotalElements();
        return new PageImpl<>(
                entityPage.getContent().stream()
                        .map(this::mapRolGetAllDto)
                        .collect(Collectors.toList()),
                pagingSort,
                totalElements
        );
    }

    /**
     * Mapea Set de EntityPermission a List de PermissionListDto
     */
    private List<PermissionListDto> mapPermissions(Set<EntityPermission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return new ArrayList<>();
        }
        return permissions.stream()
                .map(perm -> PermissionListDto.builder()
                        .permissionId(perm.getId())
                        .permissionName(perm.getName())
                        .build())
                .collect(Collectors.toList());
    }
}