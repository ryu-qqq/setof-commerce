package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@Schema(description = "주문 QnA 수정 요청")
public record UpdateOrderQnaV1ApiRequest(
        @Schema(description = "Q&A 내용") QnaContentsV1ApiRequest qnaContents,
        @Schema(description = "Q&A 이미지 목록 (최대 3장)") @Size(
                max = 3) List<UpdateQnaImageV1ApiRequest> qnaImages)
        implements UpdateQnaV1ApiRequest {

    @Schema(description = "Q&A 내용")
    public record QnaContentsV1ApiRequest(
            @Schema(description = "제목", example = "배송 문의 수정") @Size(max = 100) String title,
            @Schema(description = "내용",
                    example = "배송 일정 수정 문의 내용입니다.") @Size(max = 500) String content) {
    }

    @Schema(description = "Q&A 이미지 수정 정보")
    public record UpdateQnaImageV1ApiRequest(
            @JsonInclude(JsonInclude.Include.NON_NULL) @Schema(description = "QnA 이미지 ID",
                    example = "11") Long qnaImageId,
            @JsonInclude(JsonInclude.Include.NON_NULL) @Schema(description = "QnA ID",
                    example = "1") Long qnaId,
            @JsonInclude(JsonInclude.Include.NON_NULL) @Schema(description = "QnA 답변 ID",
                    example = "10") Long qnaAnswerId,
            @Schema(description = "이미지 URL") String imageUrl,
            @Schema(description = "노출 순서", example = "1") @NotNull Integer displayOrder) {
    }
}
