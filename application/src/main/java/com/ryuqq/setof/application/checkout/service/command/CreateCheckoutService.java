package com.ryuqq.setof.application.checkout.service.command;

import com.ryuqq.setof.application.checkout.assembler.CheckoutAssembler;
import com.ryuqq.setof.application.checkout.dto.command.CreateCheckoutCommand;
import com.ryuqq.setof.application.checkout.dto.response.CheckoutResponse;
import com.ryuqq.setof.application.checkout.factory.command.CheckoutCommandFactory;
import com.ryuqq.setof.application.checkout.port.in.command.CreateCheckoutUseCase;
import com.ryuqq.setof.application.checkout.port.out.command.CheckoutPersistencePort;
import com.ryuqq.setof.application.common.port.out.DistributedLockPort;
import com.ryuqq.setof.application.common.port.out.StockCounterPort;
import com.ryuqq.setof.application.payment.factory.command.PaymentCommandFactory;
import com.ryuqq.setof.application.payment.port.out.command.PaymentPersistencePort;
import com.ryuqq.setof.domain.checkout.aggregate.Checkout;
import com.ryuqq.setof.domain.checkout.exception.DuplicateCheckoutException;
import com.ryuqq.setof.domain.checkout.exception.InsufficientStockException;
import com.ryuqq.setof.domain.checkout.vo.IdempotencyLockKey;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.payment.vo.PaymentMethod;
import com.ryuqq.setof.domain.payment.vo.PgProvider;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 체크아웃 생성 Service
 *
 * <p>체크아웃과 결제(PENDING)를 동시에 생성합니다.
 *
 * <p>흐름:
 *
 * <ol>
 *   <li>멱등키 기반 분산락 획득
 *   <li>Redis 재고 확인 (차감 없음)
 *   <li>Checkout Aggregate 생성
 *   <li>Payment Aggregate 생성 (PENDING 상태)
 *   <li>영속화 (트랜잭션 내)
 *   <li>분산락 해제
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class CreateCheckoutService implements CreateCheckoutUseCase {

    private static final long LOCK_WAIT_SECONDS = 5;
    private static final long LOCK_LEASE_SECONDS = 30;

    private final CheckoutCommandFactory checkoutCommandFactory;
    private final PaymentCommandFactory paymentCommandFactory;
    private final CheckoutPersistencePort checkoutPersistencePort;
    private final PaymentPersistencePort paymentPersistencePort;
    private final CheckoutAssembler checkoutAssembler;
    private final DistributedLockPort distributedLockPort;
    private final StockCounterPort stockCounterPort;
    private final TransactionTemplate transactionTemplate;

    public CreateCheckoutService(
            CheckoutCommandFactory checkoutCommandFactory,
            PaymentCommandFactory paymentCommandFactory,
            CheckoutPersistencePort checkoutPersistencePort,
            PaymentPersistencePort paymentPersistencePort,
            CheckoutAssembler checkoutAssembler,
            DistributedLockPort distributedLockPort,
            StockCounterPort stockCounterPort,
            TransactionTemplate transactionTemplate) {
        this.checkoutCommandFactory = checkoutCommandFactory;
        this.paymentCommandFactory = paymentCommandFactory;
        this.checkoutPersistencePort = checkoutPersistencePort;
        this.paymentPersistencePort = paymentPersistencePort;
        this.checkoutAssembler = checkoutAssembler;
        this.distributedLockPort = distributedLockPort;
        this.stockCounterPort = stockCounterPort;
        this.transactionTemplate = transactionTemplate;
    }

    /**
     * 체크아웃 생성
     *
     * @param command 체크아웃 생성 Command
     * @return 생성된 체크아웃 응답 (paymentId 포함)
     * @throws DuplicateCheckoutException 이미 처리 중인 멱등키인 경우
     * @throws InsufficientStockException 재고가 부족한 경우
     */
    @Override
    public CheckoutResponse createCheckout(CreateCheckoutCommand command) {
        IdempotencyLockKey lockKey = new IdempotencyLockKey(command.idempotencyKey());

        boolean lockAcquired =
                distributedLockPort.tryLock(
                        lockKey, LOCK_WAIT_SECONDS, LOCK_LEASE_SECONDS, TimeUnit.SECONDS);

        if (!lockAcquired) {
            throw DuplicateCheckoutException.forIdempotencyKey(command.idempotencyKey());
        }

        try {
            return transactionTemplate.execute(status -> executeCheckoutCreation(command));
        } finally {
            distributedLockPort.unlock(lockKey);
        }
    }

    private CheckoutResponse executeCheckoutCreation(CreateCheckoutCommand command) {
        // 1. Checkout Aggregate 생성
        Checkout checkout = checkoutCommandFactory.createCheckout(command);

        // 2. 재고 확인 (차감 없음 - CompleteCheckout에서 차감)
        Map<Long, Integer> stockRequirements = checkout.getStockRequirements();
        validateStockAvailability(stockRequirements);

        // 3. Payment Aggregate 생성 (PENDING 상태)
        PgProvider pgProvider = PgProvider.valueOf(command.pgProvider());
        PaymentMethod paymentMethod = PaymentMethod.valueOf(command.paymentMethod());
        Payment payment =
                paymentCommandFactory.createFromCheckout(checkout, pgProvider, paymentMethod);

        // 4. 영속화
        checkoutPersistencePort.persist(checkout);
        paymentPersistencePort.persist(payment);

        // 5. Response 반환
        return checkoutAssembler.toResponse(checkout, payment);
    }

    private void validateStockAvailability(Map<Long, Integer> stockRequirements) {
        if (!stockCounterPort.hasStocks(stockRequirements)) {
            throw InsufficientStockException.forProducts(
                    stockRequirements.keySet().stream().toList());
        }
    }
}
