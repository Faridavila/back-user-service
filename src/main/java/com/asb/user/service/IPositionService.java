package com.asb.user.service;

import com.asb.user.model.dto.PositionDto;
import com.asb.user.model.dto.PositionGetAllDto;
import com.asb.user.model.dto.PositionSaveAndUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IPositionService {

    PositionDto save(PositionSaveAndUpdateDto positionDto);

    PositionDto update(Long positionId, PositionSaveAndUpdateDto positionDto);

    boolean delete(Long id);

    PositionDto get(Long id);

    Page<PositionGetAllDto> getAll(Map<String, String> customQuery);

    Page<PositionGetAllDto> getAll(int page, int size, String orders, String sortBy);

    List<PositionGetAllDto> getAllWithOutPage(Map<String, String> customQuery);

    Page<PositionGetAllDto> searchCustom(Map<String, String> customQuery);
}
