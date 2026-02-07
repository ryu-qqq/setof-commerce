package com.ryuqq.setof.adapter.in.rest.v1.mileage.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;

/**
 * SearchMileageHistoriesV1ApiRequest - 마일리지 이력 검색 요청 DTO.
 *
 * <p>레거시 MileageFilter 기반 변환.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-010: Request DTO 조회 네이밍 규칙 (Search*V1ApiRequest - 오프셋 페이징).
 *
 * <p>기본값 처리는 Mapper에서 수행합니다. Request DTO에서는 기본값 설정 금지.
 *
 * @param reasons 마일리지 사유 필터 (SAVE, USE, REFUND, EXPIRED)
 * @param page 페이지 번호 (0부터 시작)
 * @param size 페이지 크기 (1~100)
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.mileage.dto.filter.MileageFilter
 */
@Schema(description = "마일리지 이력 검색 요청")
public record SearchMileageHistoriesV1ApiRequest(
        @Parameter(
                        description = "마일리지 사유 필터 (복수 선택 가능)",
                        example = "[\"SAVE\", \"USE\"]",
                        schema =
                                @Schema(
                                        allowableValues = {"SAVE", "USE", "REFUND", "EXPIRED"},
                                        nullable = true))
                List<String> reasons,
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
                @Schema(description = "페이지 번호", defaultValue = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
                Integer page,
        @Parameter(description = "페이지 크기 (1~100)", example = "20")
                @Schema(description = "페이지 크기", defaultValue = "20")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
                Integer size) {}
