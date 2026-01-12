package com.asb.user.service.impl;

import com.asb.user.exception.CustomErrorException;
import com.asb.user.model.dto.AreaDto;
import com.asb.user.model.dto.AreaGetAllDto;
import com.asb.user.model.dto.AreaSaveAndUpdateDto;
import com.asb.user.model.entity.EntityArea;
import com.asb.user.repository.IAreaRepository;
import com.asb.user.service.IAreaService;
import com.asb.user.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
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
public class AreaServiceImpl implements IAreaService {

    private final IAreaRepository areaRepository;

    @Override
    @Transactional
    public AreaDto save(AreaSaveAndUpdateDto areaDto) {
        Optional<EntityArea> areaOptional = areaRepository.findByDescription(areaDto.getDescription());
        if (areaOptional.isPresent()) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El área ya existe");
        }

        EntityArea entityArea = new EntityArea();
        BeanUtils.copyProperties(areaDto, entityArea);
        entityArea.setStatus(areaDto.getStatus());

        EntityArea newEntityArea = areaRepository.save(entityArea);
        return mapAreaDto(newEntityArea);
    }

    @Override
    @Transactional
    public AreaDto update(Long areaId, AreaSaveAndUpdateDto areaDto) {
        Optional<EntityArea> areaOptional = areaRepository.findById(areaId);
        if (!areaOptional.isPresent()) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El área no existe");
        }

        EntityArea entityArea = areaOptional.get();
        BeanUtils.copyProperties(areaDto, entityArea);
        entityArea.setStatus(areaDto.getStatus());

        EntityArea updatedEntityArea = areaRepository.save(entityArea);
        return mapAreaDto(updatedEntityArea);
    }

    @Override
    @Transactional
    public boolean delete(Long areaId) {
        Optional<EntityArea> areaOptional = areaRepository.findById(areaId);
        if (areaOptional.isPresent()) {
            EntityArea entityArea = areaOptional.get();
            entityArea.setStatus(Constants.INACTIVE_STATUS);
            areaRepository.save(entityArea);
            return true;
        }
        return false;
    }

    @Override
    public AreaDto get(Long areaId) {
        Optional<EntityArea> areaOptional = areaRepository.findById(areaId);
        if (areaOptional.isPresent()) {
            return mapAreaDto(areaOptional.get());
        } else {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El área no existe");
        }
    }

    @Override
    public Page<AreaGetAllDto> getAll(Map<String, String> customQuery) {
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

        return mapPageAreaDto(areaRepository.findByStatus(status, pagingSort), pagingSort);
    }

    @Override
    public Page<AreaGetAllDto> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return mapPageAreaDto(areaRepository.findByStatus(Constants.ACTIVE_STATUS, pagingSort), pagingSort);
    }

    @Override
    public List<AreaGetAllDto> getAllWithOutPage(Map<String, String> customQuery) {
        String status = Constants.ACTIVE_STATUS;

        if (customQuery.containsKey("status")) {
            status = customQuery.get("status");
        }

        return areaRepository.findByStatus(status).stream()
                .map(area -> AreaGetAllDto.builder()
                        .id(area.getId())
                        .description(area.getDescription())
                        .status(area.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Page<AreaGetAllDto> searchCustom(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 5;
        String status = Constants.ACTIVE_STATUS;
        String id = null;
        String description = null;

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
        if (customQuery.containsKey("description") && !customQuery.get("description").isEmpty()) {
            description = customQuery.get("description");
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Specification<EntityArea> spec = Specification.where(null);

        final String statusParam = status;
        spec = spec.and((root, query, cb) ->
                cb.equal(root.get("status"), statusParam));

        if (id != null) {
            final String idParam = id;
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("id").as(String.class), "%" + idParam + "%"));
        }

        if (description != null) {
            final String descParam = description;
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.upper(root.get("description")), "%" + descParam.toUpperCase() + "%"));
        }

        Page<EntityArea> entityPage = areaRepository.findAll(spec, pagingSort);

        return entityPage.map(entity -> AreaGetAllDto.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .build());
    }

    private AreaDto mapAreaDto(EntityArea entityArea) {
        AreaDto areaDto = new AreaDto();
        BeanUtils.copyProperties(entityArea, areaDto);
        return areaDto;
    }

        private Page<AreaGetAllDto> mapPageAreaDto(Page<EntityArea> entityPage, Pageable pagingSort) {
            int totalElements = (int) entityPage.getTotalElements();
            return new PageImpl<>(
                    entityPage.getContent().stream()
                            .map(area -> AreaGetAllDto.builder()
                                    .id(area.getId())
                                    .description(area.getDescription())
                                    .status(area.getStatus())
                                    .build())
                            .collect(Collectors.toList()), pagingSort, totalElements);
        }
}
