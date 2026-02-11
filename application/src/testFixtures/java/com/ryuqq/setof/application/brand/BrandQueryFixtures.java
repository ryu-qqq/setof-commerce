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
        return BrandSearchParams.of(null, null, defaultCommonSearchParams());
    }

    public static BrandSearchParams searchParams(String searchField, String searchWord) {
        return BrandSearchParams.of(searchField, searchWord, defaultCommonSearchParams());
    }

    public static BrandSearchParams searchParams(
            String searchField, String searchWord, int page, int size) {
        return BrandSearchParams.of(searchField, searchWord, commonSearchParams(page, size));
    }

    /** searchWord만 전달 시 searchField=brandName 기본 적용 (편의용) */
    public static BrandSearchParams searchParams(String searchWord) {
        return BrandSearchParams.of("brandName", searchWord, defaultCommonSearchParams());
    }

    /** searchWord만 전달 시 searchField=brandName 기본 적용 (편의용) */
    public static BrandSearchParams searchParams(String searchWord, int page, int size) {
        return BrandSearchParams.of("brandName", searchWord, commonSearchParams(page, size));
    }

    // ===== BrandDisplaySearchParams =====

    public static BrandDisplaySearchParams displaySearchParams() {
        return BrandDisplaySearchParams.of(null, null, true);
    }

    public static BrandDisplaySearchParams displaySearchParams(
            String searchField, String searchWord, Boolean displayed) {
        return BrandDisplaySearchParams.of(searchField, searchWord, displayed);
    }

    /** searchWord만 전달 시 searchField=brandName 기본 적용 (편의용) */
    public static BrandDisplaySearchParams displaySearchParams(
            String searchWord, Boolean displayed) {
        return BrandDisplaySearchParams.of("brandName", searchWord, displayed);
    }

    // ===== CommonSearchParams =====

    public static CommonSearchParams defaultCommonSearchParams() {
        return CommonSearchParams.of(false, null, null, "createdAt", "DESC", 0, 20);
    }

    public static CommonSearchParams commonSearchParams(int page, int size) {
        return CommonSearchParams.of(false, null, null, "createdAt", "DESC", page, size);
    }
}
