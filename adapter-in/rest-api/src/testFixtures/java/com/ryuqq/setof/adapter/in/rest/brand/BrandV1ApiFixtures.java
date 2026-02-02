package com.ryuqq.setof.adapter.in.rest.brand;

import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.query.BrandSearchV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.response.BrandDisplayV1ApiResponse;
import com.ryuqq.setof.application.brand.dto.response.BrandDisplayResult;
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
    public static final String DEFAULT_BRAND_ICON_URL = "https://cdn.setof.com/brands/nike.png";

    // ===== Search Request Fixtures =====

    /** 기본 검색 요청 */
    public static BrandSearchV1ApiRequest searchRequest() {
        return new BrandSearchV1ApiRequest(null, null);
    }

    /** 브랜드명 검색 요청 */
    public static BrandSearchV1ApiRequest searchRequest(String brandName) {
        return new BrandSearchV1ApiRequest(brandName, null);
    }

    /** 노출 여부 포함 검색 요청 */
    public static BrandSearchV1ApiRequest searchRequest(String brandName, Boolean displayed) {
        return new BrandSearchV1ApiRequest(brandName, displayed);
    }

    // ===== BrandDisplayResult Fixtures =====

    /** 기본 브랜드 표시 결과 */
    public static BrandDisplayResult brandDisplayResult() {
        return BrandDisplayResult.of(
                DEFAULT_BRAND_ID,
                DEFAULT_BRAND_NAME,
                DEFAULT_BRAND_NAME_KO,
                DEFAULT_BRAND_ICON_URL);
    }

    /** 커스텀 브랜드 표시 결과 */
    public static BrandDisplayResult brandDisplayResult(
            Long brandId, String brandName, String brandNameKo) {
        return BrandDisplayResult.of(brandId, brandName, brandNameKo, DEFAULT_BRAND_ICON_URL);
    }

    /** 여러 브랜드 표시 결과 */
    public static List<BrandDisplayResult> multipleBrandDisplayResults() {
        return List.of(
                BrandDisplayResult.of(1L, "NIKE", "나이키", "https://cdn.setof.com/brands/nike.png"),
                BrandDisplayResult.of(
                        2L, "ADIDAS", "아디다스", "https://cdn.setof.com/brands/adidas.png"),
                BrandDisplayResult.of(3L, "PUMA", "푸마", "https://cdn.setof.com/brands/puma.png"));
    }

    // ===== BrandResult Fixtures =====

    /** 기본 브랜드 결과 */
    public static BrandResult brandResult() {
        return BrandResult.of(
                DEFAULT_BRAND_ID,
                DEFAULT_BRAND_NAME,
                DEFAULT_BRAND_NAME_KO,
                DEFAULT_BRAND_ICON_URL,
                true);
    }

    /** 커스텀 브랜드 결과 */
    public static BrandResult brandResult(Long brandId, String brandName, String brandNameKo) {
        return BrandResult.of(brandId, brandName, brandNameKo, DEFAULT_BRAND_ICON_URL, true);
    }

    // ===== Response Fixtures =====

    /** 기본 브랜드 표시 응답 */
    public static BrandDisplayV1ApiResponse brandDisplayResponse() {
        return new BrandDisplayV1ApiResponse(
                DEFAULT_BRAND_ID,
                DEFAULT_BRAND_NAME,
                DEFAULT_BRAND_NAME_KO,
                DEFAULT_BRAND_ICON_URL);
    }

    /** 여러 브랜드 표시 응답 */
    public static List<BrandDisplayV1ApiResponse> multipleBrandDisplayResponses() {
        return List.of(
                new BrandDisplayV1ApiResponse(
                        1L, "NIKE", "나이키", "https://cdn.setof.com/brands/nike.png"),
                new BrandDisplayV1ApiResponse(
                        2L, "ADIDAS", "아디다스", "https://cdn.setof.com/brands/adidas.png"),
                new BrandDisplayV1ApiResponse(
                        3L, "PUMA", "푸마", "https://cdn.setof.com/brands/puma.png"));
    }
}
