package com.ryuqq.setof.application.productgroup.service.query;

import com.ryuqq.setof.application.productgroup.dto.composite.LegacyProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupDetailResult;
import com.ryuqq.setof.application.productgroup.internal.ProductGroupReadFacade;
import com.ryuqq.setof.application.productgroup.port.in.query.GetProductGroupDetailUseCase;
import org.springframework.stereotype.Service;

/**
 * GetProductGroupDetailService - 상품그룹 단건 상세 조회 Service.
 *
 * <p>GET /api/v1/product/group/{productGroupId}
 *
 * <p>기본 정보(쿼리 1) + 개별 상품 목록(쿼리 2) + 이미지 목록(쿼리 3)을 조합하여 반환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetProductGroupDetailService implements GetProductGroupDetailUseCase {

    private final ProductGroupReadFacade readFacade;

    public GetProductGroupDetailService(ProductGroupReadFacade readFacade) {
        this.readFacade = readFacade;
    }

    @Override
    public ProductGroupDetailResult execute(Long productGroupId) {
        LegacyProductGroupDetailCompositeResult composite =
                readFacade.getDetailBundle(productGroupId);
        return ProductGroupDetailResult.from(composite);
    }
}
