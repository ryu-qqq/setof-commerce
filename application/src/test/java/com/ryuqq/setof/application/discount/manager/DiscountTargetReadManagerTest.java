package com.ryuqq.setof.application.discount.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.discount.port.out.query.DiscountTargetQueryPort;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.aggregate.DiscountTarget;
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
@DisplayName("DiscountTargetReadManager 단위 테스트")
class DiscountTargetReadManagerTest {

    @InjectMocks private DiscountTargetReadManager sut;

    @Mock private DiscountTargetQueryPort targetQueryPort;

    @Nested
    @DisplayName("findByPolicyId() - 정책 ID로 타겟 조회")
    class FindByPolicyIdTest {

        @Test
        @DisplayName("정책 ID로 타겟 목록을 조회한다")
        void findByPolicyId_ValidPolicyId_ReturnsTargets() {
            // given
            long policyId = 1L;
            List<DiscountTarget> targets =
                    List.of(DiscountFixtures.activeTarget(1L), DiscountFixtures.activeTarget(2L));

            given(targetQueryPort.findByPolicyId(policyId)).willReturn(targets);

            // when
            List<DiscountTarget> result = sut.findByPolicyId(policyId);

            // then
            assertThat(result).hasSize(2);
            then(targetQueryPort).should().findByPolicyId(policyId);
        }

        @Test
        @DisplayName("타겟이 없으면 빈 목록을 반환한다")
        void findByPolicyId_NoTargets_ReturnsEmptyList() {
            // given
            long policyId = 99L;

            given(targetQueryPort.findByPolicyId(policyId)).willReturn(List.of());

            // when
            List<DiscountTarget> result = sut.findByPolicyId(policyId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByPolicyIds() - 다수 정책 ID로 타겟 일괄 조회")
    class FindByPolicyIdsTest {

        @Test
        @DisplayName("여러 정책 ID로 타겟 목록을 일괄 조회한다")
        void findByPolicyIds_ValidPolicyIds_ReturnsTargets() {
            // given
            List<Long> policyIds = List.of(1L, 2L, 3L);
            List<DiscountTarget> targets =
                    List.of(DiscountFixtures.activeTarget(1L), DiscountFixtures.activeTarget(2L));

            given(targetQueryPort.findByPolicyIds(policyIds)).willReturn(targets);

            // when
            List<DiscountTarget> result = sut.findByPolicyIds(policyIds);

            // then
            assertThat(result).hasSize(2);
            then(targetQueryPort).should().findByPolicyIds(policyIds);
        }
    }

    @Nested
    @DisplayName("countByPolicyId() - 정책별 활성 타겟 수 조회")
    class CountByPolicyIdTest {

        @Test
        @DisplayName("정책 ID로 활성 타겟 수를 반환한다")
        void countByPolicyId_ValidPolicyId_ReturnsCount() {
            // given
            long policyId = 1L;

            given(targetQueryPort.countByPolicyId(policyId)).willReturn(3L);

            // when
            long result = sut.countByPolicyId(policyId);

            // then
            assertThat(result).isEqualTo(3L);
            then(targetQueryPort).should().countByPolicyId(policyId);
        }

        @Test
        @DisplayName("타겟이 없으면 0을 반환한다")
        void countByPolicyId_NoTargets_ReturnsZero() {
            // given
            long policyId = 99L;

            given(targetQueryPort.countByPolicyId(policyId)).willReturn(0L);

            // when
            long result = sut.countByPolicyId(policyId);

            // then
            assertThat(result).isZero();
        }
    }
}
