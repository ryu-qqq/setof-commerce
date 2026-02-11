package com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * BannerGroupV1ApiResponse - 배너 그룹 목록 응답 DTO.
 *
 * <p>레거시 BannerGroupDto 기반 변환.
 *
 * <p>GET /api/v1/content/banners - 배너 그룹 목록 조회
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>Lombok @Getter → record 기본 접근자
 *   <li>BannerType enum → String 타입
 *   <li>Yn enum → String 타입
 *   <li>DisplayPeriod Embedded → 중첩 record
 *   <li>@Schema 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.display.dto.banner.BannerGroupDto
 */
@Schema(description = "배너 그룹 목록 응답")
public record BannerGroupV1ApiResponse(
        @Schema(description = "배너 ID", example = "1") long bannerId,
        @Schema(description = "제목", example = "메인 배너") String title,
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
        @Schema(description = "전시 기간 정보") DisplayPeriodResponse displayPeriod,
        @Schema(
                        description = "전시 여부",
                        example = "Y",
                        allowableValues = {"Y", "N"})
                String displayYn,
        @Schema(description = "등록일시", example = "2024-01-15T10:30:00") LocalDateTime insertDate,
        @Schema(description = "수정일시", example = "2024-01-20T14:20:00") LocalDateTime updateDate,
        @Schema(description = "등록자", example = "admin") String insertOperator,
        @Schema(description = "수정자", example = "admin") String updateOperator) {

    /**
     * 팩토리 메서드.
     *
     * @param bannerId 배너 ID
     * @param title 제목
     * @param bannerType 배너 타입
     * @param displayPeriod 전시 기간
     * @param displayYn 전시 여부
     * @param insertDate 등록일시
     * @param updateDate 수정일시
     * @param insertOperator 등록자
     * @param updateOperator 수정자
     * @return BannerGroupV1ApiResponse 인스턴스
     */
    public static BannerGroupV1ApiResponse of(
            long bannerId,
            String title,
            String bannerType,
            DisplayPeriodResponse displayPeriod,
            String displayYn,
            LocalDateTime insertDate,
            LocalDateTime updateDate,
            String insertOperator,
            String updateOperator) {
        return new BannerGroupV1ApiResponse(
                bannerId,
                title,
                bannerType,
                displayPeriod,
                displayYn,
                insertDate,
                updateDate,
                insertOperator,
                updateOperator);
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
}
