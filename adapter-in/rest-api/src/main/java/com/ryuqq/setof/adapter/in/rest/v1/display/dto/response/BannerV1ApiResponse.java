package com.ryuqq.setof.adapter.in.rest.v1.display.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * 배너 Response
 *
 * <p>배너 정보를 반환하는 응답 DTO입니다.
 *
 * @param bannerItemId 배너 아이템 ID
 * @param title 제목
 * @param imageUrl 이미지 URL
 * @param linkUrl 링크 URL
 * @param displayPeriod 링크 타겟 (_self, _blank)
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "Banner 응답")
public record BannerV1ApiResponse(
        @Schema(description = "배너 ID", example = "1") Long bannerItemId,
        @Schema(description = "제목", example = "여름 세일 배너") String title,
        @Schema(description = "이미지 URL", example = "https://cdn.example.com/banner/summer-pc.jpg")
                String imageUrl,
        @Schema(description = "링크 URL", example = "/event/summer-sale") String linkUrl,
        @Schema(description = "전시 기간") BannerDisplayPeriodV1Response displayPeriod) {

    @Schema(description = "Banner 전시 기간 응답")
    public record BannerDisplayPeriodV1Response(
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    @Schema(description = "전시 시작 기간", example = "2024-12-30 00:00:00")
                    LocalDateTime displayStartDate,
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    @Schema(description = "전시 종료 기간", example = "2025-12-30 00:00:00")
                    LocalDateTime displayEndDate) {}
}
