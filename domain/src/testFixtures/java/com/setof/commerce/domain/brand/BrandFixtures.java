package com.ryuqq.setof.domain.brand;

import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.aggregate.BrandUpdateData;
import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.brand.vo.BrandIconImageUrl;
import com.ryuqq.setof.domain.brand.vo.BrandName;
import com.ryuqq.setof.domain.brand.vo.DisplayEnglishName;
import com.ryuqq.setof.domain.brand.vo.DisplayKoreanName;
import com.ryuqq.setof.domain.brand.vo.DisplayOrder;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import java.time.Instant;

/**
 * Brand 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 Brand 관련 객체들을 생성합니다.
 */
public final class BrandFixtures {

    private BrandFixtures() {}

    // ===== 상수 =====
    public static final String DEFAULT_BRAND_NAME = "테스트브랜드";
    public static final String DEFAULT_ICON_URL = "https://example.com/brand-icon.png";
    public static final String DEFAULT_DISPLAY_KOREAN_NAME = "테스트 브랜드";
    public static final String DEFAULT_DISPLAY_ENGLISH_NAME = "Test Brand";
    public static final int DEFAULT_DISPLAY_ORDER = 1;

    // ===== Brand Aggregate Fixtures =====
    public static Brand newBrand() {
        return Brand.forNew(
                defaultBrandName(),
                defaultBrandIconImageUrl(),
                defaultDisplayKoreanName(),
                defaultDisplayEnglishName(),
                defaultDisplayOrder(),
                CommonVoFixtures.now());
    }

    public static Brand newBrand(
            String brandName, String displayKoreanName, String displayEnglishName) {
        return Brand.forNew(
                BrandName.of(brandName),
                defaultBrandIconImageUrl(),
                DisplayKoreanName.of(displayKoreanName),
                DisplayEnglishName.of(displayEnglishName),
                defaultDisplayOrder(),
                CommonVoFixtures.now());
    }

    public static Brand activeBrand() {
        return Brand.reconstitute(
                BrandId.of(1L),
                defaultBrandName(),
                defaultBrandIconImageUrl(),
                defaultDisplayKoreanName(),
                defaultDisplayEnglishName(),
                defaultDisplayOrder(),
                true,
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Brand activeBrand(Long id) {
        return Brand.reconstitute(
                BrandId.of(id),
                defaultBrandName(),
                defaultBrandIconImageUrl(),
                defaultDisplayKoreanName(),
                defaultDisplayEnglishName(),
                defaultDisplayOrder(),
                true,
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Brand inactiveBrand() {
        return Brand.reconstitute(
                BrandId.of(2L),
                defaultBrandName(),
                defaultBrandIconImageUrl(),
                defaultDisplayKoreanName(),
                defaultDisplayEnglishName(),
                defaultDisplayOrder(),
                false,
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Brand deletedBrand() {
        Instant deletedAt = CommonVoFixtures.yesterday();
        return Brand.reconstitute(
                BrandId.of(3L),
                defaultBrandName(),
                defaultBrandIconImageUrl(),
                defaultDisplayKoreanName(),
                defaultDisplayEnglishName(),
                defaultDisplayOrder(),
                false,
                deletedAt,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    // ===== VO Fixtures =====
    public static BrandName defaultBrandName() {
        return BrandName.of(DEFAULT_BRAND_NAME);
    }

    public static BrandIconImageUrl defaultBrandIconImageUrl() {
        return BrandIconImageUrl.of(DEFAULT_ICON_URL);
    }

    public static DisplayKoreanName defaultDisplayKoreanName() {
        return DisplayKoreanName.of(DEFAULT_DISPLAY_KOREAN_NAME);
    }

    public static DisplayEnglishName defaultDisplayEnglishName() {
        return DisplayEnglishName.of(DEFAULT_DISPLAY_ENGLISH_NAME);
    }

    public static DisplayOrder defaultDisplayOrder() {
        return DisplayOrder.of(DEFAULT_DISPLAY_ORDER);
    }

    // ===== BrandUpdateData Fixtures =====
    public static BrandUpdateData brandUpdateData() {
        return BrandUpdateData.of(2, true);
    }

    public static BrandUpdateData brandUpdateData(int displayOrder, boolean displayed) {
        return BrandUpdateData.of(displayOrder, displayed);
    }
}
