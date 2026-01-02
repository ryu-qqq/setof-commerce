package com.ryuqq.setof.application.cart.dto.command;

import java.util.List;
import java.util.UUID;

/**
 * 장바구니 아이템 삭제 Command
 *
 * @param memberId 회원 ID (UUID)
 * @param cartItemIds 삭제할 장바구니 아이템 ID 목록
 */
public record RemoveCartItemCommand(UUID memberId, List<Long> cartItemIds) {}
