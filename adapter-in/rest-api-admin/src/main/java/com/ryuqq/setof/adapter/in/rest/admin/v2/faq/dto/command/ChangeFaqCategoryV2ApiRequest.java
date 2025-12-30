package com.ryuqq.setof.adapter.in.rest.admin.v2.faq.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * FAQ 카테고리 변경 요청 DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "FAQ 카테고리 변경 요청")
public record ChangeFaqCategoryV2ApiRequest(
        @Schema(description = "새 카테고리 코드", example = "SHIPPING") @NotBlank
                String newCategoryCode) {}
