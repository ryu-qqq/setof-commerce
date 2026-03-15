package com.ryuqq.setof.application.discount.manager;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.discount.port.out.command.DiscountTargetCommandPort;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.aggregate.DiscountTarget;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetDiff;
import java.time.Instant;
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
@DisplayName("DiscountTargetCommandManager 단위 테스트")
class DiscountTargetCommandManagerTest {

    @InjectMocks private DiscountTargetCommandManager sut;

    @Mock private DiscountTargetCommandPort targetCommandPort;

    @Nested
    @DisplayName("persistAll() - 타겟 일괄 INSERT")
    class PersistAllTest {

        @Test
        @DisplayName("타겟 목록을 CommandPort에 위임하여 일괄 저장한다")
        void persistAll_ValidTargets_DelegatesToCommandPort() {
            // given
            long policyId = 1L;
            List<DiscountTarget> targets =
                    List.of(DiscountFixtures.activeTarget(1L), DiscountFixtures.activeTarget(2L));

            willDoNothing().given(targetCommandPort).persistAll(policyId, targets);

            // when
            sut.persistAll(policyId, targets);

            // then
            then(targetCommandPort).should().persistAll(policyId, targets);
        }

        @Test
        @DisplayName("빈 타겟 목록도 CommandPort에 위임한다")
        void persistAll_EmptyTargets_DelegatesToCommandPort() {
            // given
            long policyId = 1L;
            List<DiscountTarget> targets = List.of();

            willDoNothing().given(targetCommandPort).persistAll(policyId, targets);

            // when
            sut.persistAll(policyId, targets);

            // then
            then(targetCommandPort).should().persistAll(policyId, targets);
        }
    }

    @Nested
    @DisplayName("persistDiff() - Diff 기반 타겟 persist")
    class PersistDiffTest {

        @Test
        @DisplayName("추가된 타겟이 있으면 persistAll을 호출한다")
        void persistDiff_WithAddedTargets_CallsPersistAll() {
            // given
            long policyId = 1L;
            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            List<DiscountTarget> added = List.of(DiscountFixtures.activeTarget(1L));
            DiscountTargetDiff diff = DiscountTargetDiff.of(added, List.of(), List.of(), now);

            willDoNothing().given(targetCommandPort).persistAll(policyId, added);

            // when
            sut.persistDiff(policyId, diff);

            // then
            then(targetCommandPort).should().persistAll(policyId, added);
            then(targetCommandPort).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("제거된 타겟이 있으면 updateAll을 호출한다")
        void persistDiff_WithRemovedTargets_CallsUpdateAll() {
            // given
            long policyId = 1L;
            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            List<DiscountTarget> removed = List.of(DiscountFixtures.inactiveTarget(1L));
            DiscountTargetDiff diff = DiscountTargetDiff.of(List.of(), removed, List.of(), now);

            willDoNothing().given(targetCommandPort).updateAll(policyId, removed);

            // when
            sut.persistDiff(policyId, diff);

            // then
            then(targetCommandPort).should().updateAll(policyId, removed);
            then(targetCommandPort).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("추가와 제거가 동시에 있으면 persistAll과 updateAll을 모두 호출한다")
        void persistDiff_WithAddedAndRemovedTargets_CallsBothOperations() {
            // given
            long policyId = 1L;
            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            List<DiscountTarget> added = List.of(DiscountFixtures.activeTarget(2L));
            List<DiscountTarget> removed = List.of(DiscountFixtures.inactiveTarget(1L));
            DiscountTargetDiff diff = DiscountTargetDiff.of(added, removed, List.of(), now);

            willDoNothing().given(targetCommandPort).persistAll(policyId, added);
            willDoNothing().given(targetCommandPort).updateAll(policyId, removed);

            // when
            sut.persistDiff(policyId, diff);

            // then
            then(targetCommandPort).should().persistAll(policyId, added);
            then(targetCommandPort).should().updateAll(policyId, removed);
        }

        @Test
        @DisplayName("변경이 없으면 CommandPort를 호출하지 않는다")
        void persistDiff_NoChanges_DoesNotCallCommandPort() {
            // given
            long policyId = 1L;
            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            DiscountTargetDiff diff = DiscountTargetDiff.of(List.of(), List.of(), List.of(), now);

            // when
            sut.persistDiff(policyId, diff);

            // then
            then(targetCommandPort).shouldHaveNoInteractions();
        }
    }
}
