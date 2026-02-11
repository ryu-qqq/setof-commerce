package com.ryuqq.setof.storage.legacy.composite.web.content.dto;

import java.time.LocalDateTime;

/**
 * LegacyWebContentQueryDto - 레거시 웹 콘텐츠 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * @param contentId 콘텐츠 ID
 * @param title 콘텐츠 제목
 * @param memo 메모
 * @param imageUrl 이미지 URL
 * @param displayStartDate 표시 시작일
 * @param displayEndDate 표시 종료일
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebContentQueryDto(
        long contentId,
        String title,
        String memo,
        String imageUrl,
        LocalDateTime displayStartDate,
        LocalDateTime displayEndDate) {}
