package com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.response;

import com.ryuqq.setof.application.banner.dto.response.BannerResponse;
import io.swagger.v3.oas.annotations.media.Schema;
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
 * @since 2.0.0
 */
@Schema(description = "배너 응답")
public record BannerV2ApiResponse(
        @Schema(description = "배너 ID", example = "1") Long bannerId,
        @Schema(description = "배너 제목", example = "메인 배너") String title,
        @Schema(description = "배너 타입", example = "CATEGORY") String bannerType,
        @Schema(description = "상태", example = "ACTIVE") String status,
        @Schema(description = "노출 시작일시", example = "2024-12-01T00:00:00Z") Instant displayStartDate,
        @Schema(description = "노출 종료일시", example = "2024-12-31T23:59:59Z") Instant displayEndDate,
        @Schema(description = "생성일시", example = "2024-11-01T10:00:00Z") Instant createdAt,
        @Schema(description = "수정일시", example = "2024-11-15T14:30:00Z") Instant updatedAt) {

    /**
     * Application Layer 응답으로부터 변환
     *
     * @param response Application Layer 응답
     * @return API 응답
     */
    public static BannerV2ApiResponse from(BannerResponse response) {
        return new BannerV2ApiResponse(
                response.bannerId(),
                response.title(),
                response.bannerType(),
                response.status(),
                response.displayStartDate(),
                response.displayEndDate(),
                response.createdAt(),
                response.updatedAt());
    }
}
