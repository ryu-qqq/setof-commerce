package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 QnA 생성 요청")
public record CreateProductQnaV1ApiRequest(
        @Schema(description = "Q&A 내용") QnaContentsV1ApiRequest qnaContents,
        @Schema(description = "비공개 여부 (Y/N)", example = "N") String privateYn,
        @Schema(description = "Q&A 타입", example = "PRODUCT_QNA") String qnaType,
        @Schema(description = "Q&A 상세 타입", example = "PRODUCT_INFO") String qnaDetailType,
        @Schema(description = "판매자 ID", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull Long sellerId,
        @Schema(description = "상품 그룹 ID (1 이상)", example = "12345",
                minimum = "1") @Min(1) long productGroupId)
        implements CreateQnaV1ApiRequest {

    @Schema(description = "Q&A 내용")
    public record QnaContentsV1ApiRequest(
            @Schema(description = "제목", example = "상품 문의 제목") @Size(max = 100) String title,
            @Schema(description = "내용",
                    example = "상품 정보가 궁금합니다.") @Size(max = 500) String content) {
    }
}
