package com.ryuqq.setof.application.productgroup.dto.composite;

import com.ryuqq.setof.application.productgroupimage.dto.response.ImageWithVariantsResult;
import com.ryuqq.setof.domain.imagevariant.vo.ImageVariantType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ProductGroupDetailImageResults - 상세 이미지 목록 + Variant URL 래핑 객체.
 *
 * <p>이미지 + Variant 조회 결과를 래핑하고, 내부에서 LARGE_WEBP variant URL 해석까지 캡슐화합니다. Facade에서 생성하면 내부에서 variant
 * URL 맵을 구성하고, Assembler에서는 조회된 결과만 사용합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ProductGroupDetailImageResults {

    private final List<ImageWithVariantsResult> images;
    private final Map<Long, String> variantUrlsByImageId;

    private ProductGroupDetailImageResults(
            List<ImageWithVariantsResult> images, Map<Long, String> variantUrlsByImageId) {
        this.images = images;
        this.variantUrlsByImageId = variantUrlsByImageId;
    }

    /**
     * 이미지 목록에서 LARGE_WEBP variant URL을 해석하여 래핑 객체를 생성합니다.
     *
     * @param images 이미지 + Variant 통합 조회 결과
     * @return variant URL이 포함된 래핑 객체
     */
    public static ProductGroupDetailImageResults create(List<ImageWithVariantsResult> images) {
        if (images == null || images.isEmpty()) {
            return new ProductGroupDetailImageResults(List.of(), Map.of());
        }

        Map<Long, String> variantUrls =
                images.stream()
                        .filter(ImageWithVariantsResult::hasVariants)
                        .collect(
                                Collectors.toMap(
                                        ImageWithVariantsResult::imageId,
                                        img -> img.resolveUrl(ImageVariantType.LARGE_WEBP.name()),
                                        (a, b) -> a));

        return new ProductGroupDetailImageResults(images, variantUrls);
    }

    /**
     * 특정 이미지의 enriched URL을 반환합니다.
     *
     * <p>variant URL이 있으면 variant, 없으면 원본 URL을 반환합니다.
     *
     * @param imageId 이미지 ID
     * @param originalUrl 원본 이미지 URL
     * @return enriched URL
     */
    public String resolveImageUrl(long imageId, String originalUrl) {
        return variantUrlsByImageId.getOrDefault(imageId, originalUrl);
    }

    public List<ImageWithVariantsResult> images() {
        return images;
    }

    public Map<Long, String> variantUrlsByImageId() {
        return variantUrlsByImageId;
    }

    public boolean isEmpty() {
        return images.isEmpty();
    }
}
