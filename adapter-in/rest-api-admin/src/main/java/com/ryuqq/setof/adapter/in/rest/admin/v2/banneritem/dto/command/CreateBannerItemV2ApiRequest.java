package com.ryuqq.setof.adapter.in.rest.admin.v2.banneritem.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

/**
 * BannerItem 생성 요청 DTO
 *
 * @param title 제목 (nullable)
 * @param imageUrl 이미지 URL
 * @param linkUrl 링크 URL (nullable)
 * @param displayOrder 노출 순서
 * @param displayStartDate 노출 시작일시 (nullable)
 * @param displayEndDate 노출 종료일시 (nullable)
 * @param imageWidth 이미지 너비 (nullable)
 * @param imageHeight 이미지 높이 (nullable)
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "배너 아이템 생성 요청")
public record CreateBannerItemV2ApiRequest(
        @Schema(description = "제목", example = "프로모션 배너") String title,
        @Schema(description = "이미지 URL", example = "https://cdn.example.com/banner.jpg")
                @NotBlank(message = "이미지 URL은 필수입니다")
                String imageUrl,
        @Schema(description = "링크 URL", example = "https://example.com/promotion") String linkUrl,
        @Schema(description = "노출 순서", example = "1") @NotNull(message = "노출 순서는 필수입니다")
                Integer displayOrder,
        @Schema(description = "노출 시작일시", example = "2024-12-01T00:00:00Z") Instant displayStartDate,
        @Schema(description = "노출 종료일시", example = "2024-12-31T23:59:59Z") Instant displayEndDate,
        @Schema(description = "이미지 너비", example = "1920") Integer imageWidth,
        @Schema(description = "이미지 높이", example = "600") Integer imageHeight) {}
