package com.ryuqq.setof.domain.cart.vo;

import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 장바구니 아이템 Diff Value Object.
 *
 * <p>기존 아이템과 신규 아이템의 차이를 나타냅니다. ProductDiff 패턴을 따릅니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record CartItemDiff(List<CartItem> added, List<CartItem> updated, Instant occurredAt) {

    public static CartItemDiff of(
            List<CartItem> added, List<CartItem> updated, Instant occurredAt) {
        return new CartItemDiff(
                Collections.unmodifiableList(added),
                Collections.unmodifiableList(updated),
                occurredAt);
    }

    public List<CartItem> allItems() {
        List<CartItem> all = new ArrayList<>(added.size() + updated.size());
        all.addAll(updated);
        all.addAll(added);
        return Collections.unmodifiableList(all);
    }

    public boolean hasNoChanges() {
        return added.isEmpty() && updated.isEmpty();
    }
}
