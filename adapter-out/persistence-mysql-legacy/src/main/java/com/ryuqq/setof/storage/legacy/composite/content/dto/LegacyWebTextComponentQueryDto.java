package com.ryuqq.setof.storage.legacy.composite.content.dto;

/**
 * LegacyWebTextComponentQueryDto - 텍스트 컴포넌트 조회 DTO.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebTextComponentQueryDto(
        long textComponentId, long componentId, String content) {}
