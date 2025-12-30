package com.ryuqq.setof.application.content.dto.command;

import java.time.Instant;

/**
 * Content 생성 Command
 *
 * @param title 제목
 * @param memo 메모 (nullable)
 * @param imageUrl 대표 이미지 URL (nullable)
 * @param displayStartDate 노출 시작일시
 * @param displayEndDate 노출 종료일시
 * @author development-team
 * @since 1.0.0
 */
public record CreateContentCommand(
        String title,
        String memo,
        String imageUrl,
        Instant displayStartDate,
        Instant displayEndDate) {

    /**
     * 노출 기간이 설정되었는지 확인
     *
     * @return 노출 기간이 모두 설정되면 true
     */
    public boolean hasDisplayPeriod() {
        return displayStartDate != null && displayEndDate != null;
    }
}
