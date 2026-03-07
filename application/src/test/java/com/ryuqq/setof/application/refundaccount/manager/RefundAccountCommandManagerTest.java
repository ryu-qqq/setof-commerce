package com.ryuqq.setof.application.refundaccount.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.refundaccount.RefundAccountDomainFixtures;
import com.ryuqq.setof.application.refundaccount.port.out.command.RefundAccountCommandPort;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
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
@DisplayName("RefundAccountCommandManager 단위 테스트")
class RefundAccountCommandManagerTest {

    @InjectMocks private RefundAccountCommandManager sut;

    @Mock private RefundAccountCommandPort commandPort;

    @Nested
    @DisplayName("persist() - 환불 계좌 저장")
    class PersistTest {

        @Test
        @DisplayName("환불 계좌를 저장하고 생성된 ID를 반환한다")
        void persist_SavesRefundAccount_ReturnsId() {
            // given
            Long refundAccountId = 100L;
            Long userId = 1L;
            RefundAccount refundAccount =
                    RefundAccountDomainFixtures.activeRefundAccount(refundAccountId, userId);
            Long expectedId = refundAccountId;

            given(commandPort.persist(refundAccount)).willReturn(expectedId);

            // when
            Long result = sut.persist(refundAccount);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(commandPort).should().persist(refundAccount);
        }

        @Test
        @DisplayName("신규 환불 계좌를 저장하고 생성된 ID를 반환한다")
        void persist_NewRefundAccount_ReturnsGeneratedId() {
            // given
            Long userId = 1L;
            RefundAccount newRefundAccount = RefundAccountDomainFixtures.newRefundAccount(userId);
            Long expectedId = 200L;

            given(commandPort.persist(newRefundAccount)).willReturn(expectedId);

            // when
            Long result = sut.persist(newRefundAccount);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(commandPort).should().persist(newRefundAccount);
        }
    }
}
