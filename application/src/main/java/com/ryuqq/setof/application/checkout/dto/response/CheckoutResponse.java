package com.ryuqq.setof.application.checkout.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * 체크아웃 응답 DTO
 *
 * @param checkoutId 체크아웃 ID (UUID)
 * @param paymentId 결제 ID (UUID) - Checkout과 함께 생성됨
 * @param memberId 회원 ID (UUIDv7 String)
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
 * @since 1.0.0
 */
public record CheckoutResponse(
        String checkoutId,
        String paymentId,
        String memberId,
        String status,
        List<CheckoutItemResponse> items,
        String receiverName,
        String receiverPhone,
        String zipCode,
        String address,
        String addressDetail,
        BigDecimal totalAmount,
        Instant createdAt,
        Instant expiredAt) {}
