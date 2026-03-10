package com.ryuqq.setof.application.payment.manager;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.payment.PaymentCommandFixtures;
import com.ryuqq.setof.application.payment.port.out.command.RefundAccountSnapshotCommandPort;
import com.ryuqq.setof.domain.payment.vo.RefundAccountSnapshot;
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
@DisplayName("RefundAccountSnapshotManager 단위 테스트")
class RefundAccountSnapshotManagerTest {

    @InjectMocks private RefundAccountSnapshotManager sut;

    @Mock private RefundAccountSnapshotCommandPort refundAccountSnapshotCommandPort;

    @Nested
    @DisplayName("persist() - 환불 계좌 스냅샷 저장")
    class PersistTest {

        @Test
        @DisplayName("RefundAccountSnapshot을 RefundAccountSnapshotCommandPort에 위임하여 저장한다")
        void persist_ValidRefundAccountSnapshot_DelegatesToCommandPort() {
            // given
            RefundAccountSnapshot snapshot = PaymentCommandFixtures.refundAccountSnapshot();
            willDoNothing().given(refundAccountSnapshotCommandPort).persist(snapshot);

            // when
            sut.persist(snapshot);

            // then
            then(refundAccountSnapshotCommandPort).should().persist(snapshot);
        }

        @Test
        @DisplayName("RefundAccountSnapshotCommandPort.persist에 snapshot이 전달된다")
        void persist_PassesSnapshotToPort() {
            // given
            RefundAccountSnapshot snapshot = PaymentCommandFixtures.refundAccountSnapshot();
            willDoNothing().given(refundAccountSnapshotCommandPort).persist(snapshot);

            // when
            sut.persist(snapshot);

            // then
            then(refundAccountSnapshotCommandPort).should().persist(snapshot);
            then(refundAccountSnapshotCommandPort).shouldHaveNoMoreInteractions();
        }
    }
}
