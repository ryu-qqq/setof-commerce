package com.ryuqq.setof.storage.legacy.composite.content.dto;

/**
 * LegacyWebProductComponentQueryDto - 상품 컴포넌트 조회 DTO.
 *
 * @param productComponentId 상품 컴포넌트 ID
 * @param componentId 컴포넌트 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebProductComponentQueryDto(long productComponentId, long componentId) {}
