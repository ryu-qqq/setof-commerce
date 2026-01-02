package com.ryuqq.setof.application.checkout.service.command;

import com.ryuqq.setof.application.checkout.dto.command.CompleteCheckoutCommand;
import com.ryuqq.setof.application.checkout.manager.query.CheckoutReadManager;
import com.ryuqq.setof.application.checkout.port.in.command.CompleteCheckoutUseCase;
import com.ryuqq.setof.application.checkout.port.out.command.CheckoutPersistencePort;
import com.ryuqq.setof.application.common.port.out.DistributedLockPort;
import com.ryuqq.setof.application.common.port.out.StockCounterPort;
import com.ryuqq.setof.application.discountusagehistory.factory.command.DiscountUsageHistoryCommandFactory;
import com.ryuqq.setof.application.discountusagehistory.manager.command.DiscountUsageHistoryPersistenceManager;
import com.ryuqq.setof.application.order.dto.command.CreateOrderCommand;
import com.ryuqq.setof.application.order.dto.command.CreateOrderDiscountCommand;
import com.ryuqq.setof.application.order.dto.command.CreateOrderItemCommand;
import com.ryuqq.setof.application.order.factory.command.OrderCommandFactory;
import com.ryuqq.setof.application.order.port.out.command.OrderPersistencePort;
import com.ryuqq.setof.application.payment.manager.query.PaymentReadManager;
import com.ryuqq.setof.application.payment.port.out.command.PaymentPersistencePort;
import com.ryuqq.setof.domain.checkout.aggregate.Checkout;
import com.ryuqq.setof.domain.checkout.exception.InsufficientStockException;
import com.ryuqq.setof.domain.checkout.vo.CheckoutItem;
import com.ryuqq.setof.domain.checkout.vo.ShippingAddressSnapshot;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.payment.exception.PaymentCompletionInProgressException;
import com.ryuqq.setof.domain.payment.vo.PaymentLockKey;
import com.ryuqq.setof.domain.payment.vo.PaymentMoney;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 체크아웃 완료 Service
 *
 * <p>PG 결제 완료 후 체크아웃을 완료 처리합니다.
 *
 * <p>흐름:
 *
 * <ol>
 *   <li>paymentId 기반 분산락 획득
 *   <li>Payment 조회 및 상태 검증
 *   <li>Checkout 조회 및 상태 검증
 *   <li>Redis 재고 차감 (DECRBY)
 *   <li>Payment 승인 처리
 *   <li>판매자별 Order 생성 (할인 정보 포함)
 *   <li>할인 사용 이력 기록 (추후 Checkout 확장 시 활성화)
 *   <li>Checkout 완료 처리
 *   <li>분산락 해제
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class CompleteCheckoutService implements CompleteCheckoutUseCase {

    private static final long LOCK_WAIT_SECONDS = 5;
    private static final long LOCK_LEASE_SECONDS = 30;

    private final PaymentReadManager paymentReadManager;
    private final CheckoutReadManager checkoutReadManager;
    private final CheckoutPersistencePort checkoutPersistencePort;
    private final PaymentPersistencePort paymentPersistencePort;
    private final OrderPersistencePort orderPersistencePort;
    private final OrderCommandFactory orderCommandFactory;
    private final DiscountUsageHistoryCommandFactory discountUsageHistoryCommandFactory;
    private final DiscountUsageHistoryPersistenceManager discountUsageHistoryPersistenceManager;
    private final StockCounterPort stockCounterPort;
    private final DistributedLockPort distributedLockPort;
    private final TransactionTemplate transactionTemplate;
    private final ClockHolder clockHolder;

    public CompleteCheckoutService(
            PaymentReadManager paymentReadManager,
            CheckoutReadManager checkoutReadManager,
            CheckoutPersistencePort checkoutPersistencePort,
            PaymentPersistencePort paymentPersistencePort,
            OrderPersistencePort orderPersistencePort,
            OrderCommandFactory orderCommandFactory,
            DiscountUsageHistoryCommandFactory discountUsageHistoryCommandFactory,
            DiscountUsageHistoryPersistenceManager discountUsageHistoryPersistenceManager,
            StockCounterPort stockCounterPort,
            DistributedLockPort distributedLockPort,
            TransactionTemplate transactionTemplate,
            ClockHolder clockHolder) {
        this.paymentReadManager = paymentReadManager;
        this.checkoutReadManager = checkoutReadManager;
        this.checkoutPersistencePort = checkoutPersistencePort;
        this.paymentPersistencePort = paymentPersistencePort;
        this.orderPersistencePort = orderPersistencePort;
        this.orderCommandFactory = orderCommandFactory;
        this.discountUsageHistoryCommandFactory = discountUsageHistoryCommandFactory;
        this.discountUsageHistoryPersistenceManager = discountUsageHistoryPersistenceManager;
        this.stockCounterPort = stockCounterPort;
        this.distributedLockPort = distributedLockPort;
        this.transactionTemplate = transactionTemplate;
        this.clockHolder = clockHolder;
    }

    /**
     * 체크아웃 완료 처리
     *
     * @param command 체크아웃 완료 Command (paymentId, pgTransactionId, approvedAmount 포함)
     * @throws PaymentCompletionInProgressException 이미 결제 완료 처리 중인 경우
     * @throws InsufficientStockException 재고가 부족한 경우
     */
    @Override
    public void completeCheckout(CompleteCheckoutCommand command) {
        PaymentLockKey lockKey = PaymentLockKey.forComplete(command.paymentId());

        boolean lockAcquired =
                distributedLockPort.tryLock(
                        lockKey, LOCK_WAIT_SECONDS, LOCK_LEASE_SECONDS, TimeUnit.SECONDS);

        if (!lockAcquired) {
            throw PaymentCompletionInProgressException.forPayment(command.paymentId());
        }

        try {
            transactionTemplate.executeWithoutResult(status -> executeCheckoutCompletion(command));
        } finally {
            distributedLockPort.unlock(lockKey);
        }
    }

    private void executeCheckoutCompletion(CompleteCheckoutCommand command) {
        Instant now = Instant.now(clockHolder.getClock());

        // 1. Payment 조회 및 상태 검증
        Payment payment = paymentReadManager.findById(command.paymentId());

        // 2. Checkout 조회
        String checkoutIdString = payment.checkoutId().value().toString();
        Checkout checkout = checkoutReadManager.findById(checkoutIdString);

        // 3. 재고 차감
        Map<Long, Integer> stockRequirements = checkout.getStockRequirements();
        decrementStocks(stockRequirements);

        // 4. Payment 승인
        PaymentMoney approvedAmount = PaymentMoney.of(command.approvedAmount());
        Payment approvedPayment = payment.approve(command.pgTransactionId(), approvedAmount, now);
        paymentPersistencePort.persist(approvedPayment);

        // 5. 판매자별 Order 생성
        createOrdersPerSeller(checkout, command.paymentId(), now);

        // 6. Checkout 상태 전이: PENDING → PROCESSING → COMPLETED
        Checkout processingCheckout = checkout.startProcessing();
        Checkout completedCheckout = processingCheckout.complete(now);
        checkoutPersistencePort.persist(completedCheckout);
    }

    private void decrementStocks(Map<Long, Integer> stockRequirements) {
        Map<Long, Integer> decrementedStocks = new HashMap<>();

        try {
            for (Map.Entry<Long, Integer> entry : stockRequirements.entrySet()) {
                Long productStockId = entry.getKey();
                int requiredQuantity = entry.getValue();

                int remainingStock = stockCounterPort.decrement(productStockId, requiredQuantity);

                if (remainingStock < 0) {
                    // 음수면 재고 부족 - 롤백 후 예외 발생
                    stockCounterPort.increment(productStockId, requiredQuantity);
                    rollbackDecrements(decrementedStocks);
                    throw InsufficientStockException.forProduct(
                            productStockId, requiredQuantity, remainingStock + requiredQuantity);
                }

                decrementedStocks.put(productStockId, requiredQuantity);
            }
        } catch (InsufficientStockException e) {
            throw e;
        } catch (Exception e) {
            // 예기치 않은 오류 시 롤백
            rollbackDecrements(decrementedStocks);
            throw e;
        }
    }

    private void rollbackDecrements(Map<Long, Integer> decrementedStocks) {
        for (Map.Entry<Long, Integer> entry : decrementedStocks.entrySet()) {
            stockCounterPort.increment(entry.getKey(), entry.getValue());
        }
    }

    private void createOrdersPerSeller(Checkout checkout, String paymentId, Instant now) {
        Map<Long, List<CheckoutItem>> itemsBySeller = checkout.groupItemsBySeller();
        ShippingAddressSnapshot shipping = checkout.shippingAddress();
        String checkoutIdString = checkout.id().value().toString();
        int sellerCount = itemsBySeller.size();

        for (Map.Entry<Long, List<CheckoutItem>> entry : itemsBySeller.entrySet()) {
            Long sellerId = entry.getKey();
            List<CheckoutItem> sellerItems = entry.getValue();

            // 판매자별 할인 금액 배분 (단순 균등 배분)
            BigDecimal sellerDiscountAmount = calculateSellerDiscount(checkout, sellerCount);

            CreateOrderCommand orderCommand =
                    createOrderCommand(
                            checkoutIdString,
                            paymentId,
                            sellerId,
                            checkout.memberId(),
                            sellerItems,
                            shipping,
                            sellerDiscountAmount);

            Order order = orderCommandFactory.createOrder(orderCommand);
            orderPersistencePort.persist(order);
        }
    }

    private BigDecimal calculateSellerDiscount(Checkout checkout, int sellerCount) {
        BigDecimal totalDiscount = checkout.discountAmount().value();
        if (totalDiscount.compareTo(BigDecimal.ZERO) == 0 || sellerCount == 0) {
            return BigDecimal.ZERO;
        }
        // 판매자별 균등 배분 (실제 서비스에서는 상품 금액 비율로 배분 권장)
        return totalDiscount.divide(
                BigDecimal.valueOf(sellerCount), 0, java.math.RoundingMode.HALF_UP);
    }

    private CreateOrderCommand createOrderCommand(
            String checkoutId,
            String paymentId,
            Long sellerId,
            String memberId,
            List<CheckoutItem> items,
            ShippingAddressSnapshot shipping,
            BigDecimal discountAmount) {

        List<CreateOrderItemCommand> itemCommands = new ArrayList<>();
        for (CheckoutItem item : items) {
            CreateOrderItemCommand itemCommand =
                    new CreateOrderItemCommand(
                            item.productId(),
                            item.productStockId(),
                            item.quantity(),
                            item.unitPrice().value(),
                            item.productName(),
                            item.productImage(),
                            item.optionName(),
                            item.brandName(),
                            item.sellerName());
            itemCommands.add(itemCommand);
        }

        // 할인 상세 정보 (현재 Checkout에는 상세 정보 없음 - 추후 확장 가능)
        List<CreateOrderDiscountCommand> discountCommands = new ArrayList<>();

        return new CreateOrderCommand(
                checkoutId,
                paymentId,
                sellerId,
                memberId,
                itemCommands,
                shipping.receiverName(),
                shipping.receiverPhone(),
                shipping.address(),
                shipping.addressDetail(),
                shipping.zipCode(),
                shipping.memo(),
                discountAmount,
                discountCommands,
                BigDecimal.ZERO); // 배송비는 별도 계산 필요 시 추가
    }
}
