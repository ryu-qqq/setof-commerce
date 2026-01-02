package com.connectly.partnerAdmin.module.brand.mapper;


import com.connectly.partnerAdmin.module.brand.core.ExtendedBrandContext;
import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BrandPageableMapperImpl implements BrandPageableMapper {

    @Override
    public CustomPageable<ExtendedBrandContext> toBrandResponses(List<ExtendedBrandContext> brandContexts, Pageable pageable, long total) {
        Long lastDomainId = brandContexts.isEmpty() ? null : brandContexts.getLast().getBrandId();
        return new CustomPageable<>(brandContexts, pageable, total, lastDomainId);
    }



}
