package com.ryuqq.setof.adapter.in.rest.v1.display.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 텍스트 컴포넌트 상세 Response
 *
 * <p>텍스트 컴포넌트의 상세 정보를 반환하는 응답 DTO입니다.
 *
 * @param componentId 컴포넌트 ID
 * @param componentName 컴포넌트명
 * @param displayOrder 전시 순서
 * @param componentType 컴포넌트 타입
 * @param displayPeriod 전시 기간
 * @param displayYn 전시 여부
 * @param textComponentId 텍스트 컴포넌트 ID
 * @param content 텍스트 내용
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "텍스트 컴포넌트 상세 응답")
public record TextComponentDetailV1ApiResponse(
        @Schema(description = "컴포넌트 ID", example = "1") Long componentId,
        @Schema(description = "컴포넌트명", example = "텍스트 컴포넌트") String componentName,
        @Schema(description = "전시 순서", example = "1") int displayOrder,
        @Schema(description = "컴포넌트 타입", example = "TEXT") String componentType,
        @Schema(description = "전시 기간")
                SubComponentV1ApiResponse.DisplayPeriodV1ApiResponse displayPeriod,
        @Schema(description = "전시 여부", example = "Y") String displayYn,
        @Schema(description = "텍스트 컴포넌트 ID", example = "1") Long textComponentId,
        @Schema(description = "텍스트 내용", example = "안녕하세요") String content)
        implements SubComponentV1ApiResponse {}
