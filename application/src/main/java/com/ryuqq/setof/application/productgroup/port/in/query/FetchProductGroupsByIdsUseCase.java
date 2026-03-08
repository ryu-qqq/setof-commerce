package com.ryuqq.setof.application.productgroup.port.in.query;

import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupSliceResult;
import java.util.List;

/**
 * FetchProductGroupsByIdsUseCase - ID 목록 기반 상품그룹 조회 UseCase.
 *
 * <p>GET /api/v1/products/group/recent (찜 목록 등 ID 목록 기반 조회)
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface FetchProductGroupsByIdsUseCase {
    ProductGroupSliceResult execute(List<Long> productGroupIds);
}
