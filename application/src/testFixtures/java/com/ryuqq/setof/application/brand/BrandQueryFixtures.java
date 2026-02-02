package com.ryuqq.setof.application.brand;

import com.ryuqq.setof.application.brand.dto.query.BrandDisplaySearchParams;
import com.ryuqq.setof.application.brand.dto.query.BrandSearchParams;
import com.ryuqq.setof.application.common.dto.query.CommonSearchParams;

/**
 * Brand Query 테스트 Fixtures.
 *
 * <p>Brand 관련 Query 객체들을 생성하는 테스트 유틸리티입니다.
 */
public final class BrandQueryFixtures {

    private BrandQueryFixtures() {}

    // ===== BrandSearchParams =====

    public static BrandSearchParams searchParams() {
        return BrandSearchParams.of(null, defaultCommonSearchParams());
    }

    public static BrandSearchParams searchParams(String brandName) {
        return BrandSearchParams.of(brandName, defaultCommonSearchParams());
    }

    public static BrandSearchParams searchParams(String brandName, int page, int size) {
        return BrandSearchParams.of(brandName, commonSearchParams(page, size));
    }

    // ===== BrandDisplaySearchParams =====

    public static BrandDisplaySearchParams displaySearchParams() {
        return BrandDisplaySearchParams.of(null, true);
    }

    public static BrandDisplaySearchParams displaySearchParams(
            String brandName, Boolean displayed) {
        return BrandDisplaySearchParams.of(brandName, displayed);
    }

    // ===== CommonSearchParams =====

    public static CommonSearchParams defaultCommonSearchParams() {
        return CommonSearchParams.of(false, null, null, "createdAt", "DESC", 0, 20);
    }

    public static CommonSearchParams commonSearchParams(int page, int size) {
        return CommonSearchParams.of(false, null, null, "createdAt", "DESC", page, size);
    }
}
