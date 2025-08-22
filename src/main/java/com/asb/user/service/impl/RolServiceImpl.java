package com.asb.user.service.impl;

import com.asb.user.exception.CustomErrorException;
import com.asb.user.model.dto.RolDto;
import com.asb.user.model.dto.RolGetAllDto;
import com.asb.user.model.dto.RolSaveAndUpdateDto;
import com.asb.user.model.entity.EntityRol;
import com.asb.user.repository.IRolRepository;
import com.asb.user.service.IRolService;
import com.asb.user.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RolServiceImpl implements IRolService {

    private final IRolRepository rolRepository;

    @Override
    @Transactional
    public RolDto save(RolSaveAndUpdateDto rolDto) {
        Optional<EntityRol> rolOptional = rolRepository.findByName(rolDto.getName());
        if (rolOptional.isPresent()) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El rol ya existe");
        }

        EntityRol entityRol = new EntityRol();
        BeanUtils.copyProperties(rolDto, entityRol);
        entityRol.setStatus(rolDto.getStatus());

        EntityRol newEntityRol = rolRepository.save(entityRol);
        return mapRolDto(newEntityRol);
    }

    @Override
    @Transactional
    public RolDto update(Long rolId, RolSaveAndUpdateDto rolDto) {
        Optional<EntityRol> rolOptional = rolRepository.findById(rolId);
        if (!rolOptional.isPresent()) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El rol no existe");
        }

        EntityRol entityRol = rolOptional.get();
        BeanUtils.copyProperties(rolDto, entityRol);
        entityRol.setStatus(rolDto.getStatus());

        EntityRol updatedEntityRol = rolRepository.save(entityRol);
        return mapRolDto(updatedEntityRol);
    }

    @Override
    @Transactional
    public boolean delete(Long rolId) {
        Optional<EntityRol> rolOptional = rolRepository.findById(rolId);
        if (rolOptional.isPresent()) {
            EntityRol entityRol = rolOptional.get();
            entityRol.setStatus(Constants.INACTIVE_STATUS);
            rolRepository.save(entityRol);
            return true;
        }
        return false;
    }

    @Override
    public RolDto get(long id) {
        Optional<EntityRol> rolOptional = rolRepository.findById(id);
        if (rolOptional.isPresent()) {
            return mapRolDto(rolOptional.get());
        } else {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El rol no existe");
        }
    }

    @Override
    public Page<RolGetAllDto> getAll(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 5;
        String status = Constants.ACTIVE_STATUS;

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

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return mapPageRolDto(rolRepository.findByStatus(status, pagingSort), pagingSort);
    }

    @Override
    public Page<RolGetAllDto> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return mapPageRolDto(rolRepository.findByStatus(Constants.ACTIVE_STATUS, pagingSort), pagingSort);
    }

    @Override
    public List<RolGetAllDto> getAllWithOutPage(Map<String, String> customQuery) {
        String status = Constants.ACTIVE_STATUS;

        if (customQuery.containsKey("status")) {
            status = customQuery.get("status");
        }

        return rolRepository.findByStatus(status).stream()
                .map(rol -> RolGetAllDto.builder()
                        .id(rol.getId())
                        .name(rol.getName())
                        .status(rol.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Page<RolGetAllDto> searchCustom(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 5;
        Long id = null;
        String name = null;
        String status = Constants.ACTIVE_STATUS;

        if (customQuery.containsKey("id")) {
            id = Long.valueOf(customQuery.get("id"));
        }
        if (customQuery.containsKey("name")) {
            name = customQuery.get("name");
        }
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

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return mapPageRolDto(
                rolRepository.findByIdOrNameContainingIgnoreCaseAndStatus(
                        id, name, status, pagingSort), pagingSort);
    }

    private RolDto mapRolDto(EntityRol entityRol) {
        RolDto rolDto = new RolDto();
        BeanUtils.copyProperties(entityRol, rolDto);
        return rolDto;
    }

    private Page<RolGetAllDto> mapPageRolDto(Page<EntityRol> entityPage, Pageable pagingSort) {
        int totalElements = (int) entityPage.getTotalElements();
        return new PageImpl<>(
                entityPage.getContent().stream()
                        .map(rol -> RolGetAllDto.builder()
                                .id(rol.getId())
                                .name(rol.getName())
                                .status(rol.getStatus())
                                .build())
                        .collect(Collectors.toList()), pagingSort, totalElements);
    }
}
