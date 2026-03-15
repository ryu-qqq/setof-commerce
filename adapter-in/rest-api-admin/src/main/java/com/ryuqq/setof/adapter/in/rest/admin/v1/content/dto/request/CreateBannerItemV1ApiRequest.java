package com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * CreateBannerItemV1ApiRequest - 배너 슬라이드 등록/수정 요청 DTO.
 *
 * <p>레거시 CreateBannerItem 구조를 그대로 유지합니다.
 *
 * <p>POST /api/v1/content/banner/items — 슬라이드 일괄 등록/수정/삭제
 *
 * <p>변환 규칙:
 *
 * <ul>
 *   <li>bannerItemId가 null → 신규 생성
 *   <li>bannerItemId가 값 있음 → 기존 수정
 *   <li>기존 아이템이 요청에 없으면 → 삭제
 * </ul>
 *
 * @param bannerId 배너 그룹 ID
 * @param bannerItemId 배너 아이템 ID (null이면 신규)
 * @param title 슬라이드 제목
 * @param imageUrl 이미지 URL
 * @param linkUrl 링크 URL
 * @param displayPeriod 전시 기간
 * @param displayOrder 전시 순서
 * @param displayYn 전시 여부 ("Y" or "N")
 * @param width 이미지 너비
 * @param height 이미지 높이
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "배너 슬라이드 등록/수정 요청")
public record CreateBannerItemV1ApiRequest(
        @Schema(description = "배너 그룹 ID", example = "1") long bannerId,
        @Schema(description = "배너 아이템 ID (null이면 신규 생성)", example = "5", nullable = true)
                @JsonInclude(JsonInclude.Include.NON_NULL)
                Long bannerItemId,
        @Schema(description = "슬라이드 제목", example = "슬라이드 1")
                @NotBlank(message = "슬라이드 제목은 필수입니다.")
                @Size(max = 50, message = "슬라이드 제목은 50자 이하여야 합니다.")
                String title,
        @Schema(description = "이미지 URL", example = "https://cdn.example.com/img.jpg")
                @NotBlank(message = "이미지 URL은 필수입니다.")
                String imageUrl,
        @Schema(description = "링크 URL", example = "/event/1") String linkUrl,
        @Schema(description = "전시 기간") @NotNull(message = "전시 기간은 필수입니다.") @Valid
                DisplayPeriodV1ApiRequest displayPeriod,
        @Schema(description = "전시 순서", example = "1")
                @Min(value = 1, message = "전시 순서는 1 이상이어야 합니다.")
                @Max(value = 10, message = "전시 순서는 10 이하여야 합니다.")
                int displayOrder,
        @Schema(
                        description = "전시 여부",
                        example = "Y",
                        allowableValues = {"Y", "N"})
                @NotNull(message = "전시 여부는 필수입니다.")
                String displayYn,
        @Schema(description = "이미지 너비", example = "1920.0") Double width,
        @Schema(description = "이미지 높이", example = "600.0") Double height) {}
