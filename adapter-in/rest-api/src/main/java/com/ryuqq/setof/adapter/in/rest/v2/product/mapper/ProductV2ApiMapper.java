package com.ryuqq.setof.adapter.in.rest.v2.product.mapper;

import com.ryuqq.setof.adapter.in.rest.v2.product.dto.query.ProductSearchV2ApiRequest;
import com.ryuqq.setof.application.product.dto.query.ProductGroupSearchQuery;
import org.springframework.stereotype.Component;

/**
 * Product V2 API Mapper
 *
 * <p>API Request DTO를 Application Query로 변환
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ProductV2ApiMapper {

    /**
     * 검색 요청을 Query로 변환
     *
     * @param request API 요청
     * @return Application Query
     */
    public ProductGroupSearchQuery toSearchQuery(ProductSearchV2ApiRequest request) {
        return new ProductGroupSearchQuery(
                request.sellerId(),
                request.categoryId(),
                request.brandId(),
                request.name(),
                request.status(),
                request.page(),
                request.size());
    }
}
