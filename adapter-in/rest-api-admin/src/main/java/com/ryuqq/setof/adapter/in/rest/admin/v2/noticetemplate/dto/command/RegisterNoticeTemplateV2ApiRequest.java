package com.ryuqq.setof.adapter.in.rest.admin.v2.noticetemplate.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 상품고시 템플릿 등록 API 요청 DTO
 *
 * @param categoryId 카테고리 ID
 * @param templateName 템플릿명
 * @param fields 필드 목록
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품고시 템플릿 등록 요청")
public record RegisterNoticeTemplateV2ApiRequest(
        @Schema(description = "카테고리 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "카테고리 ID는 필수입니다")
                Long categoryId,
        @Schema(description = "템플릿명", example = "의류", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "템플릿명은 필수입니다")
                @Size(max = 100, message = "템플릿명은 100자를 초과할 수 없습니다")
                String templateName,
        @Schema(description = "필드 목록", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotEmpty(message = "최소 하나의 필드가 필요합니다")
                @Valid
                List<NoticeTemplateFieldV2ApiRequest> fields) {}
