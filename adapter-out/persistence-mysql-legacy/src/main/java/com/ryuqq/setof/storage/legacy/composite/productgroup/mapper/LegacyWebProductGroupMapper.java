package com.ryuqq.setof.storage.legacy.composite.productgroup.mapper;

import com.ryuqq.setof.application.productgroup.dto.composite.LegacyProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.composite.LegacyProductGroupDetailCompositeResult.OptionInfoResult;
import com.ryuqq.setof.application.productgroup.dto.composite.LegacyProductGroupDetailCompositeResult.ProductImageResult;
import com.ryuqq.setof.application.productgroup.dto.composite.LegacyProductGroupDetailCompositeResult.ProductInfoResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListCompositeResult;
import com.ryuqq.setof.storage.legacy.composite.productgroup.dto.LegacyWebProductGroupBasicQueryDto;
import com.ryuqq.setof.storage.legacy.composite.productgroup.dto.LegacyWebProductGroupThumbnailQueryDto;
import com.ryuqq.setof.storage.legacy.composite.productgroup.dto.LegacyWebProductImageQueryDto;
import com.ryuqq.setof.storage.legacy.composite.productgroup.dto.LegacyWebProductQueryDto;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * LegacyWebProductGroupMapper - 레거시 Web 상품그룹 Mapper.
 *
 * <p>QueryDto → Application Layer DTO 변환을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebProductGroupMapper {

    /**
     * 썸네일 QueryDto → ProductGroupListCompositeResult 변환.
     *
     * <p>레거시 DB에 없는 필드(sellerName, categoryId 등)는 기본값으로 설정됩니다.
     */
    public ProductGroupListCompositeResult toListCompositeResult(
            LegacyWebProductGroupThumbnailQueryDto dto) {
        boolean soldOut = "Y".equals(dto.soldOutYn());
        boolean displayed = "Y".equals(dto.displayYn());
        String status = soldOut ? "SOLD_OUT" : (displayed ? "ACTIVE" : "INACTIVE");

        return new ProductGroupListCompositeResult(
                dto.productGroupId(),
                dto.sellerId(),
                null,
                dto.brandId(),
                dto.brandName(),
                dto.displayKoreanName(),
                dto.displayEnglishName(),
                dto.brandIconImageUrl(),
                0L,
                null,
                null,
                0,
                dto.productGroupName(),
                null,
                status,
                soldOut,
                displayed,
                dto.productImageUrl(),
                0,
                dto.regularPrice(),
                dto.currentPrice(),
                dto.salePrice(),
                dto.discountRate(),
                0,
                0,
                0,
                List.of(),
                dto.insertDate() != null
                        ? dto.insertDate().toInstant(ZoneOffset.of("+09:00"))
                        : null,
                dto.insertDate() != null
                        ? dto.insertDate().toInstant(ZoneOffset.of("+09:00"))
                        : null);
    }

    /** 썸네일 QueryDto 목록 → ProductGroupListCompositeResult 목록 변환. */
    public List<ProductGroupListCompositeResult> toListCompositeResults(
            List<LegacyWebProductGroupThumbnailQueryDto> dtos) {
        return dtos.stream().map(this::toListCompositeResult).toList();
    }

    /**
     * 요청 ID 순서 기준으로 목록 재정렬.
     *
     * @param productGroupIds 요청 순서 ID 목록
     * @param results 조회된 목록
     * @return 요청 ID 순서로 재정렬된 목록
     */
    public List<ProductGroupListCompositeResult> reOrder(
            List<Long> productGroupIds, List<ProductGroupListCompositeResult> results) {
        Map<Long, ProductGroupListCompositeResult> resultMap =
                results.stream()
                        .collect(
                                Collectors.toMap(
                                        ProductGroupListCompositeResult::id, r -> r, (a, b) -> a));
        List<ProductGroupListCompositeResult> ordered = new ArrayList<>();
        for (Long id : productGroupIds) {
            ProductGroupListCompositeResult r = resultMap.get(id);
            if (r != null) {
                ordered.add(r);
            }
        }
        return ordered;
    }

    /** 상품그룹 상세 정보 조합 → LegacyProductGroupDetailCompositeResult 변환. */
    public LegacyProductGroupDetailCompositeResult toDetailCompositeResult(
            LegacyWebProductGroupBasicQueryDto basic,
            List<LegacyWebProductQueryDto> products,
            List<LegacyWebProductImageQueryDto> images) {

        Set<ProductInfoResult> productInfos = toProductInfoSet(products);
        Set<ProductImageResult> imageInfos = toImageInfoSet(images);

        return new LegacyProductGroupDetailCompositeResult(
                basic.productGroupId(),
                basic.productGroupName(),
                basic.sellerId(),
                basic.sellerName(),
                basic.brandId(),
                basic.brandName(),
                basic.displayKoreanName(),
                basic.displayEnglishName(),
                basic.brandIconImageUrl(),
                basic.categoryId(),
                basic.path(),
                basic.regularPrice(),
                basic.currentPrice(),
                basic.salePrice(),
                basic.directDiscountRate(),
                basic.directDiscountPrice(),
                basic.discountRate(),
                basic.optionType(),
                basic.displayYn(),
                basic.soldOutYn(),
                basic.detailDescription(),
                basic.deliveryNotice(),
                basic.refundNotice(),
                basic.averageRating(),
                basic.reviewCount(),
                productInfos,
                imageInfos,
                basic.insertDate());
    }

    private Set<ProductInfoResult> toProductInfoSet(List<LegacyWebProductQueryDto> dtos) {
        Map<Long, List<LegacyWebProductQueryDto>> grouped = new LinkedHashMap<>();
        for (LegacyWebProductQueryDto dto : dtos) {
            grouped.computeIfAbsent(dto.productId(), k -> new ArrayList<>()).add(dto);
        }

        Set<ProductInfoResult> result = new LinkedHashSet<>();
        for (Map.Entry<Long, List<LegacyWebProductQueryDto>> entry : grouped.entrySet()) {
            List<LegacyWebProductQueryDto> group = entry.getValue();
            LegacyWebProductQueryDto first = group.get(0);

            List<OptionInfoResult> options =
                    group.stream()
                            .filter(d -> d.optionGroupId() != 0)
                            .map(
                                    d ->
                                            new OptionInfoResult(
                                                    d.optionGroupId(),
                                                    d.optionGroupName(),
                                                    d.optionDetailId(),
                                                    d.optionValue()))
                            .toList();

            result.add(
                    new ProductInfoResult(
                            first.productId(),
                            first.additionalPrice(),
                            first.stockQuantity(),
                            first.soldOutYn(),
                            options));
        }
        return result;
    }

    private Set<ProductImageResult> toImageInfoSet(List<LegacyWebProductImageQueryDto> dtos) {
        Set<ProductImageResult> result = new LinkedHashSet<>();
        dtos.stream()
                .sorted(
                        (a, b) -> {
                            boolean aMain = "MAIN".equals(a.imageType());
                            boolean bMain = "MAIN".equals(b.imageType());
                            if (aMain && !bMain) {
                                return -1;
                            }
                            if (!aMain && bMain) {
                                return 1;
                            }
                            return 0;
                        })
                .forEach(
                        dto ->
                                result.add(
                                        new ProductImageResult(
                                                dto.productGroupImageId(),
                                                dto.imageType(),
                                                dto.imageUrl())));
        return result;
    }
}
