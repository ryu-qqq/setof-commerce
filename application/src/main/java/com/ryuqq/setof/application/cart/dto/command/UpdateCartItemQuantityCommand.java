package com.ryuqq.setof.application.cart.dto.command;

import java.util.UUID;

/**
 * 장바구니 아이템 수량 변경 Command
 *
 * @param memberId 회원 ID (UUID)
 * @param cartItemId 장바구니 아이템 ID
 * @param quantity 새 수량
 */
public record UpdateCartItemQuantityCommand(UUID memberId, Long cartItemId, int quantity) {}
