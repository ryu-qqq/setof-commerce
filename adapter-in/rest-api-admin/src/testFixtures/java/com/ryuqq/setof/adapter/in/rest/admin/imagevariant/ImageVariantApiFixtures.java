package com.ryuqq.setof.adapter.in.rest.admin.imagevariant;

import com.ryuqq.setof.adapter.in.rest.admin.v2.imagevariant.dto.command.SyncImageVariantsApiRequest;
import java.util.List;

/**
 * ImageVariant API 테스트 Fixtures.
 *
 * <p>이미지 Variant API 테스트에서 사용되는 Request 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ImageVariantApiFixtures {

    private ImageVariantApiFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_SOURCE_IMAGE_ID = 123L;
    public static final String DEFAULT_SOURCE_TYPE = "PRODUCT_GROUP_IMAGE";

    public static final String DEFAULT_VARIANT_TYPE_SMALL = "SMALL_WEBP";
    public static final String DEFAULT_VARIANT_TYPE_MEDIUM = "MEDIUM_WEBP";
    public static final String DEFAULT_RESULT_ASSET_ID = "asset-abc-123";
    public static final String DEFAULT_VARIANT_URL =
            "https://stage-cdn.set-of.com/public/2026/03/732c9b01.webp";
    public static final Integer DEFAULT_WIDTH = 300;
    public static final Integer DEFAULT_HEIGHT = 300;

    // ===== Sync Request Fixtures =====

    public static SyncImageVariantsApiRequest syncRequest() {
        return new SyncImageVariantsApiRequest(
                DEFAULT_SOURCE_IMAGE_ID,
                DEFAULT_SOURCE_TYPE,
                List.of(
                        new SyncImageVariantsApiRequest.VariantApiRequest(
                                DEFAULT_VARIANT_TYPE_SMALL,
                                DEFAULT_RESULT_ASSET_ID,
                                DEFAULT_VARIANT_URL,
                                DEFAULT_WIDTH,
                                DEFAULT_HEIGHT),
                        new SyncImageVariantsApiRequest.VariantApiRequest(
                                DEFAULT_VARIANT_TYPE_MEDIUM,
                                "asset-def-456",
                                "https://stage-cdn.set-of.com/public/2026/03/a1b2c3d4.webp",
                                600,
                                600)));
    }

    public static SyncImageVariantsApiRequest syncRequest(
            Long sourceImageId,
            String sourceType,
            List<SyncImageVariantsApiRequest.VariantApiRequest> variants) {
        return new SyncImageVariantsApiRequest(sourceImageId, sourceType, variants);
    }

    public static SyncImageVariantsApiRequest.VariantApiRequest variantRequest() {
        return new SyncImageVariantsApiRequest.VariantApiRequest(
                DEFAULT_VARIANT_TYPE_SMALL,
                DEFAULT_RESULT_ASSET_ID,
                DEFAULT_VARIANT_URL,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT);
    }

    public static SyncImageVariantsApiRequest.VariantApiRequest variantRequest(
            String variantType,
            String resultAssetId,
            String variantUrl,
            Integer width,
            Integer height) {
        return new SyncImageVariantsApiRequest.VariantApiRequest(
                variantType, resultAssetId, variantUrl, width, height);
    }

    public static SyncImageVariantsApiRequest syncRequestWithSingleVariant() {
        return new SyncImageVariantsApiRequest(
                DEFAULT_SOURCE_IMAGE_ID, DEFAULT_SOURCE_TYPE, List.of(variantRequest()));
    }

    public static SyncImageVariantsApiRequest syncRequestWithNullDimensions() {
        return new SyncImageVariantsApiRequest(
                DEFAULT_SOURCE_IMAGE_ID,
                DEFAULT_SOURCE_TYPE,
                List.of(
                        new SyncImageVariantsApiRequest.VariantApiRequest(
                                DEFAULT_VARIANT_TYPE_SMALL,
                                DEFAULT_RESULT_ASSET_ID,
                                DEFAULT_VARIANT_URL,
                                null,
                                null)));
    }
}
