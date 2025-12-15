package com.ryuqq.setof.application.productimage.assembler;

import com.ryuqq.setof.application.productimage.dto.response.ProductImageResponse;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductImage Assembler
 *
 * <p>Domain 객체를 Response DTO로 변환
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductImageAssembler {

    /**
     * ProductImage -> Response 변환
     *
     * @param productImage ProductImage 도메인
     * @return ProductImageResponse
     */
    public ProductImageResponse toResponse(ProductImage productImage) {
        return new ProductImageResponse(
                productImage.getIdValue(),
                productImage.getProductGroupIdValue(),
                productImage.getImageTypeValue(),
                productImage.getOriginUrlValue(),
                productImage.getCdnUrlValue(),
                productImage.getDisplayOrder(),
                productImage.getCreatedAt());
    }

    /**
     * ProductImage 목록 -> Response 목록 변환
     *
     * @param productImages ProductImage 목록
     * @return ProductImageResponse 목록
     */
    public List<ProductImageResponse> toResponses(List<ProductImage> productImages) {
        return productImages.stream().map(this::toResponse).toList();
    }
}
