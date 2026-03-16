package com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ViewExtensionDetailsV1ApiRequest - 뷰 확장 설정 v1 요청 DTO.
 *
 * <p>레거시 ViewExtensionDetails(Embedded) 형식을 그대로 유지합니다.
 *
 * @param viewExtensionType 뷰 확장 타입
 * @param linkUrl 링크 URL
 * @param buttonName 버튼 이름
 * @param productCountPerClick 클릭당 상품 수
 * @param maxClickCount 최대 클릭 수
 * @param afterMaxActionType 최대 이후 액션 타입
 * @param afterMaxActionLinkUrl 최대 이후 액션 링크 URL
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "뷰 확장 설정")
public record ViewExtensionDetailsV1ApiRequest(
        @Schema(description = "뷰 확장 타입") String viewExtensionType,
        @Schema(description = "링크 URL") String linkUrl,
        @Schema(description = "버튼 이름") String buttonName,
        @Schema(description = "클릭당 상품 수") int productCountPerClick,
        @Schema(description = "최대 클릭 수") int maxClickCount,
        @Schema(description = "최대 이후 액션 타입") String afterMaxActionType,
        @Schema(description = "최대 이후 액션 링크 URL") String afterMaxActionLinkUrl) {}
