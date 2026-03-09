package com.ryuqq.setof.storage.legacy.composite.content.dto;

/**
 * LegacyWebTitleComponentQueryDto - 타이틀 컴포넌트 조회 DTO.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebTitleComponentQueryDto(
        long titleComponentId,
        long componentId,
        String title1,
        String title2,
        String subTitle1,
        String subTitle2) {}
