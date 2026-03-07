package com.ryuqq.setof.application.refundaccount.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.refundaccount.RefundAccountDomainFixtures;
import com.ryuqq.setof.application.refundaccount.RefundAccountQueryFixtures;
import com.ryuqq.setof.application.refundaccount.assembler.RefundAccountAssembler;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResult;
import com.ryuqq.setof.application.refundaccount.port.out.query.RefundAccountQueryPort;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.exception.RefundAccountNotFoundException;
import java.util.Optional;
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
@DisplayName("RefundAccountReadManager 단위 테스트")
class RefundAccountReadManagerTest {

    @InjectMocks private RefundAccountReadManager sut;

    @Mock private RefundAccountQueryPort queryPort;

    @Mock private RefundAccountAssembler assembler;

    @Nested
    @DisplayName("fetchRefundAccount() - userId로 활성 환불 계좌 조회")
    class FetchRefundAccountTest {

        @Test
        @DisplayName("userId로 활성 환불 계좌를 조회하여 Result DTO로 반환한다")
        void fetchRefundAccount_ExistingAccount_ReturnsResult() {
            // given
            Long userId = 1L;
            Long refundAccountId = 100L;
            RefundAccount refundAccount =
                    RefundAccountDomainFixtures.activeRefundAccount(refundAccountId, userId);
            RefundAccountResult expected =
                    RefundAccountQueryFixtures.refundAccountResult(refundAccountId);

            given(queryPort.fetchRefundAccount(userId)).willReturn(Optional.of(refundAccount));
            given(assembler.toResult(refundAccount)).willReturn(expected);

            // when
            RefundAccountResult result = sut.fetchRefundAccount(userId);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.refundAccountId()).isEqualTo(refundAccountId);
            then(queryPort).should().fetchRefundAccount(userId);
            then(assembler).should().toResult(refundAccount);
        }

        @Test
        @DisplayName("환불 계좌가 없는 경우 RefundAccountNotFoundException이 발생한다")
        void fetchRefundAccount_NonExisting_ThrowsException() {
            // given
            Long userId = 999L;

            given(queryPort.fetchRefundAccount(userId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.fetchRefundAccount(userId))
                    .isInstanceOf(RefundAccountNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getByUserIdAndId() - userId와 refundAccountId로 도메인 객체 조회")
    class GetByUserIdAndIdTest {

        @Test
        @DisplayName("userId와 refundAccountId로 RefundAccount 도메인 객체를 반환한다")
        void getByUserIdAndId_ExistingAccount_ReturnsRefundAccount() {
            // given
            Long userId = 1L;
            Long refundAccountId = 100L;
            RefundAccount expected =
                    RefundAccountDomainFixtures.activeRefundAccount(refundAccountId, userId);

            given(queryPort.findByUserIdAndId(userId, refundAccountId))
                    .willReturn(Optional.of(expected));

            // when
            RefundAccount result = sut.getByUserIdAndId(userId, refundAccountId);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().findByUserIdAndId(userId, refundAccountId);
        }

        @Test
        @DisplayName("존재하지 않는 환불 계좌 조회 시 RefundAccountNotFoundException이 발생한다")
        void getByUserIdAndId_NonExisting_ThrowsException() {
            // given
            Long userId = 1L;
            Long refundAccountId = 999L;

            given(queryPort.findByUserIdAndId(userId, refundAccountId))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getByUserIdAndId(userId, refundAccountId))
                    .isInstanceOf(RefundAccountNotFoundException.class);
        }

        @Test
        @DisplayName("다른 userId로 조회 시도 시 Optional.empty()가 반환되어 예외가 발생한다")
        void getByUserIdAndId_WrongUserId_ThrowsException() {
            // given
            Long userId = 2L;
            Long refundAccountId = 100L;

            given(queryPort.findByUserIdAndId(userId, refundAccountId))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getByUserIdAndId(userId, refundAccountId))
                    .isInstanceOf(RefundAccountNotFoundException.class);
        }
    }
}
