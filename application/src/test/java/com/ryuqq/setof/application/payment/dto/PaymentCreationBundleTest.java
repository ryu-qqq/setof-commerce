package com.ryuqq.setof.application.payment.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.application.payment.PaymentCommandFixtures;
import com.ryuqq.setof.application.payment.dto.bundle.PaymentCreationBundle;
import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.order.vo.ReceiverInfo;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.stock.vo.StockDeductionItem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("PaymentCreationBundle 단위 테스트")
class PaymentCreationBundleTest {

    @Nested
    @DisplayName("생성자 유효성 검증")
    class ValidationTest {

        @Test
        @DisplayName("payment가 null이면 IllegalArgumentException이 발생한다")
        void constructor_NullPayment_ThrowsIllegalArgumentException() {
            // given
            Order order = PaymentCommandFixtures.order();
            List<StockDeductionItem> stockItems = PaymentCommandFixtures.stockDeductionItems();
            ReceiverInfo receiverInfo = PaymentCommandFixtures.receiverInfo();

            // when & then
            assertThatThrownBy(
                            () ->
                                    new PaymentCreationBundle(
                                            null, order, stockItems, receiverInfo, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("payment");
        }

        @Test
        @DisplayName("order가 null이면 IllegalArgumentException이 발생한다")
        void constructor_NullOrder_ThrowsIllegalArgumentException() {
            // given
            Payment payment = PaymentCommandFixtures.payment();
            List<StockDeductionItem> stockItems = PaymentCommandFixtures.stockDeductionItems();
            ReceiverInfo receiverInfo = PaymentCommandFixtures.receiverInfo();

            // when & then
            assertThatThrownBy(
                            () ->
                                    new PaymentCreationBundle(
                                            payment, null, stockItems, receiverInfo, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("order");
        }

        @Test
        @DisplayName("receiverInfo가 null이면 IllegalArgumentException이 발생한다")
        void constructor_NullReceiverInfo_ThrowsIllegalArgumentException() {
            // given
            Payment payment = PaymentCommandFixtures.payment();
            Order order = PaymentCommandFixtures.order();
            List<StockDeductionItem> stockItems = PaymentCommandFixtures.stockDeductionItems();

            // when & then
            assertThatThrownBy(
                            () -> new PaymentCreationBundle(payment, order, stockItems, null, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("receiverInfo");
        }

        @Test
        @DisplayName("stockDeductionItems가 null이면 빈 리스트로 초기화된다")
        void constructor_NullStockDeductionItems_InitializesEmptyList() {
            // given
            Payment payment = PaymentCommandFixtures.payment();
            Order order = PaymentCommandFixtures.order();
            ReceiverInfo receiverInfo = PaymentCommandFixtures.receiverInfo();

            // when
            PaymentCreationBundle bundle =
                    new PaymentCreationBundle(payment, order, null, receiverInfo, null);

            // then
            assertThat(bundle.stockDeductionItems()).isEmpty();
        }

        @Test
        @DisplayName("유효한 인자로 PaymentCreationBundle이 정상 생성된다")
        void constructor_ValidArguments_CreatesBundleSuccessfully() {
            // when
            PaymentCreationBundle bundle = PaymentCommandFixtures.paymentCreationBundle();

            // then
            assertThat(bundle.payment()).isNotNull();
            assertThat(bundle.order()).isNotNull();
            assertThat(bundle.receiverInfo()).isNotNull();
            assertThat(bundle.stockDeductionItems()).isNotEmpty();
        }

        @Test
        @DisplayName("refundAccountSnapshot은 nullable이다")
        void constructor_NullRefundAccountSnapshot_IsAllowed() {
            // when
            PaymentCreationBundle bundle = PaymentCommandFixtures.paymentCreationBundle();

            // then
            assertThat(bundle.refundAccountSnapshot()).isNull();
        }

        @Test
        @DisplayName("refundAccountSnapshot이 있으면 그대로 보존된다")
        void constructor_WithRefundAccountSnapshot_PreservesSnapshot() {
            // when
            PaymentCreationBundle bundle =
                    PaymentCommandFixtures.paymentCreationBundleWithRefundAccount();

            // then
            assertThat(bundle.refundAccountSnapshot()).isNotNull();
            assertThat(bundle.refundAccountSnapshot().bankCode()).isEqualTo("004");
        }
    }
}
