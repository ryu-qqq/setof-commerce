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

    /**
     * SearchBrandsV1ApiRequest → BrandDisplaySearchParams 변환.
     *
     * <p>레거시 호환: searchField=null (displayKoreanName OR displayEnglishName 검색), displayed=true (전시
     * 중 브랜드만).
     *
     * @param request 검색 요청 DTO
     * @return BrandDisplaySearchParams
     */
    public BrandDisplaySearchParams toSearchParams(SearchBrandsV1ApiRequest request) {
        String searchWord =
                request.searchWord() != null && !request.searchWord().isBlank()
                        ? request.searchWord().trim()
                        : null;
        return BrandDisplaySearchParams.of(null, searchWord, true);
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
