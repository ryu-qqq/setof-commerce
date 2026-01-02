package com.ryuqq.setof.adapter.in.rest.admin.v2.noticetemplate.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 상품고시 템플릿 필드 API 요청 DTO
 *
 * @param key 필드 키 (예: material, color)
 * @param description 필드 설명 (예: 소재, 색상)
 * @param required 필수 여부
 * @param displayOrder 표시 순서
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품고시 템플릿 필드 요청")
public record NoticeTemplateFieldV2ApiRequest(
        @Schema(
                        description = "필드 키",
                        example = "material",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "필드 키는 필수입니다")
                @Size(max = 50, message = "필드 키는 50자를 초과할 수 없습니다")
                String key,
        @Schema(description = "필드 설명", example = "소재", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "필드 설명은 필수입니다")
                @Size(max = 200, message = "필드 설명은 200자를 초과할 수 없습니다")
                String description,
        @Schema(
                        description = "필수 여부",
                        example = "true",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "필수 여부는 필수입니다")
                Boolean required,
        @Schema(description = "표시 순서", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "표시 순서는 필수입니다")
                @Min(value = 0, message = "표시 순서는 0 이상이어야 합니다")
                Integer displayOrder) {}
