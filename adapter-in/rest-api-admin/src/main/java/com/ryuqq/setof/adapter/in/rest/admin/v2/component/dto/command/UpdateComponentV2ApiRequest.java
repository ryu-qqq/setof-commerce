package com.ryuqq.setof.adapter.in.rest.admin.v2.component.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.time.Instant;

/**
 * Component 수정 요청 DTO
 *
 * @param componentName 컴포넌트 이름 (nullable)
 * @param displayOrder 노출 순서
 * @param exposedProducts 노출 상품 수
 * @param displayStartDate 노출 시작일시 (nullable)
 * @param displayEndDate 노출 종료일시 (nullable)
 * @param detail 타입별 상세 정보
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "컴포넌트 수정 요청")
public record UpdateComponentV2ApiRequest(
        @Schema(description = "컴포넌트 이름", example = "인기 상품") String componentName,
        @Schema(description = "노출 순서", example = "1") Integer displayOrder,
        @Schema(description = "노출 상품 수", example = "10") Integer exposedProducts,
        @Schema(description = "노출 시작일시") Instant displayStartDate,
        @Schema(description = "노출 종료일시") Instant displayEndDate,
        @Schema(description = "타입별 상세 정보") @Valid ComponentDetailV2ApiRequest detail) {}
