package com.ryuqq.setof.application.shippingaddress.dto.command;

/**
 * 배송지 수정 Command DTO.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record UpdateShippingAddressCommand(
        Long userId,
        Long shippingAddressId,
        String receiverName,
        String shippingAddressName,
        String addressLine1,
        String addressLine2,
        String zipCode,
        String country,
        String deliveryRequest,
        String phoneNumber,
        boolean defaultAddress) {}
