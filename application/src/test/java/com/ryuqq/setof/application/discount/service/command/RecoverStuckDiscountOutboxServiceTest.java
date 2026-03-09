package com.ryuqq.setof.application.discount.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.discount.DiscountDomainFixtures;
import com.ryuqq.setof.application.discount.manager.DiscountOutboxCommandManager;
import com.ryuqq.setof.application.discount.manager.DiscountOutboxReadManager;
import com.ryuqq.setof.domain.discount.aggregate.DiscountOutbox;
import com.ryuqq.setof.domain.discount.vo.OutboxStatus;
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
@DisplayName("RecoverStuckDiscountOutboxService 단위 테스트")
class RecoverStuckDiscountOutboxServiceTest {

    @InjectMocks private RecoverStuckDiscountOutboxService sut;

    @Mock private DiscountOutboxReadManager readManager;
    @Mock private DiscountOutboxCommandManager commandManager;

    @Nested
    @DisplayName("execute() - Stuck 아웃박스 복구")
    class ExecuteTest {

        @Test
        @DisplayName("Stuck 상태 아웃박스를 PENDING으로 복구하고 복구 건수를 반환한다")
        void execute_StuckOutboxes_RecoversAndReturnsCount() {
            // given
            long timeoutSeconds = 300L;
            int batchSize = 10;
            List<DiscountOutbox> stuckList =
                    List.of(
                            DiscountDomainFixtures.stuckPublishedOutbox(1L),
                            DiscountDomainFixtures.stuckPublishedOutbox(2L));

            given(readManager.findStuckPublished(timeoutSeconds, batchSize)).willReturn(stuckList);

            // when
            int result = sut.execute(timeoutSeconds, batchSize);

            // then
            assertThat(result).isEqualTo(2);
            then(commandManager).should().persist(stuckList.get(0));
            then(commandManager).should().persist(stuckList.get(1));
        }

        @Test
        @DisplayName("Stuck 상태 아웃박스가 없으면 0을 반환한다")
        void execute_NoStuckOutboxes_ReturnsZero() {
            // given
            long timeoutSeconds = 300L;
            int batchSize = 10;

            given(readManager.findStuckPublished(timeoutSeconds, batchSize)).willReturn(List.of());

            // when
            int result = sut.execute(timeoutSeconds, batchSize);

            // then
            assertThat(result).isZero();
            then(commandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("재시도 횟수가 MAX_RETRY 미만이면 PENDING으로 복구한다")
        void execute_RetryCountBelowMax_RecoversToPending() {
            // given
            long timeoutSeconds = 300L;
            int batchSize = 10;
            // retryCount=1인 아웃박스 (MAX_RETRY=3 미만)
            DiscountOutbox stuckOutbox = DiscountDomainFixtures.stuckPublishedOutbox(1L);

            given(readManager.findStuckPublished(timeoutSeconds, batchSize))
                    .willReturn(List.of(stuckOutbox));

            // when
            sut.execute(timeoutSeconds, batchSize);

            // then: recoverStuck()은 retryCount를 증가시키고 PENDING 또는 FAILED로 변경
            assertThat(stuckOutbox.status()).isIn(OutboxStatus.PENDING, OutboxStatus.FAILED);
            then(commandManager).should().persist(stuckOutbox);
        }

        @Test
        @DisplayName("재시도 횟수가 MAX_RETRY를 초과하면 FAILED 상태가 된다")
        void execute_RetryCountExceedsMax_MarksAsFailed() {
            // given
            long timeoutSeconds = 300L;
            int batchSize = 10;
            // retryCount=2인 아웃박스 → recoverStuck() 호출 시 retryCount=3 >= MAX_RETRY(3) → FAILED
            DiscountOutbox outboxNearMaxRetry = DiscountDomainFixtures.outboxWithRetryCount(2);

            given(readManager.findStuckPublished(timeoutSeconds, batchSize))
                    .willReturn(List.of(outboxNearMaxRetry));

            // when
            int result = sut.execute(timeoutSeconds, batchSize);

            // then
            assertThat(result).isEqualTo(1);
            assertThat(outboxNearMaxRetry.status()).isEqualTo(OutboxStatus.FAILED);
            assertThat(outboxNearMaxRetry.isMaxRetryExceeded()).isTrue();
            then(commandManager).should().persist(outboxNearMaxRetry);
        }

        @Test
        @DisplayName("제공된 timeoutSeconds와 batchSize로 Stuck 아웃박스를 조회한다")
        void execute_UsesProvidedParameters() {
            // given
            long timeoutSeconds = 600L;
            int batchSize = 5;

            given(readManager.findStuckPublished(timeoutSeconds, batchSize)).willReturn(List.of());

            // when
            sut.execute(timeoutSeconds, batchSize);

            // then
            then(readManager).should().findStuckPublished(timeoutSeconds, batchSize);
        }

        @Test
        @DisplayName("여러 Stuck 아웃박스를 모두 처리한다")
        void execute_MultipleStuckOutboxes_ProcessesAll() {
            // given
            long timeoutSeconds = 300L;
            int batchSize = 10;
            List<DiscountOutbox> stuckList =
                    List.of(
                            DiscountDomainFixtures.stuckPublishedOutbox(1L),
                            DiscountDomainFixtures.stuckPublishedOutbox(2L),
                            DiscountDomainFixtures.stuckPublishedOutbox(3L));

            given(readManager.findStuckPublished(timeoutSeconds, batchSize)).willReturn(stuckList);

            // when
            int result = sut.execute(timeoutSeconds, batchSize);

            // then
            assertThat(result).isEqualTo(3);
            then(commandManager).should().persist(stuckList.get(0));
            then(commandManager).should().persist(stuckList.get(1));
            then(commandManager).should().persist(stuckList.get(2));
        }
    }
}
