package com.ryuqq.setof.application.banner.dto.command;

import com.ryuqq.setof.domain.cms.vo.BannerId;
import java.time.Instant;

/**
 * Banner 수정 Command
 *
 * @param bannerId 배너 ID
 * @param title 배너 제목
 * @param displayStartDate 노출 시작일시
 * @param displayEndDate 노출 종료일시
 * @author development-team
 * @since 1.0.0
 */
public record UpdateBannerCommand(
        BannerId bannerId, String title, Instant displayStartDate, Instant displayEndDate) {

    /** 유효성 검증 */
    public UpdateBannerCommand {
        if (bannerId == null) {
            throw new IllegalArgumentException("배너 ID는 필수입니다");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다");
        }
        if (displayStartDate == null || displayEndDate == null) {
            throw new IllegalArgumentException("노출 기간은 필수입니다");
        }
    }
}
