package com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * BannerItemV1ApiResponse - 배너 아이템 목록 응답 DTO.
 *
 * <p>레거시 BannerItemDto 기반 변환.
 *
 * <p>GET /api/v1/content/banner/{bannerId} - 배너 아이템 목록 조회
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>Lombok @Getter → record 기본 접근자
 *   <li>BannerType enum → String 타입
 *   <li>Yn enum → String 타입
 *   <li>DisplayPeriod Embedded → 중첩 record
 *   <li>ImageSize Embedded → 중첩 record
 *   <li>@Schema 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.display.dto.banner.BannerItemDto
 */
@Schema(description = "배너 아이템 목록 응답")
public record BannerItemV1ApiResponse(
        @Schema(description = "배너 아이템 ID", example = "1") long bannerItemId,
        @Schema(
                        description = "배너 타입",
                        example = "CATEGORY",
                        allowableValues = {
                            "CATEGORY",
                            "MY_PAGE",
                            "CART",
                            "PRODUCT_DETAIL_DESCRIPTION",
                            "RECOMMEND",
                            "LOGIN"
                        })
                String bannerType,
        @Schema(description = "제목", example = "메인 배너 아이템 1") String title,
        @Schema(description = "전시 기간 정보") DisplayPeriodResponse displayPeriod,
        @Schema(description = "이미지 URL", example = "https://cdn.example.com/banner1.jpg")
                String imageUrl,
        @Schema(description = "링크 URL", example = "https://example.com/event/1") String linkUrl,
        @Schema(description = "전시 순서", example = "1") int displayOrder,
        @Schema(
                        description = "전시 여부",
                        example = "Y",
                        allowableValues = {"Y", "N"})
                String displayYn,
        @Schema(description = "이미지 크기 정보") ImageSizeResponse imageSize) {

    /**
     * 팩토리 메서드.
     *
     * @param bannerItemId 배너 아이템 ID
     * @param bannerType 배너 타입
     * @param title 제목
     * @param displayPeriod 전시 기간
     * @param imageUrl 이미지 URL
     * @param linkUrl 링크 URL
     * @param displayOrder 전시 순서
     * @param displayYn 전시 여부
     * @param imageSize 이미지 크기
     * @return BannerItemV1ApiResponse 인스턴스
     */
    public static BannerItemV1ApiResponse of(
            long bannerItemId,
            String bannerType,
            String title,
            DisplayPeriodResponse displayPeriod,
            String imageUrl,
            String linkUrl,
            int displayOrder,
            String displayYn,
            ImageSizeResponse imageSize) {
        return new BannerItemV1ApiResponse(
                bannerItemId,
                bannerType,
                title,
                displayPeriod,
                imageUrl,
                linkUrl,
                displayOrder,
                displayYn,
                imageSize);
    }

    /** 전시 기간 정보 응답 DTO. */
    @Schema(description = "전시 기간 정보")
    public record DisplayPeriodResponse(
            @Schema(description = "전시 시작일시", example = "2024-01-01T00:00:00")
                    LocalDateTime displayStartDate,
            @Schema(description = "전시 종료일시", example = "2024-12-31T23:59:59")
                    LocalDateTime displayEndDate) {

        public static DisplayPeriodResponse of(
                LocalDateTime displayStartDate, LocalDateTime displayEndDate) {
            return new DisplayPeriodResponse(displayStartDate, displayEndDate);
        }
    }

    /** 이미지 크기 정보 응답 DTO. */
    @Schema(description = "이미지 크기 정보")
    public record ImageSizeResponse(
            @Schema(description = "이미지 너비", example = "1920.0") double width,
            @Schema(description = "이미지 높이", example = "600.0") double height) {

        public static ImageSizeResponse of(double width, double height) {
            return new ImageSizeResponse(width, height);
        }
    }
}
