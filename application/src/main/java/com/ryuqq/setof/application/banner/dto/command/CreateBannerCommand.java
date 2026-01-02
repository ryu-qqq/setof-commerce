package com.ryuqq.setof.application.banner.dto.command;

import java.time.Instant;

/**
 * Banner 생성 Command
 *
 * @param title 배너 제목
 * @param bannerType 배너 타입 (MAIN, SUB, POPUP 등)
 * @param displayStartDate 노출 시작일시
 * @param displayEndDate 노출 종료일시
 * @author development-team
 * @since 1.0.0
 */
public record CreateBannerCommand(
        String title, String bannerType, Instant displayStartDate, Instant displayEndDate) {

    /** 유효성 검증 */
    public CreateBannerCommand {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다");
        }
        if (bannerType == null || bannerType.isBlank()) {
            throw new IllegalArgumentException("배너 타입은 필수입니다");
        }
        if (displayStartDate == null || displayEndDate == null) {
            throw new IllegalArgumentException("노출 기간은 필수입니다");
        }
    }
}
