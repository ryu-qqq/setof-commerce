package com.ryuqq.setof.application.checkout.dto.command;

import java.util.List;

/**
 * 체크아웃 생성 Command
 *
 * @param idempotencyKey 멱등성 키 (프론트엔드에서 생성)
 * @param memberId 회원 ID (UUIDv7 String)
 * @param items 체크아웃 아이템 목록
 * @param pgProvider PG사 (예: TOSS, KAKAO, NICE)
 * @param paymentMethod 결제 수단 (예: CARD, KAKAO_PAY, TOSS_PAY)
 * @param receiverName 수령인 이름
 * @param receiverPhone 수령인 연락처
 * @param zipCode 우편번호
 * @param address 주소
 * @param addressDetail 상세주소
 * @param deliveryRequest 배송 요청사항
 * @author development-team
 * @since 1.0.0
 */
public record CreateCheckoutCommand(
        String idempotencyKey,
        String memberId,
        List<CreateCheckoutItemCommand> items,
        String pgProvider,
        String paymentMethod,
        String receiverName,
        String receiverPhone,
        String zipCode,
        String address,
        String addressDetail,
        String deliveryRequest) {}
