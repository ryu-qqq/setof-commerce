package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * QnaPageV1ApiResponse - Q&A 페이지 응답 DTO (Offset 페이징).
 *
 * <p>상품 Q&A 목록 조회 시 사용됩니다. (Page 기반)
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * @param content Q&A 목록
 * @param page 현재 페이지 번호
 * @param size 페이지 크기
 * @param totalElements 전체 요소 수
 * @param totalPages 전체 페이지 수
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "Q&A 페이지 응답 (Offset 페이징)")
public record QnaPageV1ApiResponse(

        @Schema(description = "Q&A 목록")
        List<QnaV1ApiResponse> content,

        @Schema(description = "현재 페이지 번호", example = "0")
        int page,

        @Schema(description = "페이지 크기", example = "10")
        int size,

        @Schema(description = "전체 요소 수", example = "50")
        long totalElements,

        @Schema(description = "전체 페이지 수", example = "5")
        int totalPages

) {}
