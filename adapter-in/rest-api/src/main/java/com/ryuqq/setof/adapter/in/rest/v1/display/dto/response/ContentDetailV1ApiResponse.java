package com.ryuqq.setof.adapter.in.rest.v1.display.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

@Schema(description = "Content 상세 정보 응답")
public record ContentDetailV1ApiResponse(

    @Schema(description = "컨테츠 아이디", example = "1") long contentId,
    @Schema(description = "전시 기간") ContentDisplayPeriodV1Response displayPeriod,
    @Schema(description = "컨텐츠 제목", example = "인기 상품") String title,
    @Schema(description = "컨텐츠 메모", example = "메모") String memo,
    @Schema(description = "컨텐츠 이미지 Url", example = "/products/group/12345") String imageUrl,
    @Schema(description = "컨텐츠 하위 컴포넌트 리스트") List<ComponentDetailV1ApiResponse> componentDetails
) {

    @Schema(description = "Content 전시 기간 응답")
    public record ContentDisplayPeriodV1Response(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(description = "전시 시작 기간", example = "2024-12-30 00:00:00") LocalDateTime displayStartDate,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(description = "전시 종료 기간", example = "2025-12-30 00:00:00") LocalDateTime displayEndDate
    ){}

    @Schema(description = "Component 상세 정보 응답")
    public record ComponentDetailV1ApiResponse(
        @Schema(description = "컴포넌트 ID", example = "1") long componentId,
        @Schema(description = "컴포넌트 제목", example = "인기 상품") String componentName,
        @Schema(description = "컴포넌트 유형", example = "TITLE")  String componentType,
        @Schema(description = "전시 정렬 유형", example = "ONE_STEP") String listType,
        @Schema(description = "정렬 유형", example = "PRODUCT_LIST") String orderType,
        @Schema(description = "뱃지 유형", example = "PRODUCT_LIST") String badgeType,
        @Schema(description = "필터 여부", example = "PRODUCT_LIST") String filterYn,
        @Schema(description = "전시 여부", example = "PRODUCT_LIST") String displayYn,
        @Schema(description = "서브 컴포넌트 유형") SubComponentV1ApiResponse component,
        @Schema(description = "더보기 페이지 상세 정보") ViewExtensionDetailV1ApiResponse viewExtensionDetails
    ){}

    @Schema(description = "더보기 페이지 상세 정보 응답")
    public record ViewExtensionDetailV1ApiResponse(
        @Schema(description = "더보기 페이지 유형", example = "PAGE") String viewExtensionType,
        @Schema(description = "링크 URL", example = "/products/group/12345") String linkUrl,
        @Schema(description = "버튼 명", example = "더보기") String buttonName,
        @Schema(description = "상품 클릭", example = "5") int productCountPerClick,
        @Schema(description = "버튼 클릭 최대 횟수", example = "5") int maxClickCount,
        @Schema(description = "버튼 클릭 액션 유형", example = "PAGE") String afterMaxActionType,
        @Schema(description = "버튼 클릭 후 링크 Url", example = "/products/group/12345") String afterMaxActionLinkUrl
    ){}


}
