package com.ryuqq.setof.application.cart.dto.command;

import java.util.UUID;

/**
 * 장바구니 비우기 Command
 *
 * @param memberId 회원 ID (UUID)
 */
public record ClearCartCommand(UUID memberId) {}
