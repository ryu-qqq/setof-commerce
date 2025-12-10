package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(description = "주문 QnA 생성 요청")
public record CreateOrderQnaReplyV1ApiRequest(
        @Schema(description = "Q&A 내용") QnaContentsV1ApiRequest qnaContents,
        @Schema(description = "Q&A 이미지 목록 (최대 3장)") @Size(max = 3)
                List<CreateQnaImageV1ApiRequest> qnaImages)
        implements CreateQnaReplyV1ApiRequest {

    @Schema(description = "Q&A 내용")
    public record QnaContentsV1ApiRequest(
            @Schema(description = "제목", example = "배송 문의") @Size(max = 100) String title,
            @Schema(description = "내용", example = "배송 일정이 궁금합니다.") @Size(max = 500)
                    String content) {}

    @Schema(description = "Q&A 이미지 생성 정보")
    public record CreateQnaImageV1ApiRequest(
            @Schema(description = "이미지 URL") String imageUrl,
            @Schema(description = "노출 순서", example = "1") @NotNull Integer displayOrder) {}
}
