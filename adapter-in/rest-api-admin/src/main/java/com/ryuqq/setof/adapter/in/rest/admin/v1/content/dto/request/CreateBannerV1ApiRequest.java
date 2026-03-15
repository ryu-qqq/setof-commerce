package com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * CreateBannerV1ApiRequest - 배너 그룹 등록/수정 요청 DTO.
 *
 * <p>레거시 CreateBanner 구조를 그대로 유지합니다.
 *
 * <p>POST /api/v1/content/banner — 배너 그룹 등록
 *
 * <p>PUT /api/v1/content/banner/{bannerId} — 배너 그룹 수정
 *
 * @param title 배너 제목
 * @param bannerType 배너 타입
 * @param displayPeriod 전시 기간
 * @param displayYn 전시 여부 ("Y" or "N")
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "배너 그룹 등록/수정 요청")
public record CreateBannerV1ApiRequest(
        @Schema(description = "배너 제목", example = "메인 배너")
                @NotBlank(message = "배너 제목은 필수입니다.")
                @Size(max = 50, message = "배너 제목은 50자 이하여야 합니다.")
                String title,
        @Schema(
                        description = "배너 타입",
                        example = "CATEGORY",
                        allowableValues = {
                            "CATEGORY",
                            "MY_PAGE",
                            "CART",
                            "PRODUCT_DETAIL_DESCRIPTION",
                            "RECOMMEND",
                            "LOGIN"
                        })
                @NotNull(message = "배너 타입은 필수입니다.")
                String bannerType,
        @Schema(description = "전시 기간") @NotNull(message = "전시 기간은 필수입니다.") @Valid
                DisplayPeriodV1ApiRequest displayPeriod,
        @Schema(
                        description = "전시 여부",
                        example = "Y",
                        allowableValues = {"Y", "N"})
                @NotNull(message = "전시 여부는 필수입니다.")
                String displayYn) {}
