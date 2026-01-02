package com.connectly.partnerAdmin.module.external.dto.brand;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandListWrapper {

    private List<OcoBrandResponseDto> brandList;

    public BrandListWrapper(List<OcoBrandResponseDto> brandList) {
        this.brandList = brandList;
    }
}

