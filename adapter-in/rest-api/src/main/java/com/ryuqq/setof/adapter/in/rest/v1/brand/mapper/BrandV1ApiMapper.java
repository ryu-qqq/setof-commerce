package com.ryuqq.setof.adapter.in.rest.v1.brand.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.query.BrandSearchV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.response.BrandDisplayV1ApiResponse;
import com.ryuqq.setof.application.brand.dto.query.BrandDisplaySearchParams;
import com.ryuqq.setof.application.brand.dto.response.BrandDisplayResult;
import com.ryuqq.setof.application.brand.dto.response.BrandResult;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * BrandV1ApiMapper - V1 Public 브랜드 API 매퍼.
 *
 * <p>Application Layer의 결과를 V1 Public API 응답으로 변환합니다.
 *
 * <p>레거시 BrandDisplayDto와 동일한 구조로 변환합니다.
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
public class BrandV1ApiMapper {

    /**
     * BrandSearchV1ApiRequest를 BrandDisplaySearchParams로 변환.
     *
     * @param request V1 검색 요청
     * @return UseCase 검색 파라미터
     */
    public BrandDisplaySearchParams toSearchParams(BrandSearchV1ApiRequest request) {
        return BrandDisplaySearchParams.of(request.brandName(), request.displayed());
    }

    /**
     * BrandDisplayResult 목록을 V1 브랜드 응답 목록으로 변환.
     *
     * <p>레거시 BrandDisplayDto와 동일한 필드로 변환합니다.
     *
     * @param results UseCase 실행 결과
     * @return V1 호환 브랜드 목록
     */
    public List<BrandDisplayV1ApiResponse> toListResponse(List<BrandDisplayResult> results) {
        return results.stream().map(this::toResponse).toList();
    }

    /**
     * BrandDisplayResult를 V1 브랜드 응답으로 변환.
     *
     * <p>레거시 BrandDisplayDto와 동일한 필드로 변환합니다.
     *
     * @param brand 브랜드 조회 결과
     * @return V1 호환 브랜드 응답
     */
    private BrandDisplayV1ApiResponse toResponse(BrandDisplayResult brand) {
        return new BrandDisplayV1ApiResponse(
                brand.brandId(), brand.brandName(), brand.brandNameKo(), brand.brandIconUrl());
    }

    /**
     * BrandResult를 V1 브랜드 응답으로 변환.
     *
     * <p>레거시 BrandDisplayDto와 동일한 필드로 변환합니다.
     *
     * @param brand 브랜드 조회 결과
     * @return V1 호환 브랜드 응답
     */
    public BrandDisplayV1ApiResponse toResponse(BrandResult brand) {
        return new BrandDisplayV1ApiResponse(
                brand.brandId(), brand.brandName(), brand.brandNameKo(), brand.brandIconUrl());
    }
}
