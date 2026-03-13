package com.ryuqq.setof.adapter.in.rest.v1.brand;

import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.request.SearchBrandsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.response.BrandV1ApiResponse;
import com.ryuqq.setof.application.brand.dto.response.BrandDisplayResult;
import com.ryuqq.setof.application.brand.dto.response.BrandResult;
import java.util.List;

/**
 * Brand V1 API 테스트 Fixtures.
 *
 * <p>Brand 관련 API Request/Response 및 Application Result 객체를 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class BrandApiFixtures {

    private BrandApiFixtures() {}

    // ===== SearchBrandsV1ApiRequest =====

    public static SearchBrandsV1ApiRequest searchRequest() {
        return new SearchBrandsV1ApiRequest("나이키");
    }

    public static SearchBrandsV1ApiRequest searchRequest(String searchWord) {
        return new SearchBrandsV1ApiRequest(searchWord);
    }

    public static SearchBrandsV1ApiRequest searchRequestEmpty() {
        return new SearchBrandsV1ApiRequest(null);
    }

    // ===== BrandV1ApiResponse =====

    public static BrandV1ApiResponse brandResponse(long brandId) {
        return new BrandV1ApiResponse(
                brandId, "NIKE", "나이키", "https://cdn.example.com/brands/nike.png");
    }

    public static BrandV1ApiResponse brandResponse(
            long brandId, String brandName, String korBrandName, String iconUrl) {
        return new BrandV1ApiResponse(brandId, brandName, korBrandName, iconUrl);
    }

    public static List<BrandV1ApiResponse> brandResponseList() {
        return List.of(
                brandResponse(1L),
                brandResponse(2L, "ADIDAS", "아디다스", "https://cdn.example.com/brands/adidas.png"));
    }

    // ===== BrandDisplayResult =====

    public static BrandDisplayResult displayResult(long brandId) {
        return BrandDisplayResult.of(
                brandId, "NIKE", "나이키", "Nike", "https://cdn.example.com/brands/nike.png");
    }

    public static BrandDisplayResult displayResult(
            long brandId, String brandName, String brandNameKo, String brandIconUrl) {
        return BrandDisplayResult.of(brandId, brandName, brandNameKo, brandName, brandIconUrl);
    }

    public static List<BrandDisplayResult> displayResultList() {
        return List.of(
                displayResult(1L),
                displayResult(2L, "ADIDAS", "아디다스", "https://cdn.example.com/brands/adidas.png"));
    }

    // ===== BrandResult =====

    public static BrandResult brandResult(long brandId) {
        return BrandResult.of(
                brandId, "NIKE", "나이키", "Nike", "https://cdn.example.com/brands/nike.png", true);
    }

    public static BrandResult brandResult(
            long brandId,
            String brandName,
            String brandNameKo,
            String brandIconUrl,
            boolean displayed) {
        return BrandResult.of(brandId, brandName, brandNameKo, brandName, brandIconUrl, displayed);
    }
}
