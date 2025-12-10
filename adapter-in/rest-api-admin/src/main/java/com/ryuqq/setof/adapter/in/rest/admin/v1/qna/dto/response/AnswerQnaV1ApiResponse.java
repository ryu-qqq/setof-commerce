package com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

/**
 * V1 답변 QNA Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "답변 QNA 응답")
public record AnswerQnaV1ApiResponse(
        @Schema(description = "QNA 답변 ID", example = "1") Long qnaAnswerId,
        @Schema(description = "QNA 답변 부모 ID", example = "0") Long qnaAnswerParentId,
        @Schema(description = "QNA 작성자 타입", example = "ADMIN") String qnaWriterType,
        @Schema(description = "QNA 내용") QnaContentsV1ApiResponse qnaContents,
        @Schema(description = "QNA 이미지 목록") List<QnaImageV1ApiResponse> qnaImages,
        @Schema(description = "등록자", example = "admin") String insertOperator,
        @Schema(description = "수정자", example = "admin") String updateOperator,
        @Schema(description = "등록 일시", example = "2024-01-01 00:00:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime insertDate,
        @Schema(description = "수정 일시", example = "2024-01-01 00:00:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime updateDate) {

    @Schema(description = "QNA 내용")
    public record QnaContentsV1ApiResponse(
            @Schema(description = "제목", example = "답변 제목") String title,
            @Schema(description = "내용", example = "답변 내용") String content) {}

    @Schema(description = "QNA 이미지")
    public record QnaImageV1ApiResponse(
            @Schema(description = "QNA 이슈 타입", example = "ANSWER") String qnaIssueType,
            @Schema(description = "QNA 이미지 ID", example = "1") Long qnaImageId,
            @Schema(description = "QNA ID", example = "1") Long qnaId,
            @Schema(description = "QNA 답변 ID", example = "1") Long qnaAnswerId,
            @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
                    String imageUrl,
            @Schema(description = "표시 순서", example = "1") Integer displayOrder) {}
}
