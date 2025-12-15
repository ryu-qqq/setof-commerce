package com.setof.connectly.module.brand.repository;

import com.setof.connectly.module.brand.dto.BrandDisplayDto;
import com.setof.connectly.module.brand.dto.BrandFilter;
import java.util.List;
import java.util.Optional;

public interface BrandFindRepository {

    Optional<BrandDisplayDto> fetchBrand(long brandId);

    List<BrandDisplayDto> fetchBrands();

    List<BrandDisplayDto> fetchBrands(BrandFilter filter);
}
