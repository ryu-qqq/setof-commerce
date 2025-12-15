package com.ryuqq.setof.application.productdescription.port.in.query;

import com.ryuqq.setof.application.productdescription.dto.response.ProductDescriptionResponse;
import java.util.Optional;

/**
 * 상품설명 조회 UseCase
 *
 * <p>상품설명을 조회합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetProductDescriptionUseCase {

    /**
     * 상품설명 ID로 조회
     *
     * @param productDescriptionId 상품설명 ID
     * @return 상품설명 응답
     */
    ProductDescriptionResponse execute(Long productDescriptionId);

    /**
     * 상품그룹 ID로 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품설명 응답 (없으면 Optional.empty)
     */
    Optional<ProductDescriptionResponse> findByProductGroupId(Long productGroupId);
}
