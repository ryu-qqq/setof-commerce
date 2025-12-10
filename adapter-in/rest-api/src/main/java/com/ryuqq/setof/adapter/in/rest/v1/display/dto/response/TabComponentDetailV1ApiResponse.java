package com.ryuqq.setof.adapter.in.rest.v1.display.dto.response;

import com.ryuqq.setof.adapter.in.rest.v1.product.dto.response.ProductGroupThumbnailV1ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 탭 컴포넌트 상세 Response
 *
 * <p>탭 컴포넌트의 상세 정보를 반환하는 응답 DTO입니다.
 *
 * @param componentId 컴포넌트 ID
 * @param componentName 컴포넌트명
 * @param displayOrder 전시 순서
 * @param viewExtensionId 뷰 확장 ID
 * @param componentType 컴포넌트 타입
 * @param displayPeriod 전시 기간
 * @param displayYn 전시 여부
 * @param tabComponentId 탭 컴포넌트 ID
 * @param tabDetails 탭 상세 목록
 * @param exposedProducts 노출 상품 수
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "탭 컴포넌트 상세 응답")
public record TabComponentDetailV1ApiResponse(
        @Schema(description = "컴포넌트 ID", example = "1") Long componentId,
        @Schema(description = "컴포넌트명", example = "탭 컴포넌트") String componentName,
        @Schema(description = "전시 순서", example = "1") int displayOrder,
        @Schema(description = "뷰 확장 ID", example = "100") Long viewExtensionId,
        @Schema(description = "컴포넌트 타입", example = "TAB") String componentType,
        @Schema(description = "전시 기간")
                SubComponentV1ApiResponse.DisplayPeriodV1ApiResponse displayPeriod,
        @Schema(description = "전시 여부", example = "Y") String displayYn,
        @Schema(description = "탭 컴포넌트 ID", example = "1") Long tabComponentId,
        @Schema(description = "탭 상세 목록") List<TabDetailV1ApiResponse> tabDetails,
        @Schema(description = "노출 상품 수", example = "10") int exposedProducts)
        implements SubComponentV1ApiResponse {

    @Schema(description = "탭 상세 응답")
    public record TabDetailV1ApiResponse(
            @Schema(description = "탭 ID", example = "1") Long tabId,
            @Schema(description = "탭명", example = "인기 상품") String tabName,
            @Schema(description = "상품 그룹 썸네일 목록")
                    List<ProductGroupThumbnailV1ApiResponse> productGroupThumbnails) {}
}
