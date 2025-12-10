package com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * V1 QNA 답변 생성 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "QNA 답변 생성 응답")
public record CreateQnaAnswerV1ApiResponse(
        @Schema(description = "QNA ID", example = "1") Long qnaId,
        @Schema(description = "QNA 답변 ID", example = "1") Long qnaAnswerId,
        @Schema(description = "QNA 타입", example = "PRODUCT") String qnaType,
        @Schema(description = "QNA 상태", example = "ANSWERED") String qnaStatus,
        @Schema(description = "QNA 이미지 목록") List<QnaImageV1ApiResponse> qnaImages) {

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
