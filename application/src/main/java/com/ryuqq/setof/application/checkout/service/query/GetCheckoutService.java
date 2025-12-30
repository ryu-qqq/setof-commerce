package com.ryuqq.setof.application.checkout.service.query;

import com.ryuqq.setof.application.checkout.assembler.CheckoutAssembler;
import com.ryuqq.setof.application.checkout.dto.response.CheckoutResponse;
import com.ryuqq.setof.application.checkout.manager.query.CheckoutReadManager;
import com.ryuqq.setof.application.checkout.port.in.query.GetCheckoutUseCase;
import com.ryuqq.setof.application.payment.manager.query.PaymentReadManager;
import com.ryuqq.setof.domain.checkout.aggregate.Checkout;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 체크아웃 조회 Service
 *
 * <p>체크아웃 정보를 조회합니다
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class GetCheckoutService implements GetCheckoutUseCase {

    private final CheckoutReadManager checkoutReadManager;
    private final PaymentReadManager paymentReadManager;
    private final CheckoutAssembler checkoutAssembler;

    public GetCheckoutService(
            CheckoutReadManager checkoutReadManager,
            PaymentReadManager paymentReadManager,
            CheckoutAssembler checkoutAssembler) {
        this.checkoutReadManager = checkoutReadManager;
        this.paymentReadManager = paymentReadManager;
        this.checkoutAssembler = checkoutAssembler;
    }

    /**
     * 체크아웃 조회
     *
     * @param checkoutId 체크아웃 ID (UUID String)
     * @return 체크아웃 응답
     */
    @Override
    @Transactional(readOnly = true)
    public CheckoutResponse getCheckout(String checkoutId) {
        Checkout checkout = checkoutReadManager.findById(checkoutId);
        Payment payment = paymentReadManager.findByCheckoutId(checkoutId);
        return checkoutAssembler.toResponse(checkout, payment);
    }
}
