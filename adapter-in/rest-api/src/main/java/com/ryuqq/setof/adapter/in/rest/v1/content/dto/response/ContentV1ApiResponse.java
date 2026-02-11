package com.ryuqq.setof.adapter.in.rest.v1.content.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
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
                    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    LocalDateTime displayStartDate,
            @Schema(description = "전시 종료일", example = "2026-12-31 23:59:59")
                    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    LocalDateTime displayEndDate) {}

    /**
     * ComponentDetailV1ApiResponse - 컴포넌트 상세 응답 DTO.
     *
     * <p>컴포넌트 타입에 따라 동적 구조를 가짐.
     */
    @Schema(description = "컴포넌트 상세")
    public record ComponentDetailV1ApiResponse(
            @Schema(description = "컴포넌트 ID", example = "1") long componentId,
            @Schema(description = "컴포넌트 이름", example = "메인 배너") String componentName,
            @Schema(description = "컴포넌트 타입", example = "PRODUCT") String componentType,
            @Schema(description = "전시 순서", example = "1") int displayOrder,
            @Schema(description = "컴포넌트 데이터 (타입별 상이)") Object data) {}
}
