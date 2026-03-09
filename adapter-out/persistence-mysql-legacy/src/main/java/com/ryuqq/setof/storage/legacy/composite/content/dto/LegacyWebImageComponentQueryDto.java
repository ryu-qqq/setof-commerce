package com.ryuqq.setof.storage.legacy.composite.content.dto;

/**
 * LegacyWebImageComponentQueryDto - 이미지 컴포넌트 조회 DTO.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebImageComponentQueryDto(
        long imageComponentId, long componentId, String imageType) {}
