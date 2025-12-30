package com.ryuqq.setof.application.componentitem.dto.command;

/**
 * ComponentItem 생성 Command
 *
 * @param componentId Component ID
 * @param itemType 아이템 타입 (PRODUCT, BRAND 등)
 * @param referenceId 참조 ID (상품 그룹 ID, 브랜드 ID 등)
 * @param title 표시 제목 (nullable)
 * @param imageUrl 표시 이미지 URL (nullable)
 * @param linkUrl 링크 URL (nullable)
 * @param displayOrder 노출 순서
 * @param sortType 정렬 타입 (nullable)
 * @author development-team
 * @since 1.0.0
 */
public record CreateComponentItemCommand(
        Long componentId,
        String itemType,
        Long referenceId,
        String title,
        String imageUrl,
        String linkUrl,
        Integer displayOrder,
        String sortType) {}
