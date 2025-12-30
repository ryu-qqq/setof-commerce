package com.ryuqq.setof.adapter.in.rest.v2.checkout.dto.response;

import com.ryuqq.setof.application.checkout.dto.response.CheckoutResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * 체크아웃 API 응답 DTO
 *
 * @param checkoutId 체크아웃 ID (UUID)
 * @param paymentId 결제 ID (UUID)
 * @param memberId 회원 ID (UUIDv7)
 * @param status 체크아웃 상태
 * @param items 체크아웃 아이템 목록
 * @param receiverName 수령인 이름
 * @param receiverPhone 수령인 연락처
 * @param zipCode 우편번호
 * @param address 주소
 * @param addressDetail 상세주소
 * @param totalAmount 총 금액
 * @param createdAt 생성 시각
 * @param expiredAt 만료 시각
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "체크아웃 응답")
public record CheckoutV2ApiResponse(
        @Schema(description = "체크아웃 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
                String checkoutId,
        @Schema(description = "결제 ID (UUID)", example = "660e8400-e29b-41d4-a716-446655440001")
                String paymentId,
        @Schema(description = "회원 ID (UUIDv7)", example = "01912345-6789-7abc-def0-123456789abc")
                String memberId,
        @Schema(description = "체크아웃 상태", example = "PENDING") String status,
        @Schema(description = "체크아웃 아이템 목록") List<CheckoutItemV2ApiResponse> items,
        @Schema(description = "수령인 이름", example = "홍길동") String receiverName,
        @Schema(description = "수령인 연락처", example = "010-1234-5678") String receiverPhone,
        @Schema(description = "우편번호", example = "06234") String zipCode,
        @Schema(description = "주소", example = "서울시 강남구 테헤란로") String address,
        @Schema(description = "상세주소", example = "123동 456호") String addressDetail,
        @Schema(description = "총 금액", example = "59800") BigDecimal totalAmount,
        @Schema(description = "생성 시각") Instant createdAt,
        @Schema(description = "만료 시각") Instant expiredAt) {

    /**
     * Application Response → API Response 변환
     *
     * @param response Application 응답
     * @return API 응답
     */
    public static CheckoutV2ApiResponse from(CheckoutResponse response) {
        List<CheckoutItemV2ApiResponse> itemResponses =
                response.items() != null
                        ? response.items().stream().map(CheckoutItemV2ApiResponse::from).toList()
                        : List.of();

        return new CheckoutV2ApiResponse(
                response.checkoutId(),
                response.paymentId(),
                response.memberId(),
                response.status(),
                itemResponses,
                response.receiverName(),
                response.receiverPhone(),
                response.zipCode(),
                response.address(),
                response.addressDetail(),
                response.totalAmount(),
                response.createdAt(),
                response.expiredAt());
    }
}
