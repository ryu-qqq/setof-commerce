package com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * V1 QNA 답변 수정 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "QNA 답변 수정 요청")
public record UpdateQnaAnswerV1ApiRequest(
        @Schema(description = "QNA ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "QNA ID는 필수입니다.")
                Long qnaId,
        @Schema(
                        description = "QNA 답변 ID",
                        example = "1",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "QNA 답변 ID는 필수입니다.")
                Long qnaAnswerId,
        @Schema(description = "QNA 내용", requiredMode = Schema.RequiredMode.REQUIRED)
                CreateQnaAnswerV1ApiRequest.CreateQnaContentsV1ApiRequest qnaContents,
        @Schema(description = "QNA 이미지 목록")
                java.util.List<CreateQnaAnswerV1ApiRequest.CreateQnaImageV1ApiRequest> qnaImages) {}
