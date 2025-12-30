package com.ryuqq.setof.adapter.in.rest.admin.v2.noticetemplate.dto.response;

import com.ryuqq.setof.application.noticetemplate.dto.response.NoticeTemplateResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * 상품고시 템플릿 API 응답 DTO
 *
 * @param templateId 템플릿 ID
 * @param categoryId 카테고리 ID
 * @param templateName 템플릿명
 * @param requiredFields 필수 필드 목록
 * @param optionalFields 선택 필드 목록
 * @param totalFieldCount 전체 필드 개수
 * @param createdAt 생성일시
 * @param updatedAt 수정일시
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품고시 템플릿 응답")
public record NoticeTemplateV2ApiResponse(
        @Schema(description = "템플릿 ID", example = "1") Long templateId,
        @Schema(description = "카테고리 ID", example = "1") Long categoryId,
        @Schema(description = "템플릿명", example = "의류") String templateName,
        @Schema(description = "필수 필드 목록") List<NoticeTemplateFieldV2ApiResponse> requiredFields,
        @Schema(description = "선택 필드 목록") List<NoticeTemplateFieldV2ApiResponse> optionalFields,
        @Schema(description = "전체 필드 개수", example = "9") int totalFieldCount,
        @Schema(description = "생성일시") Instant createdAt,
        @Schema(description = "수정일시") Instant updatedAt) {

    /**
     * Application Response를 API Response로 변환
     *
     * @param response Application 계층 응답
     * @return API 응답 DTO
     */
    public static NoticeTemplateV2ApiResponse from(NoticeTemplateResponse response) {
        List<NoticeTemplateFieldV2ApiResponse> requiredFieldResponses =
                response.requiredFields() != null
                        ? response.requiredFields().stream()
                                .map(NoticeTemplateFieldV2ApiResponse::from)
                                .toList()
                        : new ArrayList<>();

        List<NoticeTemplateFieldV2ApiResponse> optionalFieldResponses =
                response.optionalFields() != null
                        ? response.optionalFields().stream()
                                .map(NoticeTemplateFieldV2ApiResponse::from)
                                .toList()
                        : new ArrayList<>();

        return new NoticeTemplateV2ApiResponse(
                response.templateId(),
                response.categoryId(),
                response.templateName(),
                requiredFieldResponses,
                optionalFieldResponses,
                response.totalFieldCount(),
                response.createdAt(),
                response.updatedAt());
    }
}
