package com.ryuqq.setof.domain.imagevariant;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
import com.ryuqq.setof.domain.imagevariant.id.ImageVariantId;
import com.ryuqq.setof.domain.imagevariant.vo.ImageDimension;
import com.ryuqq.setof.domain.imagevariant.vo.ImageSourceType;
import com.ryuqq.setof.domain.imagevariant.vo.ImageVariantDiff;
import com.ryuqq.setof.domain.imagevariant.vo.ImageVariantType;
import com.ryuqq.setof.domain.imagevariant.vo.ImageVariantUpdateData;
import com.ryuqq.setof.domain.imagevariant.vo.ImageVariants;
import com.ryuqq.setof.domain.imagevariant.vo.ResultAssetId;
import com.ryuqq.setof.domain.productgroup.vo.ImageUrl;
import java.util.List;

/**
 * ImageVariant 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 ImageVariant 관련 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ImageVariantFixtures {

    private ImageVariantFixtures() {}

    // ===== ID Fixtures =====

    public static ImageVariantId defaultImageVariantId() {
        return ImageVariantId.of(1L);
    }

    public static ImageVariantId imageVariantId(Long value) {
        return ImageVariantId.of(value);
    }

    public static ImageVariantId newImageVariantId() {
        return ImageVariantId.forNew();
    }

    // ===== VO Fixtures =====

    public static ResultAssetId defaultResultAssetId() {
        return ResultAssetId.of("asset-uuid-001");
    }

    public static ResultAssetId resultAssetId(String value) {
        return ResultAssetId.of(value);
    }

    public static ImageDimension defaultDimension() {
        return ImageDimension.of(600, 600);
    }

    public static ImageDimension smallDimension() {
        return ImageDimension.of(300, 300);
    }

    public static ImageDimension largeDimension() {
        return ImageDimension.of(1200, 1200);
    }

    public static ImageDimension nullDimension() {
        return ImageDimension.of(null, null);
    }

    public static ImageUrl defaultVariantUrl() {
        return ImageUrl.of("https://cdn.example.com/images/variant/600x600.webp");
    }

    public static ImageUrl variantUrl(String value) {
        return ImageUrl.of(value);
    }

    // ===== Aggregate Fixtures =====

    public static ImageVariant newImageVariant() {
        return ImageVariant.forNew(
                100L,
                ImageSourceType.PRODUCT_GROUP_IMAGE,
                ImageVariantType.MEDIUM_WEBP,
                defaultResultAssetId(),
                defaultVariantUrl(),
                defaultDimension(),
                CommonVoFixtures.now());
    }

    public static ImageVariant newImageVariant(Long sourceImageId, ImageVariantType variantType) {
        return ImageVariant.forNew(
                sourceImageId,
                ImageSourceType.PRODUCT_GROUP_IMAGE,
                variantType,
                ResultAssetId.of("asset-" + variantType.name().toLowerCase()),
                ImageUrl.of("https://cdn.example.com/images/" + variantType.toFileSuffix()),
                ImageDimension.of(variantType.width(), variantType.height()),
                CommonVoFixtures.now());
    }

    public static ImageVariant activeImageVariant() {
        return ImageVariant.reconstitute(
                defaultImageVariantId(),
                100L,
                ImageSourceType.PRODUCT_GROUP_IMAGE,
                ImageVariantType.MEDIUM_WEBP,
                defaultResultAssetId(),
                defaultVariantUrl(),
                defaultDimension(),
                CommonVoFixtures.yesterday(),
                null);
    }

    public static ImageVariant activeImageVariant(Long id) {
        return ImageVariant.reconstitute(
                ImageVariantId.of(id),
                100L,
                ImageSourceType.PRODUCT_GROUP_IMAGE,
                ImageVariantType.MEDIUM_WEBP,
                defaultResultAssetId(),
                defaultVariantUrl(),
                defaultDimension(),
                CommonVoFixtures.yesterday(),
                null);
    }

    public static ImageVariant deletedImageVariant() {
        return ImageVariant.reconstitute(
                ImageVariantId.of(99L),
                100L,
                ImageSourceType.PRODUCT_GROUP_IMAGE,
                ImageVariantType.SMALL_WEBP,
                ResultAssetId.of("asset-deleted-001"),
                ImageUrl.of("https://cdn.example.com/images/300x300.webp"),
                smallDimension(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static ImageVariant originalWebpVariant() {
        return ImageVariant.reconstitute(
                ImageVariantId.of(2L),
                100L,
                ImageSourceType.PRODUCT_GROUP_IMAGE,
                ImageVariantType.ORIGINAL_WEBP,
                ResultAssetId.of("asset-original-001"),
                ImageUrl.of("https://cdn.example.com/images/original.webp"),
                nullDimension(),
                CommonVoFixtures.yesterday(),
                null);
    }

    // ===== ImageVariants (Collection VO) Fixtures =====

    public static ImageVariants emptyImageVariants() {
        return ImageVariants.of(List.of());
    }

    public static ImageVariants singleImageVariants() {
        return ImageVariants.of(List.of(activeImageVariant()));
    }

    public static ImageVariants multipleImageVariants() {
        return ImageVariants.of(
                List.of(
                        ImageVariant.reconstitute(
                                ImageVariantId.of(1L),
                                100L,
                                ImageSourceType.PRODUCT_GROUP_IMAGE,
                                ImageVariantType.SMALL_WEBP,
                                ResultAssetId.of("asset-small-001"),
                                ImageUrl.of("https://cdn.example.com/images/300x300.webp"),
                                smallDimension(),
                                CommonVoFixtures.yesterday(),
                                null),
                        ImageVariant.reconstitute(
                                ImageVariantId.of(2L),
                                100L,
                                ImageSourceType.PRODUCT_GROUP_IMAGE,
                                ImageVariantType.MEDIUM_WEBP,
                                ResultAssetId.of("asset-medium-001"),
                                ImageUrl.of("https://cdn.example.com/images/600x600.webp"),
                                defaultDimension(),
                                CommonVoFixtures.yesterday(),
                                null)));
    }

    // ===== ImageVariantUpdateData Fixtures =====

    public static ImageVariantUpdateData defaultUpdateData() {
        ImageVariants newVariants = ImageVariants.of(List.of(newImageVariant()));
        return ImageVariantUpdateData.of(newVariants, CommonVoFixtures.now());
    }

    public static ImageVariantUpdateData emptyUpdateData() {
        return ImageVariantUpdateData.of(emptyImageVariants(), CommonVoFixtures.now());
    }

    // ===== ImageVariantDiff Fixtures =====

    public static ImageVariantDiff emptyDiff() {
        return ImageVariantDiff.of(List.of(), List.of(), List.of(), CommonVoFixtures.now());
    }
}
