package com.ryuqq.setof.application.productimage.port.in.query;

import com.ryuqq.setof.application.productimage.dto.response.ProductImageResponse;
import java.util.List;

/**
 * 상품이미지 조회 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetProductImageUseCase {

    /**
     * 상품이미지 단건 조회
     *
     * @param productImageId 상품이미지 ID
     * @return 상품이미지 정보
     */
    ProductImageResponse getById(Long productImageId);

    /**
     * 상품그룹의 모든 이미지 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품이미지 목록
     */
    List<ProductImageResponse> getByProductGroupId(Long productGroupId);
}
