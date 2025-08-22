package com.asb.user.service;

import com.asb.user.model.dto.MenuDto;
import com.asb.user.model.dto.MenuGetAllDto;
import com.asb.user.model.dto.MenuSaveAndUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IMenuService {

    MenuDto save(MenuSaveAndUpdateDto menuDto);

    MenuDto update(Long menuId, MenuSaveAndUpdateDto menuDto);

    boolean delete(Long id);

    MenuDto get(Long id);

    Page<MenuGetAllDto> getAll(Map<String, String> customQuery);

    Page<MenuGetAllDto> getAll(int page, int size, String orders, String sortBy);

    List<MenuGetAllDto> getAllWithOutPage(Map<String, String> customQuery);

    Page<MenuGetAllDto> searchCustom(Map<String, String> customQuery);
}
