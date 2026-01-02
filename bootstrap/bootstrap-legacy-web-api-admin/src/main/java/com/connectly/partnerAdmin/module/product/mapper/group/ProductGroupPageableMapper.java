package com.connectly.partnerAdmin.module.product.mapper.group;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductGroupPageableMapper {

    CustomPageable<ProductGroupDetailResponse> toProductGroupDetailResponse(List<ProductGroupDetailResponse> productGroupDetailResponses, Pageable pageable, long total);

}
