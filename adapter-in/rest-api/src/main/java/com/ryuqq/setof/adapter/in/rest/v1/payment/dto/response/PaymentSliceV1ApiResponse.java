package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * PaymentSliceV1ApiResponse - 결제 목록 슬라이스 응답 DTO (커서 페이징).
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>커서 기반 무한 스크롤 페이징을 위한 슬라이스 응답 래퍼입니다.
 *
 * @param content 결제 목록
 * @param hasNext 다음 페이지 존재 여부
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "결제 목록 슬라이스 응답 (커서 페이징)")
public record PaymentSliceV1ApiResponse(
        @Schema(description = "결제 목록") List<PaymentV1ApiResponse> content,
        @Schema(description = "다음 페이지 존재 여부", example = "true") boolean hasNext) {}
