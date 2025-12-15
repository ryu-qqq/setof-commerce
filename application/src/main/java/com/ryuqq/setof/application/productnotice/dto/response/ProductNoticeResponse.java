package com.ryuqq.setof.application.productnotice.dto.response;

import java.time.Instant;
import java.util.List;

/**
 * 상품고시 Response DTO
 *
 * @param productNoticeId 상품고시 ID
 * @param productGroupId 상품그룹 ID
 * @param templateId 템플릿 ID
 * @param items 고시 항목 목록
 * @param itemCount 항목 개수
 * @param createdAt 생성일시
 * @param updatedAt 수정일시
 */
public record ProductNoticeResponse(
        Long productNoticeId,
        Long productGroupId,
        Long templateId,
        List<NoticeItemResponse> items,
        int itemCount,
        Instant createdAt,
        Instant updatedAt) {}
