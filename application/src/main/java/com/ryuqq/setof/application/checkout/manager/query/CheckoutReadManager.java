package com.ryuqq.setof.application.checkout.manager.query;

import com.ryuqq.setof.application.checkout.port.out.query.CheckoutQueryPort;
import com.ryuqq.setof.domain.checkout.aggregate.Checkout;
import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * Checkout Read Manager
 *
 * <p>Checkout 조회 관리자
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CheckoutReadManager {

    private final CheckoutQueryPort checkoutQueryPort;

    public CheckoutReadManager(CheckoutQueryPort checkoutQueryPort) {
        this.checkoutQueryPort = checkoutQueryPort;
    }

    /**
     * CheckoutId 문자열로 Checkout 조회
     *
     * @param checkoutIdString 체크아웃 ID 문자열 (UUID)
     * @return Checkout Aggregate
     */
    public Checkout findById(String checkoutIdString) {
        CheckoutId checkoutId = CheckoutId.of(UUID.fromString(checkoutIdString));
        return checkoutQueryPort.getById(checkoutId);
    }

    /**
     * CheckoutId로 Checkout 조회
     *
     * @param checkoutId 체크아웃 ID
     * @return Checkout Aggregate
     */
    public Checkout findById(CheckoutId checkoutId) {
        return checkoutQueryPort.getById(checkoutId);
    }
}
