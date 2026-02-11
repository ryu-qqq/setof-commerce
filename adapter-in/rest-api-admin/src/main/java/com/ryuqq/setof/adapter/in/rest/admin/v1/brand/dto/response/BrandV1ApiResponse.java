package com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * BrandV1ApiResponse - 브랜드 응답 DTO.
 *
 * <p>레거시 ExtendedBrandContext 기반 변환.
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>Lombok @Getter → record 기본 접근자
 *   <li>MainDisplayNameType enum → String 타입
 *   <li>@Schema 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.brand.core.ExtendedBrandContext
 */
@Schema(description = "브랜드 응답")
public record BrandV1ApiResponse(
        @Schema(description = "브랜드 ID", example = "1") long brandId,
        @Schema(description = "브랜드명 (시스템 내부 식별용)", example = "Nike") String brandName,
        @Schema(
                        description = "메인 표시 타입 (US: 영문명 우선, KR: 한글명 우선)",
                        example = "US",
                        allowableValues = {"US", "KR"})
                String mainDisplayType,
        @Schema(description = "영문 표시명", example = "Nike") String displayEnglishName,
        @Schema(description = "한글 표시명", example = "나이키") String displayKoreanName) {}
