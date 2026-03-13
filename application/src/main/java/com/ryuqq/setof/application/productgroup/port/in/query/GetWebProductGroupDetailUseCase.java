package com.ryuqq.setof.application.productgroup.port.in.query;

import com.ryuqq.setof.application.productgroup.dto.composite.WebProductGroupDetailCompositeResult;

/**
 * GetWebProductGroupDetailUseCase - 웹(사용자) 상품그룹 상세 조회 UseCase.
 *
 * <p>GET /api/v2/product-groups/{productGroupId}
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetWebProductGroupDetailUseCase {
    WebProductGroupDetailCompositeResult execute(Long productGroupId);
}
