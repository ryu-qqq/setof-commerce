package com.ryuqq.setof.application.shippingaddress.dto.command;

/**
 * 배송지 삭제 Command DTO.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DeleteShippingAddressCommand(Long userId, Long shippingAddressId) {}
