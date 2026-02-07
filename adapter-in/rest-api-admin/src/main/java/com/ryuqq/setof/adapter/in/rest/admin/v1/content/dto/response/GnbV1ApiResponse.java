package com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * GnbV1ApiResponse - GNB 목록 응답 DTO.
 *
 * <p>레거시 GnbResponse 기반 변환.
 *
 * <p>GET /api/v1/content/gnbs - GNB 목록 조회
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>Lombok @Getter → record 기본 접근자
 *   <li>Yn enum → String 타입
 *   <li>GnbDetails Embedded → gnbDetails 중첩 record
 *   <li>@Schema 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.display.dto.gnb.GnbResponse
 */
@Schema(description = "GNB 목록 응답")
public record GnbV1ApiResponse(
        @Schema(description = "GNB ID", example = "1") long gnbId,
        @Schema(description = "GNB 상세 정보") GnbDetailsResponse gnbDetails) {

    /**
     * 팩토리 메서드.
     *
     * @param gnbId GNB ID
     * @param gnbDetails GNB 상세 정보
     * @return GnbV1ApiResponse 인스턴스
     */
    public static GnbV1ApiResponse of(long gnbId, GnbDetailsResponse gnbDetails) {
        return new GnbV1ApiResponse(gnbId, gnbDetails);
    }

    /** GNB 상세 정보 응답 DTO. */
    @Schema(description = "GNB 상세 정보")
    public record GnbDetailsResponse(
            @Schema(description = "제목", example = "홈") String title,
            @Schema(description = "링크 URL", example = "/") String linkUrl,
            @Schema(description = "전시 순서", example = "1") int displayOrder,
            @Schema(description = "전시 기간 정보") DisplayPeriodResponse displayPeriod,
            @Schema(
                            description = "전시 여부",
                            example = "Y",
                            allowableValues = {"Y", "N"})
                    String displayYn) {

        public static GnbDetailsResponse of(
                String title,
                String linkUrl,
                int displayOrder,
                DisplayPeriodResponse displayPeriod,
                String displayYn) {
            return new GnbDetailsResponse(title, linkUrl, displayOrder, displayPeriod, displayYn);
        }
    }

    /** 전시 기간 정보 응답 DTO. */
    @Schema(description = "전시 기간 정보")
    public record DisplayPeriodResponse(
            @Schema(description = "전시 시작일시", example = "2024-01-01T00:00:00")
                    LocalDateTime displayStartDate,
            @Schema(description = "전시 종료일시", example = "2099-12-31T23:59:59")
                    LocalDateTime displayEndDate) {

        public static DisplayPeriodResponse of(
                LocalDateTime displayStartDate, LocalDateTime displayEndDate) {
            return new DisplayPeriodResponse(displayStartDate, displayEndDate);
        }
    }
}
