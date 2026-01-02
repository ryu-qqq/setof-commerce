package com.ryuqq.setof.application.banneritem.dto.command;

import java.time.Instant;

/**
 * BannerItem 생성 Command
 *
 * @param bannerId Banner ID
 * @param title 제목 (nullable)
 * @param imageUrl 이미지 URL (필수)
 * @param linkUrl 링크 URL (nullable)
 * @param displayOrder 노출 순서
 * @param displayStartDate 노출 시작일 (nullable)
 * @param displayEndDate 노출 종료일 (nullable)
 * @param imageWidth 이미지 너비 (nullable)
 * @param imageHeight 이미지 높이 (nullable)
 * @author development-team
 * @since 1.0.0
 */
public record CreateBannerItemCommand(
        Long bannerId,
        String title,
        String imageUrl,
        String linkUrl,
        Integer displayOrder,
        Instant displayStartDate,
        Instant displayEndDate,
        Integer imageWidth,
        Integer imageHeight) {}
