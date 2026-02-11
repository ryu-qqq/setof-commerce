package com.ryuqq.setof.adapter.in.rest.admin.v1.brand.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.request.BrandSearchV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.response.BrandV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.application.brand.dto.query.BrandSearchParams;
import com.ryuqq.setof.application.brand.dto.response.BrandPageResult;
import com.ryuqq.setof.application.brand.dto.response.BrandResult;
import com.ryuqq.setof.application.common.dto.query.CommonSearchParams;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * BrandAdminV1ApiMapper - 브랜드 Admin V1 API Request/Response 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-002: 양방향 변환 지원.
 *
 * <p>API-MAP-003: Application Result → API Response 변환.
 *
 * <p>레거시 BrandController.fetchBrands 흐름 변환.
 *
 * <p>mainDisplayType (US/KR) → searchField 매핑:
 *
 * <ul>
 *   <li>US → displayEnglishName
 *   <li>KR → displayKoreanName
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class BrandAdminV1ApiMapper {

    private static final String SEARCH_FIELD_KR = "displayKoreanName";
    private static final String SEARCH_FIELD_US = "displayEnglishName";
    private static final String DEFAULT_SORT_KEY = "createdAt";
    private static final String DEFAULT_MAIN_DISPLAY_TYPE = "US";
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final String DEFAULT_SORT_DIRECTION = "ASC";

    /**
     * BrandSearchV1ApiRequest → BrandSearchParams 변환.
     *
     * <p>mainDisplayType에 따라 검색 필드를 결정합니다.
     *
     * <p>No-Offset(lastBrandId)은 현재 SearchBrandByOffsetUseCase 미지원으로 Offset만 사용합니다.
     *
     * @param request 검색 요청 DTO
     * @return BrandSearchParams
     */
    public BrandSearchParams toSearchParams(BrandSearchV1ApiRequest request) {
        String mainDisplayType =
                request.mainDisplayType() != null && !request.mainDisplayType().isBlank()
                        ? request.mainDisplayType()
                        : DEFAULT_MAIN_DISPLAY_TYPE;
        String searchField =
                "KR".equalsIgnoreCase(mainDisplayType) ? SEARCH_FIELD_KR : SEARCH_FIELD_US;
        String searchWord =
                request.brandName() != null && !request.brandName().isBlank()
                        ? request.brandName().trim()
                        : null;
        int page = request.page() != null ? request.page() : DEFAULT_PAGE;
        int size = request.size() != null ? request.size() : DEFAULT_SIZE;
        String sortDirection =
                request.sortDirection() != null && !request.sortDirection().isBlank()
                        ? request.sortDirection()
                        : DEFAULT_SORT_DIRECTION;

        CommonSearchParams searchParams =
                CommonSearchParams.of(
                        false, null, null, DEFAULT_SORT_KEY, sortDirection, page, size);

        return BrandSearchParams.of(searchField, searchWord, searchParams);
    }

    /**
     * BrandPageResult → CustomPageableV1ApiResponse 변환.
     *
     * <p>레거시 CustomPageable 구조 호환.
     *
     * @param pageResult Application 페이지 결과
     * @return CustomPageableV1ApiResponse
     */
    public CustomPageableV1ApiResponse<BrandV1ApiResponse> toPageResponse(
            BrandPageResult pageResult) {
        List<BrandV1ApiResponse> content =
                pageResult.content().stream().map(this::toResponse).toList();
        return CustomPageableV1ApiResponse.of(
                content,
                pageResult.pageMeta().page(),
                pageResult.pageMeta().size(),
                pageResult.pageMeta().totalElements());
    }

    /**
     * BrandResult → BrandV1ApiResponse 변환.
     *
     * <p>Application Layer의 brandName/brandNameKo를 displayEnglishName/displayKoreanName으로 매핑.
     *
     * <p>mainDisplayType은 기본값 "US".
     *
     * @param result BrandResult
     * @return BrandV1ApiResponse
     */
    public BrandV1ApiResponse toResponse(BrandResult result) {
        long brandId = result.brandId() != null ? result.brandId() : 0L;
        String brandName = result.brandName() != null ? result.brandName() : "";
        String brandNameKo = result.brandNameKo() != null ? result.brandNameKo() : "";
        return new BrandV1ApiResponse(
                brandId, brandName, DEFAULT_MAIN_DISPLAY_TYPE, brandName, brandNameKo);
    }
}
