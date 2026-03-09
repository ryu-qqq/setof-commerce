package com.ryuqq.setof.storage.legacy.composite.content.dto;

/**
 * LegacyWebBrandComponentQueryDto - 브랜드 컴포넌트 조회 DTO.
 *
 * @param brandComponentId 브랜드 컴포넌트 ID
 * @param componentId 컴포넌트 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebBrandComponentQueryDto(long brandComponentId, long componentId) {}
