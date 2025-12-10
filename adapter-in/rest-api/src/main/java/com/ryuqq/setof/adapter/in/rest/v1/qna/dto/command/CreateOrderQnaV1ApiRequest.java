package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.command;

import java.util.List;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 QnA 생성 요청")
public record CreateOrderQnaV1ApiRequest(
        @Schema(description = "Q&A 내용") QnaContentsV1ApiRequest qnaContents,
        @Schema(description = "비공개 여부 (Y/N)", example = "N") String privateYn,
        @Schema(description = "Q&A 타입", example = "ORDER_QNA") String qnaType,
        @Schema(description = "Q&A 상세 타입", example = "DELIVERY") String qnaDetailType,
        @Schema(description = "판매자 ID", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull Long sellerId,
        @Schema(description = "주문 ID (1 이상)", example = "12345",
                minimum = "1") @Min(1) long orderId,
        @Schema(description = "Q&A 이미지 목록 (최대 3장)") @Size(
                max = 3) List<CreateQnaImageV1ApiRequest> qnaImages)
        implements CreateQnaV1ApiRequest {

    @Schema(description = "Q&A 내용")
    public record QnaContentsV1ApiRequest(
            @Schema(description = "제목", example = "배송 문의") @Size(max = 100) String title,
            @Schema(description = "내용",
                    example = "배송 일정이 궁금합니다.") @Size(max = 500) String content) {
    }

    @Schema(description = "Q&A 이미지 생성 정보")
    public record CreateQnaImageV1ApiRequest(
            @Schema(description = "이미지 URL") String imageUrl,
            @Schema(description = "노출 순서", example = "1") @NotNull Integer displayOrder) {
    }
}
