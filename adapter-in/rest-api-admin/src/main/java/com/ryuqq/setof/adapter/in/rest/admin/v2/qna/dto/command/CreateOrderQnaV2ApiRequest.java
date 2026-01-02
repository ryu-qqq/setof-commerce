package com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * Create Order QnA V2 API Request
 *
 * <p>주문 문의 생성 API 요청 DTO
 *
 * @param orderId 주문 ID
 * @param writerName 작성자 이름
 * @param title 문의 제목
 * @param content 문의 내용
 * @param detailType 문의 세부 유형 (DELIVERY, CANCEL, EXCHANGE, RETURN, OTHER)
 * @param isSecret 비밀글 여부
 * @param imageUrls 이미지 URL 목록 (최대 3개)
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "주문 문의 생성 요청")
public record CreateOrderQnaV2ApiRequest(
        @Schema(description = "주문 ID", example = "2001") @NotNull(message = "주문 ID는 필수입니다.")
                Long orderId,
        @Schema(description = "작성자 이름", example = "홍길동")
                @NotBlank(message = "작성자 이름은 필수입니다.")
                @Size(max = 50, message = "작성자 이름은 50자를 초과할 수 없습니다.")
                String writerName,
        @Schema(description = "문의 제목", example = "배송 문의드립니다")
                @NotBlank(message = "제목은 필수입니다.")
                @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다.")
                String title,
        @Schema(description = "문의 내용", example = "배송이 언제쯤 도착할까요?")
                @NotBlank(message = "내용은 필수입니다.")
                @Size(max = 4000, message = "내용은 4000자를 초과할 수 없습니다.")
                String content,
        @Schema(
                        description = "문의 세부 유형 (DELIVERY, CANCEL, EXCHANGE, RETURN, OTHER)",
                        example = "DELIVERY")
                @NotBlank(message = "문의 유형은 필수입니다.")
                String detailType,
        @Schema(description = "비밀글 여부", example = "false") boolean isSecret,
        @Schema(description = "이미지 URL 목록 (최대 3개)")
                @Size(max = 3, message = "이미지는 최대 3개까지 첨부할 수 있습니다.")
                List<String> imageUrls) {}
