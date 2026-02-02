package com.ryuqq.setof.adapter.in.rest.admin.brand;

import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.query.BrandSearchV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.response.ExtendedBrandV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.application.brand.dto.response.BrandPageResult;
import com.ryuqq.setof.application.brand.dto.response.BrandResult;
import java.util.List;

/**
 * Brand V1 API 테스트 Fixtures.
 *
 * <p>브랜드 V1 API 테스트에서 사용되는 Request/Response/Result 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class BrandV1ApiFixtures {

    private BrandV1ApiFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_BRAND_ID = 1L;
    public static final String DEFAULT_BRAND_NAME = "NIKE";
    public static final String DEFAULT_BRAND_NAME_KO = "나이키";
    public static final String DEFAULT_ICON_URL = "https://example.com/nike-icon.png";
    public static final String DEFAULT_MAIN_DISPLAY_TYPE = "ENGLISH";

    // ===== Search Request Fixtures =====

    /** 기본 검색 요청 */
    public static BrandSearchV1ApiRequest searchRequest() {
        return new BrandSearchV1ApiRequest(null, 0, 20);
    }

    /** 브랜드명 검색 요청 */
    public static BrandSearchV1ApiRequest searchRequest(String brandName) {
        return new BrandSearchV1ApiRequest(brandName, 0, 20);
    }

    /** 페이징 포함 검색 요청 */
    public static BrandSearchV1ApiRequest searchRequest(
            String brandName, Integer page, Integer size) {
        return new BrandSearchV1ApiRequest(brandName, page, size);
    }

    // ===== Result Fixtures (Application Layer 응답) =====

    /** 기본 브랜드 결과 */
    public static BrandResult brandResult() {
        return brandResult(DEFAULT_BRAND_ID, DEFAULT_BRAND_NAME, DEFAULT_BRAND_NAME_KO);
    }

    /** 커스텀 브랜드 결과 */
    public static BrandResult brandResult(Long id, String brandName, String brandNameKo) {
        return BrandResult.of(id, brandName, brandNameKo, DEFAULT_ICON_URL, true);
    }

    /** 비활성 브랜드 결과 */
    public static BrandResult inactiveBrandResult(Long id, String brandName, String brandNameKo) {
        return BrandResult.of(id, brandName, brandNameKo, DEFAULT_ICON_URL, false);
    }

    /** 브랜드 페이지 결과 */
    public static BrandPageResult pageResult() {
        return pageResult(List.of(brandResult()), 1L, 0, 20);
    }

    /** 커스텀 페이지 결과 */
    public static BrandPageResult pageResult(
            List<BrandResult> content, long totalCount, int page, int size) {
        return BrandPageResult.of(content, totalCount, page, size);
    }

    /** 빈 페이지 결과 */
    public static BrandPageResult emptyPageResult() {
        return BrandPageResult.empty();
    }

    /** 여러 브랜드 결과 */
    public static List<BrandResult> multipleBrandResults() {
        return List.of(
                brandResult(1L, "NIKE", "나이키"),
                brandResult(2L, "ADIDAS", "아디다스"),
                brandResult(3L, "PUMA", "퓨마"));
    }

    // ===== Response Fixtures (API 응답) =====

    /** 확장 브랜드 응답 */
    public static ExtendedBrandV1ApiResponse extendedBrandResponse() {
        return new ExtendedBrandV1ApiResponse(
                DEFAULT_BRAND_ID,
                DEFAULT_BRAND_NAME,
                DEFAULT_MAIN_DISPLAY_TYPE,
                DEFAULT_BRAND_NAME,
                DEFAULT_BRAND_NAME_KO);
    }

    /** 페이지 응답 */
    public static CustomPageableV1ApiResponse<ExtendedBrandV1ApiResponse> pageResponse() {
        return CustomPageableV1ApiResponse.of(List.of(extendedBrandResponse()), 0, 20, 1L);
    }
}
