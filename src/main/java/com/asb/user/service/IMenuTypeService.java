package com.asb.user.service;

import com.asb.user.model.dto.MenuTypeDto;
import com.asb.user.model.dto.MenuTypeGetAllDto;
import com.asb.user.model.dto.MenuTypeSaveAndUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IMenuTypeService {


    MenuTypeDto save(MenuTypeSaveAndUpdateDto menuTypeDto);


    MenuTypeDto update(Long menuTypeId, MenuTypeSaveAndUpdateDto menuTypeDto);


    boolean delete(Long id);

    MenuTypeDto get(Long id);


    Page<MenuTypeGetAllDto> getAll(Map<String, String> customQuery);


    Page<MenuTypeGetAllDto> getAll(int page, int size, String orders, String sortBy);

    List<MenuTypeGetAllDto> getAllWithOutPage(Map<String, String> customQuery);


    Page<MenuTypeGetAllDto> searchCustom(Map<String, String> customQuery);
}
