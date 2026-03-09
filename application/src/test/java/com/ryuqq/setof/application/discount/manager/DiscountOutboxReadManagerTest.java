package com.ryuqq.setof.application.discount.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.discount.DiscountDomainFixtures;
import com.ryuqq.setof.application.discount.port.out.query.DiscountOutboxQueryPort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountOutbox;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.OutboxStatus;
import java.time.Instant;
import java.util.List;
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
@DisplayName("DiscountOutboxReadManager 단위 테스트")
class DiscountOutboxReadManagerTest {

    @InjectMocks private DiscountOutboxReadManager sut;

    @Mock private DiscountOutboxQueryPort queryPort;

    @Nested
    @DisplayName("findById() - ID로 아웃박스 조회")
    class FindByIdTest {

        @Test
        @DisplayName("존재하는 ID로 아웃박스를 조회한다")
        void findById_ExistingId_ReturnsOutbox() {
            // given
            long outboxId = DiscountDomainFixtures.DEFAULT_OUTBOX_ID;
            DiscountOutbox outbox = DiscountDomainFixtures.publishedOutbox(outboxId);

            given(queryPort.findById(outboxId)).willReturn(Optional.of(outbox));

            // when
            Optional<DiscountOutbox> result = sut.findById(outboxId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(outbox);
            then(queryPort).should().findById(outboxId);
        }

        @Test
        @DisplayName("존재하지 않는 ID면 빈 Optional을 반환한다")
        void findById_NotExistingId_ReturnsEmpty() {
            // given
            long outboxId = 999L;

            given(queryPort.findById(outboxId)).willReturn(Optional.empty());

            // when
            Optional<DiscountOutbox> result = sut.findById(outboxId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findPending() - PENDING 아웃박스 조회")
    class FindPendingTest {

        @Test
        @DisplayName("PENDING 상태의 아웃박스를 batchSize만큼 조회한다")
        void findPending_WithBatchSize_ReturnsPendingOutboxes() {
            // given
            int batchSize = 10;
            List<DiscountOutbox> pendingOutboxes =
                    List.of(
                            DiscountDomainFixtures.pendingOutbox(1L),
                            DiscountDomainFixtures.pendingOutbox(2L));

            given(queryPort.findByStatus(OutboxStatus.PENDING, batchSize))
                    .willReturn(pendingOutboxes);

            // when
            List<DiscountOutbox> result = sut.findPending(batchSize);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).isEqualTo(pendingOutboxes);
            then(queryPort).should().findByStatus(OutboxStatus.PENDING, batchSize);
        }

        @Test
        @DisplayName("PENDING 아웃박스가 없으면 빈 목록을 반환한다")
        void findPending_NoPendingOutboxes_ReturnsEmptyList() {
            // given
            int batchSize = 10;

            given(queryPort.findByStatus(OutboxStatus.PENDING, batchSize)).willReturn(List.of());

            // when
            List<DiscountOutbox> result = sut.findPending(batchSize);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findStuckPublished() - Stuck PUBLISHED 아웃박스 조회")
    class FindStuckPublishedTest {

        @Test
        @DisplayName("타임아웃 시간 이전에 업데이트된 PUBLISHED 아웃박스를 조회한다")
        void findStuckPublished_ValidTimeout_ReturnsStuckOutboxes() {
            // given
            long timeoutSeconds = 300L;
            int batchSize = 10;
            List<DiscountOutbox> stuckOutboxes =
                    List.of(DiscountDomainFixtures.stuckPublishedOutbox(1L));

            given(queryPort.findStuckPublished(any(Instant.class), eq(batchSize)))
                    .willReturn(stuckOutboxes);

            // when
            List<DiscountOutbox> result = sut.findStuckPublished(timeoutSeconds, batchSize);

            // then
            assertThat(result).hasSize(1);
            then(queryPort).should().findStuckPublished(any(Instant.class), eq(batchSize));
        }

        @Test
        @DisplayName("Stuck 아웃박스가 없으면 빈 목록을 반환한다")
        void findStuckPublished_NoStuckOutboxes_ReturnsEmptyList() {
            // given
            long timeoutSeconds = 300L;
            int batchSize = 10;

            given(queryPort.findStuckPublished(any(Instant.class), eq(batchSize)))
                    .willReturn(List.of());

            // when
            List<DiscountOutbox> result = sut.findStuckPublished(timeoutSeconds, batchSize);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsPendingForTarget() - 타겟의 PENDING 존재 여부")
    class ExistsPendingForTargetTest {

        @Test
        @DisplayName("타겟에 PENDING 아웃박스가 존재하면 true를 반환한다")
        void existsPendingForTarget_PendingExists_ReturnsTrue() {
            // given
            DiscountTargetType targetType = DiscountTargetType.SELLER;
            long targetId = 1L;

            given(queryPort.existsByTargetAndStatus(targetType, targetId, OutboxStatus.PENDING))
                    .willReturn(true);

            // when
            boolean result = sut.existsPendingForTarget(targetType, targetId);

            // then
            assertThat(result).isTrue();
            then(queryPort)
                    .should()
                    .existsByTargetAndStatus(targetType, targetId, OutboxStatus.PENDING);
        }

        @Test
        @DisplayName("타겟에 PENDING 아웃박스가 없으면 false를 반환한다")
        void existsPendingForTarget_NoPending_ReturnsFalse() {
            // given
            DiscountTargetType targetType = DiscountTargetType.SELLER;
            long targetId = 1L;

            given(queryPort.existsByTargetAndStatus(targetType, targetId, OutboxStatus.PENDING))
                    .willReturn(false);

            // when
            boolean result = sut.existsPendingForTarget(targetType, targetId);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("existsPublishedForTarget() - 타겟의 PUBLISHED 존재 여부")
    class ExistsPublishedForTargetTest {

        @Test
        @DisplayName("타겟에 PUBLISHED 아웃박스가 존재하면 true를 반환한다")
        void existsPublishedForTarget_PublishedExists_ReturnsTrue() {
            // given
            DiscountTargetType targetType = DiscountTargetType.BRAND;
            long targetId = 5L;

            given(queryPort.existsByTargetAndStatus(targetType, targetId, OutboxStatus.PUBLISHED))
                    .willReturn(true);

            // when
            boolean result = sut.existsPublishedForTarget(targetType, targetId);

            // then
            assertThat(result).isTrue();
            then(queryPort)
                    .should()
                    .existsByTargetAndStatus(targetType, targetId, OutboxStatus.PUBLISHED);
        }

        @Test
        @DisplayName("타겟에 PUBLISHED 아웃박스가 없으면 false를 반환한다")
        void existsPublishedForTarget_NoPublished_ReturnsFalse() {
            // given
            DiscountTargetType targetType = DiscountTargetType.BRAND;
            long targetId = 5L;

            given(queryPort.existsByTargetAndStatus(targetType, targetId, OutboxStatus.PUBLISHED))
                    .willReturn(false);

            // when
            boolean result = sut.existsPublishedForTarget(targetType, targetId);

            // then
            assertThat(result).isFalse();
        }
    }
}
