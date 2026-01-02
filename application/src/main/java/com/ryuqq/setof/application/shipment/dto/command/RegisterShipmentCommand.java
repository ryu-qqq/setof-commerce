package com.ryuqq.setof.application.shipment.dto.command;

/**
 * 운송장 등록 Command DTO
 *
 * @param sellerId 셀러 ID
 * @param checkoutId 결제건 ID
 * @param carrierId 택배사 ID
 * @param invoiceNumber 운송장 번호
 * @param senderName 발송인 이름
 * @param senderPhone 발송인 연락처
 * @param senderAddress 발송지 주소 (nullable)
 * @param type 배송 유형 (nullable - 기본값: NORMAL)
 * @author development-team
 * @since 1.0.0
 */
public record RegisterShipmentCommand(
        Long sellerId,
        Long checkoutId,
        Long carrierId,
        String invoiceNumber,
        String senderName,
        String senderPhone,
        String senderAddress,
        String type) {}
