package com.ryuqq.setof.adapter.in.rest.admin.v2.board.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

/**
 * Board 수정 요청 DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "게시글 수정 요청")
public record UpdateBoardV2ApiRequest(
        @Schema(
                        description = "제목",
                        example = "수정된 공지사항 제목",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "제목은 필수입니다")
                String title,
        @Schema(
                        description = "내용",
                        example = "수정된 공지사항 내용입니다.",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "내용은 필수입니다")
                String content,
        @Schema(description = "요약", example = "수정된 공지사항 요약") String summary,
        @Schema(description = "썸네일 URL", example = "https://example.com/thumbnail-new.jpg")
                String thumbnailUrl,
        @Schema(description = "노출 시작일시", example = "2024-01-01T00:00:00Z") Instant displayStartAt,
        @Schema(description = "노출 종료일시", example = "2024-12-31T23:59:59Z") Instant displayEndAt) {}
