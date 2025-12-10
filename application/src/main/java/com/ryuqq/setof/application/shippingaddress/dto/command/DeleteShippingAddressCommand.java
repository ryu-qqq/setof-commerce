package com.ryuqq.setof.application.shippingaddress.dto.command;

import java.util.UUID;

/**
 * 배송지 삭제 Command DTO
 *
 * @param memberId 회원 ID (소유권 검증용)
 * @param shippingAddressId 배송지 ID
 */
public record DeleteShippingAddressCommand(UUID memberId, Long shippingAddressId) {

    /** Static Factory Method */
    public static DeleteShippingAddressCommand of(UUID memberId, Long shippingAddressId) {
        return new DeleteShippingAddressCommand(memberId, shippingAddressId);
    }
}
