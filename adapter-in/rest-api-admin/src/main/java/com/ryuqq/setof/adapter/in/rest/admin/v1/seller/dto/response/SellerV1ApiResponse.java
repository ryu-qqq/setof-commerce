package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * SellerV1ApiResponse - 셀러 목록 아이템 응답 DTO.
 *
 * <p>Application Layer SellerResult 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>GET /api/v1/sellers - 셀러 목록 조회 응답.
 *
 * @param sellerId 셀러 ID
 * @param sellerName 셀러명
 * @param displayName 표시명
 * @param logoUrl 로고 URL
 * @param description 설명
 * @param active 활성화 여부
 * @param createdAt 생성일시
 * @param updatedAt 수정일시
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "셀러 목록 응답")
public record SellerV1ApiResponse(
        @Schema(description = "셀러 ID", example = "1") long sellerId,
        @Schema(description = "셀러명", example = "나이키코리아") String sellerName,
        @Schema(description = "표시명", example = "나이키 공식스토어") String displayName,
        @Schema(description = "로고 URL", example = "https://cdn.example.com/sellers/nike.png")
                String logoUrl,
        @Schema(description = "설명", example = "나이키 공식 판매처") String description,
        @Schema(description = "활성화 여부", example = "true") boolean active,
        @Schema(description = "생성일시") Instant createdAt,
        @Schema(description = "수정일시") Instant updatedAt) {}
