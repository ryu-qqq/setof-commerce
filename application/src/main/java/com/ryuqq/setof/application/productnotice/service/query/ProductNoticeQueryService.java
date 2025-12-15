package com.ryuqq.setof.application.productnotice.service.query;

import com.ryuqq.setof.application.productnotice.assembler.ProductNoticeAssembler;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeResponse;
import com.ryuqq.setof.application.productnotice.manager.query.ProductNoticeReadManager;
import com.ryuqq.setof.application.productnotice.port.in.query.GetProductNoticeUseCase;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * 상품고시 조회 서비스
 *
 * <p>상품고시를 조회합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ProductNoticeQueryService implements GetProductNoticeUseCase {

    private final ProductNoticeReadManager productNoticeReadManager;
    private final ProductNoticeAssembler productNoticeAssembler;

    public ProductNoticeQueryService(
            ProductNoticeReadManager productNoticeReadManager,
            ProductNoticeAssembler productNoticeAssembler) {
        this.productNoticeReadManager = productNoticeReadManager;
        this.productNoticeAssembler = productNoticeAssembler;
    }

    @Override
    public ProductNoticeResponse execute(Long productNoticeId) {
        ProductNotice productNotice = productNoticeReadManager.findById(productNoticeId);
        return productNoticeAssembler.toResponse(productNotice);
    }

    @Override
    public Optional<ProductNoticeResponse> findByProductGroupId(Long productGroupId) {
        return productNoticeReadManager
                .findByProductGroupId(productGroupId)
                .map(productNoticeAssembler::toResponse);
    }
}
