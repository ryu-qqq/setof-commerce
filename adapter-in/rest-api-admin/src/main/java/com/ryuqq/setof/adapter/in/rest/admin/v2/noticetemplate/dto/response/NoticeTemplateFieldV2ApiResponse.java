package com.ryuqq.setof.adapter.in.rest.admin.v2.noticetemplate.dto.response;

import com.ryuqq.setof.application.noticetemplate.dto.response.NoticeTemplateFieldResponse;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 상품고시 템플릿 필드 API 응답 DTO
 *
 * @param key 필드 키
 * @param description 필드 설명
 * @param required 필수 여부
 * @param displayOrder 표시 순서
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품고시 템플릿 필드 응답")
public record NoticeTemplateFieldV2ApiResponse(
        @Schema(description = "필드 키", example = "material") String key,
        @Schema(description = "필드 설명", example = "소재") String description,
        @Schema(description = "필수 여부", example = "true") boolean required,
        @Schema(description = "표시 순서", example = "1") int displayOrder) {

    /**
     * Application Response를 API Response로 변환
     *
     * @param response Application 계층 응답
     * @return API 응답 DTO
     */
    public static NoticeTemplateFieldV2ApiResponse from(NoticeTemplateFieldResponse response) {
        return new NoticeTemplateFieldV2ApiResponse(
                response.key(),
                response.description(),
                response.required(),
                response.displayOrder());
    }
}
