package com.ryuqq.setof.application.payment.service.command;

import com.ryuqq.setof.application.cart.dto.command.RestoreCartItemsCommand;
import com.ryuqq.setof.application.cart.port.in.command.RestoreCartItemsUseCase;
import com.ryuqq.setof.application.checkout.manager.query.CheckoutReadManager;
import com.ryuqq.setof.application.payment.assembler.PaymentAssembler;
import com.ryuqq.setof.application.payment.dto.command.FailPaymentCommand;
import com.ryuqq.setof.application.payment.dto.response.PaymentResponse;
import com.ryuqq.setof.application.payment.manager.command.PaymentPersistenceManager;
import com.ryuqq.setof.application.payment.manager.query.PaymentReadManager;
import com.ryuqq.setof.application.payment.port.in.command.FailPaymentUseCase;
import com.ryuqq.setof.domain.checkout.aggregate.Checkout;
import com.ryuqq.setof.domain.checkout.vo.CheckoutItem;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 결제 실패 처리 Service
 *
 * <p>결제 실패 시:
 *
 * <ul>
 *   <li>Payment 상태를 FAILED로 변경
 *   <li>관련된 장바구니 아이템 복원 (소프트 딜리트 해제)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class FailPaymentService implements FailPaymentUseCase {

    private final PaymentReadManager paymentReadManager;
    private final PaymentPersistenceManager paymentPersistenceManager;
    private final CheckoutReadManager checkoutReadManager;
    private final RestoreCartItemsUseCase restoreCartItemsUseCase;
    private final PaymentAssembler paymentAssembler;
    private final ClockHolder clockHolder;

    public FailPaymentService(
            PaymentReadManager paymentReadManager,
            PaymentPersistenceManager paymentPersistenceManager,
            CheckoutReadManager checkoutReadManager,
            RestoreCartItemsUseCase restoreCartItemsUseCase,
            PaymentAssembler paymentAssembler,
            ClockHolder clockHolder) {
        this.paymentReadManager = paymentReadManager;
        this.paymentPersistenceManager = paymentPersistenceManager;
        this.checkoutReadManager = checkoutReadManager;
        this.restoreCartItemsUseCase = restoreCartItemsUseCase;
        this.paymentAssembler = paymentAssembler;
        this.clockHolder = clockHolder;
    }

    /**
     * 결제 실패 처리
     *
     * <p>Payment 조회 → Checkout 조회 → 장바구니 아이템 복원 → Payment 상태 변경
     *
     * @param command 결제 실패 Command
     * @return 실패 처리된 결제 응답
     */
    @Override
    @Transactional
    public PaymentResponse failPayment(FailPaymentCommand command) {
        // 1. Payment 조회
        Payment payment = paymentReadManager.findById(command.paymentId());

        // 2. Checkout 조회하여 memberId와 items 추출
        Checkout checkout = checkoutReadManager.findById(payment.checkoutId());

        // 3. 장바구니 아이템 복원 (productStockId 기반)
        restoreCartItems(checkout);

        // 4. Payment 상태를 FAILED로 변경
        Instant now = Instant.now(clockHolder.getClock());
        Payment failedPayment = payment.fail(now);

        // 5. Payment 저장
        paymentPersistenceManager.persist(failedPayment);

        // 6. Response 반환
        return paymentAssembler.toResponse(failedPayment);
    }

    /**
     * 장바구니 아이템 복원
     *
     * <p>Checkout에 포함된 productStockIds를 기반으로 소프트 딜리트된 장바구니 아이템을 복원합니다.
     *
     * @param checkout 체크아웃 정보
     */
    private void restoreCartItems(Checkout checkout) {
        String memberIdString = checkout.memberId();
        UUID memberId = UUID.fromString(memberIdString);

        List<Long> productStockIds =
                checkout.items().stream().map(CheckoutItem::productStockId).toList();

        RestoreCartItemsCommand restoreCommand =
                RestoreCartItemsCommand.of(memberId, productStockIds);

        restoreCartItemsUseCase.restoreItems(restoreCommand);
    }
}
