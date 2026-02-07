package com.ryuqq.setof.adapter.in.rest.v1.news.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * BoardV1ApiResponse - 게시판 응답 DTO.
 *
 * <p>레거시 BoardDto 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param title 게시글 제목
 * @param contents 게시글 내용
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.news.dto.board.BoardDto
 */
@Schema(description = "게시판 응답")
public record BoardV1ApiResponse(
        @Schema(description = "게시글 제목", example = "공지사항 제목") String title,
        @Schema(description = "게시글 내용", example = "공지사항 내용...") String contents) {}
