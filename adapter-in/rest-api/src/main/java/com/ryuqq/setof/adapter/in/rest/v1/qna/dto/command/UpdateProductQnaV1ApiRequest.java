package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "상품 QnA 수정 요청")
public record UpdateProductQnaV1ApiRequest(
        @Schema(description = "Q&A 내용") QnaContentsV1ApiRequest qnaContents)
        implements UpdateQnaV1ApiRequest {

    @Schema(description = "Q&A 내용")
    public record QnaContentsV1ApiRequest(
            @Schema(description = "제목", example = "상품 문의 수정 제목") @Size(max = 100) String title,
            @Schema(description = "내용",
                    example = "상품 정보 수정 문의 내용입니다.") @Size(max = 500) String content) {
    }
}
