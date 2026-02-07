package com.ryuqq.setof.adapter.in.rest.v1.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * OrderSliceV1ApiResponse - 주문 목록 슬라이스 응답 DTO.
 *
 * <p>레거시 OrderSlice 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>커서 페이징 방식의 응답으로, 주문 목록과 함께 상태별 건수를 포함합니다.
 *
 * @param content 주문 목록
 * @param hasNext 다음 페이지 존재 여부
 * @param lastOrderId 마지막 주문 ID (다음 페이지 조회 시 커서로 사용)
 * @param orderCounts 상태별 건수 목록
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.order.dto.slice.OrderSlice
 */
@Schema(description = "주문 목록 슬라이스 응답 (커서 페이징)")
public record OrderSliceV1ApiResponse(
        @Schema(description = "주문 목록") List<OrderV1ApiResponse> content,
        @Schema(description = "다음 페이지 존재 여부", example = "true") boolean hasNext,
        @Schema(description = "마지막 주문 ID (다음 페이지 조회 시 커서로 사용)", example = "67890") Long lastOrderId,
        @Schema(description = "상태별 건수 목록") List<OrderCountV1ApiResponse> orderCounts) {}
