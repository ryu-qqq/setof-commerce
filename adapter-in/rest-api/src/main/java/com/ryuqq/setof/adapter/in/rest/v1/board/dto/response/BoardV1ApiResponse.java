package com.ryuqq.setof.adapter.in.rest.v1.board.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Board Response
 *
 * <p>Board 정보를 반환하는 응답 DTO입니다.
 *
 * @param title Board ID
 * @param contents Board 내용
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "Board 응답")
public record BoardV1ApiResponse(
        @Schema(description = "Board 제목", example = "공지사항") Long title,
        @Schema(description = "Board 내용", example = "안내 드립니다.") String contents) {}
