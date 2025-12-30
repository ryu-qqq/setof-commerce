package com.ryuqq.setof.adapter.in.rest.admin.v2.faq.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * FAQ 수정 요청 DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "FAQ 수정 요청")
public record UpdateFaqV2ApiRequest(
        @Schema(description = "질문", example = "주문 취소는 어떻게 하나요?") @NotBlank String question,
        @Schema(description = "답변", example = "마이페이지에서 주문 내역을 확인 후 취소하실 수 있습니다.") @NotBlank
                String answer,
        @Schema(description = "표시 순서", example = "1") @Min(0) int displayOrder) {}
