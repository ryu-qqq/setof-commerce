package com.connectly.partnerAdmin.module.brand.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.connectly.partnerAdmin.module.brand.core.ExtendedBrandContext;
import com.connectly.partnerAdmin.module.brand.core.ExternalBrandContext;
import com.connectly.partnerAdmin.module.brand.filter.BrandFilter;
import com.querydsl.jpa.impl.JPAQuery;

public interface BrandFetchRepository {
    boolean hasBrandIdExist(long brandId);

    List<ExtendedBrandContext> fetchBrandContexts(BrandFilter filter, Pageable pageable);
    List<ExtendedBrandContext> fetchBrandContextsWithNoOffset(BrandFilter filter, Pageable pageable);
    List<ExternalBrandContext> fetchExternalBrandMappingContexts(long siteId, List<String> mappingBrandIds);
    JPAQuery<Long> fetchBrandCountQuery(BrandFilter filterDto);

    Optional<ExtendedBrandContext> fetchBrandWithNameLike(String brandName);
    Optional<ExtendedBrandContext> fetchBrandWithNameEq(String brandName);
    Long fetchBySiteIdAndMappingBrandId(long siteId, String mappingBrandId);


}
