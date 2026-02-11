package com.ryuqq.setof.adapter.in.rest.v1.brand.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.request.SearchBrandsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.response.BrandV1ApiResponse;
import com.ryuqq.setof.application.brand.dto.query.BrandDisplaySearchParams;
import com.ryuqq.setof.application.brand.dto.response.BrandDisplayResult;
import com.ryuqq.setof.application.brand.dto.response.BrandResult;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * BrandV1ApiMapper - 브랜드 V1 API Request/Response 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-002: 양방향 변환 지원.
 *
 * <p>API-MAP-003: Application Result → API Response 변환.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * <p>레거시 BrandController.fetchBrand / fetchBrands 흐름 변환.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class BrandV1ApiMapper {

    /** V1 API 기본 검색 필드. 브랜드명 검색. 향후 필드 추가 시 코드 변경 최소화. */
    private static final String DEFAULT_SEARCH_FIELD = "brandName";

    /**
     * SearchBrandsV1ApiRequest → BrandDisplaySearchParams 변환.
     *
     * <p>searchField는 brandName으로 디폴트 설정 (향후 필드 추가 시 Request에 searchField 추가).
     *
     * <p>Public API는 displayed=true(노출 대상) 기본 적용.
     *
     * @param request 검색 요청 DTO
     * @return BrandDisplaySearchParams
     */
    public BrandDisplaySearchParams toSearchParams(SearchBrandsV1ApiRequest request) {
        String searchWord =
                request.searchWord() != null && !request.searchWord().isBlank()
                        ? request.searchWord().trim()
                        : null;
        return BrandDisplaySearchParams.of(DEFAULT_SEARCH_FIELD, searchWord, true);
    }

    /**
     * BrandDisplayResult 목록 → BrandV1ApiResponse 목록 변환.
     *
     * @param results BrandDisplayResult 목록
     * @return BrandV1ApiResponse 목록
     */
    public List<BrandV1ApiResponse> toListResponse(List<BrandDisplayResult> results) {
        return results.stream().map(this::toResponse).toList();
    }

    /**
     * BrandDisplayResult → BrandV1ApiResponse 변환.
     *
     * @param result BrandDisplayResult
     * @return BrandV1ApiResponse
     */
    public BrandV1ApiResponse toResponse(BrandDisplayResult result) {
        return new BrandV1ApiResponse(
                result.brandId(), result.brandName(), result.brandNameKo(), result.brandIconUrl());
    }

    /**
     * BrandResult → BrandV1ApiResponse 변환.
     *
     * <p>단건 조회(GetBrandById) 응답용.
     *
     * @param result BrandResult
     * @return BrandV1ApiResponse
     */
    public BrandV1ApiResponse toResponse(BrandResult result) {
        return new BrandV1ApiResponse(
                result.brandId(), result.brandName(), result.brandNameKo(), result.brandIconUrl());
    }
}
