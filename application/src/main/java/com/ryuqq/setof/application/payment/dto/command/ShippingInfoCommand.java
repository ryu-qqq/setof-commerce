package com.ryuqq.setof.application.payment.dto.command;

/**
 * 배송지 정보 커맨드.
 *
 * @param receiverName 수령인명
 * @param receiverPhoneNumber 수령인 전화번호
 * @param zipCode 우편번호
 * @param address 주소
 * @param addressDetail 상세주소
 * @param deliveryMessage 배송 메시지
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ShippingInfoCommand(
        String receiverName,
        String receiverPhoneNumber,
        String zipCode,
        String address,
        String addressDetail,
        String deliveryMessage) {}
