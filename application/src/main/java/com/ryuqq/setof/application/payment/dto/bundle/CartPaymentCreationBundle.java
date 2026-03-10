package com.ryuqq.setof.application.payment.dto.bundle;

import com.ryuqq.setof.domain.cart.vo.CartCheckoutItem;
import java.util.List;

/**
 * 장바구니 결제 생성 번들.
 *
 * <p>PaymentCreationBundle + 카트 체크아웃 항목을 포함합니다.
 *
 * @param paymentBundle 결제 생성 번들
 * @param cartCheckoutItems 카트 상태 변경 대상 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record CartPaymentCreationBundle(
        PaymentCreationBundle paymentBundle, List<CartCheckoutItem> cartCheckoutItems) {

    public CartPaymentCreationBundle {
        if (paymentBundle == null) {
            throw new IllegalArgumentException(
                    "CartPaymentCreationBundle의 paymentBundle은 null일 수 없습니다");
        }
        if (cartCheckoutItems == null || cartCheckoutItems.isEmpty()) {
            throw new IllegalArgumentException(
                    "CartPaymentCreationBundle의 cartCheckoutItems는 비어있을 수 없습니다");
        }
        cartCheckoutItems = List.copyOf(cartCheckoutItems);
    }
}
