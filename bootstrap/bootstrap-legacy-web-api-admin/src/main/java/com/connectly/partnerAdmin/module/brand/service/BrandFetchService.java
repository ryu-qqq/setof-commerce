package com.connectly.partnerAdmin.module.brand.service;

import com.connectly.partnerAdmin.module.brand.core.ExtendedBrandContext;
import com.connectly.partnerAdmin.module.brand.core.ExternalBrandContext;
import com.connectly.partnerAdmin.module.brand.filter.BrandFilter;
import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BrandFetchService {

    boolean hasBrandIdExist(long brandId);

    CustomPageable<ExtendedBrandContext> fetchBrands(BrandFilter filter, Pageable pageable);
    Optional<ExtendedBrandContext> fetchBrandWithName(String brandName);

    List<ExternalBrandContext> fetchExternalBrands(long siteId, List<String> mappingBrandIds);
}

