package com.ryuqq.setof.application.payment.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.payment.PaymentCommandFixtures;
import com.ryuqq.setof.application.payment.port.out.command.OrderCreationPort;
import com.ryuqq.setof.domain.order.aggregate.Order;
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
@DisplayName("PaymentOrderCommandManager 단위 테스트")
class PaymentOrderCommandManagerTest {

    @InjectMocks private PaymentOrderCommandManager sut;

    @Mock private OrderCreationPort orderCreationPort;

    @Nested
    @DisplayName("persist() - Order 저장")
    class PersistTest {

        @Test
        @DisplayName("Order를 저장하고 생성된 주문 아이템 ID 목록을 반환한다")
        void persist_ValidOrder_ReturnsOrderItemIds() {
            // given
            Order order = PaymentCommandFixtures.order();
            List<Long> expectedOrderIds = List.of(100L, 101L);

            given(orderCreationPort.persist(order)).willReturn(expectedOrderIds);

            // when
            List<Long> result = sut.persist(order);

            // then
            assertThat(result).isEqualTo(expectedOrderIds);
            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("OrderCreationPort.persist에 Order를 위임한다")
        void persist_DelegatesToOrderCreationPort() {
            // given
            Order order = PaymentCommandFixtures.order();
            List<Long> expectedOrderIds = List.of(100L);

            given(orderCreationPort.persist(order)).willReturn(expectedOrderIds);

            // when
            sut.persist(order);

            // then
            then(orderCreationPort).should().persist(order);
        }

        @Test
        @DisplayName("빈 주문 아이템 ID 목록이 반환되어도 그대로 전달된다")
        void persist_EmptyOrderIds_ReturnsEmptyList() {
            // given
            Order order = PaymentCommandFixtures.order();
            List<Long> emptyOrderIds = List.of();

            given(orderCreationPort.persist(order)).willReturn(emptyOrderIds);

            // when
            List<Long> result = sut.persist(order);

            // then
            assertThat(result).isEmpty();
        }
    }
}
