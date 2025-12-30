package com.ryuqq.setof.application.cart.dto.command;

import java.util.List;
import java.util.UUID;

/**
 * 장바구니 아이템 소프트 딜리트 Command
 *
 * <p>체크아웃 생성 시 장바구니 아이템을 소프트 딜리트하기 위한 Command입니다.
 *
 * @param memberId 회원 ID (UUID)
 * @param productStockIds 소프트 딜리트할 상품 재고 ID 목록
 * @author development-team
 * @since 1.0.0
 */
public record SoftDeleteCartItemsCommand(UUID memberId, List<Long> productStockIds) {

    public SoftDeleteCartItemsCommand {
        if (memberId == null) {
            throw new IllegalArgumentException("memberId는 필수입니다");
        }
        if (productStockIds == null || productStockIds.isEmpty()) {
            throw new IllegalArgumentException("productStockIds는 필수입니다");
        }
    }

    /** Static Factory Method */
    public static SoftDeleteCartItemsCommand of(UUID memberId, List<Long> productStockIds) {
        return new SoftDeleteCartItemsCommand(memberId, productStockIds);
    }

    /** memberId가 String인 경우 */
    public static SoftDeleteCartItemsCommand of(String memberIdString, List<Long> productStockIds) {
        return new SoftDeleteCartItemsCommand(UUID.fromString(memberIdString), productStockIds);
    }
}
