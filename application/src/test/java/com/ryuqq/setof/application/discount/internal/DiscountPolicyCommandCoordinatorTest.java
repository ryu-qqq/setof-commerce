package com.ryuqq.setof.application.discount.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.discount.manager.DiscountOutboxCommandManager;
import com.ryuqq.setof.application.discount.manager.DiscountPolicyCommandManager;
import com.ryuqq.setof.application.discount.manager.DiscountTargetCommandManager;
import com.ryuqq.setof.application.discount.validator.DiscountOutboxValidator;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.aggregate.DiscountTarget;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetDiff;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
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
@DisplayName("DiscountPolicyCommandCoordinator 단위 테스트")
class DiscountPolicyCommandCoordinatorTest {

    @InjectMocks private DiscountPolicyCommandCoordinator sut;

    @Mock private DiscountPolicyCommandManager policyCommandManager;
    @Mock private DiscountTargetCommandManager targetCommandManager;
    @Mock private DiscountOutboxCommandManager outboxCommandManager;
    @Mock private DiscountOutboxValidator outboxValidator;

    @Nested
    @DisplayName("createPolicyWithTargets() - 정책 생성 + 타겟 저장 + 아웃박스 생성")
    class CreatePolicyWithTargetsTest {

        @Test
        @DisplayName("타겟이 없는 정책 생성 시 정책만 저장하고 ID를 반환한다")
        void createPolicyWithTargets_PolicyWithoutTargets_ReturnsPolicyId() {
            // given
            DiscountPolicy policy = DiscountFixtures.newRateInstantPolicy();
            long expectedPolicyId = 1L;

            given(policyCommandManager.persist(policy)).willReturn(expectedPolicyId);

            // when
            long result = sut.createPolicyWithTargets(policy);

            // then
            assertThat(result).isEqualTo(expectedPolicyId);
            then(policyCommandManager).should().persist(policy);
            then(targetCommandManager).shouldHaveNoInteractions();
            then(outboxCommandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("타겟이 있는 정책 생성 시 정책 저장 후 타겟을 저장한다")
        void createPolicyWithTargets_PolicyWithTargets_PersistsTargets() {
            // given
            DiscountPolicy policy = DiscountFixtures.newRateInstantPolicy();
            policy.addTarget(DiscountTargetType.PRODUCT, 300L, Instant.parse("2025-01-01T00:00:00Z"));
            long expectedPolicyId = 1L;

            given(policyCommandManager.persist(policy)).willReturn(expectedPolicyId);
            given(outboxValidator.canCreateOutbox(
                    org.mockito.ArgumentMatchers.any(DiscountTargetType.class),
                    org.mockito.ArgumentMatchers.anyLong()))
                    .willReturn(false);

            // when
            long result = sut.createPolicyWithTargets(policy);

            // then
            assertThat(result).isEqualTo(expectedPolicyId);
            then(policyCommandManager).should().persist(policy);
            then(targetCommandManager).should().persistAll(
                    org.mockito.ArgumentMatchers.eq(expectedPolicyId),
                    org.mockito.ArgumentMatchers.anyList());
        }

        @Test
        @DisplayName("아웃박스 생성 가능한 타겟에 대해 아웃박스를 생성한다")
        void createPolicyWithTargets_EligibleTargets_CreatesOutboxes() {
            // given
            DiscountPolicy policy = DiscountFixtures.newRateInstantPolicy();
            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            policy.addTarget(DiscountTargetType.PRODUCT, 300L, now);
            long policyId = 1L;

            given(policyCommandManager.persist(policy)).willReturn(policyId);
            given(outboxValidator.canCreateOutbox(DiscountTargetType.PRODUCT, 300L))
                    .willReturn(true);

            // when
            sut.createPolicyWithTargets(policy);

            // then
            then(outboxCommandManager).should().create(
                    org.mockito.ArgumentMatchers.eq(DiscountTargetType.PRODUCT),
                    org.mockito.ArgumentMatchers.eq(300L),
                    org.mockito.ArgumentMatchers.any(Instant.class));
        }

        @Test
        @DisplayName("아웃박스 생성 불가한 타겟에 대해서는 아웃박스를 생성하지 않는다")
        void createPolicyWithTargets_IneligibleTargets_DoesNotCreateOutboxes() {
            // given
            DiscountPolicy policy = DiscountFixtures.newRateInstantPolicy();
            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            policy.addTarget(DiscountTargetType.PRODUCT, 300L, now);
            long policyId = 1L;

            given(policyCommandManager.persist(policy)).willReturn(policyId);
            given(outboxValidator.canCreateOutbox(DiscountTargetType.PRODUCT, 300L))
                    .willReturn(false);

            // when
            sut.createPolicyWithTargets(policy);

            // then
            then(outboxCommandManager).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("updatePolicy() - 정책 수정 + 아웃박스 생성")
    class UpdatePolicyTest {

        @Test
        @DisplayName("정책을 저장하고 활성 타겟에 대해 아웃박스 생성 여부를 검사한다")
        void updatePolicy_PolicyWithActiveTargets_ChecksAndCreatesOutboxes() {
            // given
            DiscountPolicy policy = DiscountFixtures.activeRatePolicy(1L);

            given(policyCommandManager.persist(policy)).willReturn(1L);

            // when
            sut.updatePolicy(policy);

            // then
            then(policyCommandManager).should().persist(policy);
        }

        @Test
        @DisplayName("아웃박스 생성 가능한 활성 타겟에 대해 아웃박스를 생성한다")
        void updatePolicy_EligibleActiveTargets_CreatesOutboxes() {
            // given
            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            DiscountPolicy policy = DiscountFixtures.newRateInstantPolicy();
            policy.addTarget(DiscountTargetType.SELLER, 1L, now);

            given(policyCommandManager.persist(policy)).willReturn(1L);
            given(outboxValidator.canCreateOutbox(DiscountTargetType.SELLER, 1L))
                    .willReturn(true);

            // when
            sut.updatePolicy(policy);

            // then
            then(outboxCommandManager).should().create(
                    org.mockito.ArgumentMatchers.eq(DiscountTargetType.SELLER),
                    org.mockito.ArgumentMatchers.eq(1L),
                    org.mockito.ArgumentMatchers.any(Instant.class));
        }
    }

    @Nested
    @DisplayName("persistTargetDiff() - Diff 기반 타겟 persist + 아웃박스 생성")
    class PersistTargetDiffTest {

        @Test
        @DisplayName("변경이 없으면 아무것도 수행하지 않는다")
        void persistTargetDiff_NoChanges_DoesNothing() {
            // given
            long policyId = 1L;
            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            DiscountTargetDiff diff = DiscountTargetDiff.of(List.of(), List.of(), List.of(), now);

            // when
            sut.persistTargetDiff(policyId, diff);

            // then
            then(targetCommandManager).shouldHaveNoInteractions();
            then(outboxCommandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("변경이 있으면 TargetCommandManager에 위임한다")
        void persistTargetDiff_WithChanges_DelegatesToTargetCommandManager() {
            // given
            long policyId = 1L;
            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            List<DiscountTarget> added = List.of(DiscountFixtures.activeTarget(1L));
            DiscountTargetDiff diff = DiscountTargetDiff.of(added, List.of(), List.of(), now);

            given(outboxValidator.canCreateOutbox(
                    org.mockito.ArgumentMatchers.any(DiscountTargetType.class),
                    org.mockito.ArgumentMatchers.anyLong()))
                    .willReturn(false);

            // when
            sut.persistTargetDiff(policyId, diff);

            // then
            then(targetCommandManager).should().persistDiff(policyId, diff);
        }

        @Test
        @DisplayName("변경된 타겟에 대해 아웃박스 생성 가능 여부를 검사한다")
        void persistTargetDiff_WithChanges_ChecksOutboxEligibility() {
            // given
            long policyId = 1L;
            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            List<DiscountTarget> added = List.of(DiscountFixtures.activeTarget(1L));
            DiscountTargetDiff diff = DiscountTargetDiff.of(added, List.of(), List.of(), now);

            given(outboxValidator.canCreateOutbox(
                    org.mockito.ArgumentMatchers.any(DiscountTargetType.class),
                    org.mockito.ArgumentMatchers.anyLong()))
                    .willReturn(true);

            // when
            sut.persistTargetDiff(policyId, diff);

            // then
            then(outboxValidator).should().canCreateOutbox(
                    org.mockito.ArgumentMatchers.any(DiscountTargetType.class),
                    org.mockito.ArgumentMatchers.anyLong());
        }
    }
}
