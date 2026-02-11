package com.ryuqq.setof.adapter.in.rest.admin.v1.brand;

import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.request.BrandSearchV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.response.BrandV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.application.brand.dto.response.BrandPageResult;
import com.ryuqq.setof.application.brand.dto.response.BrandResult;
import java.util.List;

/**
 * Brand Admin V1 API 테스트 Fixtures.
 *
 * <p>Brand Admin 관련 API Request/Response 및 Application Result 객체를 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class BrandAdminApiFixtures {

    private BrandAdminApiFixtures() {}

    // ===== BrandSearchV1ApiRequest =====

    public static BrandSearchV1ApiRequest searchRequest() {
        return new BrandSearchV1ApiRequest(null, "Nike", "US", 0, 20, "ASC");
    }

    public static BrandSearchV1ApiRequest searchRequestKr() {
        return new BrandSearchV1ApiRequest(null, "나이키", "KR", 0, 20, "ASC");
    }

    public static BrandSearchV1ApiRequest searchRequestNullBrandName() {
        return new BrandSearchV1ApiRequest(null, null, "US", 0, 20, "ASC");
    }

    public static BrandSearchV1ApiRequest searchRequestBlankBrandName() {
        return new BrandSearchV1ApiRequest(null, "   ", "US", 0, 20, "ASC");
    }

    public static BrandSearchV1ApiRequest searchRequestDefaultValues() {
        return new BrandSearchV1ApiRequest(null, null, null, null, null, null);
    }

    public static BrandSearchV1ApiRequest searchRequestWithPage(int page, int size) {
        return new BrandSearchV1ApiRequest(null, "Nike", "US", page, size, "ASC");
    }

    public static BrandSearchV1ApiRequest searchRequestWithSortDirection(String sortDirection) {
        return new BrandSearchV1ApiRequest(null, "Nike", "US", 0, 20, sortDirection);
    }

    // ===== BrandV1ApiResponse =====

    public static BrandV1ApiResponse brandResponse(long brandId) {
        return new BrandV1ApiResponse(brandId, "Nike", "US", "Nike", "나이키");
    }

    public static BrandV1ApiResponse brandResponse(
            long brandId, String brandName, String brandNameKo) {
        return new BrandV1ApiResponse(brandId, brandName, "US", brandName, brandNameKo);
    }

    public static List<BrandV1ApiResponse> brandResponseList() {
        return List.of(brandResponse(1L), brandResponse(2L, "Adidas", "아디다스"));
    }

    // ===== BrandResult =====

    public static BrandResult brandResult(long brandId) {
        return BrandResult.of(
                brandId, "Nike", "나이키", "https://cdn.example.com/brands/nike.png", true);
    }

    public static BrandResult brandResult(long brandId, String brandName, String brandNameKo) {
        return BrandResult.of(
                brandId, brandName, brandNameKo, "https://cdn.example.com/brands/icon.png", true);
    }

    public static BrandResult brandResultNullFields() {
        return BrandResult.of(null, null, null, null, false);
    }

    public static List<BrandResult> brandResultList() {
        return List.of(brandResult(1L), brandResult(2L, "Adidas", "아디다스"));
    }

    // ===== BrandPageResult =====

    public static BrandPageResult brandPageResult() {
        return BrandPageResult.of(brandResultList(), 0, 20, 2L);
    }

    public static BrandPageResult brandPageResult(int page, int size, long totalCount) {
        return BrandPageResult.of(brandResultList(), page, size, totalCount);
    }

    public static BrandPageResult emptyBrandPageResult() {
        return BrandPageResult.empty();
    }

    // ===== CustomPageableV1ApiResponse =====

    public static CustomPageableV1ApiResponse<BrandV1ApiResponse> brandPageResponse() {
        return CustomPageableV1ApiResponse.of(brandResponseList(), 0, 20, 2L);
    }

    public static CustomPageableV1ApiResponse<BrandV1ApiResponse> brandPageResponse(
            int page, int size, long totalElements) {
        return CustomPageableV1ApiResponse.of(brandResponseList(), page, size, totalElements);
    }

    public static CustomPageableV1ApiResponse<BrandV1ApiResponse> emptyBrandPageResponse() {
        return CustomPageableV1ApiResponse.of(List.of(), 0, 20, 0L);
    }
}
