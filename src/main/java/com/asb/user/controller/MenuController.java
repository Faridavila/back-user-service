package com.asb.user.controller;

import com.asb.user.model.dto.MenuDto;
import com.asb.user.model.dto.MenuGetAllDto;
import com.asb.user.model.dto.MenuSaveAndUpdateDto;
import com.asb.user.service.IMenuService;
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

@Tag(name = "Menu", description = "Menu APIs")
@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/menu")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class MenuController {

    private final IMenuService iMenuService;

    @PostMapping("/create")
    public ResponseEntity<MenuDto> save(@RequestBody @Valid MenuSaveAndUpdateDto menuDto) {
        return ResponseEntity.ok(iMenuService.save(menuDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuDto> get(@PathVariable("id") long id) {
        return ResponseEntity.ok(iMenuService.get(id));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<MenuDto> update(@PathVariable("id") Long menuId, @RequestBody @Valid MenuSaveAndUpdateDto menuDto) {
        return ResponseEntity.ok(iMenuService.update(menuId, menuDto));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) {
        boolean result = iMenuService.delete(id);
        if (result) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/get-all")
    public ResponseEntity<Page<MenuGetAllDto>> getAll(@RequestParam Map<String, String> customQuery) {
        return ResponseEntity.ok(iMenuService.getAll(customQuery));
    }


    @GetMapping
    public ResponseEntity<Page<MenuGetAllDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "5") int size,
                                                      @RequestParam(defaultValue = "ASC") String orders,
                                                      @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(iMenuService.getAll(page, size, orders, sortBy));
    }


    @GetMapping("/get-all-without-page")
    public ResponseEntity<List<MenuGetAllDto>> getAllWithoutPage(@RequestParam Map<String, String> customQuery) {
        return ResponseEntity.ok(iMenuService.getAllWithOutPage(customQuery));
    }


    @GetMapping("/search")
    public ResponseEntity<Page<MenuGetAllDto>> search(@RequestParam Map<String, String> customQuery) {
        return ResponseEntity.ok(iMenuService.searchCustom(customQuery));
    }
}
