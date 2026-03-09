package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * UpdateQnaReplyV1ApiRequest - Q&A 답변 수정 요청 DTO.
 *
 * <p>레거시 CreateQna(인터페이스) 기반. PUT /api/v1/qna/{qnaId}/reply/{qnaAnswerId} 에 대응.
 *
 * <p>레거시 @JsonTypeInfo 다형성 역직렬화 형식을 그대로 수용합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "Q&A 답변 수정 요청")
public record UpdateQnaReplyV1ApiRequest(
        @Schema(description = "다형성 타입 (productQna | orderQna)", example = "orderQna") String type,
        @Schema(
                        description = "Q&A 유형",
                        example = "ORDER",
                        allowableValues = {"PRODUCT", "ORDER"})
                String qnaType,
        @Schema(
                        description = "Q&A 상세 유형",
                        example = "ORDER_PAYMENT",
                        allowableValues = {
                            "SIZE",
                            "SHIPMENT",
                            "RESTOCK",
                            "ORDER_PAYMENT",
                            "CANCEL",
                            "EXCHANGE",
                            "AS",
                            "REFUND",
                            "ETC"
                        })
                String qnaDetailType,
        @Schema(description = "Q&A 제목/내용") @Valid QnaContentsRequest qnaContents,
        @Schema(description = "비밀글 여부 (Y: 비밀글, N: 공개)", example = "N") String privateYn,
        @Schema(description = "셀러 ID", example = "1") @NotNull(message = "셀러 ID는 필수입니다.")
                Long sellerId,
        @Schema(description = "상품그룹 ID (PRODUCT 타입일 때)", example = "123") Long productGroupId,
        @Schema(description = "주문 ID (ORDER 타입일 때)", example = "456") Long orderId,
        @Schema(description = "첨부 이미지 목록 (ORDER 타입 최대 3장, diff 처리)")
                @Size(max = 3, message = "이미지는 최대 3장까지 첨부할 수 있습니다.")
                List<QnaImageRequest> qnaImages) {

    /**
     * QnaContentsRequest - Q&A 제목/내용 중첩 DTO.
     *
     * @param title 제목 (최대 100자)
     * @param content 내용 (최대 500자)
     */
    @Schema(description = "Q&A 제목/내용")
    public record QnaContentsRequest(
            @Schema(description = "답변 제목 (최대 100자)", example = "답변 제목을 수정합니다")
                    @Size(max = 100, message = "제목은 최대 100자까지 입력할 수 있습니다.")
                    String title,
            @Schema(description = "답변 내용 (최대 500자)", example = "답변 내용을 수정합니다.")
                    @Size(max = 500, message = "내용은 최대 500자까지 입력할 수 있습니다.")
                    String content) {}

    /** 제목 추출 (qnaContents.title). */
    public String title() {
        return qnaContents != null ? qnaContents.title() : null;
    }

    /** 내용 추출 (qnaContents.content). */
    public String content() {
        return qnaContents != null ? qnaContents.content() : null;
    }

    /** privateYn → boolean 변환. */
    public boolean isSecret() {
        return "Y".equals(privateYn);
    }

    /** 이미지 URL 목록 (null-safe). */
    public List<String> imageUrls() {
        if (qnaImages == null) {
            return List.of();
        }
        return qnaImages.stream()
                .map(QnaImageRequest::imageUrl)
                .filter(url -> url != null && !url.isBlank())
                .toList();
    }
}
