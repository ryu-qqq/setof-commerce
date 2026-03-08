package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * UpdateQnaV1ApiRequest - Q&A 질문 수정 요청 DTO.
 *
 * <p>레거시 CreateQna(인터페이스) 기반 변환. PUT /api/v1/qna/{qnaId} 에 대응.
 *
 * <p>레거시에서는 @JsonTypeInfo를 이용한 다형성 역직렬화를 사용했으나, 신규 아키텍처에서는 qnaType 필드 값(PRODUCT / ORDER)으로 분기합니다.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-003: @Schema 어노테이션 (Request Body).
 *
 * <p>qnaId는 @PathVariable로 수신하므로 이 DTO에 포함하지 않습니다.
 *
 * <p>레거시 버그(qna.updateContents(qna.getQnaContents()))로 인해 실제로는 제목/내용이 변경되지 않았으나, 신규 아키텍처에서는 요청 바디의
 * 값으로 정상 수정합니다.
 *
 * @param qnaType Q&A 유형 (PRODUCT / ORDER)
 * @param qnaDetailType Q&A 상세 유형
 * @param title 제목 (최대 100자)
 * @param content 내용 (최대 500자)
 * @param secret 비밀글 여부
 * @param sellerId 셀러 ID
 * @param productGroupId 상품그룹 ID (PRODUCT 타입일 때)
 * @param orderId 주문 ID (ORDER 타입일 때)
 * @param images 첨부 이미지 목록 (ORDER 타입 최대 3장, diff 처리)
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "Q&A 질문 수정 요청")
public record UpdateQnaV1ApiRequest(
        @Schema(
                        description = "Q&A 유형",
                        example = "PRODUCT",
                        allowableValues = {"PRODUCT", "ORDER"})
                @NotBlank(message = "Q&A 유형은 필수입니다.")
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
        @Schema(description = "제목 (최대 100자)", example = "사이즈 재문의드립니다")
                @Size(max = 100, message = "제목은 최대 100자까지 입력할 수 있습니다.")
                String title,
        @Schema(description = "내용 (최대 500자)", example = "XL 사이즈 재고 있나요?")
                @Size(max = 500, message = "내용은 최대 500자까지 입력할 수 있습니다.")
                String content,
        @Schema(description = "비밀글 여부 (true: 비밀글, false: 공개)", example = "false") boolean secret,
        @Schema(description = "셀러 ID", example = "1") @NotNull(message = "셀러 ID는 필수입니다.")
                Long sellerId,
        @Schema(description = "상품그룹 ID (PRODUCT 타입일 때)", example = "42") Long productGroupId,
        @Schema(description = "주문 ID (ORDER 타입일 때)", example = "999") Long orderId,
        @Schema(description = "첨부 이미지 목록 (ORDER 타입 최대 3장, diff 처리)")
                @Size(max = 3, message = "이미지는 최대 3장까지 첨부할 수 있습니다.")
                @Valid
                List<QnaImageRequest> images) {

    /**
     * QnaImageRequest - Q&A 첨부 이미지 수정 요청.
     *
     * <p>qnaImageId가 null이면 신규 이미지 추가, 값이 있으면 기존 이미지 수정입니다. 기존 이미지 ID가 요청 목록에 없으면 소프트 삭제 처리됩니다.
     *
     * @param qnaImageId 기존 이미지 ID (null이면 신규)
     * @param imageUrl 이미지 URL
     * @param displayOrder 이미지 표시 순서
     */
    @Schema(description = "Q&A 첨부 이미지 수정 요청")
    public record QnaImageRequest(
            @Schema(description = "기존 이미지 ID (null이면 신규 추가)", example = "10", nullable = true)
                    Long qnaImageId,
            @Schema(description = "이미지 URL", example = "https://cdn.example.com/qna/image1.jpg")
                    String imageUrl,
            @Schema(description = "이미지 표시 순서", example = "1")
                    @NotNull(message = "이미지 표시 순서는 필수입니다.")
                    Integer displayOrder) {}
}
