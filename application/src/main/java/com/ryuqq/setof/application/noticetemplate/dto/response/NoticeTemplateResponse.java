package com.ryuqq.setof.application.noticetemplate.dto.response;

import java.time.Instant;
import java.util.List;

/**
 * 상품고시 템플릿 Response
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
 * @since 1.0.0
 */
public record NoticeTemplateResponse(
        Long templateId,
        Long categoryId,
        String templateName,
        List<NoticeTemplateFieldResponse> requiredFields,
        List<NoticeTemplateFieldResponse> optionalFields,
        int totalFieldCount,
        Instant createdAt,
        Instant updatedAt) {}
