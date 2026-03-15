package com.ryuqq.setof.application.imagevariant.internal;

import com.ryuqq.setof.application.imagevariant.manager.ImageVariantReadManager;
import com.ryuqq.setof.application.productgroupimage.manager.ProductGroupImageReadManager;
import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
import com.ryuqq.setof.domain.imagevariant.vo.ImageVariantType;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * ImageVariantUrlResolver - 이미지 Variant URL 해석기.
 *
 * <p>원본 이미지 ID를 기반으로 최적의 Variant URL을 조회합니다. Variant가 없으면 맵에 포함하지 않아 호출자가 원본 URL을 그대로 사용할 수 있습니다.
 *
 * <p>선택 전략:
 *
 * <ul>
 *   <li>리스트 썸네일: MEDIUM_WEBP (600x600) 우선
 *   <li>상세 이미지: LARGE_WEBP (1200x1200) 우선
 *   <li>fallback: 같은 sourceImageId의 아무 variant
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ImageVariantUrlResolver {

    private final ImageVariantReadManager variantReadManager;
    private final ProductGroupImageReadManager imageReadManager;

    public ImageVariantUrlResolver(
            ImageVariantReadManager variantReadManager,
            ProductGroupImageReadManager imageReadManager) {
        this.variantReadManager = variantReadManager;
        this.imageReadManager = imageReadManager;
    }

    /**
     * 이미지 ID 목록에 대해 Variant URL을 해석합니다.
     *
     * @param imageIds 원본 이미지 ID 목록
     * @param preferredType 우선 Variant 타입
     * @return imageId → variant URL 맵 (variant가 없는 이미지는 포함되지 않음)
     */
    public Map<Long, String> resolveByImageIds(
            List<Long> imageIds, ImageVariantType preferredType) {
        if (imageIds == null || imageIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<ImageVariant> variants = variantReadManager.findBySourceImageIds(imageIds);
        if (variants.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Long, List<ImageVariant>> variantsBySourceId =
                variants.stream().collect(Collectors.groupingBy(ImageVariant::sourceImageId));

        return variantsBySourceId.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> selectBestVariantUrl(entry.getValue(), preferredType)));
    }

    /**
     * 상품그룹 ID 목록의 대표(THUMBNAIL) 이미지에 대해 Variant URL을 해석합니다.
     *
     * <p>새 persistence-mysql 모듈에서 대표 이미지 ID를 조회한 뒤, 해당 imageId로 variant를 찾습니다.
     *
     * @param productGroupIds 상품그룹 ID 목록
     * @param preferredType 우선 Variant 타입
     * @return productGroupId → variant URL 맵 (variant가 없는 상품그룹은 포함되지 않음)
     */
    public Map<Long, String> resolveByProductGroupIds(
            List<Long> productGroupIds, ImageVariantType preferredType) {
        if (productGroupIds == null || productGroupIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<ProductGroupId> pgIds = productGroupIds.stream().map(ProductGroupId::of).toList();

        Map<Long, Long> thumbnailImageIdByPgId =
                imageReadManager.getThumbnailImageIdsByProductGroupIds(pgIds);
        if (thumbnailImageIdByPgId.isEmpty()) {
            return Collections.emptyMap();
        }

        List<Long> imageIds = thumbnailImageIdByPgId.values().stream().distinct().toList();
        Map<Long, String> variantUrlByImageId = resolveByImageIds(imageIds, preferredType);

        return thumbnailImageIdByPgId.entrySet().stream()
                .filter(entry -> variantUrlByImageId.containsKey(entry.getValue()))
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> variantUrlByImageId.get(entry.getValue())));
    }

    private String selectBestVariantUrl(
            List<ImageVariant> variants, ImageVariantType preferredType) {
        return variants.stream()
                .filter(v -> v.variantType() == preferredType)
                .findFirst()
                .map(ImageVariant::variantUrlValue)
                .orElseGet(
                        () ->
                                variants.stream()
                                        .findFirst()
                                        .map(ImageVariant::variantUrlValue)
                                        .orElse(null));
    }
}
