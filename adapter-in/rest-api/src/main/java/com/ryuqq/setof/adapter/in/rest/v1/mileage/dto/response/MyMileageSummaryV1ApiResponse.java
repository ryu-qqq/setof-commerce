package com.ryuqq.setof.adapter.in.rest.v1.mileage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "내 마일리지 요약 정보")
public record MyMileageSummaryV1ApiResponse(
   @Schema(description = "유저 ID", example = "1") String userId,
   @Schema(description = "현재 마일리지 금액", example = "1") Double currentMileage,
   @Schema(description = "예상 마일리지 적립 금액", example = "1") Double expectedSaveMileage,
   @Schema(description = "만료 예상 마일리지 금액", example = "1") Double expectedExpireMileage
) {}
