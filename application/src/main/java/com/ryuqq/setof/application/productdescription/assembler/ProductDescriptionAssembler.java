package com.ryuqq.setof.application.productdescription.assembler;

import com.ryuqq.setof.application.productdescription.dto.response.DescriptionImageResponse;
import com.ryuqq.setof.application.productdescription.dto.response.ProductDescriptionResponse;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import com.ryuqq.setof.domain.productdescription.vo.DescriptionImage;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 상품설명 Assembler
 *
 * <p>Domain ↔ Response DTO 변환을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductDescriptionAssembler {

    /**
     * Domain → Response 변환
     *
     * @param domain 상품설명 도메인
     * @return Response DTO
     */
    public ProductDescriptionResponse toResponse(ProductDescription domain) {
        List<DescriptionImageResponse> imageResponses =
                domain.getImages().stream().map(this::toImageResponse).toList();

        return new ProductDescriptionResponse(
                domain.getIdValue(),
                domain.getProductGroupIdValue(),
                domain.getHtmlContentValue(),
                imageResponses,
                domain.hasContent(),
                domain.allImagesCdnConverted(),
                domain.getCreatedAt(),
                domain.getUpdatedAt());
    }

    /**
     * 이미지 Domain → Response 변환
     *
     * @param image 이미지 VO
     * @return 이미지 Response DTO
     */
    private DescriptionImageResponse toImageResponse(DescriptionImage image) {
        return new DescriptionImageResponse(
                image.displayOrder(),
                image.getOriginUrlValue(),
                image.getCdnUrlValue(),
                image.uploadedAt(),
                image.isCdnConverted());
    }
}
