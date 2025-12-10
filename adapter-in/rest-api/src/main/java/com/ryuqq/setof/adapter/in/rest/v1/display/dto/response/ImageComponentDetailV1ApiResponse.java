package com.ryuqq.setof.adapter.in.rest.v1.display.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 이미지 컴포넌트 상세 Response
 *
 * <p>이미지 컴포넌트의 상세 정보를 반환하는 응답 DTO입니다.
 *
 * @param componentId 컴포넌트 ID
 * @param componentName 컴포넌트명
 * @param displayOrder 전시 순서
 * @param componentType 컴포넌트 타입
 * @param displayPeriod 전시 기간
 * @param displayYn 전시 여부
 * @param imageComponentId 이미지 컴포넌트 ID
 * @param imageType 이미지 타입
 * @param imageComponentLinks 이미지 컴포넌트 링크 목록
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "이미지 컴포넌트 상세 응답")
public record ImageComponentDetailV1ApiResponse(
        @Schema(description = "컴포넌트 ID", example = "1") Long componentId,
        @Schema(description = "컴포넌트명", example = "이미지 컴포넌트") String componentName,
        @Schema(description = "전시 순서", example = "1") int displayOrder,
        @Schema(description = "컴포넌트 타입", example = "IMAGE") String componentType,
        @Schema(description = "전시 기간")
                SubComponentV1ApiResponse.DisplayPeriodV1ApiResponse displayPeriod,
        @Schema(description = "전시 여부", example = "Y") String displayYn,
        @Schema(description = "이미지 컴포넌트 ID", example = "1") Long imageComponentId,
        @Schema(description = "이미지 타입", example = "BANNER") String imageType,
        @Schema(description = "이미지 컴포넌트 링크 목록")
                List<ImageComponentLinkV1ApiResponse> imageComponentLinks)
        implements SubComponentV1ApiResponse {

    @Schema(description = "이미지 컴포넌트 링크 응답")
    public record ImageComponentLinkV1ApiResponse(
            @Schema(description = "이미지 URL", example = "https://cdn.example.com/image.jpg")
                    String imageUrl,
            @Schema(description = "링크 URL", example = "/product/123") String linkUrl,
            @Schema(description = "링크 타겟", example = "_blank") String linkTarget) {}
}
