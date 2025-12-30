package com.ryuqq.setof.adapter.in.rest.admin.integration.fixture;

import com.ryuqq.setof.application.brand.dto.response.BrandSummaryResponse;
import com.ryuqq.setof.application.common.response.PageResponse;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Brand Admin 통합 테스트 Fixture
 *
 * <p>Admin API 통합 테스트에서 사용하는 Brand 관련 상수 및 Response 빌더를 제공합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public final class BrandAdminTestFixture {

    private BrandAdminTestFixture() {
        // Utility class
    }

    // ============================================================
    // Brand Constants
    // ============================================================

    public static final Long DEFAULT_BRAND_ID = 1L;
    public static final String DEFAULT_BRAND_CODE = "BRAND001";
    public static final String DEFAULT_BRAND_NAME_KO = "테스트 브랜드";
    public static final String DEFAULT_BRAND_LOGO_URL = "https://example.com/brand-logo.png";

    // ============================================================
    // Response Builders for Mocking
    // ============================================================

    /**
     * BrandSummaryResponse Mock 데이터를 생성합니다.
     *
     * @return BrandSummaryResponse
     */
    public static BrandSummaryResponse createBrandSummaryResponse() {
        return createBrandSummaryResponse(DEFAULT_BRAND_ID, DEFAULT_BRAND_CODE);
    }

    /**
     * 커스텀 값으로 BrandSummaryResponse Mock 데이터를 생성합니다.
     *
     * @param id 브랜드 ID
     * @param code 브랜드 코드
     * @return BrandSummaryResponse
     */
    public static BrandSummaryResponse createBrandSummaryResponse(Long id, String code) {
        return new BrandSummaryResponse(id, code, DEFAULT_BRAND_NAME_KO, DEFAULT_BRAND_LOGO_URL);
    }

    /**
     * 여러 BrandSummaryResponse를 생성합니다.
     *
     * @param count 개수
     * @return BrandSummaryResponse 목록
     */
    public static List<BrandSummaryResponse> createBrandSummaryResponses(int count) {
        return IntStream.rangeClosed(1, count)
                .mapToObj(
                        i ->
                                new BrandSummaryResponse(
                                        (long) i,
                                        "BRAND" + String.format("%03d", i),
                                        DEFAULT_BRAND_NAME_KO + " " + i,
                                        DEFAULT_BRAND_LOGO_URL))
                .toList();
    }

    /**
     * PageResponse 형태의 BrandSummaryResponse Mock 데이터를 생성합니다.
     *
     * @param count 개수
     * @param page 현재 페이지
     * @param size 페이지 크기
     * @return PageResponse
     */
    public static PageResponse<BrandSummaryResponse> createBrandPageResponse(
            int count, int page, int size) {
        List<BrandSummaryResponse> content = createBrandSummaryResponses(count);
        long totalCount = count;
        int totalPages = (int) Math.ceil((double) totalCount / size);
        boolean isFirst = page == 0;
        boolean isLast = page >= totalPages - 1;
        return PageResponse.of(content, page, size, totalCount, totalPages, isFirst, isLast);
    }
}
