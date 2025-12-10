package com.ryuqq.setof.adapter.in.rest.v1.display.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 타이틀 컴포넌트 상세 Response
 *
 * <p>
 * 타이틀 컴포넌트의 상세 정보를 반환하는 응답 DTO입니다.
 *
 * @param componentId 컴포넌트 ID
 * @param componentName 컴포넌트명
 * @param displayOrder 전시 순서
 * @param componentType 컴포넌트 타입
 * @param displayPeriod 전시 기간
 * @param displayYn 전시 여부
 * @param titleComponentId 타이틀 컴포넌트 ID
 * @param title1 타이틀1
 * @param title2 타이틀2
 * @param subTitle1 서브 타이틀1
 * @param subTitle2 서브 타이틀2
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "타이틀 컴포넌트 상세 응답")
public record TitleComponentDetailV1ApiResponse(
        @Schema(description = "컴포넌트 ID", example = "1") Long componentId,
        @Schema(description = "컴포넌트명", example = "타이틀 컴포넌트") String componentName,
        @Schema(description = "전시 순서", example = "1") int displayOrder,
        @Schema(description = "컴포넌트 타입", example = "TITLE") String componentType,
        @Schema(description = "전시 기간") SubComponentV1ApiResponse.DisplayPeriodV1ApiResponse displayPeriod,
        @Schema(description = "전시 여부", example = "Y") String displayYn,
        @Schema(description = "타이틀 컴포넌트 ID", example = "1") Long titleComponentId,
        @Schema(description = "타이틀1", example = "인기 상품") String title1,
        @Schema(description = "타이틀2", example = "NEW") String title2,
        @Schema(description = "서브 타이틀1", example = "지금 바로 확인하세요") String subTitle1,
        @Schema(description = "서브 타이틀2", example = "특가 할인") String subTitle2)
        implements SubComponentV1ApiResponse {
}
