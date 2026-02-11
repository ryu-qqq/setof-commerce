package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * SearchProductQnasV1ApiRequest - 상품 Q&A 목록 검색 요청 DTO (Offset 페이징).
 *
 * <p>레거시 QnaController.fetchProductQnas 기반 변환.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-010: Request DTO 조회 네이밍 규칙 (Search*ApiRequest - Offset 페이징).
 *
 * <p>기본값 처리는 Mapper에서 수행합니다. Request DTO에서는 기본값 설정 금지.
 *
 * @param page 페이지 번호 (0부터 시작)
 * @param size 페이지 크기 (1~100)
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.qna.controller.QnaController#fetchProductQnas
 */
@Schema(description = "상품 Q&A 목록 검색 요청")
public record SearchProductQnasV1ApiRequest(
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
                @Schema(description = "페이지 번호", defaultValue = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
                Integer page,
        @Parameter(description = "페이지 크기 (1~100)", example = "10")
                @Schema(description = "페이지 크기", defaultValue = "10")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
                Integer size) {}
