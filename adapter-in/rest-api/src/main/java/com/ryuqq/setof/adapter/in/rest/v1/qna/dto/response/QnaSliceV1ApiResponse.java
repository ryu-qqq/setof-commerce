package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * QnaSliceV1ApiResponse - Q&A 슬라이스 응답 DTO (커서 페이징).
 *
 * <p>내 Q&A 목록 조회 시 사용됩니다. (Slice 기반, 무한 스크롤)
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * @param content Q&A 목록
 * @param size 페이지 크기
 * @param hasNext 다음 페이지 존재 여부
 * @param lastQnaId 마지막 Q&A ID (다음 페이지 조회 시 커서로 사용)
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "Q&A 슬라이스 응답 (커서 페이징)")
public record QnaSliceV1ApiResponse(

        @Schema(description = "Q&A 목록")
        List<QnaV1ApiResponse> content,

        @Schema(description = "페이지 크기", example = "10")
        int size,

        @Schema(description = "다음 페이지 존재 여부", example = "true")
        boolean hasNext,

        @Schema(description = "마지막 Q&A ID (다음 페이지 조회 시 lastQnaId로 사용)", example = "991", nullable = true)
        Long lastQnaId

) {}
