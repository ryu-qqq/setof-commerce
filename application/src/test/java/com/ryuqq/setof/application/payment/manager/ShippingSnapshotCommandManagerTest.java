package com.ryuqq.setof.application.payment.manager;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.payment.PaymentCommandFixtures;
import com.ryuqq.setof.application.payment.port.out.command.ShippingSnapshotCommandPort;
import com.ryuqq.setof.domain.order.aggregate.Order;
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
@DisplayName("ShippingSnapshotCommandManager 단위 테스트")
class ShippingSnapshotCommandManagerTest {

    @InjectMocks private ShippingSnapshotCommandManager sut;

    @Mock private ShippingSnapshotCommandPort shippingSnapshotCommandPort;

    @Nested
    @DisplayName("persist() - 배송지 스냅샷 저장")
    class PersistTest {

        @Test
        @DisplayName("Order를 ShippingSnapshotCommandPort에 위임하여 저장한다")
        void persist_ValidOrder_DelegatesToCommandPort() {
            // given
            Order order = PaymentCommandFixtures.order();
            willDoNothing().given(shippingSnapshotCommandPort).persist(order);

            // when
            sut.persist(order);

            // then
            then(shippingSnapshotCommandPort).should().persist(order);
        }

        @Test
        @DisplayName("ShippingSnapshotCommandPort.persist에 Order가 전달된다")
        void persist_PassesOrderToPort() {
            // given
            Order order = PaymentCommandFixtures.order();
            willDoNothing().given(shippingSnapshotCommandPort).persist(order);

            // when
            sut.persist(order);

            // then
            then(shippingSnapshotCommandPort).should().persist(order);
            then(shippingSnapshotCommandPort).shouldHaveNoMoreInteractions();
        }
    }
}
