package com.ryuqq.setof.application.legacy.gnb.dto.response;

/**
 * LegacyGnbResult - 레거시 GNB 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param gnbId GNB ID
 * @param title 제목
 * @param linkUrl 링크 URL
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyGnbResult(long gnbId, String title, String linkUrl) {

    /**
     * 팩토리 메서드.
     *
     * @param gnbId GNB ID
     * @param title 제목
     * @param linkUrl 링크 URL
     * @return LegacyGnbResult
     */
    public static LegacyGnbResult of(long gnbId, String title, String linkUrl) {
        return new LegacyGnbResult(gnbId, title, linkUrl);
    }
}
