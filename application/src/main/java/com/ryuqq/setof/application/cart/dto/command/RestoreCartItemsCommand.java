package com.ryuqq.setof.application.cart.dto.command;

import java.util.List;
import java.util.UUID;

/**
 * 장바구니 아이템 복원 Command
 *
 * <p>결제 실패 시 소프트 딜리트된 장바구니 아이템을 복원하기 위한 Command입니다.
 *
 * @param memberId 회원 ID (UUID)
 * @param productStockIds 복원할 상품 재고 ID 목록
 * @author development-team
 * @since 1.0.0
 */
public record RestoreCartItemsCommand(UUID memberId, List<Long> productStockIds) {

    public RestoreCartItemsCommand {
        if (memberId == null) {
            throw new IllegalArgumentException("memberId는 필수입니다");
        }
        if (productStockIds == null || productStockIds.isEmpty()) {
            throw new IllegalArgumentException("productStockIds는 필수입니다");
        }
    }

    /** Static Factory Method */
    public static RestoreCartItemsCommand of(UUID memberId, List<Long> productStockIds) {
        return new RestoreCartItemsCommand(memberId, productStockIds);
    }

    /** memberId가 String인 경우 */
    public static RestoreCartItemsCommand of(String memberIdString, List<Long> productStockIds) {
        return new RestoreCartItemsCommand(UUID.fromString(memberIdString), productStockIds);
    }
}
