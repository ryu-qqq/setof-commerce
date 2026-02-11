package com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * BrandSearchV1ApiRequest - 브랜드 검색 요청 DTO.
 *
 * <p>레거시 BrandFilter 기반 변환.
 *
 * <p>No-Offset 페이징과 일반 Offset 페이징 모두 지원.
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>MainDisplayNameType enum → String + @Schema(allowableValues)
 *   <li>Spring Pageable → page, size 필드 내장
 *   <li>미사용 필드(siteId, brandNames) 제거
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.brand.filter.BrandFilter
 */
@Schema(description = "브랜드 검색 요청")
public record BrandSearchV1ApiRequest(
        @Parameter(
                        description = "No-Offset 페이징용 마지막 브랜드 ID. 이 값이 있으면 커서 기반 페이징 사용",
                        example = "100")
                Long lastBrandId,
        @Parameter(description = "브랜드명 검색어 (mainDisplayType에 따라 한글/영문 검색)", example = "Nike")
                String brandName,
        @Parameter(
                        description = "메인 표시 타입. 검색 필드 결정에 사용 (US: 영문명 검색, KR: 한글명 검색)",
                        example = "US",
                        schema =
                                @Schema(
                                        allowableValues = {"US", "KR"},
                                        defaultValue = "US"))
                String mainDisplayType,
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
                Integer page,
        @Parameter(description = "페이지 크기 (1~100)", example = "20")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
                Integer size,
        @Parameter(
                        description = "정렬 방향 (id 기준). No-Offset 페이징 시 커서 방향 결정에 사용",
                        example = "ASC",
                        schema =
                                @Schema(
                                        allowableValues = {"ASC", "DESC"},
                                        defaultValue = "ASC"))
                String sortDirection) {}
