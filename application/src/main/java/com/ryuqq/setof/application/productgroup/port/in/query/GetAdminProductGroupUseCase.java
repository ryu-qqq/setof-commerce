package com.ryuqq.setof.application.productgroup.port.in.query;

import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeResult;

/**
 * GetAdminProductGroupUseCase - Admin 상품그룹 상세 조회 UseCase.
 *
 * <p>Admin API 전용 상품그룹 상세 조회입니다. 셀러 권한 검증 없이 모든 상품그룹에 접근할 수 있습니다.
 *
 * <p>GET /api/v2/admin/product-groups/{productGroupId}
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface GetAdminProductGroupUseCase {
    ProductGroupDetailCompositeResult execute(Long productGroupId);
}
