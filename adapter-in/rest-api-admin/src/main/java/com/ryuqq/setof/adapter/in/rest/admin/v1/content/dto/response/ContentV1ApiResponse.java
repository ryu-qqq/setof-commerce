package com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * ContentV1ApiResponse - 콘텐츠 목록 응답 DTO.
 *
 * <p>레거시 ContentResponse 기반 변환.
 *
 * <p>GET /api/v1/contents - 콘텐츠 목록 조회
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>Lombok @Getter → record 기본 접근자
 *   <li>Yn enum → String 타입
 *   <li>DisplayPeriod Embedded → DisplayPeriodResponse 중첩 record
 *   <li>@Schema 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.display.dto.content.ContentResponse
 */
@Schema(description = "콘텐츠 목록 응답")
public record ContentV1ApiResponse(
        @Schema(description = "콘텐츠 ID", example = "1") long contentId,
        @Schema(
                        description = "전시 여부",
                        example = "Y",
                        allowableValues = {"Y", "N"})
                String displayYn,
        @Schema(description = "제목", example = "메인 콘텐츠") String title,
        @Schema(description = "전시 기간 정보") DisplayPeriodResponse displayPeriod,
        @Schema(description = "등록자", example = "admin") String insertOperator,
        @Schema(description = "수정자", example = "admin") String updateOperator,
        @Schema(description = "등록일시", example = "2024-01-15T10:30:00") LocalDateTime insertDate,
        @Schema(description = "수정일시", example = "2024-01-20T14:20:00") LocalDateTime updateDate) {

    /**
     * 팩토리 메서드.
     *
     * @param contentId 콘텐츠 ID
     * @param displayYn 전시 여부
     * @param title 제목
     * @param displayPeriod 전시 기간
     * @param insertOperator 등록자
     * @param updateOperator 수정자
     * @param insertDate 등록일시
     * @param updateDate 수정일시
     * @return ContentV1ApiResponse 인스턴스
     */
    public static ContentV1ApiResponse of(
            long contentId,
            String displayYn,
            String title,
            DisplayPeriodResponse displayPeriod,
            String insertOperator,
            String updateOperator,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        return new ContentV1ApiResponse(
                contentId,
                displayYn,
                title,
                displayPeriod,
                insertOperator,
                updateOperator,
                insertDate,
                updateDate);
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
