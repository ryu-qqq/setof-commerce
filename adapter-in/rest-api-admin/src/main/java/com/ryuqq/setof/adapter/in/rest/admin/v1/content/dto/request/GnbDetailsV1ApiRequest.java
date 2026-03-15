package com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * GnbDetailsV1ApiRequest - GNB 상세 정보 요청 DTO.
 *
 * <p>레거시 GnbDetails 형식을 그대로 유지합니다.
 *
 * @param title 메뉴 제목
 * @param linkUrl 이동 URL
 * @param displayOrder 노출 순서
 * @param displayPeriod 전시 기간
 * @param displayYn 전시 여부 (Y/N)
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "GNB 상세 정보 요청")
public record GnbDetailsV1ApiRequest(
        @Schema(description = "메뉴 제목", example = "홈") @NotBlank(message = "메뉴 제목은 필수입니다.")
                String title,
        @Schema(description = "이동 URL", example = "/") @NotBlank(message = "링크 URL은 필수입니다.")
                String linkUrl,
        @Schema(description = "노출 순서", example = "1") int displayOrder,
        @Schema(description = "전시 기간") @NotNull(message = "전시 기간은 필수입니다.") @Valid
                DisplayPeriodV1ApiRequest displayPeriod,
        @Schema(
                        description = "전시 여부",
                        example = "Y",
                        allowableValues = {"Y", "N"})
                @NotNull(message = "전시 여부는 필수입니다.")
                String displayYn) {}
