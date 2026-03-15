package com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * BannerCommandV1ApiResponse - 배너 그룹 Command 응답 DTO.
 *
 * <p>레거시 BannerResponse 형태를 그대로 유지합니다.
 *
 * <p>Command(등록/수정/상태변경) 후 응답으로 사용됩니다.
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>Lombok @Getter → record 기본 접근자
 *   <li>BannerType enum → String 타입
 *   <li>Yn enum → String 타입 (Y/N)
 *   <li>DisplayPeriod Embedded → 중첩 record
 *   <li>@Schema 어노테이션 추가
 * </ul>
 *
 * @param bannerId 배너 ID
 * @param title 배너 제목
 * @param bannerType 배너 타입
 * @param displayPeriod 전시 기간
 * @param displayYn 전시 여부
 * @param insertOperator 등록자
 * @param updateOperator 수정자
 * @param insertDate 등록일시
 * @param updateDate 수정일시
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "배너 그룹 Command 응답")
public record BannerCommandV1ApiResponse(
        @Schema(description = "배너 ID", example = "1") long bannerId,
        @Schema(description = "배너 제목", example = "메인 배너") String title,
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
        @Schema(description = "전시 기간") DisplayPeriodV1ApiResponse displayPeriod,
        @Schema(
                        description = "전시 여부",
                        example = "Y",
                        allowableValues = {"Y", "N"})
                String displayYn,
        @Schema(description = "등록자", example = "admin") String insertOperator,
        @Schema(description = "수정자", example = "admin") String updateOperator,
        @Schema(description = "등록일시", example = "2024-01-15 10:30:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime insertDate,
        @Schema(description = "수정일시", example = "2024-01-20 14:20:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime updateDate) {

    /**
     * 팩토리 메서드.
     *
     * @param bannerId 배너 ID
     * @param title 배너 제목
     * @param bannerType 배너 타입
     * @param displayPeriod 전시 기간
     * @param displayYn 전시 여부
     * @param insertOperator 등록자
     * @param updateOperator 수정자
     * @param insertDate 등록일시
     * @param updateDate 수정일시
     * @return BannerCommandV1ApiResponse 인스턴스
     */
    public static BannerCommandV1ApiResponse of(
            long bannerId,
            String title,
            String bannerType,
            DisplayPeriodV1ApiResponse displayPeriod,
            String displayYn,
            String insertOperator,
            String updateOperator,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        return new BannerCommandV1ApiResponse(
                bannerId,
                title,
                bannerType,
                displayPeriod,
                displayYn,
                insertOperator,
                updateOperator,
                insertDate,
                updateDate);
    }

    /** 전시 기간 응답 DTO. */
    @Schema(description = "전시 기간 정보")
    public record DisplayPeriodV1ApiResponse(
            @Schema(description = "전시 시작일시", example = "2024-01-01 00:00:00")
                    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    LocalDateTime displayStartDate,
            @Schema(description = "전시 종료일시", example = "2024-12-31 23:59:59")
                    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    LocalDateTime displayEndDate) {

        /**
         * 팩토리 메서드.
         *
         * @param displayStartDate 전시 시작일시
         * @param displayEndDate 전시 종료일시
         * @return DisplayPeriodV1ApiResponse 인스턴스
         */
        public static DisplayPeriodV1ApiResponse of(
                LocalDateTime displayStartDate, LocalDateTime displayEndDate) {
            return new DisplayPeriodV1ApiResponse(displayStartDate, displayEndDate);
        }
    }
}
