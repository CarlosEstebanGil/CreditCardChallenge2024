package com.charly.service;

import com.charly.dto.BrandDTO;
import com.charly.entity.BrandEntity;

public interface BrandService {
    BrandDTO getBrandByName(String name);
    Long saveBrand(BrandEntity brandEntity);
    BrandEntity findBrandEntityByName(String name);
    Double getTasa();
}
