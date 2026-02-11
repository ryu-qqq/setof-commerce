package com.ryuqq.setof.adapter.in.rest.v1.board.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * BoardV1ApiResponse - 공지사항 응답 DTO.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 → Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param boardId 공지사항 ID
 * @param title 공지사항 제목
 * @param contents 공지사항 내용
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "공지사항 응답")
public record BoardV1ApiResponse(
        @Schema(description = "공지사항 ID", example = "1") Long boardId,
        @Schema(description = "공지사항 제목", example = "공지사항 제목입니다") String title,
        @Schema(description = "공지사항 내용", example = "공지사항 내용입니다") String contents) {}
