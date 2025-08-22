package com.asb.user.controller;

import com.asb.user.model.dto.MenuTypeDto;
import com.asb.user.model.dto.MenuTypeGetAllDto;
import com.asb.user.model.dto.MenuTypeSaveAndUpdateDto;
import com.asb.user.service.IMenuTypeService;
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

@Tag(name = "MenuType", description = "APIs para la gestión de tipos de menú")
@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/menu-type")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class MenuTypeController {

    private final IMenuTypeService iMenuTypeService;


    @PostMapping("/create")
    public ResponseEntity<MenuTypeDto> save(@RequestBody @Valid MenuTypeSaveAndUpdateDto menuTypeDto) {
        return ResponseEntity.ok(iMenuTypeService.save(menuTypeDto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<MenuTypeDto> get(@PathVariable("id") long id) {
        return ResponseEntity.ok(iMenuTypeService.get(id));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<MenuTypeDto> update(@PathVariable("id") Long menuTypeId, @RequestBody @Valid MenuTypeSaveAndUpdateDto menuTypeDto) {
        return ResponseEntity.ok(iMenuTypeService.update(menuTypeId, menuTypeDto));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) {
        boolean result = iMenuTypeService.delete(id);
        if (result) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/get-all")
    public ResponseEntity<Page<MenuTypeGetAllDto>> getAll(@RequestParam Map<String, String> customQuery) {
        return ResponseEntity.ok(iMenuTypeService.getAll(customQuery));
    }


    @GetMapping
    public ResponseEntity<Page<MenuTypeGetAllDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size,
                                                          @RequestParam(defaultValue = "ASC") String orders,
                                                          @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(iMenuTypeService.getAll(page, size, orders, sortBy));
    }


    @GetMapping("/get-all-without-page")
    public ResponseEntity<List<MenuTypeGetAllDto>> getAllWithoutPage(@RequestParam Map<String, String> customQuery) {
        return ResponseEntity.ok(iMenuTypeService.getAllWithOutPage(customQuery));
    }


    @GetMapping("/search")
    public ResponseEntity<Page<MenuTypeGetAllDto>> search(@RequestParam Map<String, String> customQuery) {
        return ResponseEntity.ok(iMenuTypeService.searchCustom(customQuery));
    }
}
