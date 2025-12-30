package com.ryuqq.setof.application.componentitem.dto.response;

import java.time.Instant;

/**
 * ComponentItem 응답 DTO
 *
 * @param componentItemId ComponentItem ID
 * @param componentId Component ID
 * @param itemType 아이템 타입 (PRODUCT, BRAND 등)
 * @param referenceId 참조 ID (상품 그룹 ID, 브랜드 ID 등)
 * @param title 표시 제목
 * @param imageUrl 표시 이미지 URL
 * @param linkUrl 링크 URL
 * @param displayOrder 노출 순서
 * @param status 상태 (ACTIVE, INACTIVE 등)
 * @param sortType 정렬 타입
 * @param createdAt 생성일시
 * @author development-team
 * @since 1.0.0
 */
public record ComponentItemResponse(
        Long componentItemId,
        Long componentId,
        String itemType,
        Long referenceId,
        String title,
        String imageUrl,
        String linkUrl,
        Integer displayOrder,
        String status,
        String sortType,
        Instant createdAt) {}
