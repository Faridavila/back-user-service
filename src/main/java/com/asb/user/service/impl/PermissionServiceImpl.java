package com.asb.user.service.impl;

import com.asb.user.model.dto.PermissionDto;
import com.asb.user.model.entity.EntityPermission;
import com.asb.user.repository.IPermissionRepository;
import com.asb.user.service.IPermissionService;
import com.asb.user.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PermissionServiceImpl implements IPermissionService {

    private final IPermissionRepository permissionRepository;

    @Override
    public List<PermissionDto> getAllActive() {
        return permissionRepository.findByStatus(Constants.ACTIVE_STATUS)
                .stream()
                .map(this::mapPermissionDto)
                .collect(Collectors.toList());
    }

    private PermissionDto mapPermissionDto(EntityPermission entity) {
        return PermissionDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .path(entity.getPath())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .build();
    }
}