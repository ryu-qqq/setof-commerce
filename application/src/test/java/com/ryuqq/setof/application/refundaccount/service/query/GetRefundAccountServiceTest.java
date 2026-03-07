package com.ryuqq.setof.application.refundaccount.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.refundaccount.RefundAccountQueryFixtures;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResult;
import com.ryuqq.setof.application.refundaccount.manager.RefundAccountReadManager;
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
@DisplayName("GetRefundAccountService 단위 테스트")
class GetRefundAccountServiceTest {

    @InjectMocks private GetRefundAccountService sut;

    @Mock private RefundAccountReadManager readManager;

    @Nested
    @DisplayName("execute() - 환불 계좌 단건 조회")
    class ExecuteTest {

        @Test
        @DisplayName("userId로 환불 계좌를 조회하고 RefundAccountResult를 반환한다")
        void execute_ValidUserId_ReturnsRefundAccountResult() {
            // given
            Long userId = 1L;
            Long refundAccountId = 100L;
            RefundAccountResult expected =
                    RefundAccountQueryFixtures.refundAccountResult(refundAccountId);

            given(readManager.fetchRefundAccount(userId)).willReturn(expected);

            // when
            RefundAccountResult result = sut.execute(userId);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.refundAccountId()).isEqualTo(refundAccountId);
            then(readManager).should().fetchRefundAccount(userId);
        }

        @Test
        @DisplayName("readManager.fetchRefundAccount에 userId를 정확히 전달한다")
        void execute_DelegatesToReadManager_WithCorrectUserId() {
            // given
            Long userId = 5L;
            Long refundAccountId = 200L;
            RefundAccountResult expected =
                    RefundAccountQueryFixtures.refundAccountResult(refundAccountId);

            given(readManager.fetchRefundAccount(userId)).willReturn(expected);

            // when
            sut.execute(userId);

            // then
            then(readManager).should().fetchRefundAccount(userId);
        }
    }
}
