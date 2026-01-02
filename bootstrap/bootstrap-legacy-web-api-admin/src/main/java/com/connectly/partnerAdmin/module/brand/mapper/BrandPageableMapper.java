package com.connectly.partnerAdmin.module.brand.mapper;

import com.connectly.partnerAdmin.module.brand.core.ExtendedBrandContext;
import com.connectly.partnerAdmin.module.brand.core.ExternalBrandContext;
import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandPageableMapper {
    CustomPageable<ExtendedBrandContext> toBrandResponses(List<ExtendedBrandContext> brandContexts, Pageable pageable, long total);

}
