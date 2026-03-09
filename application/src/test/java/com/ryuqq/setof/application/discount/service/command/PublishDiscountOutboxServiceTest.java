package com.ryuqq.setof.application.discount.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;

import com.ryuqq.setof.application.discount.DiscountDomainFixtures;
import com.ryuqq.setof.application.discount.manager.DiscountOutboxCommandManager;
import com.ryuqq.setof.application.discount.manager.DiscountOutboxReadManager;
import com.ryuqq.setof.application.discount.port.out.client.DiscountOutboxMessageClient;
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
@DisplayName("PublishDiscountOutboxService 단위 테스트")
class PublishDiscountOutboxServiceTest {

    @InjectMocks private PublishDiscountOutboxService sut;

    @Mock private DiscountOutboxReadManager readManager;
    @Mock private DiscountOutboxCommandManager commandManager;
    @Mock private DiscountOutboxMessageClient messageClient;

    @Nested
    @DisplayName("execute() - 아웃박스 SQS 발행")
    class ExecuteTest {

        @Test
        @DisplayName("PENDING 상태 아웃박스를 조회하여 SQS로 발행하고 발행 건수를 반환한다")
        void execute_PendingOutboxes_PublishesAndReturnsCount() {
            // given
            int batchSize = 10;
            List<DiscountOutbox> pendingList =
                    List.of(
                            DiscountDomainFixtures.pendingOutbox(1L),
                            DiscountDomainFixtures.pendingOutbox(2L));

            given(readManager.findPending(batchSize)).willReturn(pendingList);

            // when
            int result = sut.execute(batchSize);

            // then
            assertThat(result).isEqualTo(2);
            then(messageClient).should().publish(pendingList.get(0));
            then(messageClient).should().publish(pendingList.get(1));
            then(commandManager).should().persist(pendingList.get(0));
            then(commandManager).should().persist(pendingList.get(1));
        }

        @Test
        @DisplayName("PENDING 상태 아웃박스가 없으면 0을 반환하고 발행하지 않는다")
        void execute_NoPendingOutboxes_ReturnsZero() {
            // given
            int batchSize = 10;

            given(readManager.findPending(batchSize)).willReturn(List.of());

            // when
            int result = sut.execute(batchSize);

            // then
            assertThat(result).isZero();
            then(messageClient).shouldHaveNoInteractions();
            then(commandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("단건 PENDING 아웃박스가 발행되면 PUBLISHED 상태로 변경된다")
        void execute_SinglePendingOutbox_MarksPublished() {
            // given
            int batchSize = 10;
            DiscountOutbox pendingOutbox = DiscountDomainFixtures.pendingOutbox(1L);

            given(readManager.findPending(batchSize)).willReturn(List.of(pendingOutbox));

            // when
            sut.execute(batchSize);

            // then
            assertThat(pendingOutbox.status()).isEqualTo(OutboxStatus.PUBLISHED);
        }

        @Test
        @DisplayName("SQS 발행 실패 시 해당 아웃박스는 FAILED 또는 PENDING으로 변경되고 나머지는 계속 처리된다")
        void execute_OnePublishFails_ContinuesWithOthers() {
            // given
            int batchSize = 10;
            DiscountOutbox outbox1 = DiscountDomainFixtures.pendingOutbox(1L);
            DiscountOutbox outbox2 = DiscountDomainFixtures.pendingOutbox(2L);
            List<DiscountOutbox> pendingList = List.of(outbox1, outbox2);

            given(readManager.findPending(batchSize)).willReturn(pendingList);
            willThrow(new RuntimeException("SQS 연결 실패")).given(messageClient).publish(outbox1);

            // when
            int result = sut.execute(batchSize);

            // then
            assertThat(result).isEqualTo(1); // outbox2만 성공
            then(messageClient).should().publish(outbox1);
            then(messageClient).should().publish(outbox2);
            then(commandManager).should().persist(outbox1); // 실패한 것도 상태 저장
            then(commandManager).should().persist(outbox2);
        }

        @Test
        @DisplayName("SQS 발행 실패 시 failReason이 설정된다")
        void execute_PublishFails_OutboxHasFailReason() {
            // given
            int batchSize = 10;
            DiscountOutbox outbox = DiscountDomainFixtures.pendingOutbox(1L);

            given(readManager.findPending(batchSize)).willReturn(List.of(outbox));
            willThrow(new RuntimeException("SQS 타임아웃")).given(messageClient).publish(outbox);

            // when
            sut.execute(batchSize);

            // then
            assertThat(outbox.failReason()).contains("SQS 타임아웃");
        }

        @Test
        @DisplayName("batchSize만큼 PENDING 아웃박스를 조회한다")
        void execute_UsesProvidedBatchSize() {
            // given
            int batchSize = 5;

            given(readManager.findPending(batchSize)).willReturn(List.of());

            // when
            sut.execute(batchSize);

            // then
            then(readManager).should().findPending(batchSize);
        }
    }
}
