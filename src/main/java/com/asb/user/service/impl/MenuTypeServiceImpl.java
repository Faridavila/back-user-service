package com.asb.user.service.impl;

import com.asb.user.exception.CustomErrorException;
import com.asb.user.model.dto.MenuTypeDto;
import com.asb.user.model.dto.MenuTypeGetAllDto;
import com.asb.user.model.dto.MenuTypeSaveAndUpdateDto;
import com.asb.user.model.entity.EntityMenuType;
import com.asb.user.repository.IMenuTypeRepository;
import com.asb.user.service.IMenuTypeService;
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
public class MenuTypeServiceImpl implements IMenuTypeService {

    private final IMenuTypeRepository iMenuTypeRepository;

    @Override
    @Transactional
    public MenuTypeDto save(MenuTypeSaveAndUpdateDto menuTypeDto) {
        Optional<EntityMenuType> menuTypeOptional = iMenuTypeRepository.findByDescription(menuTypeDto.getDescription());
        if (menuTypeOptional.isPresent()) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El tipo de menú ya existe");
        }

        EntityMenuType entityMenuType = new EntityMenuType();
        BeanUtils.copyProperties(menuTypeDto, entityMenuType);
        entityMenuType.setStatus(menuTypeDto.getStatus());

        EntityMenuType newEntityMenuType = iMenuTypeRepository.save(entityMenuType);
        return mapMenuTypeDto(newEntityMenuType);
    }

    @Override
    @Transactional
    public MenuTypeDto update(Long menuTypeId, MenuTypeSaveAndUpdateDto menuTypeDto) {
        Optional<EntityMenuType> menuTypeOptional = iMenuTypeRepository.findById(menuTypeId);
        if (!menuTypeOptional.isPresent()) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El tipo de menú no existe");
        }

        EntityMenuType entityMenuType = menuTypeOptional.get();
        BeanUtils.copyProperties(menuTypeDto, entityMenuType);
        entityMenuType.setStatus(menuTypeDto.getStatus());

        EntityMenuType updatedEntityMenuType = iMenuTypeRepository.save(entityMenuType);
        return mapMenuTypeDto(updatedEntityMenuType);
    }

    @Override
    @Transactional
    public boolean delete(Long menuTypeId) {
        Optional<EntityMenuType> menuTypeOptional = iMenuTypeRepository.findById(menuTypeId);
        if (menuTypeOptional.isPresent()) {
            EntityMenuType entityMenuType = menuTypeOptional.get();
            entityMenuType.setStatus(Constants.INACTIVE_STATUS);
            iMenuTypeRepository.save(entityMenuType);
            return true;
        }
        return false;
    }

    @Override
    public MenuTypeDto get(Long menuTypeId) {
        Optional<EntityMenuType> menuTypeOptional = iMenuTypeRepository.findById(menuTypeId);
        if (menuTypeOptional.isPresent()) {
            return mapMenuTypeDto(menuTypeOptional.get());
        } else {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El tipo de menú no existe");
        }
    }

    @Override
    public Page<MenuTypeGetAllDto> getAll(Map<String, String> customQuery) {
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

        return mapPageMenuTypeDto(iMenuTypeRepository.findByStatus(status, pagingSort), pagingSort);
    }

    @Override
    public Page<MenuTypeGetAllDto> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return mapPageMenuTypeDto(iMenuTypeRepository.findByStatus(Constants.ACTIVE_STATUS, pagingSort), pagingSort);
    }

    @Override
    public List<MenuTypeGetAllDto> getAllWithOutPage(Map<String, String> customQuery) {
        String status = Constants.ACTIVE_STATUS;

        if (customQuery.containsKey("status")) {
            status = customQuery.get("status");
        }

        return iMenuTypeRepository.findByStatus(status).stream()
                .map(menuType -> MenuTypeGetAllDto.builder()
                        .id(menuType.getId())
                        .description(menuType.getDescription())
                        .status(menuType.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Page<MenuTypeGetAllDto> searchCustom(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 5;
        Long id = null;
        String description = null;
        String status = Constants.ACTIVE_STATUS;

        if (customQuery.containsKey("id")) {
            id = Long.valueOf(customQuery.get("id"));
        }
        if (customQuery.containsKey("description")) {
            description = customQuery.get("description");
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

        return mapPageMenuTypeDto(
                iMenuTypeRepository.findByIdOrDescriptionContainingIgnoreCaseAndStatus(
                        id, description, status, pagingSort), pagingSort);
    }

    private MenuTypeDto mapMenuTypeDto(EntityMenuType entityMenuType) {
        MenuTypeDto menuTypeDto = new MenuTypeDto();
        BeanUtils.copyProperties(entityMenuType, menuTypeDto);
        return menuTypeDto;
    }

    private Page<MenuTypeGetAllDto> mapPageMenuTypeDto(Page<EntityMenuType> entityPage, Pageable pagingSort) {
        int totalElements = (int) entityPage.getTotalElements();
        return new PageImpl<>(
                entityPage.getContent().stream()
                        .map(menuType -> MenuTypeGetAllDto.builder()
                                .id(menuType.getId())
                                .description(menuType.getDescription())
                                .status(menuType.getStatus())
                                .build())
                        .collect(Collectors.toList()), pagingSort, totalElements);
    }
}
