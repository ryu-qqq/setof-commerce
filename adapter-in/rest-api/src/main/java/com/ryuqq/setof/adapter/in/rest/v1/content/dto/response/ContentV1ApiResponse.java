package com.ryuqq.setof.adapter.in.rest.v1.content.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * ContentV1ApiResponse - 콘텐츠 응답 DTO.
 *
 * <p>레거시 ContentGroupResponse 기반 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "콘텐츠 응답")
public record ContentV1ApiResponse(
        @Schema(description = "콘텐츠 ID", example = "1") long contentId,
        @Schema(description = "전시 기간") DisplayPeriodResponse displayPeriod,
        @Schema(description = "콘텐츠 제목", example = "메인 콘텐츠") String title,
        @Schema(description = "메모", example = "메인 페이지용 콘텐츠") String memo,
        @Schema(description = "이미지 URL", example = "https://cdn.example.com/content/1.jpg")
                String imageUrl,
        @Schema(description = "컴포넌트 상세 목록") List<ComponentDetailV1ApiResponse> componentDetails) {

    /** DisplayPeriodResponse - 전시 기간 응답 DTO. */
    @Schema(description = "전시 기간")
    public record DisplayPeriodResponse(
            @Schema(description = "전시 시작일", example = "2026-01-01 00:00:00")
                    String displayStartDate,
            @Schema(description = "전시 종료일", example = "2026-12-31 23:59:59")
                    String displayEndDate) {}

    /**
     * ComponentDetailV1ApiResponse - 컴포넌트 상세 응답 DTO.
     *
     * <p>레거시 호환: flat 상위 필드 + 타입별 중첩 component 객체.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "컴포넌트 상세")
    public record ComponentDetailV1ApiResponse(
            @Schema(description = "컴포넌트 ID") long componentId,
            @Schema(description = "컴포넌트 이름") String componentName,
            @Schema(description = "컴포넌트 타입") String componentType,
            @Schema(description = "리스트 타입") String listType,
            @Schema(description = "정렬 타입") String orderType,
            @Schema(description = "뱃지 타입") String badgeType,
            @Schema(description = "필터 여부") String filterYn,
            @Schema(description = "전시 여부") String displayYn,
            @Schema(description = "컴포넌트 데이터 (타입별 상이)") Object component,
            @Schema(description = "뷰 확장 상세") Object viewExtensionDetails) {}
}
