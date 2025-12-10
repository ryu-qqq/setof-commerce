package com.ryuqq.setof.application.shippingaddress.dto.command;

import java.util.UUID;

/**
 * 기본 배송지 설정 Command DTO
 *
 * @param memberId 회원 ID (소유권 검증용)
 * @param shippingAddressId 배송지 ID
 */
public record SetDefaultShippingAddressCommand(UUID memberId, Long shippingAddressId) {

    /** Static Factory Method */
    public static SetDefaultShippingAddressCommand of(UUID memberId, Long shippingAddressId) {
        return new SetDefaultShippingAddressCommand(memberId, shippingAddressId);
    }
}
