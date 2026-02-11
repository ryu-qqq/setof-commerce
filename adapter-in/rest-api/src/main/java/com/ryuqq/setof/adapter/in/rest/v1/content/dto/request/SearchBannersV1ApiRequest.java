package com.ryuqq.setof.adapter.in.rest.v1.content.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * SearchBannersV1ApiRequest - 배너 검색 요청 DTO.
 *
 * <p>레거시 BannerFilter 기반 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "배너 검색 요청")
public record SearchBannersV1ApiRequest(
        @Parameter(
                        description = "배너 타입",
                        required = true,
                        example = "CATEGORY",
                        schema =
                                @Schema(
                                        allowableValues = {
                                            "CATEGORY",
                                            "MY_PAGE",
                                            "CART",
                                            "PRODUCT_DETAIL_DESCRIPTION",
                                            "RECOMMEND",
                                            "LOGIN"
                                        }))
                @NotBlank(message = "bannerType은 필수입니다.")
                String bannerType) {}
