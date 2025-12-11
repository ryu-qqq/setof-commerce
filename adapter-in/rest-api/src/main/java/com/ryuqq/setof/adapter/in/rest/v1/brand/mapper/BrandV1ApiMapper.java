package com.ryuqq.setof.adapter.in.rest.v1.brand.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.response.BrandV1ApiResponse;
import com.ryuqq.setof.application.brand.dto.response.BrandResponse;
import com.ryuqq.setof.application.brand.dto.response.BrandSummaryResponse;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Brand V2 → V1 API 응답 변환 매퍼
 *
 * <p>V2 응답(BrandResponse, BrandSummaryResponse)을 V1 응답(BrandV1ApiResponse)으로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BrandV1ApiMapper {

    /**
     * V2 BrandResponse를 V1 응답으로 변환
     *
     * @param response V2 브랜드 응답
     * @return V1 브랜드 응답
     */
    public BrandV1ApiResponse toV1Response(BrandResponse response) {
        return new BrandV1ApiResponse(
                response.id(), response.code(), response.nameKo(), response.logoUrl());
    }

    /**
     * V2 BrandSummaryResponse를 V1 응답으로 변환
     *
     * @param response V2 브랜드 요약 응답
     * @return V1 브랜드 응답
     */
    public BrandV1ApiResponse toV1Response(BrandSummaryResponse response) {
        return new BrandV1ApiResponse(
                response.id(), response.code(), response.nameKo(), response.logoUrl());
    }

    /**
     * V2 BrandSummaryResponse 목록을 V1 응답 목록으로 변환
     *
     * @param responses V2 브랜드 요약 응답 목록
     * @return V1 브랜드 응답 목록
     */
    public List<BrandV1ApiResponse> toV1ResponseList(List<BrandSummaryResponse> responses) {
        return responses.stream().map(this::toV1Response).toList();
    }
}
