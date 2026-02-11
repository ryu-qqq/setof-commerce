package com.ryuqq.setof.adapter.in.rest.v1.faq.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.faq.dto.request.SearchFaqsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.faq.dto.response.FaqV1ApiResponse;
import com.ryuqq.setof.application.faq.dto.query.FaqSearchParams;
import com.ryuqq.setof.application.faq.dto.response.FaqResult;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * FaqV1ApiMapper - FAQ V1 API Request/Response 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-002: 양방향 변환 지원.
 *
 * <p>API-MAP-003: Application Result → API Response 변환.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class FaqV1ApiMapper {

    /**
     * SearchFaqsV1ApiRequest → FaqSearchParams 변환.
     *
     * @param request FAQ 검색 요청 DTO
     * @return FaqSearchParams
     */
    public FaqSearchParams toSearchParams(SearchFaqsV1ApiRequest request) {
        return FaqSearchParams.of(request.faqType());
    }

    /**
     * FaqResult 목록 → FaqV1ApiResponse 목록 변환.
     *
     * @param results FaqResult 목록
     * @return FaqV1ApiResponse 목록
     */
    public List<FaqV1ApiResponse> toListResponse(List<FaqResult> results) {
        return results.stream().map(this::toResponse).toList();
    }

    /**
     * FaqResult → FaqV1ApiResponse 변환.
     *
     * <p>FaqType → String 변환하여 Response DTO의 Domain 의존 제거.
     *
     * @param result FaqResult
     * @return FaqV1ApiResponse
     */
    public FaqV1ApiResponse toResponse(FaqResult result) {
        String faqTypeName = result.faqType() != null ? result.faqType().name() : null;
        return new FaqV1ApiResponse(result.faqId(), faqTypeName, result.title(), result.contents());
    }
}
