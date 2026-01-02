package com.ryuqq.setof.application.banner.dto.response;

import java.time.Instant;

/**
 * Banner 응답 DTO
 *
 * @param bannerId 배너 ID
 * @param title 배너 제목
 * @param bannerType 배너 타입
 * @param status 상태
 * @param displayStartDate 노출 시작일시
 * @param displayEndDate 노출 종료일시
 * @param createdAt 생성일시
 * @param updatedAt 수정일시
 * @author development-team
 * @since 1.0.0
 */
public record BannerResponse(
        Long bannerId,
        String title,
        String bannerType,
        String status,
        Instant displayStartDate,
        Instant displayEndDate,
        Instant createdAt,
        Instant updatedAt) {

    /** Static Factory Method */
    public static BannerResponse of(
            Long bannerId,
            String title,
            String bannerType,
            String status,
            Instant displayStartDate,
            Instant displayEndDate,
            Instant createdAt,
            Instant updatedAt) {
        return new BannerResponse(
                bannerId,
                title,
                bannerType,
                status,
                displayStartDate,
                displayEndDate,
                createdAt,
                updatedAt);
    }
}
