package com.ryuqq.setof.application.gnb.dto.command;

import com.ryuqq.setof.domain.cms.vo.GnbId;
import java.time.Instant;

/**
 * Gnb 수정 Command
 *
 * @param gnbId GNB ID
 * @param title GNB 제목
 * @param linkUrl 링크 URL (nullable)
 * @param displayOrder 노출 순서
 * @param displayStartDate 노출 시작일시 (nullable)
 * @param displayEndDate 노출 종료일시 (nullable)
 * @author development-team
 * @since 1.0.0
 */
public record UpdateGnbCommand(
        GnbId gnbId,
        String title,
        String linkUrl,
        Integer displayOrder,
        Instant displayStartDate,
        Instant displayEndDate) {

    /** 유효성 검증 및 기본값 적용 */
    public UpdateGnbCommand {
        if (gnbId == null) {
            throw new IllegalArgumentException("GNB ID는 필수입니다");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다");
        }
        if (displayOrder == null) {
            displayOrder = 0;
        }
    }

    /** 노출 기간이 설정되었는지 확인 */
    public boolean hasDisplayPeriod() {
        return displayStartDate != null && displayEndDate != null;
    }
}
