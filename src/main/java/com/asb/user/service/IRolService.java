package com.asb.user.service;

import com.asb.user.model.dto.RolDto;
import com.asb.user.model.dto.RolGetAllDto;
import com.asb.user.model.dto.RolSaveAndUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IRolService {

    RolDto get(long id);

    RolDto save(RolSaveAndUpdateDto rolDto);

    RolDto update(Long rolId, RolSaveAndUpdateDto rolDto);

    boolean delete(Long id);

    Page<RolGetAllDto> getAll(Map<String, String> customQuery);

    Page<RolGetAllDto> getAll(int page, int size, String orders, String sortBy);

    List<RolGetAllDto> getAllWithOutPage(Map<String, String> customQuery);

    Page<RolGetAllDto> searchCustom(Map<String, String> customQuery);
}
