package com.ryuqq.setof.adapter.in.rest.admin.v2.noticetemplate.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 상품고시 템플릿 수정 API 요청 DTO
 *
 * <p>수정할 필드만 포함하여 요청합니다. null인 필드는 수정하지 않습니다.
 *
 * @param templateName 템플릿명 (null이면 수정하지 않음)
 * @param fields 필드 목록 (null이면 수정하지 않음, 전체 교체)
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품고시 템플릿 수정 요청")
public record UpdateNoticeTemplateV2ApiRequest(
        @Schema(description = "템플릿명", example = "의류 (수정)")
                @Size(max = 100, message = "템플릿명은 100자를 초과할 수 없습니다")
                String templateName,
        @Schema(description = "필드 목록 (전체 교체)") @Valid
                List<NoticeTemplateFieldV2ApiRequest> fields) {}
