package com.ryuqq.setof.application.banneritem.dto.response;

import java.time.Instant;

/**
 * BannerItem 응답 DTO
 *
 * @param bannerItemId BannerItem ID
 * @param bannerId Banner ID
 * @param title 제목
 * @param imageUrl 이미지 URL
 * @param linkUrl 링크 URL
 * @param displayOrder 노출 순서
 * @param status 상태
 * @param displayStartDate 노출 시작일
 * @param displayEndDate 노출 종료일
 * @param imageWidth 이미지 너비
 * @param imageHeight 이미지 높이
 * @param createdAt 생성일시
 * @author development-team
 * @since 1.0.0
 */
public record BannerItemResponse(
        Long bannerItemId,
        Long bannerId,
        String title,
        String imageUrl,
        String linkUrl,
        Integer displayOrder,
        String status,
        Instant displayStartDate,
        Instant displayEndDate,
        Integer imageWidth,
        Integer imageHeight,
        Instant createdAt) {}
