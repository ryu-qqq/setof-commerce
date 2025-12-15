package com.ryuqq.setof.adapter.in.rest.admin.v1.brand.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.response.ExtendedBrandContextV1ApiResponse;
import com.ryuqq.setof.application.brand.dto.response.BrandSummaryResponse;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Brand V2 → Admin V1 API 응답 변환 매퍼
 *
 * <p>V2 응답(BrandSummaryResponse)을 Admin V1 응답(ExtendedBrandContextV1ApiResponse)으로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BrandAdminV1ApiMapper {

    /**
     * V2 BrandSummaryResponse를 Admin V1 응답으로 변환
     *
     * @param response V2 브랜드 요약 응답
     * @return Admin V1 브랜드 응답
     */
    public ExtendedBrandContextV1ApiResponse toV1Response(BrandSummaryResponse response) {
        return new ExtendedBrandContextV1ApiResponse(
                response.id(), response.code(), "KOREAN", response.code(), response.nameKo());
    }

    /**
     * V2 BrandSummaryResponse 목록을 Admin V1 응답 목록으로 변환
     *
     * @param responses V2 브랜드 요약 응답 목록
     * @return Admin V1 브랜드 응답 목록
     */
    public List<ExtendedBrandContextV1ApiResponse> toV1ResponseList(
            List<BrandSummaryResponse> responses) {
        return responses.stream().map(this::toV1Response).toList();
    }
}
