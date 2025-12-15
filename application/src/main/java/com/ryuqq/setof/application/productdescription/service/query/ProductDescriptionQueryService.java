package com.ryuqq.setof.application.productdescription.service.query;

import com.ryuqq.setof.application.productdescription.assembler.ProductDescriptionAssembler;
import com.ryuqq.setof.application.productdescription.dto.response.ProductDescriptionResponse;
import com.ryuqq.setof.application.productdescription.manager.query.ProductDescriptionReadManager;
import com.ryuqq.setof.application.productdescription.port.in.query.GetProductDescriptionUseCase;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * 상품설명 조회 서비스
 *
 * <p>상품설명을 조회합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ProductDescriptionQueryService implements GetProductDescriptionUseCase {

    private final ProductDescriptionReadManager productDescriptionReadManager;
    private final ProductDescriptionAssembler productDescriptionAssembler;

    public ProductDescriptionQueryService(
            ProductDescriptionReadManager productDescriptionReadManager,
            ProductDescriptionAssembler productDescriptionAssembler) {
        this.productDescriptionReadManager = productDescriptionReadManager;
        this.productDescriptionAssembler = productDescriptionAssembler;
    }

    @Override
    public ProductDescriptionResponse execute(Long productDescriptionId) {
        ProductDescription productDescription =
                productDescriptionReadManager.findById(productDescriptionId);
        return productDescriptionAssembler.toResponse(productDescription);
    }

    @Override
    public Optional<ProductDescriptionResponse> findByProductGroupId(Long productGroupId) {
        return productDescriptionReadManager
                .findByProductGroupId(productGroupId)
                .map(productDescriptionAssembler::toResponse);
    }
}
