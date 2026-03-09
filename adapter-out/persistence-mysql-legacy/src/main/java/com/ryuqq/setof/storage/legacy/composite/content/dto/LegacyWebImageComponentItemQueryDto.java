package com.ryuqq.setof.storage.legacy.composite.content.dto;

/**
 * LegacyWebImageComponentItemQueryDto - 이미지 컴포넌트 아이템 조회 DTO.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebImageComponentItemQueryDto(
        long imageComponentItemId,
        long imageComponentId,
        String imageUrl,
        int displayOrder,
        String linkUrl,
        long width,
        long height) {}
