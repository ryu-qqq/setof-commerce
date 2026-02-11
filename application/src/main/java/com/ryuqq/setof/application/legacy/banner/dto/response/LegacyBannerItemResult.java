package com.ryuqq.setof.application.legacy.banner.dto.response;

/**
 * LegacyBannerItemResult - 레거시 배너 아이템 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param bannerItemId 배너 아이템 ID
 * @param title 배너 제목
 * @param imageUrl 이미지 URL
 * @param linkUrl 링크 URL
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyBannerItemResult(
        long bannerItemId, String title, String imageUrl, String linkUrl) {

    /**
     * 팩토리 메서드.
     *
     * @param bannerItemId 배너 아이템 ID
     * @param title 배너 제목
     * @param imageUrl 이미지 URL
     * @param linkUrl 링크 URL
     * @return LegacyBannerItemResult
     */
    public static LegacyBannerItemResult of(
            long bannerItemId, String title, String imageUrl, String linkUrl) {
        return new LegacyBannerItemResult(bannerItemId, title, imageUrl, linkUrl);
    }
}
