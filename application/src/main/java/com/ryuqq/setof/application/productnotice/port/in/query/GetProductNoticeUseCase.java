package com.ryuqq.setof.application.productnotice.port.in.query;

import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeResponse;
import java.util.Optional;

/**
 * 상품고시 조회 UseCase
 *
 * <p>상품고시를 조회합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetProductNoticeUseCase {

    /**
     * 상품고시 ID로 조회
     *
     * @param productNoticeId 상품고시 ID
     * @return 상품고시 응답
     */
    ProductNoticeResponse execute(Long productNoticeId);

    /**
     * 상품그룹 ID로 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품고시 응답 (없으면 Optional.empty)
     */
    Optional<ProductNoticeResponse> findByProductGroupId(Long productGroupId);
}
