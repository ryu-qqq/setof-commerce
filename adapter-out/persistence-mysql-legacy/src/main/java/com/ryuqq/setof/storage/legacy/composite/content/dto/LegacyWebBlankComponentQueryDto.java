package com.ryuqq.setof.storage.legacy.composite.content.dto;

/**
 * LegacyWebBlankComponentQueryDto - 여백 컴포넌트 조회 DTO.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebBlankComponentQueryDto(
        long blankComponentId, long componentId, double height, String lineYn) {}
