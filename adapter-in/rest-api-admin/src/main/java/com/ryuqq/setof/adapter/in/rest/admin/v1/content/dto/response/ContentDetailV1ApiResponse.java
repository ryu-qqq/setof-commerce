package com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ContentDetailV1ApiResponse - 콘텐츠 상세 조회 응답 DTO.
 *
 * <p>레거시 ContentGroupResponse 기반 변환.
 *
 * <p>GET /api/v1/content/{contentId} - 콘텐츠 상세 조회
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>Lombok @Getter → record 기본 접근자
 *   <li>Yn enum → String 타입
 *   <li>DisplayPeriod Embedded → 중첩 record
 *   <li>SubComponent interface → ComponentResponse 중첩 record
 *   <li>@Schema 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.display.dto.content.ContentGroupResponse
 */
@Schema(description = "콘텐츠 상세 조회 응답")
public record ContentDetailV1ApiResponse(
        @Schema(description = "콘텐츠 ID", example = "1") long contentId,
        @Schema(description = "전시 기간 정보") DisplayPeriodResponse displayPeriod,
        @Schema(description = "제목", example = "메인 콘텐츠") String title,
        @Schema(description = "메모", example = "관리자 메모") String memo,
        @Schema(description = "이미지 URL", example = "https://cdn.example.com/image.jpg")
                String imageUrl,
        @Schema(
                        description = "전시 여부",
                        example = "Y",
                        allowableValues = {"Y", "N"})
                String displayYn,
        @Schema(description = "컴포넌트 목록") List<ComponentResponse> components) {

    /**
     * 팩토리 메서드.
     *
     * @param contentId 콘텐츠 ID
     * @param displayPeriod 전시 기간
     * @param title 제목
     * @param memo 메모
     * @param imageUrl 이미지 URL
     * @param displayYn 전시 여부
     * @param components 컴포넌트 목록
     * @return ContentDetailV1ApiResponse 인스턴스
     */
    public static ContentDetailV1ApiResponse of(
            long contentId,
            DisplayPeriodResponse displayPeriod,
            String title,
            String memo,
            String imageUrl,
            String displayYn,
            List<ComponentResponse> components) {
        return new ContentDetailV1ApiResponse(
                contentId, displayPeriod, title, memo, imageUrl, displayYn, components);
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

    /** 컴포넌트 정보 응답 DTO. */
    @Schema(description = "컴포넌트 정보")
    public record ComponentResponse(
            @Schema(description = "컴포넌트 ID", example = "100") Long componentId,
            @Schema(description = "전시 순서", example = "1") int displayOrder,
            @Schema(description = "뷰 확장 ID", example = "10") Long viewExtensionId,
            @Schema(
                            description = "컴포넌트 타입",
                            example = "PRODUCT",
                            allowableValues = {
                                "TEXT",
                                "TITLE",
                                "IMAGE",
                                "BLANK",
                                "TAB",
                                "BRAND",
                                "CATEGORY",
                                "PRODUCT"
                            })
                    String componentType,
            @Schema(description = "컴포넌트명", example = "메인 상품") String componentName,
            @Schema(description = "전시 기간 정보") DisplayPeriodResponse displayPeriod,
            @Schema(
                            description = "전시 여부",
                            example = "Y",
                            allowableValues = {"Y", "N"})
                    String displayYn,
            @Schema(description = "노출 상품 수", example = "10") int exposedProducts) {

        public static ComponentResponse of(
                Long componentId,
                int displayOrder,
                Long viewExtensionId,
                String componentType,
                String componentName,
                DisplayPeriodResponse displayPeriod,
                String displayYn,
                int exposedProducts) {
            return new ComponentResponse(
                    componentId,
                    displayOrder,
                    viewExtensionId,
                    componentType,
                    componentName,
                    displayPeriod,
                    displayYn,
                    exposedProducts);
        }
    }
}
