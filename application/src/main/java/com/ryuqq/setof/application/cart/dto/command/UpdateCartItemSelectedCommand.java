package com.ryuqq.setof.application.cart.dto.command;

import java.util.List;
import java.util.UUID;

/**
 * 장바구니 아이템 선택 상태 변경 Command
 *
 * @param memberId 회원 ID (UUID)
 * @param cartItemIds 장바구니 아이템 ID 목록
 * @param selected 선택 상태
 */
public record UpdateCartItemSelectedCommand(
        UUID memberId, List<Long> cartItemIds, boolean selected) {}
