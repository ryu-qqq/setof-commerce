package com.ryuqq.setof.adapter.in.rest.v1.faq.controller;

import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.faq.FaqV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.faq.dto.request.SearchFaqsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.faq.dto.response.FaqV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.faq.mapper.FaqV1ApiMapper;
import com.ryuqq.setof.application.faq.dto.query.FaqSearchParams;
import com.ryuqq.setof.application.faq.dto.response.FaqResult;
import com.ryuqq.setof.application.faq.port.in.GetFaqsByTypeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FaqQueryV1Controller - FAQ 조회 V1 Public API.
 *
 * <p>API-CTR-001: @RestController 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-004: ResponseEntity + V1ApiResponse 래핑.
 *
 * <p>API-CTR-005: Controller @Transactional 금지.
 *
 * <p>API-CTR-007: Controller 비즈니스 로직 금지.
 *
 * <p>레거시 NewsController.fetchFaq 흐름 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "FAQ 조회 V1", description = "FAQ 조회 V1 Public API")
@RestController
@RequestMapping(FaqV1Endpoints.FAQS)
public class FaqQueryV1Controller {

    private final GetFaqsByTypeUseCase getFaqsByTypeUseCase;
    private final FaqV1ApiMapper mapper;

    public FaqQueryV1Controller(GetFaqsByTypeUseCase getFaqsByTypeUseCase, FaqV1ApiMapper mapper) {
        this.getFaqsByTypeUseCase = getFaqsByTypeUseCase;
        this.mapper = mapper;
    }

    /**
     * FAQ 목록 조회 API.
     *
     * <p>GET /api/v1/faq - FAQ 유형별 목록 조회.
     *
     * @param request 검색 요청 (faqType 필수)
     * @return FAQ 목록
     */
    @Operation(summary = "FAQ 목록 조회", description = "FAQ 유형별 목록을 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "faqType 누락")
    })
    @GetMapping
    public ResponseEntity<V1ApiResponse<List<FaqV1ApiResponse>>> getFaqs(
            @ParameterObject @Valid SearchFaqsV1ApiRequest request) {
        FaqSearchParams params = mapper.toSearchParams(request);
        List<FaqResult> results = getFaqsByTypeUseCase.execute(params);
        List<FaqV1ApiResponse> response = mapper.toListResponse(results);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
