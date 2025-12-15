package com.setof.connectly.module.brand.service;

import com.setof.connectly.module.brand.dto.BrandDisplayDto;
import com.setof.connectly.module.brand.dto.BrandFilter;
import java.util.List;

public interface BrandFindService {

    BrandDisplayDto fetchBrand(long brandId);

    List<BrandDisplayDto> fetchBrands(BrandFilter filter);
}
