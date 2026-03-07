package com.ryuqq.setof.application.shippingaddress.dto.command;

/**
 * 배송지 등록 Command DTO.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record RegisterShippingAddressCommand(
        Long userId,
        String receiverName,
        String shippingAddressName,
        String addressLine1,
        String addressLine2,
        String zipCode,
        String country,
        String deliveryRequest,
        String phoneNumber,
        boolean defaultAddress) {}
