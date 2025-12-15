package com.ryuqq.setof.application.productimage.service.query;

import com.ryuqq.setof.application.productimage.assembler.ProductImageAssembler;
import com.ryuqq.setof.application.productimage.dto.response.ProductImageResponse;
import com.ryuqq.setof.application.productimage.manager.query.ProductImageReadManager;
import com.ryuqq.setof.application.productimage.port.in.query.GetProductImageUseCase;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 상품이미지 조회 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class GetProductImageService implements GetProductImageUseCase {

    private final ProductImageReadManager readManager;
    private final ProductImageAssembler assembler;

    public GetProductImageService(
            ProductImageReadManager readManager, ProductImageAssembler assembler) {
        this.readManager = readManager;
        this.assembler = assembler;
    }

    @Override
    public ProductImageResponse getById(Long productImageId) {
        ProductImage productImage = readManager.findById(productImageId);
        return assembler.toResponse(productImage);
    }

    @Override
    public List<ProductImageResponse> getByProductGroupId(Long productGroupId) {
        List<ProductImage> productImages = readManager.findByProductGroupId(productGroupId);
        return assembler.toResponses(productImages);
    }
}
