package com.asb.user.service;

import com.asb.user.model.dto.AreaDto;
import com.asb.user.model.dto.AreaGetAllDto;
import com.asb.user.model.dto.AreaSaveAndUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IAreaService {


    AreaDto save(AreaSaveAndUpdateDto AreaDto);

    AreaDto update(Long AreaId, AreaSaveAndUpdateDto AreaDto);


    boolean delete(Long id);


    AreaDto get(Long id);


    Page<AreaGetAllDto> getAll(Map<String, String> customQuery);

    Page<AreaGetAllDto> getAll(int page, int size, String orders, String sortBy);


    List<AreaGetAllDto> getAllWithOutPage(Map<String, String> customQuery);

    Page<AreaGetAllDto> searchCustom(Map<String, String> customQuery);
}
