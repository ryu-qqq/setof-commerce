package com.ryuqq.setof.storage.legacy.composite.search.mapper;

import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupThumbnailCompositeResult;
import com.ryuqq.setof.storage.legacy.composite.search.dto.LegacyWebSearchQueryDto;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebSearchMapper - 레거시 Web 검색 Mapper.
 *
 * <p>QueryDto → Application Layer DTO 변환을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebSearchMapper {

    /**
     * 검색 결과 QueryDto → ProductGroupThumbnailCompositeResult 변환.
     *
     * @param dto QueryDto
     * @return ProductGroupThumbnailCompositeResult
     */
    public ProductGroupThumbnailCompositeResult toThumbnailCompositeResult(
            LegacyWebSearchQueryDto dto) {
        return new ProductGroupThumbnailCompositeResult(
                dto.productGroupId(),
                dto.sellerId(),
                dto.productGroupName(),
                dto.brandId(),
                dto.brandName(),
                dto.displayKoreanName(),
                dto.displayEnglishName(),
                dto.brandIconImageUrl(),
                dto.productImageUrl(),
                dto.regularPrice(),
                dto.currentPrice(),
                dto.salePrice(),
                dto.directDiscountRate(),
                dto.directDiscountPrice(),
                dto.discountRate(),
                dto.insertDate(),
                dto.averageRating(),
                dto.reviewCount(),
                dto.score(),
                dto.displayYn(),
                dto.soldOutYn());
    }

    /**
     * 검색 결과 QueryDto 목록 → ProductGroupThumbnailCompositeResult 목록 변환.
     *
     * @param dtos QueryDto 목록
     * @return ProductGroupThumbnailCompositeResult 목록
     */
    public List<ProductGroupThumbnailCompositeResult> toThumbnailCompositeResults(
            List<LegacyWebSearchQueryDto> dtos) {
        return dtos.stream().map(this::toThumbnailCompositeResult).toList();
    }
}
