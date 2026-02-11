package com.ryuqq.setof.storage.legacy.composite.web.product.mapper;

import com.ryuqq.setof.application.legacy.product.dto.response.LegacyProductGroupDetailResult;
import com.ryuqq.setof.application.legacy.product.dto.response.LegacyProductGroupThumbnailResult;
import com.ryuqq.setof.storage.legacy.composite.web.product.dto.LegacyWebProductGroupBasicQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.product.dto.LegacyWebProductGroupThumbnailQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.product.dto.LegacyWebProductImageQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.product.dto.LegacyWebProductQueryDto;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * LegacyWebProductGroupMapper - 레거시 웹 상품그룹 Mapper.
 *
 * <p>QueryDto -> Application Result 변환을 담당합니다.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: 수동 매핑 구현 (MapStruct 사용 안함).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebProductGroupMapper {

    /**
     * QueryDto -> LegacyProductGroupThumbnailResult 변환.
     *
     * @param dto QueryDto
     * @return LegacyProductGroupThumbnailResult
     */
    public LegacyProductGroupThumbnailResult toResult(LegacyWebProductGroupThumbnailQueryDto dto) {
        if (dto == null) {
            return null;
        }
        return LegacyProductGroupThumbnailResult.of(
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
                dto.createdAt(),
                dto.averageRating() != null ? dto.averageRating() : 0.0,
                dto.reviewCount() != null ? dto.reviewCount() : 0L,
                dto.score(),
                dto.displayYn(),
                dto.soldOutYn());
    }

    /**
     * QueryDto 목록 -> LegacyProductGroupThumbnailResult 목록 변환.
     *
     * @param dtos QueryDto 목록
     * @return LegacyProductGroupThumbnailResult 목록
     */
    public List<LegacyProductGroupThumbnailResult> toResults(
            List<LegacyWebProductGroupThumbnailQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toResult).toList();
    }

    /**
     * 상세 조회 DTO들 -> LegacyProductGroupDetailResult 변환.
     *
     * <p>3개의 분리된 쿼리 결과를 하나의 Result로 조합합니다:
     *
     * <ul>
     *   <li>기본 정보 (LegacyWebProductGroupBasicQueryDto)
     *   <li>상품+옵션 목록 (List&lt;LegacyWebProductQueryDto&gt;)
     *   <li>이미지 목록 (List&lt;LegacyWebProductImageQueryDto&gt;)
     * </ul>
     *
     * @param basicDto 기본 정보 DTO
     * @param productDtos 상품+옵션 DTO 목록
     * @param imageDtos 이미지 DTO 목록
     * @return LegacyProductGroupDetailResult
     */
    public LegacyProductGroupDetailResult toDetailResult(
            LegacyWebProductGroupBasicQueryDto basicDto,
            List<LegacyWebProductQueryDto> productDtos,
            List<LegacyWebProductImageQueryDto> imageDtos) {
        if (basicDto == null) {
            return null;
        }

        // 브랜드 정보 생성
        LegacyProductGroupDetailResult.LegacyBrandInfo brandInfo =
                LegacyProductGroupDetailResult.LegacyBrandInfo.of(
                        basicDto.brandId(),
                        basicDto.brandName(),
                        basicDto.displayKoreanName(),
                        basicDto.displayEnglishName(),
                        basicDto.brandIconImageUrl());

        // 상품+옵션 그룹화 (productId 기준)
        Map<Long, List<LegacyWebProductQueryDto>> productMap =
                (productDtos != null ? productDtos : List.<LegacyWebProductQueryDto>of())
                        .stream()
                                .collect(
                                        Collectors.groupingBy(LegacyWebProductQueryDto::productId));

        // 상품 정보 Set 생성
        Set<LegacyProductGroupDetailResult.LegacyProductInfo> products =
                productMap.entrySet().stream()
                        .map(
                                entry -> {
                                    LegacyWebProductQueryDto firstDto = entry.getValue().get(0);
                                    List<LegacyProductGroupDetailResult.LegacyOptionInfo> options =
                                            entry.getValue().stream()
                                                    .filter(
                                                            dto ->
                                                                    dto.optionGroupId() != null
                                                                            && dto.optionDetailId()
                                                                                    != null)
                                                    .map(
                                                            dto ->
                                                                    LegacyProductGroupDetailResult
                                                                            .LegacyOptionInfo.of(
                                                                            dto.optionGroupId(),
                                                                            dto.optionGroupName(),
                                                                            dto.optionDetailId(),
                                                                            dto.optionValue()))
                                                    .toList();

                                    return LegacyProductGroupDetailResult.LegacyProductInfo.of(
                                            firstDto.productId(),
                                            firstDto.additionalPrice(),
                                            firstDto.stockQuantity(),
                                            firstDto.soldOutYn(),
                                            options);
                                })
                        .collect(Collectors.toSet());

        // 이미지 정보 Set 생성
        Set<LegacyProductGroupDetailResult.LegacyProductImageInfo> productImages =
                (imageDtos != null ? imageDtos : List.<LegacyWebProductImageQueryDto>of())
                        .stream()
                                .map(
                                        dto ->
                                                LegacyProductGroupDetailResult
                                                        .LegacyProductImageInfo.of(
                                                        dto.productGroupImageId(),
                                                        dto.imageType(),
                                                        dto.imageUrl()))
                                .collect(Collectors.toSet());

        return LegacyProductGroupDetailResult.of(
                basicDto.productGroupId(),
                basicDto.productGroupName(),
                basicDto.sellerId(),
                basicDto.sellerName(),
                brandInfo,
                basicDto.categoryId(),
                basicDto.path(),
                basicDto.regularPrice(),
                basicDto.currentPrice(),
                basicDto.salePrice(),
                basicDto.optionType(),
                basicDto.displayYn(),
                basicDto.soldOutYn(),
                basicDto.detailDescription(),
                basicDto.deliveryNotice(),
                basicDto.refundNotice(),
                basicDto.averageRating() != null ? basicDto.averageRating() : 0.0,
                basicDto.reviewCount() != null ? basicDto.reviewCount() : 0L,
                products,
                productImages,
                basicDto.createdAt());
    }
}
