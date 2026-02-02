package com.ryuqq.setof.adapter.in.rest.admin.v1.brand.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.query.BrandSearchV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.response.ExtendedBrandV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.application.brand.dto.query.BrandSearchParams;
import com.ryuqq.setof.application.brand.dto.response.BrandPageResult;
import com.ryuqq.setof.application.brand.dto.response.BrandResult;
import com.ryuqq.setof.application.common.dto.query.CommonSearchParams;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * BrandAdminV1ApiMapper - V1 Admin 브랜드 API 매퍼.
 *
 * <p>Application Layer의 결과를 V1 Admin API 응답으로 변환합니다.
 *
 * <p>레거시 ExtendedBrandContext와 동일한 구조로 변환합니다.
 *
 * <p>API-MAP-001: Mapper는 @Component로 정의.
 *
 * <p>API-MAP-002: 순수 변환 로직만 포함 (비즈니스 로직 금지).
 *
 * <p>API-MAP-003: null-safe 변환 필수.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class BrandAdminV1ApiMapper {

    private static final Integer DEFAULT_PAGE = 0;
    private static final Integer DEFAULT_SIZE = 20;
    private static final String DEFAULT_MAIN_DISPLAY_TYPE = "ENGLISH";

    /**
     * BrandSearchV1ApiRequest를 BrandSearchParams로 변환.
     *
     * <p>기본값 처리를 Mapper에서 수행합니다.
     *
     * @param request V1 검색 요청
     * @return UseCase 검색 파라미터
     */
    public BrandSearchParams toSearchParams(BrandSearchV1ApiRequest request) {
        Integer page = request.page() != null ? request.page() : DEFAULT_PAGE;
        Integer size = request.size() != null ? request.size() : DEFAULT_SIZE;

        CommonSearchParams commonParams =
                CommonSearchParams.of(false, null, null, null, null, page, size);

        return BrandSearchParams.of(request.brandName(), commonParams);
    }

    /**
     * BrandPageResult를 V1 CustomPageable 호환 페이지 응답으로 변환.
     *
     * <p>레거시 CustomPageable과 동일한 JSON 구조를 반환합니다.
     *
     * @param pageResult UseCase 실행 결과
     * @return V1 호환 페이지 응답 (레거시 CustomPageable 구조)
     */
    public CustomPageableV1ApiResponse<ExtendedBrandV1ApiResponse> toPageResponse(
            BrandPageResult pageResult) {
        List<ExtendedBrandV1ApiResponse> content =
                pageResult.content().stream().map(this::toResponse).toList();
        return CustomPageableV1ApiResponse.of(
                content, pageResult.page(), pageResult.size(), pageResult.totalCount());
    }

    /**
     * BrandResult를 V1 확장 브랜드 응답으로 변환.
     *
     * <p>레거시 ExtendedBrandContext와 동일한 필드로 변환합니다.
     *
     * @param brand 브랜드 조회 결과
     * @return V1 호환 확장 브랜드 응답
     */
    private ExtendedBrandV1ApiResponse toResponse(BrandResult brand) {
        return new ExtendedBrandV1ApiResponse(
                brand.brandId(),
                brand.brandName(),
                DEFAULT_MAIN_DISPLAY_TYPE,
                brand.brandName(),
                brand.brandNameKo());
    }
}
