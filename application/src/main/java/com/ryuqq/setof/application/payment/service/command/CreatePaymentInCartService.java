package com.ryuqq.setof.application.payment.service.command;

import com.ryuqq.setof.application.payment.dto.bundle.CartPaymentCreationBundle;
import com.ryuqq.setof.application.payment.dto.bundle.PaymentCreationBundle;
import com.ryuqq.setof.application.payment.dto.command.CreatePaymentInCartCommand;
import com.ryuqq.setof.application.payment.dto.response.PaymentGatewayResult;
import com.ryuqq.setof.application.payment.factory.PaymentCommandFactory;
import com.ryuqq.setof.application.payment.internal.CartPaymentPersistFacade;
import com.ryuqq.setof.application.payment.internal.PaymentCreationCoordinator;
import com.ryuqq.setof.application.payment.internal.PaymentPersistFacade.PaymentPersistResult;
import com.ryuqq.setof.application.payment.port.in.command.CreatePaymentInCartUseCase;
import org.springframework.stereotype.Service;

/**
 * 장바구니 구매 결제 준비 서비스.
 *
 * <p>Factory(카트 번들 생성) → Coordinator(검증 + 재고 차감) → CartPaymentPersistFacade(영속화 + 카트 삭제) → 결과 반환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class CreatePaymentInCartService implements CreatePaymentInCartUseCase {

    private final PaymentCommandFactory paymentCommandFactory;
    private final PaymentCreationCoordinator paymentCreationCoordinator;
    private final CartPaymentPersistFacade cartPaymentPersistFacade;

    public CreatePaymentInCartService(
            PaymentCommandFactory paymentCommandFactory,
            PaymentCreationCoordinator paymentCreationCoordinator,
            CartPaymentPersistFacade cartPaymentPersistFacade) {
        this.paymentCommandFactory = paymentCommandFactory;
        this.paymentCreationCoordinator = paymentCreationCoordinator;
        this.cartPaymentPersistFacade = cartPaymentPersistFacade;
    }

    @Override
    public PaymentGatewayResult execute(CreatePaymentInCartCommand command) {
        CartPaymentCreationBundle cartBundle = paymentCommandFactory.createForCart(command);
        PaymentCreationBundle paymentBundle = cartBundle.paymentBundle();

        paymentCreationCoordinator.validateAndDeductStock(
                paymentBundle.payment(),
                paymentBundle.order(),
                paymentBundle.stockDeductionItems());

        PaymentPersistResult result = cartPaymentPersistFacade.persist(cartBundle);

        return new PaymentGatewayResult(
                result.paymentUniqueId(), result.paymentId(), result.orderIds(), 0L);
    }
}
