package com.ryuqq.setof.adapter.out.persistence.discountpolicy.adapter;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.ryuqq.setof.adapter.out.persistence.discountpolicy.DiscountTargetJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountTargetJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.mapper.DiscountPolicyJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountTargetJpaRepository;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.aggregate.DiscountTarget;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * DiscountTargetCommandAdapterTest - 할인 적용 대상 Command Adapter 단위 테스트.
 *
 * <p>PER-ADP-001: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-003: Domain -> Entity 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("DiscountTargetCommandAdapter 단위 테스트")
class DiscountTargetCommandAdapterTest {

    @Mock private DiscountTargetJpaRepository jpaRepository;

    @Mock private DiscountPolicyJpaEntityMapper mapper;

    @InjectMocks private DiscountTargetCommandAdapter commandAdapter;

    // ========================================================================
    // 1. persistAll 테스트
    // ========================================================================

    @Nested
    @DisplayName("persistAll 메서드 테스트")
    class PersistAllTest {

        @Test
        @DisplayName("타겟 목록을 Entity로 변환 후 일괄 저장합니다")
        void persistAll_WithValidTargets_SavesAll() {
            // given
            long policyId = 1L;
            DiscountTarget target1 = DiscountFixtures.newTarget();
            DiscountTarget target2 = DiscountFixtures.newTarget();
            List<DiscountTarget> targets = List.of(target1, target2);

            DiscountTargetJpaEntity entity1 =
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(policyId);
            DiscountTargetJpaEntity entity2 =
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(policyId);

            given(
                            mapper.toTargetEntity(
                                    org.mockito.ArgumentMatchers.eq(target1),
                                    org.mockito.ArgumentMatchers.eq(policyId),
                                    org.mockito.ArgumentMatchers.any(Instant.class)))
                    .willReturn(entity1);
            given(
                            mapper.toTargetEntity(
                                    org.mockito.ArgumentMatchers.eq(target2),
                                    org.mockito.ArgumentMatchers.eq(policyId),
                                    org.mockito.ArgumentMatchers.any(Instant.class)))
                    .willReturn(entity2);

            // when
            commandAdapter.persistAll(policyId, targets);

            // then
            @SuppressWarnings("unchecked")
            ArgumentCaptor<List<DiscountTargetJpaEntity>> captor =
                    ArgumentCaptor.forClass(List.class);
            then(jpaRepository).should().saveAll(captor.capture());

            List<DiscountTargetJpaEntity> savedEntities = captor.getValue();
            org.assertj.core.api.Assertions.assertThat(savedEntities).hasSize(2);
        }

        @Test
        @DisplayName("빈 타겟 목록인 경우 saveAll이 호출되지 않습니다")
        void persistAll_WithEmptyTargets_DoesNotCallSaveAll() {
            // given
            long policyId = 1L;
            List<DiscountTarget> emptyTargets = List.of();

            // when
            commandAdapter.persistAll(policyId, emptyTargets);

            // then
            then(jpaRepository).should(never()).saveAll(org.mockito.ArgumentMatchers.any());
        }

        @Test
        @DisplayName("각 타겟에 대해 Mapper가 호출됩니다")
        void persistAll_CallsMapperForEachTarget() {
            // given
            long policyId = 1L;
            DiscountTarget target1 = DiscountFixtures.activeTarget(1L);
            DiscountTarget target2 = DiscountFixtures.activeTarget(2L);
            DiscountTarget target3 = DiscountFixtures.activeTarget(3L);
            List<DiscountTarget> targets = List.of(target1, target2, target3);

            DiscountTargetJpaEntity entity =
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(policyId);
            given(
                            mapper.toTargetEntity(
                                    org.mockito.ArgumentMatchers.any(DiscountTarget.class),
                                    org.mockito.ArgumentMatchers.eq(policyId),
                                    org.mockito.ArgumentMatchers.any(Instant.class)))
                    .willReturn(entity);

            // when
            commandAdapter.persistAll(policyId, targets);

            // then
            then(mapper)
                    .should(org.mockito.Mockito.times(3))
                    .toTargetEntity(
                            org.mockito.ArgumentMatchers.any(DiscountTarget.class),
                            org.mockito.ArgumentMatchers.eq(policyId),
                            org.mockito.ArgumentMatchers.any(Instant.class));
        }
    }

    // ========================================================================
    // 2. updateAll 테스트
    // ========================================================================

    @Nested
    @DisplayName("updateAll 메서드 테스트")
    class UpdateAllTest {

        @Test
        @DisplayName("타겟 목록을 Entity로 변환 후 saveAll로 업데이트합니다")
        void updateAll_WithValidTargets_UpdatesAll() {
            // given
            long policyId = 1L;
            DiscountTarget target1 = DiscountFixtures.activeTarget(1L);
            DiscountTarget target2 = DiscountFixtures.inactiveTarget(2L);
            List<DiscountTarget> targets = List.of(target1, target2);

            DiscountTargetJpaEntity entity1 =
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(policyId, 1L);
            DiscountTargetJpaEntity entity2 =
                    DiscountTargetJpaEntityFixtures.newInactiveProductTarget(policyId);

            given(
                            mapper.toTargetEntity(
                                    org.mockito.ArgumentMatchers.eq(target1),
                                    org.mockito.ArgumentMatchers.eq(policyId),
                                    org.mockito.ArgumentMatchers.any(Instant.class)))
                    .willReturn(entity1);
            given(
                            mapper.toTargetEntity(
                                    org.mockito.ArgumentMatchers.eq(target2),
                                    org.mockito.ArgumentMatchers.eq(policyId),
                                    org.mockito.ArgumentMatchers.any(Instant.class)))
                    .willReturn(entity2);

            // when
            commandAdapter.updateAll(policyId, targets);

            // then
            @SuppressWarnings("unchecked")
            ArgumentCaptor<List<DiscountTargetJpaEntity>> captor =
                    ArgumentCaptor.forClass(List.class);
            then(jpaRepository).should().saveAll(captor.capture());

            List<DiscountTargetJpaEntity> savedEntities = captor.getValue();
            org.assertj.core.api.Assertions.assertThat(savedEntities).hasSize(2);
        }

        @Test
        @DisplayName("빈 타겟 목록인 경우 saveAll이 호출되지 않습니다")
        void updateAll_WithEmptyTargets_DoesNotCallSaveAll() {
            // given
            long policyId = 1L;
            List<DiscountTarget> emptyTargets = List.of();

            // when
            commandAdapter.updateAll(policyId, emptyTargets);

            // then
            then(jpaRepository).should(never()).saveAll(org.mockito.ArgumentMatchers.any());
        }
    }
}
