package com.ryuqq.setof.application.payment.service.command;

import com.ryuqq.setof.application.payment.dto.bundle.PaymentCreationBundle;
import com.ryuqq.setof.application.payment.dto.command.CreatePaymentCommand;
import com.ryuqq.setof.application.payment.dto.response.PaymentGatewayResult;
import com.ryuqq.setof.application.payment.factory.PaymentCommandFactory;
import com.ryuqq.setof.application.payment.internal.PaymentCreationCoordinator;
import com.ryuqq.setof.application.payment.internal.PaymentPersistFacade;
import com.ryuqq.setof.application.payment.internal.PaymentPersistFacade.PaymentPersistResult;
import com.ryuqq.setof.application.payment.port.in.command.CreatePaymentUseCase;
import org.springframework.stereotype.Service;

/**
 * 직접 구매 결제 준비 서비스.
 *
 * <p>Factory(번들 생성) → Coordinator(검증 + 재고 차감) → Facade(영속화) → 결과 반환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class CreatePaymentService implements CreatePaymentUseCase {

    private final PaymentCommandFactory paymentCommandFactory;
    private final PaymentCreationCoordinator paymentCreationCoordinator;
    private final PaymentPersistFacade paymentPersistFacade;

    public CreatePaymentService(
            PaymentCommandFactory paymentCommandFactory,
            PaymentCreationCoordinator paymentCreationCoordinator,
            PaymentPersistFacade paymentPersistFacade) {
        this.paymentCommandFactory = paymentCommandFactory;
        this.paymentCreationCoordinator = paymentCreationCoordinator;
        this.paymentPersistFacade = paymentPersistFacade;
    }

    @Override
    public PaymentGatewayResult execute(CreatePaymentCommand command) {
        PaymentCreationBundle bundle = paymentCommandFactory.create(command);

        paymentCreationCoordinator.validateAndDeductStock(
                bundle.payment(), bundle.order(), bundle.stockDeductionItems());

        PaymentPersistResult result = paymentPersistFacade.persist(bundle);

        return new PaymentGatewayResult(
                result.paymentUniqueId(), result.paymentId(), result.orderIds(), 0L);
    }
}
