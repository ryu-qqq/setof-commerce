package com.connectly.partnerAdmin.module.product.mapper.group;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductGroupPageableMapperImpl implements ProductGroupPageableMapper{


    @Override
    public CustomPageable<ProductGroupDetailResponse> toProductGroupDetailResponse(List<ProductGroupDetailResponse> productGroupDetailResponses, Pageable pageable, long total) {
        Long lastDomainId = productGroupDetailResponses.isEmpty() ? null : productGroupDetailResponses.getLast().getProductGroup().getProductGroupId();
        return new CustomPageable<>(productGroupDetailResponses, pageable, total, lastDomainId);
    }

}
