package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * SellerV1ApiResponse - V1 셀러 목록 아이템 응답 DTO.
 *
 * <p>레거시 SellerResponse 구조와 호환되는 V1 응답 형태입니다.
 *
 * <p>레거시 필드 중 새 구조에 없는 필드:
 *
 * <ul>
 *   <li>commissionRate: null (정산 모듈 분리 예정)
 *   <li>approvalStatus: null (새 도메인에서 미구현)
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "V1 셀러 목록 아이템 응답")
public record SellerV1ApiResponse(
        @Schema(description = "셀러 ID", example = "1") long sellerId,
        @Schema(description = "셀러명", example = "테스트 셀러") String sellerName,
        @Schema(description = "수수료율 (미지원)", example = "null") Double commissionRate,
        @Schema(description = "승인 상태 (미지원)", example = "null") String approvalStatus,
        @Schema(description = "고객센터 전화번호", example = "02-1234-5678") String csPhoneNumber,
        @Schema(description = "고객센터 이메일", example = "cs@example.com") String csEmail,
        @Schema(description = "등록일", example = "2024-01-01 10:30:00") String insertDate) {}
