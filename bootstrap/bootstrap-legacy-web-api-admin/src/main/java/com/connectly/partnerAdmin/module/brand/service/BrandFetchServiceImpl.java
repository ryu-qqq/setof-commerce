package com.connectly.partnerAdmin.module.brand.service;

import com.connectly.partnerAdmin.module.brand.core.ExtendedBrandContext;
import com.connectly.partnerAdmin.module.brand.core.ExternalBrandContext;
import com.connectly.partnerAdmin.module.brand.filter.BrandFilter;
import com.connectly.partnerAdmin.module.brand.mapper.BrandPageableMapper;
import com.connectly.partnerAdmin.module.brand.repository.BrandFetchRepository;
import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.querydsl.core.NonUniqueResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class BrandFetchServiceImpl implements BrandFetchService {

    private final BrandFetchRepository brandFetchRepository;
    private final BrandPageableMapper brandPageableMapper;

    @Override
    public boolean hasBrandIdExist(long brandId){
        return brandFetchRepository.hasBrandIdExist(brandId);
    }

    @Override
    public CustomPageable<ExtendedBrandContext> fetchBrands(BrandFilter filter, Pageable pageable) {
        List<ExtendedBrandContext> brandContexts;
        if (filter.isNoOffsetFetch()) {
            brandContexts = fetchBrandContextsWithNoOffset(filter, pageable);
        }else {
            brandContexts = fetchBrandContexts(filter, pageable);
        }

        long total = fetchBrandCountQuery(filter);

        return brandPageableMapper.toBrandResponses(brandContexts, pageable, total);
    }

    @Override
    public Optional<ExtendedBrandContext> fetchBrandWithName(String brandName) {
        try {
            return brandFetchRepository.fetchBrandWithNameLike(brandName);
        } catch (NonUniqueResultException e) {
            log.error("Failed to fetch brand with name {}", brandName, e);
            return brandFetchRepository.fetchBrandWithNameEq(brandName);
        }
    }

    @Override
    public List<ExternalBrandContext> fetchExternalBrands(long siteId, List<String> mappingBrandIds) {
        return brandFetchRepository.fetchExternalBrandMappingContexts(siteId, mappingBrandIds);
    }


    private List<ExtendedBrandContext> fetchBrandContexts(BrandFilter filter, Pageable pageable) {
        return brandFetchRepository.fetchBrandContexts(filter, pageable);
    }


    private List<ExtendedBrandContext> fetchBrandContextsWithNoOffset(BrandFilter filter, Pageable pageable) {
        return brandFetchRepository.fetchBrandContextsWithNoOffset(filter, pageable);
    }




    private long fetchBrandCountQuery(BrandFilter filter){
        Long total = brandFetchRepository.fetchBrandCountQuery(filter).fetchOne();
        if (total == null) return 0L;
        else return total;
    }



}
