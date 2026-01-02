package com.ryuqq.setof.application.content.dto.command;

import java.time.Instant;

/**
 * Content 수정 Command
 *
 * @param contentId 콘텐츠 ID
 * @param title 제목
 * @param memo 메모 (nullable)
 * @param imageUrl 대표 이미지 URL (nullable)
 * @param displayStartDate 노출 시작일시
 * @param displayEndDate 노출 종료일시
 * @author development-team
 * @since 1.0.0
 */
public record UpdateContentCommand(
        Long contentId,
        String title,
        String memo,
        String imageUrl,
        Instant displayStartDate,
        Instant displayEndDate) {}
