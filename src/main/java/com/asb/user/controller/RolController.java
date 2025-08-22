package com.asb.user.controller;

import com.asb.user.model.dto.RolDto;
import com.asb.user.model.dto.RolGetAllDto;
import com.asb.user.model.dto.RolSaveAndUpdateDto;
import com.asb.user.service.IRolService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Rol", description = "APIs para la gestión de roles")
@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/rol")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class RolController {

    private final IRolService iRolService;

    @PostMapping("/create")
    public ResponseEntity<RolDto> save(@RequestBody @Valid RolSaveAndUpdateDto rolDto) {
        return ResponseEntity.ok(iRolService.save(rolDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolDto> get(@PathVariable("id") long id) {
        return ResponseEntity.ok(iRolService.get(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RolDto> update(@PathVariable("id") Long rolId, @RequestBody @Valid RolSaveAndUpdateDto rolDto) {
        return ResponseEntity.ok(iRolService.update(rolId, rolDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) {
        boolean result = iRolService.delete(id);
        if (result) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<Page<RolGetAllDto>> getAll(@RequestParam Map<String, String> customQuery) {
        return ResponseEntity.ok(iRolService.getAll(customQuery));
    }

    @GetMapping
    public ResponseEntity<Page<RolGetAllDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "5") int size,
                                                     @RequestParam(defaultValue = "ASC") String orders,
                                                     @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(iRolService.getAll(page, size, orders, sortBy));
    }

    @GetMapping("/get-all-without-page")
    public ResponseEntity<List<RolGetAllDto>> getAllWithoutPage(@RequestParam Map<String, String> customQuery) {
        return ResponseEntity.ok(iRolService.getAllWithOutPage(customQuery));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<RolGetAllDto>> search(@RequestParam Map<String, String> customQuery) {
        return ResponseEntity.ok(iRolService.searchCustom(customQuery));
    }
}
