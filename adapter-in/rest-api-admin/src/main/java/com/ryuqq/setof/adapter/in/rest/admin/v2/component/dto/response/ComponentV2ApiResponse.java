package com.ryuqq.setof.adapter.in.rest.admin.v2.component.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * Component 응답 DTO
 *
 * @param componentId 컴포넌트 ID
 * @param contentId 소속 Content ID
 * @param componentType 컴포넌트 타입
 * @param componentName 컴포넌트 이름
 * @param displayOrder 노출 순서
 * @param status 상태
 * @param exposedProducts 노출 상품 수
 * @param displayStartDate 노출 시작일시
 * @param displayEndDate 노출 종료일시
 * @param detail 타입별 상세 정보
 * @param createdAt 생성일시
 * @param updatedAt 수정일시
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "컴포넌트 응답")
public record ComponentV2ApiResponse(
        @Schema(description = "컴포넌트 ID", example = "1") Long componentId,
        @Schema(description = "컨텐츠 ID", example = "1") Long contentId,
        @Schema(description = "컴포넌트 타입", example = "PRODUCT") String componentType,
        @Schema(description = "컴포넌트 이름", example = "인기 상품") String componentName,
        @Schema(description = "노출 순서", example = "1") int displayOrder,
        @Schema(description = "상태", example = "ACTIVE") String status,
        @Schema(description = "노출 상품 수", example = "10") int exposedProducts,
        @Schema(description = "노출 시작일시") Instant displayStartDate,
        @Schema(description = "노출 종료일시") Instant displayEndDate,
        @Schema(description = "타입별 상세 정보") ComponentDetailV2ApiResponse detail,
        @Schema(description = "생성일시") Instant createdAt,
        @Schema(description = "수정일시") Instant updatedAt) {}
