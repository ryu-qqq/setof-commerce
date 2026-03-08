package com.ryuqq.setof.application.cart.validator;

import com.ryuqq.setof.application.cart.manager.CartReadManager;
import com.ryuqq.setof.application.cart.manager.CartStockReadManager;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import com.ryuqq.setof.domain.cart.exception.CartItemNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * CartValidator - 장바구니 검증.
 *
 * <p>ReadManager를 통해 장바구니 아이템을 조회하고 존재 여부를 검증합니다. 재고 충분 여부도 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class CartValidator {

    private final CartReadManager readManager;
    private final CartStockReadManager stockReadManager;

    public CartValidator(CartReadManager readManager, CartStockReadManager stockReadManager) {
        this.readManager = readManager;
        this.stockReadManager = stockReadManager;
    }

    /**
     * 단건 장바구니 아이템 조회 및 존재 검증.
     *
     * @throws CartItemNotFoundException 아이템이 존재하지 않을 경우
     */
    public CartItem getExistingCartItem(Long cartId, Long userId) {
        return readManager.getByCartIdAndUserId(cartId, userId);
    }

    /**
     * 복수 장바구니 아이템 조회 및 전체 존재 검증.
     *
     * <p>요청된 cartIds가 모두 조회되는지 확인합니다. 누락된 ID가 있으면 첫 번째 누락 ID로 예외를 던집니다.
     *
     * @throws CartItemNotFoundException 일부 아이템이 존재하지 않을 경우
     */
    public List<CartItem> getExistingCartItems(List<Long> cartIds, Long userId) {
        List<CartItem> found = readManager.findByCartIdsAndUserId(cartIds, userId);

        if (found.size() != cartIds.size()) {
            Set<Long> foundIds = found.stream().map(CartItem::idValue).collect(Collectors.toSet());
            Long missingId =
                    cartIds.stream().filter(id -> !foundIds.contains(id)).findFirst().orElse(null);
            throw new CartItemNotFoundException(missingId);
        }

        return found;
    }

    /**
     * 상품 재고 충분 여부 검증.
     *
     * @param productId 상품(SKU) ID
     * @param requestedQuantity 요청 수량
     * @throws com.ryuqq.setof.domain.cart.exception.InsufficientStockException 재고 부족 시
     */
    public void validateStockAvailability(long productId, int requestedQuantity) {
        stockReadManager.validateStockAvailability(productId, requestedQuantity);
    }
}
