package com.ryuqq.setof.storage.legacy.composite.web.banner.dto;

/**
 * LegacyWebBannerItemQueryDto - 레거시 Web 배너 아이템 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * @param bannerItemId 배너 아이템 ID
 * @param title 배너 제목
 * @param imageUrl 이미지 URL
 * @param linkUrl 링크 URL
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebBannerItemQueryDto(
        long bannerItemId, String title, String imageUrl, String linkUrl) {}
