package com.ryuqq.setof.adapter.in.rest.v1.banner.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * BannerSlideV1ApiResponse - 배너 슬라이드 응답 DTO.
 *
 * <p>레거시 BannerItemDto 기반 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "배너 슬라이드 응답")
public record BannerSlideV1ApiResponse(
        @Schema(description = "배너 아이템 ID", example = "1") long bannerItemId,
        @Schema(description = "배너 제목", example = "신규 회원 이벤트") String title,
        @Schema(description = "이미지 URL", example = "https://cdn.example.com/banner/1.jpg")
                String imageUrl,
        @Schema(description = "링크 URL", example = "/event/new-member") String linkUrl) {}
