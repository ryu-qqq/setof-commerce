package com.ryuqq.setof.domain.cart;

import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import com.ryuqq.setof.domain.cart.vo.CartItemDiff;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 장바구니 등록용 도메인 객체.
 *
 * <p>등록 시점의 시각과 장바구니 아이템 목록을 캡슐화합니다. 조회용이 아닌 등록/변경 커맨드 전달 목적입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class Cart {

    private final String memberId;
    private final Long userId;
    private final Instant occurredAt;
    private final List<CartItem> items;

    private Cart(String memberId, Long userId, Instant occurredAt, List<CartItem> items) {
        this.memberId = memberId;
        this.userId = userId;
        this.occurredAt = occurredAt;
        this.items = List.copyOf(items);
    }

    public static Cart of(String memberId, Long userId, Instant occurredAt, List<CartItem> items) {
        return new Cart(memberId, userId, occurredAt, items);
    }

    /**
     * 기존 아이템과 비교하여 Diff를 생성합니다.
     *
     * <p>productId 기준으로 매칭하여 기존 항목이면 수량 증가(updated), 없으면 신규(added)로 분류합니다.
     *
     * @param existingItems 기존 장바구니 아이템 목록
     * @return CartItemDiff (added + updated)
     */
    public CartItemDiff diff(List<CartItem> existingItems) {
        Map<Long, CartItem> existingByProductId =
                existingItems.stream()
                        .collect(Collectors.toMap(CartItem::productIdValue, item -> item));

        List<CartItem> added = new ArrayList<>();
        List<CartItem> updated = new ArrayList<>();

        for (CartItem newItem : items) {
            CartItem existing = existingByProductId.get(newItem.productIdValue());
            if (existing != null) {
                existing.increaseQuantity(newItem.quantityValue(), occurredAt);
                updated.add(existing);
            } else {
                added.add(newItem);
            }
        }

        return CartItemDiff.of(added, updated, occurredAt);
    }

    public List<Long> productIds() {
        return items.stream().map(CartItem::productIdValue).toList();
    }

    public String memberId() {
        return memberId;
    }

    public Long userId() {
        return userId;
    }

    public Instant occurredAt() {
        return occurredAt;
    }

    public List<CartItem> items() {
        return items;
    }

    public int size() {
        return items.size();
    }
}
