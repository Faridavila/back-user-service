package com.asb.user.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ModelMapperLocal {

    private ModelMapper modelMapper;

    ModelMapperLocal() {
        this.modelMapper = new ModelMapper();
    }

    public ModelMapper getModelMapper() {
        return modelMapper;
    }

    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}