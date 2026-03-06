package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * SellerIdApiResponse - 셀러 ID 응답.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * @param sellerId 생성된 셀러 ID
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "셀러 ID 응답")
public record SellerIdApiResponse(@Schema(description = "셀러 ID", example = "1") Long sellerId) {}
