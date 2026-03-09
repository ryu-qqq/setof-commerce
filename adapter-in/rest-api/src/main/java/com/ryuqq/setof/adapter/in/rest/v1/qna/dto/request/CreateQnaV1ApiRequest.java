package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * CreateQnaV1ApiRequest - Q&A 질문 등록 요청 DTO.
 *
 * <p>레거시 CreateQna(인터페이스) + CreateProductQna / CreateOrderQna(구현체) 기반.
 *
 * <p>레거시 @JsonTypeInfo 다형성 역직렬화 형식을 그대로 수용합니다:
 *
 * <ul>
 *   <li>type: "productQna" | "orderQna" (다형성 discriminator, 서버에서는 무시)
 *   <li>qnaContents: { title, content } (중첩 구조)
 *   <li>privateYn: "Y" | "N" (문자열)
 *   <li>qnaImages: [{ imageUrl, displayOrder }] (CreateQnaImage 객체 목록, ORDER 전용 최대 3장)
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "Q&A 질문 등록 요청")
public record CreateQnaV1ApiRequest(
        @Schema(description = "다형성 타입 (productQna | orderQna)", example = "productQna") String type,
        @Schema(
                        description = "Q&A 유형",
                        example = "PRODUCT",
                        allowableValues = {"PRODUCT", "ORDER"})
                String qnaType,
        @Schema(
                        description = "Q&A 상세 유형",
                        example = "SIZE",
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
        @Schema(description = "상품그룹 ID (PRODUCT 타입일 때 필수)", example = "100")
                @Min(value = 1, message = "상품그룹 ID는 1 이상이어야 합니다.")
                Long productGroupId,
        @Schema(description = "주문 ID (ORDER 타입일 때 필수)", example = "5000") Long orderId,
        @Schema(description = "첨부 이미지 목록 (ORDER 타입 최대 3장)")
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
            @Schema(description = "제목 (최대 100자)", example = "사이즈 문의드립니다")
                    @Size(max = 100, message = "제목은 최대 100자까지 입력할 수 있습니다.")
                    String title,
            @Schema(description = "내용 (최대 500자)", example = "M 사이즈 어깨 실측이 어떻게 되나요?")
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

    /** privateYn → boolean 변환. "Y" → true, 그 외 → false. */
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
