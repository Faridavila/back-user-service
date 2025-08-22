package com.asb.user.service.impl;

import com.asb.user.exception.CustomErrorException;
import com.asb.user.model.dto.MenuDto;
import com.asb.user.model.dto.MenuGetAllDto;
import com.asb.user.model.dto.MenuSaveAndUpdateDto;
import com.asb.user.model.entity.EntityMenu;
import com.asb.user.model.entity.EntityMenuType;
import com.asb.user.repository.IMenuRepository;
import com.asb.user.service.IMenuService;
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
public class MenuServiceImpl implements IMenuService {

    private final IMenuRepository iRepository;

    @Override
    @Transactional
    public MenuDto save(MenuSaveAndUpdateDto menuDto) {
        Optional<EntityMenu> menuOptional = iRepository.findByName(menuDto.getName());
        if (menuOptional.isPresent()) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El nombre del menú ya existe");
        }

        EntityMenu entityMenu = new EntityMenu();
        entityMenu.setName(menuDto.getName());
        entityMenu.setDescription(menuDto.getDescription());
        entityMenu.setShortName(menuDto.getShortName());
        entityMenu.setStatus(menuDto.getStatus());

        entityMenu.setMenuTypeId(new EntityMenuType(menuDto.getMenuTypeId(), ""));

        entityMenu.setFatherMenuId(menuDto.getFatherMenuId());

        EntityMenu newEntityMenu = iRepository.save(entityMenu);
        return mapMenuDto(newEntityMenu);
    }

    @Override
    @Transactional
    public MenuDto update(Long menuId, MenuSaveAndUpdateDto menuDto) {
        Optional<EntityMenu> menuOptional = iRepository.findById(menuId);
        if (!menuOptional.isPresent()) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El menú no existe");
        }

        EntityMenu entityMenu = menuOptional.get();
        entityMenu.setName(menuDto.getName());
        entityMenu.setDescription(menuDto.getDescription());
        entityMenu.setShortName(menuDto.getShortName());
        entityMenu.setStatus(menuDto.getStatus());

        entityMenu.setMenuTypeId(new EntityMenuType(menuDto.getMenuTypeId(), ""));
        entityMenu.setFatherMenuId(menuDto.getFatherMenuId());

        EntityMenu updatedEntityMenu = iRepository.save(entityMenu);
        return mapMenuDto(updatedEntityMenu);
    }

    @Override
    @Transactional
    public boolean delete(Long menuId) {
        Optional<EntityMenu> menuOptional = iRepository.findById(menuId);
        if (menuOptional.isPresent()) {
            EntityMenu entityMenu = menuOptional.get();
            entityMenu.setStatus(Constants.INACTIVE_STATUS);
            iRepository.save(entityMenu);
            return true;
        }
        return false;
    }

    @Override
    public MenuDto get(Long menuId) {
        Optional<EntityMenu> menuOptional = iRepository.findById(menuId);
        if (menuOptional.isPresent()) {
            return mapMenuDto(menuOptional.get());
        } else {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El menú no existe");
        }
    }

    @Override
    public Page<MenuGetAllDto> getAll(Map<String, String> customQuery) {
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

        return mapPageMenuDto(iRepository.findByStatus(status, pagingSort), pagingSort);
    }

    @Override
    public Page<MenuGetAllDto> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);

        return mapPageMenuDto(iRepository.findByStatus("ACTIVE", pagingSort), pagingSort);
    }

    @Override
    public List<MenuGetAllDto> getAllWithOutPage(Map<String, String> customQuery) {
        String status = Constants.ACTIVE_STATUS;

        if (customQuery.containsKey("status")) {
            status = customQuery.get("status");
        }

        return iRepository.findByStatus(status).stream()
                .map(menu -> MenuGetAllDto.builder()
                        .id(menu.getId())
                        .name(menu.getName())
                        .description(menu.getDescription())
                        .shortName(menu.getShortName())
                        .menuTypeId(menu.getMenuTypeId())
                        .fatherMenuId(menu.getFatherMenuId())
                        .status(menu.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Page<MenuGetAllDto> searchCustom(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 5;
        Long id = null;
        String name = null;
        String description = null;
        String shortName = null;
        String status = Constants.ACTIVE_STATUS;
        String menuTypeDescription = null;
        Long fatherMenuId = null;

        if (customQuery.containsKey("id")) {
            id = Long.valueOf(customQuery.get("id"));
        }
        if (customQuery.containsKey("name")) {
            name = customQuery.get("name");
        }
        if (customQuery.containsKey("description")) {
            description = customQuery.get("description");
        }
        if (customQuery.containsKey("shortName")) {
            shortName = customQuery.get("shortName");
        }
        if (customQuery.containsKey("status")) {
            status = customQuery.get("status");
        }
        if (customQuery.containsKey("menuTypeDescription")) {
            menuTypeDescription = customQuery.get("menuTypeDescription");
        }
        if (customQuery.containsKey("fatherMenuId")) {
            fatherMenuId = Long.valueOf(customQuery.get("fatherMenuId"));
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

        return mapPageMenuDto(
                iRepository.findByIdOrNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrShortNameContainingIgnoreCaseOrMenuTypeId_DescriptionContainingIgnoreCaseAndFatherMenuIdAndStatus(
                        id, name, description, shortName, menuTypeDescription, fatherMenuId, status, pagingSort), pagingSort);
    }

    private MenuDto mapMenuDto(EntityMenu entityMenu) {
        MenuDto menuDto = new MenuDto();
        BeanUtils.copyProperties(entityMenu, menuDto);
        menuDto.setMenuTypeId(entityMenu.getMenuTypeId().getId());
        menuDto.setFatherMenuId(entityMenu.getFatherMenuId());
        return menuDto;
    }

    private Page<MenuGetAllDto> mapPageMenuDto(Page<EntityMenu> entityPage, Pageable pagingSort) {
        int totalElements = (int) entityPage.getTotalElements();
        return new PageImpl<>(
                entityPage.getContent().stream()
                        .map(menu -> MenuGetAllDto.builder()
                                .id(menu.getId())
                                .name(menu.getName())
                                .description(menu.getDescription())
                                .shortName(menu.getShortName())
                                .menuTypeId(menu.getMenuTypeId())
                                .fatherMenuId(menu.getFatherMenuId())
                                .status(menu.getStatus())
                                .build())
                        .collect(Collectors.toList()), pagingSort, totalElements);
    }
}
