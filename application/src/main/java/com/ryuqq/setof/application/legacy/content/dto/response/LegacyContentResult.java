package com.ryuqq.setof.application.legacy.content.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * LegacyContentResult - 레거시 콘텐츠 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param contentId 콘텐츠 ID
 * @param title 콘텐츠 제목
 * @param memo 메모
 * @param imageUrl 이미지 URL
 * @param displayStartDate 표시 시작일
 * @param displayEndDate 표시 종료일
 * @param components 컴포넌트 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyContentResult(
        long contentId,
        String title,
        String memo,
        String imageUrl,
        LocalDateTime displayStartDate,
        LocalDateTime displayEndDate,
        List<LegacyComponentResult> components) {

    /**
     * 컴포넌트 없이 콘텐츠 메타데이터만 생성.
     *
     * @param contentId 콘텐츠 ID
     * @param title 콘텐츠 제목
     * @param memo 메모
     * @param imageUrl 이미지 URL
     * @param displayStartDate 표시 시작일
     * @param displayEndDate 표시 종료일
     * @return LegacyContentResult
     */
    public static LegacyContentResult ofMetaData(
            long contentId,
            String title,
            String memo,
            String imageUrl,
            LocalDateTime displayStartDate,
            LocalDateTime displayEndDate) {
        return new LegacyContentResult(
                contentId, title, memo, imageUrl, displayStartDate, displayEndDate, List.of());
    }

    /**
     * 컴포넌트 포함 전체 콘텐츠 생성.
     *
     * @param contentId 콘텐츠 ID
     * @param title 콘텐츠 제목
     * @param memo 메모
     * @param imageUrl 이미지 URL
     * @param displayStartDate 표시 시작일
     * @param displayEndDate 표시 종료일
     * @param components 컴포넌트 목록
     * @return LegacyContentResult
     */
    public static LegacyContentResult of(
            long contentId,
            String title,
            String memo,
            String imageUrl,
            LocalDateTime displayStartDate,
            LocalDateTime displayEndDate,
            List<LegacyComponentResult> components) {
        return new LegacyContentResult(
                contentId, title, memo, imageUrl, displayStartDate, displayEndDate, components);
    }
}
