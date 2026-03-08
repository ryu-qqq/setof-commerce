package com.ryuqq.setof.application.cart.dto.command;

import java.util.List;

/**
 * 장바구니 항목 삭제 Command DTO.
 *
 * <p>레거시 DELETE /api/v1/carts 기반. 소프트 딜리트(delete_yn = 'Y'). cartIds + userId 소유권 검증 포함.
 *
 * @param cartIds 삭제할 장바구니 항목 ID 목록
 * @param memberId 회원 ID (UUIDv7, SecurityContext에서 추출)
 * @param userId 레거시 사용자 ID (SecurityContext에서 추출)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DeleteCartItemsCommand(List<Long> cartIds, String memberId, Long userId) {

    public static DeleteCartItemsCommand of(List<Long> cartIds, String memberId, Long userId) {
        return new DeleteCartItemsCommand(cartIds, memberId, userId);
    }

    public static DeleteCartItemsCommand ofSingle(Long cartId, String memberId, Long userId) {
        return new DeleteCartItemsCommand(List.of(cartId), memberId, userId);
    }
}
