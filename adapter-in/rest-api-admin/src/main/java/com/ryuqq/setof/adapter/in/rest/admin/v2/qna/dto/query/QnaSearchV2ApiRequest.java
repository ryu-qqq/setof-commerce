package com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * QnA Search V2 API Request
 *
 * <p>문의 목록 조회 API 요청 DTO
 *
 * @param qnaType 문의 유형 (PRODUCT, ORDER, nullable)
 * @param targetId 대상 ID (상품 그룹 ID 또는 주문 ID, nullable)
 * @param status 상태 (OPEN, CLOSED, nullable)
 * @param writerName 작성자명 (LIKE 검색, nullable)
 * @param sortBy 정렬 기준 (ID, CREATED_AT, UPDATED_AT)
 * @param sortDirection 정렬 방향 (ASC, DESC)
 * @param page 페이지 번호 (0부터 시작)
 * @param size 페이지 크기
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "문의 목록 조회 요청")
public record QnaSearchV2ApiRequest(
        @Schema(description = "문의 유형 (PRODUCT, ORDER)", example = "PRODUCT")
        String qnaType,

        @Schema(description = "대상 ID (상품 그룹 ID 또는 주문 ID)", example = "1001")
        Long targetId,

        @Schema(description = "상태 (OPEN, CLOSED)", example = "OPEN")
        String status,

        @Schema(description = "작성자명 (LIKE 검색)", example = "홍길동")
        String writerName,

        @Schema(description = "정렬 기준 (ID, CREATED_AT, UPDATED_AT)", example = "CREATED_AT")
        String sortBy,

        @Schema(description = "정렬 방향 (ASC, DESC)", example = "DESC")
        String sortDirection,

        @Schema(description = "페이지 번호 (0부터 시작)", example = "0")
        @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
        int page,

        @Schema(description = "페이지 크기", example = "20")
        @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
        @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
        int size) {

    public QnaSearchV2ApiRequest {
        if (size == 0) {
            size = 20;
        }
    }
}
