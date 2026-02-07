package com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * SearchQnasV1ApiRequest - QnA 목록 검색 요청 DTO.
 *
 * <p>레거시 QnaFilter 기반 변환.
 *
 * <p>GET /api/v1/qnas - QnA 목록 조회
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>QnaType enum → String 타입
 *   <li>QnaStatus enum → String 타입
 *   <li>QnaDetailType enum → String 타입
 *   <li>Yn enum → String 타입
 *   <li>SearchKeyword enum → String 타입
 *   <li>Pageable → record 내부 page, size 필드
 *   <li>@Parameter 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.qna.dto.filter.QnaFilter
 */
@Schema(description = "QnA 목록 검색 요청")
public record SearchQnasV1ApiRequest(
        @NotNull(message = "qnaType은 필수입니다.")
                @Parameter(
                        description = "QnA 유형 (필수)",
                        required = true,
                        example = "PRODUCT",
                        schema = @Schema(allowableValues = {"PRODUCT", "ORDER"}))
                String qnaType,
        @Parameter(
                        description = "QnA 상태",
                        example = "OPEN",
                        schema = @Schema(allowableValues = {"OPEN", "CLOSED"}))
                String qnaStatus,
        @Parameter(
                        description = "QnA 상세 유형",
                        example = "SIZE",
                        schema =
                                @Schema(
                                        allowableValues = {
                                            "SIZE",
                                            "SHIPMENT",
                                            "RESTOCK",
                                            "ORDER_PAYMENT",
                                            "CANCEL",
                                            "EXCHANGE",
                                            "AS",
                                            "REFUND",
                                            "ETC"
                                        }))
                String qnaDetailType,
        @Parameter(
                        description = "비공개 여부",
                        example = "N",
                        schema = @Schema(allowableValues = {"Y", "N"}))
                String privateYn,
        @Parameter(description = "검색 시작일시", example = "2026-01-01T00:00:00")
                LocalDateTime startDate,
        @Parameter(description = "검색 종료일시", example = "2026-01-31T23:59:59") LocalDateTime endDate,
        @Parameter(
                        description = "검색 키워드 타입",
                        example = "USER_NAME",
                        schema = @Schema(allowableValues = {"USER_NAME", "SELLER_NAME", "CONTENT"}))
                String searchKeyword,
        @Parameter(description = "검색어", example = "홍길동") String searchWord,
        @Parameter(description = "마지막 조회 QnA ID (커서 페이징용)", example = "12345") Long lastDomainId,
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
                Integer page,
        @Parameter(description = "페이지 크기 (1~100)", example = "20")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
                Integer size) {}
