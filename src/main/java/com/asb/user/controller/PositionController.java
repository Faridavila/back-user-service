package com.asb.user.controller;

import com.asb.user.model.dto.PositionDto;
import com.asb.user.model.dto.PositionGetAllDto;
import com.asb.user.model.dto.PositionSaveAndUpdateDto;
import com.asb.user.service.IPositionService;
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

@Tag(name = "Position", description = "APIs para la gestión de posiciones")
@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/position")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class PositionController {

    private final IPositionService iPositionService;

    @PostMapping("/create")
    public ResponseEntity<PositionDto> save(@RequestBody @Valid PositionSaveAndUpdateDto positionDto) {
        return ResponseEntity.ok(iPositionService.save(positionDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PositionDto> get(@PathVariable("id") long id) {
        return ResponseEntity.ok(iPositionService.get(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PositionDto> update(@PathVariable("id") Long positionId, @RequestBody @Valid PositionSaveAndUpdateDto positionDto) {
        return ResponseEntity.ok(iPositionService.update(positionId, positionDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) {
        boolean result = iPositionService.delete(id);
        if (result) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<Page<PositionGetAllDto>> getAll(@RequestParam Map<String, String> customQuery) {
        return ResponseEntity.ok(iPositionService.getAll(customQuery));
    }

    @GetMapping
    public ResponseEntity<Page<PositionGetAllDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size,
                                                          @RequestParam(defaultValue = "ASC") String orders,
                                                          @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(iPositionService.getAll(page, size, orders, sortBy));
    }

    @GetMapping("/get-all-without-page")
    public ResponseEntity<List<PositionGetAllDto>> getAllWithoutPage(@RequestParam Map<String, String> customQuery) {
        return ResponseEntity.ok(iPositionService.getAllWithOutPage(customQuery));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PositionGetAllDto>> search(@RequestParam Map<String, String> customQuery) {
        return ResponseEntity.ok(iPositionService.searchCustom(customQuery));
    }
}
