package com.asb.user.controller;

import com.asb.user.model.dto.PermissionDto;
import com.asb.user.service.IPermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Permission", description = "APIs para la gestión de permisos")
@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/permission")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class PermissionController {

    private final IPermissionService permissionService;

    @GetMapping("/get-all-no-page")
    public ResponseEntity<List<PermissionDto>> getAllActive() {
        return ResponseEntity.ok(permissionService.getAllActive());
    }
}