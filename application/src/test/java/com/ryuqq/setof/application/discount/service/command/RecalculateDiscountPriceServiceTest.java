package com.ryuqq.setof.application.discount.service.command;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;

import com.ryuqq.setof.application.discount.DiscountDomainFixtures;
import com.ryuqq.setof.application.discount.internal.DiscountPriceRecalculateProcessor;
import com.ryuqq.setof.application.discount.manager.DiscountOutboxCommandManager;
import com.ryuqq.setof.application.discount.manager.DiscountOutboxReadManager;
import com.ryuqq.setof.domain.discount.aggregate.DiscountOutbox;
import com.ryuqq.setof.domain.discount.vo.OutboxStatus;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("RecalculateDiscountPriceService 단위 테스트")
class RecalculateDiscountPriceServiceTest {

    @InjectMocks private RecalculateDiscountPriceService sut;

    @Mock private DiscountOutboxReadManager outboxReadManager;
    @Mock private DiscountOutboxCommandManager outboxCommandManager;
    @Mock private DiscountPriceRecalculateProcessor recalculateProcessor;

    @Nested
    @DisplayName("execute() - 할인 가격 재계산")
    class ExecuteTest {

        @Test
        @DisplayName("PUBLISHED 상태의 아웃박스를 찾아 재계산하고 COMPLETED로 변경한다")
        void execute_PublishedOutbox_RecalculatesAndMarkCompleted() {
            // given
            long outboxId = DiscountDomainFixtures.DEFAULT_OUTBOX_ID;
            DiscountOutbox outbox = DiscountDomainFixtures.publishedOutbox(outboxId);

            given(outboxReadManager.findById(outboxId)).willReturn(Optional.of(outbox));

            // when
            sut.execute(outboxId);

            // then
            then(recalculateProcessor).should().process(outbox.targetType(), outbox.targetId());
            then(outboxCommandManager).should().persist(outbox);
            assertThat_outboxIsCompleted(outbox);
        }

        @Test
        @DisplayName("아웃박스가 존재하지 않으면 처리를 건너뛴다")
        void execute_OutboxNotFound_SkipsProcessing() {
            // given
            long outboxId = 999L;

            given(outboxReadManager.findById(outboxId)).willReturn(Optional.empty());

            // when
            sut.execute(outboxId);

            // then
            then(recalculateProcessor).shouldHaveNoInteractions();
            then(outboxCommandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("PENDING 상태의 아웃박스는 처리를 건너뛴다")
        void execute_PendingOutbox_SkipsProcessing() {
            // given
            long outboxId = DiscountDomainFixtures.DEFAULT_OUTBOX_ID;
            DiscountOutbox pendingOutbox = DiscountDomainFixtures.pendingOutbox(outboxId);

            given(outboxReadManager.findById(outboxId)).willReturn(Optional.of(pendingOutbox));

            // when
            sut.execute(outboxId);

            // then
            then(recalculateProcessor).shouldHaveNoInteractions();
            then(outboxCommandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("COMPLETED 상태의 아웃박스는 처리를 건너뛴다")
        void execute_CompletedOutbox_SkipsProcessing() {
            // given
            long outboxId = DiscountDomainFixtures.DEFAULT_OUTBOX_ID;
            DiscountOutbox completedOutbox = DiscountDomainFixtures.completedOutbox();

            given(outboxReadManager.findById(outboxId)).willReturn(Optional.of(completedOutbox));

            // when
            sut.execute(outboxId);

            // then
            then(recalculateProcessor).shouldHaveNoInteractions();
            then(outboxCommandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("재계산 중 예외 발생 시 아웃박스를 FAILED/PENDING으로 변경하고 예외를 재던진다")
        void execute_ProcessorThrowsException_MarksFailedAndRethrows() {
            // given
            long outboxId = DiscountDomainFixtures.DEFAULT_OUTBOX_ID;
            DiscountOutbox outbox = DiscountDomainFixtures.publishedOutbox(outboxId);
            RuntimeException cause = new RuntimeException("재계산 실패");

            given(outboxReadManager.findById(outboxId)).willReturn(Optional.of(outbox));
            willThrow(cause)
                    .given(recalculateProcessor)
                    .process(outbox.targetType(), outbox.targetId());

            // when / then
            assertThatThrownBy(() -> sut.execute(outboxId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("재계산 실패");

            // 실패 처리 후 아웃박스 상태 저장
            then(outboxCommandManager).should().persist(outbox);
        }

        @Test
        @DisplayName("재계산 실패 시 아웃박스의 failReason이 설정된다")
        void execute_ProcessorFails_OutboxHasFailReason() {
            // given
            long outboxId = DiscountDomainFixtures.DEFAULT_OUTBOX_ID;
            DiscountOutbox outbox = DiscountDomainFixtures.publishedOutbox(outboxId);
            RuntimeException cause = new RuntimeException("DB 연결 오류");

            given(outboxReadManager.findById(outboxId)).willReturn(Optional.of(outbox));
            willThrow(cause)
                    .given(recalculateProcessor)
                    .process(outbox.targetType(), outbox.targetId());

            // when
            try {
                sut.execute(outboxId);
            } catch (RuntimeException ignored) {
                // expected
            }

            // then: failReason이 설정되고 outbox 상태가 변경됨
            then(outboxCommandManager)
                    .should()
                    .persist(
                            ArgumentMatchers.argThat(
                                    o ->
                                            o.failReason() != null
                                                    && o.failReason().contains("DB 연결 오류")));
        }
    }

    private void assertThat_outboxIsCompleted(DiscountOutbox outbox) {
        org.assertj.core.api.Assertions.assertThat(outbox.status())
                .isEqualTo(OutboxStatus.COMPLETED);
    }
}
