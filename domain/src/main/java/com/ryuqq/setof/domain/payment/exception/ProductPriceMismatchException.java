package com.ryuqq.setof.domain.payment.exception;

/**
 * 상품 가격 불일치 예외.
 *
 * <p>클라이언트가 보낸 주문 금액과 서버가 계산한 금액이 다를 때 발생합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ProductPriceMismatchException extends PaymentException {

    public ProductPriceMismatchException(
            long productGroupId, long clientAmount, long serverCalculated) {
        super(
                PaymentErrorCode.PRODUCT_PRICE_MISMATCH,
                String.format(
                        "상품 %d 가격 불일치: 클라이언트=%d원, 서버=%d원",
                        productGroupId, clientAmount, serverCalculated));
    }
}
