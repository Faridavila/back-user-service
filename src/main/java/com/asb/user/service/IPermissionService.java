package com.asb.user.service;

import com.asb.user.model.dto.PermissionDto;

import java.util.List;

public interface IPermissionService {
    List<PermissionDto> getAllActive();
}