package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * SearchMyQnasCursorV1ApiRequest - 내 Q&A 목록 검색 요청 DTO (커서 페이징).
 *
 * <p>레거시 QnaController.fetchMyQnas + QnaFilter 기반 변환.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-010: Request DTO 조회 네이밍 규칙 (Search*CursorApiRequest - 커서 페이징).
 *
 * <p>기본값 처리는 Mapper에서 수행합니다. Request DTO에서는 기본값 설정 금지.
 *
 * @param qnaType Q&A 유형 (PRODUCT: 상품문의, ORDER: 주문문의)
 * @param startDate 검색 시작일
 * @param endDate 검색 종료일
 * @param lastDomainId 마지막으로 조회한 Q&A ID (커서 페이징용, 레거시 파라미터명 호환)
 * @param size 조회할 아이템 수 (1~100)
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.qna.controller.QnaController#fetchMyQnas
 * @see com.setof.connectly.module.qna.dto.filter.QnaFilter
 */
@Schema(description = "내 Q&A 목록 검색 요청 (커서 페이징)")
public record SearchMyQnasCursorV1ApiRequest(
        @Parameter(
                        description = "Q&A 유형",
                        example = "PRODUCT",
                        required = true,
                        schema = @Schema(allowableValues = {"PRODUCT", "ORDER"}))
                @Schema(description = "Q&A 유형 (PRODUCT: 상품문의, ORDER: 주문문의)")
                @NotNull(message = "Q&A 유형은 필수입니다.")
                String qnaType,
        @Parameter(
                        description = "검색 시작일 (yyyy-MM-dd HH:mm:ss)",
                        example = "2024-01-01 00:00:00",
                        required = true)
                @Schema(description = "검색 시작일")
                @NotNull(message = "검색 시작일은 필수입니다.")
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime startDate,
        @Parameter(
                        description = "검색 종료일 (yyyy-MM-dd HH:mm:ss)",
                        example = "2024-12-31 23:59:59",
                        required = true)
                @Schema(description = "검색 종료일")
                @NotNull(message = "검색 종료일은 필수입니다.")
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime endDate,
        @Parameter(description = "마지막으로 조회한 Q&A ID (다음 페이지 조회 시 사용)", example = "1000")
                @Schema(description = "커서: 마지막 Q&A ID", nullable = true)
                Long lastDomainId,
        @Parameter(description = "조회할 아이템 수 (1~100)", example = "10")
                @Schema(description = "페이지 크기", defaultValue = "10")
                @Min(value = 1, message = "조회 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "조회 크기는 100 이하여야 합니다.")
                Integer size) {}
