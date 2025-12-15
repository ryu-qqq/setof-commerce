package com.ryuqq.setof.application.product.service.query;

import com.ryuqq.setof.application.product.dto.response.FullProductResponse;
import com.ryuqq.setof.application.product.facade.ProductQueryFacade;
import com.ryuqq.setof.application.product.port.in.query.GetFullProductUseCase;
import org.springframework.stereotype.Service;

/**
 * 전체 상품 조회 Service
 *
 * <p>상품그룹 + SKU + 이미지 + 설명 + 고시 + 재고 통합 조회
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class GetFullProductService implements GetFullProductUseCase {

    private final ProductQueryFacade queryFacade;

    public GetFullProductService(ProductQueryFacade queryFacade) {
        this.queryFacade = queryFacade;
    }

    /**
     * 상품 전체 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return 전체 상품 정보
     */
    @Override
    public FullProductResponse getFullProduct(Long productGroupId) {
        validateProductGroupId(productGroupId);
        return queryFacade.getFullProduct(productGroupId);
    }

    /** 상품그룹 ID 유효성 검증 */
    private void validateProductGroupId(Long productGroupId) {
        if (productGroupId == null) {
            throw new IllegalArgumentException("상품그룹 ID는 필수입니다.");
        }
    }
}
