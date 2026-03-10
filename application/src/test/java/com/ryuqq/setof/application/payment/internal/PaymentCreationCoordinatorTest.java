package com.ryuqq.setof.application.payment.internal;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.payment.PaymentCommandFixtures;
import com.ryuqq.setof.application.payment.validator.PaymentValidator;
import com.ryuqq.setof.application.stock.manager.StockDeductionManager;
import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.stock.vo.StockDeductionItem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("PaymentCreationCoordinator 단위 테스트")
class PaymentCreationCoordinatorTest {

    @InjectMocks private PaymentCreationCoordinator sut;

    @Mock private PaymentValidator paymentValidator;
    @Mock private StockDeductionManager stockDeductionManager;

    @Nested
    @DisplayName("validateAndDeductStock() - 마일리지 검증 + 가격 검증 + 재고 차감")
    class ValidateAndDeductStockTest {

        @Test
        @DisplayName("마일리지 검증, 가격 검증 후 재고 차감이 수행된다")
        void validateAndDeductStock_ValidOrderAndStockItems_CallsAllMethods() {
            // given
            Payment payment = PaymentCommandFixtures.payment();
            Order order = PaymentCommandFixtures.order();
            List<StockDeductionItem> stockItems = PaymentCommandFixtures.stockDeductionItems();

            willDoNothing()
                    .given(paymentValidator)
                    .validateMileageUsage(payment.usedMileage(), payment.paymentAmountValue());
            willDoNothing().given(paymentValidator).validatePrices(order.orderItems());
            willDoNothing().given(stockDeductionManager).deductAll(stockItems);

            // when
            sut.validateAndDeductStock(payment, order, stockItems);

            // then
            then(paymentValidator)
                    .should()
                    .validateMileageUsage(payment.usedMileage(), payment.paymentAmountValue());
            then(paymentValidator).should().validatePrices(order.orderItems());
            then(stockDeductionManager).should().deductAll(stockItems);
        }

        @Test
        @DisplayName("마일리지 검증 → 가격 검증 → 재고 차감 순서로 호출된다")
        void validateAndDeductStock_ValidatorCalledBeforeDeductor() {
            // given
            Payment payment = PaymentCommandFixtures.payment();
            Order order = PaymentCommandFixtures.order();
            List<StockDeductionItem> stockItems = PaymentCommandFixtures.stockDeductionItems();

            willDoNothing()
                    .given(paymentValidator)
                    .validateMileageUsage(payment.usedMileage(), payment.paymentAmountValue());
            willDoNothing().given(paymentValidator).validatePrices(order.orderItems());
            willDoNothing().given(stockDeductionManager).deductAll(stockItems);

            // when
            sut.validateAndDeductStock(payment, order, stockItems);

            // then
            org.mockito.InOrder inOrder =
                    org.mockito.Mockito.inOrder(paymentValidator, stockDeductionManager);
            inOrder.verify(paymentValidator)
                    .validateMileageUsage(payment.usedMileage(), payment.paymentAmountValue());
            inOrder.verify(paymentValidator).validatePrices(order.orderItems());
            inOrder.verify(stockDeductionManager).deductAll(stockItems);
        }

        @Test
        @DisplayName("빈 재고 항목 목록으로 호출되어도 모든 메서드가 호출된다")
        void validateAndDeductStock_EmptyStockItems_StillCallsAllMethods() {
            // given
            Payment payment = PaymentCommandFixtures.payment();
            Order order = PaymentCommandFixtures.order();
            List<StockDeductionItem> emptyStockItems = List.of();

            willDoNothing()
                    .given(paymentValidator)
                    .validateMileageUsage(payment.usedMileage(), payment.paymentAmountValue());
            willDoNothing().given(paymentValidator).validatePrices(order.orderItems());
            willDoNothing().given(stockDeductionManager).deductAll(emptyStockItems);

            // when
            sut.validateAndDeductStock(payment, order, emptyStockItems);

            // then
            then(paymentValidator)
                    .should()
                    .validateMileageUsage(payment.usedMileage(), payment.paymentAmountValue());
            then(paymentValidator).should().validatePrices(order.orderItems());
            then(stockDeductionManager).should().deductAll(emptyStockItems);
        }
    }
}
