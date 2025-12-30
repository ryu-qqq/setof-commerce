package com.ryuqq.setof.adapter.in.rest.admin.v2.banneritem.dto.response;

import com.ryuqq.setof.application.banneritem.dto.response.BannerItemResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * BannerItem 응답 DTO
 *
 * @param bannerItemId 배너 아이템 ID
 * @param bannerId 배너 ID
 * @param title 제목
 * @param imageUrl 이미지 URL
 * @param linkUrl 링크 URL
 * @param displayOrder 노출 순서
 * @param status 상태
 * @param displayStartDate 노출 시작일시
 * @param displayEndDate 노출 종료일시
 * @param imageWidth 이미지 너비
 * @param imageHeight 이미지 높이
 * @param createdAt 생성일시
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "배너 아이템 응답")
public record BannerItemV2ApiResponse(
        @Schema(description = "배너 아이템 ID", example = "1") Long bannerItemId,
        @Schema(description = "배너 ID", example = "1") Long bannerId,
        @Schema(description = "제목", example = "프로모션 배너") String title,
        @Schema(description = "이미지 URL", example = "https://cdn.example.com/banner.jpg")
                String imageUrl,
        @Schema(description = "링크 URL", example = "https://example.com/promotion") String linkUrl,
        @Schema(description = "노출 순서", example = "1") Integer displayOrder,
        @Schema(description = "상태", example = "ACTIVE") String status,
        @Schema(description = "노출 시작일시", example = "2024-12-01T00:00:00Z") Instant displayStartDate,
        @Schema(description = "노출 종료일시", example = "2024-12-31T23:59:59Z") Instant displayEndDate,
        @Schema(description = "이미지 너비", example = "1920") Integer imageWidth,
        @Schema(description = "이미지 높이", example = "600") Integer imageHeight,
        @Schema(description = "생성일시", example = "2024-11-01T10:00:00Z") Instant createdAt) {

    /**
     * Application Layer 응답으로부터 변환
     *
     * @param response Application Layer 응답
     * @return API 응답
     */
    public static BannerItemV2ApiResponse from(BannerItemResponse response) {
        return new BannerItemV2ApiResponse(
                response.bannerItemId(),
                response.bannerId(),
                response.title(),
                response.imageUrl(),
                response.linkUrl(),
                response.displayOrder(),
                response.status(),
                response.displayStartDate(),
                response.displayEndDate(),
                response.imageWidth(),
                response.imageHeight(),
                response.createdAt());
    }
}
