package com.ryuqq.setof.storage.legacy.composite.content.dto;

/**
 * LegacyWebTabComponentQueryDto - 탭 컴포넌트 조회 DTO.
 *
 * @param tabComponentId 탭 컴포넌트 ID
 * @param componentId 컴포넌트 ID
 * @param stickyYn 고정 여부
 * @param tabMovingType 탭 이동 타입
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebTabComponentQueryDto(
        long tabComponentId, long componentId, String stickyYn, String tabMovingType) {}
