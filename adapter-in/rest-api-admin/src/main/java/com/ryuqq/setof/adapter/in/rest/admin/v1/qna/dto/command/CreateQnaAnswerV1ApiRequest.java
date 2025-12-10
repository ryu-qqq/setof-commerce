package com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * V1 QNA 답변 생성 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "QNA 답변 생성 요청")
public record CreateQnaAnswerV1ApiRequest(
        @Schema(description = "QNA ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "QNA ID는 필수입니다.")
                Long qnaId,
        @Schema(description = "QNA 내용", requiredMode = Schema.RequiredMode.REQUIRED)
                @Valid
                @NotNull(message = "QNA 내용은 필수입니다.")
                CreateQnaContentsV1ApiRequest qnaContents,
        @Schema(description = "QNA 이미지 목록")
                @Size(max = 3, message = "질문 답변에 등록할 수 있는 사진은 최대 3장입니다.")
                List<CreateQnaImageV1ApiRequest> qnaImages) {

    @Schema(description = "QNA 내용 생성 요청")
    public record CreateQnaContentsV1ApiRequest(
            @Schema(
                            description = "제목",
                            example = "답변 제목",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @jakarta.validation.constraints.NotBlank(message = "질문/답변 제목은 비워둘 수 없습니다.")
                    @org.hibernate.validator.constraints.Length(
                            min = 1,
                            max = 100,
                            message = "질문/답변 제목은 최소 1자 최대 100자를 넘길 수 없습니다.")
                    String title,
            @Schema(
                            description = "내용",
                            example = "답변 내용",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @jakarta.validation.constraints.NotBlank(message = "질문/답변 내용은 비워둘 수 없습니다.")
                    @org.hibernate.validator.constraints.Length(
                            min = 1,
                            max = 500,
                            message = "질문/답변 내용은 최소 1자 최대 500자를 넘길 수 없습니다.")
                    String content) {}

    @Schema(description = "QNA 이미지 생성 요청")
    public record CreateQnaImageV1ApiRequest(
            @Schema(description = "QNA 이미지 ID", example = "1") Long qnaImageId,
            @Schema(description = "QNA ID", example = "1") Long qnaId,
            @Schema(description = "QNA 답변 ID", example = "1") Long qnaAnswerId,
            @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
                    String imageUrl,
            @Schema(
                            description = "표시 순서",
                            example = "1",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotNull(message = "표시 순서는 필수입니다.")
                    Integer displayOrder) {}
}
