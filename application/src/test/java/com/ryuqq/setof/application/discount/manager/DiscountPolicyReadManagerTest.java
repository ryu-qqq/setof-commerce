package com.ryuqq.setof.application.discount.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.discount.port.out.query.DiscountPolicyQueryPort;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.exception.DiscountPolicyNotFoundException;
import com.ryuqq.setof.domain.discount.query.DiscountPolicySearchCriteria;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
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
@DisplayName("DiscountPolicyReadManager 단위 테스트")
class DiscountPolicyReadManagerTest {

    @InjectMocks private DiscountPolicyReadManager sut;

    @Mock private DiscountPolicyQueryPort policyQueryPort;

    @Nested
    @DisplayName("findApplicablePolicies() - 적용 가능한 정책 조회")
    class FindApplicablePoliciesTest {

        @Test
        @DisplayName("현재 시점에서 유효한 활성 정책 목록을 반환한다")
        void findApplicablePolicies_ValidPolicies_ReturnsApplicableOnes() {
            // given
            DiscountTargetType targetType = DiscountTargetType.SELLER;
            long targetId = 1L;
            Instant now = Instant.now();

            // defaultActivePeriod()는 yesterday ~ tomorrow로 설정되어 현재 시점에서 유효
            List<DiscountPolicy> activePolicies =
                    List.of(
                            DiscountFixtures.activeRatePolicy(1L),
                            DiscountFixtures.activeFixedPolicy(2L));

            given(policyQueryPort.findActiveByTarget(targetType, targetId))
                    .willReturn(activePolicies);

            // when
            List<DiscountPolicy> result = sut.findApplicablePolicies(targetType, targetId, now);

            // then
            assertThat(result).hasSize(2);
            then(policyQueryPort).should().findActiveByTarget(targetType, targetId);
        }

        @Test
        @DisplayName("기간이 만료된 정책은 필터링하여 제외한다")
        void findApplicablePolicies_ExpiredPolicies_FiltersOut() {
            // given
            DiscountTargetType targetType = DiscountTargetType.SELLER;
            long targetId = 1L;
            Instant now = Instant.now();

            // expiredPeriod()는 이미 종료된 기간으로 설정
            DiscountPolicy expiredPolicy = createPolicyWithExpiredPeriod();
            DiscountPolicy activePolicy = DiscountFixtures.activeRatePolicy(1L);

            given(policyQueryPort.findActiveByTarget(targetType, targetId))
                    .willReturn(List.of(expiredPolicy, activePolicy));

            // when
            List<DiscountPolicy> result = sut.findApplicablePolicies(targetType, targetId, now);

            // then: 만료된 정책은 제외되고 유효한 정책만 반환
            assertThat(result).hasSize(1);
            assertThat(result.get(0)).isEqualTo(activePolicy);
        }

        @Test
        @DisplayName("활성 정책이 없으면 빈 목록을 반환한다")
        void findApplicablePolicies_NoActivePolicies_ReturnsEmptyList() {
            // given
            DiscountTargetType targetType = DiscountTargetType.BRAND;
            long targetId = 5L;
            Instant now = Instant.now();

            given(policyQueryPort.findActiveByTarget(targetType, targetId)).willReturn(List.of());

            // when
            List<DiscountPolicy> result = sut.findApplicablePolicies(targetType, targetId, now);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("PRODUCT 타겟 유형으로도 정상 조회한다")
        void findApplicablePolicies_ProductTargetType_ReturnsCorrectly() {
            // given
            DiscountTargetType targetType = DiscountTargetType.PRODUCT;
            long targetId = 200L;
            Instant now = Instant.now();
            List<DiscountPolicy> policies = List.of(DiscountFixtures.activeRatePolicy(1L));

            given(policyQueryPort.findActiveByTarget(targetType, targetId)).willReturn(policies);

            // when
            List<DiscountPolicy> result = sut.findApplicablePolicies(targetType, targetId, now);

            // then
            assertThat(result).hasSize(1);
            then(policyQueryPort).should().findActiveByTarget(targetType, targetId);
        }
    }

    @Nested
    @DisplayName("getById() - ID로 할인 정책 단건 조회")
    class GetByIdTest {

        @Test
        @DisplayName("존재하는 ID로 할인 정책을 조회한다")
        void getById_ExistingId_ReturnsPolicy() {
            // given
            long policyId = 1L;
            DiscountPolicy policy = DiscountFixtures.activeRatePolicy(policyId);

            given(policyQueryPort.findById(policyId)).willReturn(Optional.of(policy));

            // when
            DiscountPolicy result = sut.getById(policyId);

            // then
            assertThat(result).isEqualTo(policy);
            then(policyQueryPort).should().findById(policyId);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회하면 DiscountPolicyNotFoundException을 던진다")
        void getById_NonExistingId_ThrowsDiscountPolicyNotFoundException() {
            // given
            long policyId = 999L;

            given(policyQueryPort.findById(policyId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getById(policyId))
                    .isInstanceOf(DiscountPolicyNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("findById() - ID로 할인 정책 Optional 조회")
    class FindByIdTest {

        @Test
        @DisplayName("존재하는 ID로 Optional에 담긴 정책을 반환한다")
        void findById_ExistingId_ReturnsPresentOptional() {
            // given
            long policyId = 1L;
            DiscountPolicy policy = DiscountFixtures.activeRatePolicy(policyId);

            given(policyQueryPort.findById(policyId)).willReturn(Optional.of(policy));

            // when
            Optional<DiscountPolicy> result = sut.findById(policyId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(policy);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 빈 Optional을 반환한다")
        void findById_NonExistingId_ReturnsEmptyOptional() {
            // given
            long policyId = 999L;

            given(policyQueryPort.findById(policyId)).willReturn(Optional.empty());

            // when
            Optional<DiscountPolicy> result = sut.findById(policyId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByCriteria() - 검색 조건으로 정책 목록 조회")
    class FindByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 정책 목록을 조회한다")
        void findByCriteria_ValidCriteria_ReturnsPolicies() {
            // given
            DiscountPolicySearchCriteria criteria = DiscountPolicySearchCriteria.defaultCriteria();
            List<DiscountPolicy> policies =
                    List.of(
                            DiscountFixtures.activeRatePolicy(1L),
                            DiscountFixtures.activeFixedPolicy(2L));

            given(policyQueryPort.findByCriteria(criteria)).willReturn(policies);

            // when
            List<DiscountPolicy> result = sut.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(2);
            then(policyQueryPort).should().findByCriteria(criteria);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 목록을 반환한다")
        void findByCriteria_NoResults_ReturnsEmptyList() {
            // given
            DiscountPolicySearchCriteria criteria = DiscountPolicySearchCriteria.defaultCriteria();

            given(policyQueryPort.findByCriteria(criteria)).willReturn(List.of());

            // when
            List<DiscountPolicy> result = sut.findByCriteria(criteria);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("countByCriteria() - 검색 조건으로 정책 총 건수 조회")
    class CountByCriteriaTest {

        @Test
        @DisplayName("검색 조건에 맞는 총 건수를 반환한다")
        void countByCriteria_ValidCriteria_ReturnsCount() {
            // given
            DiscountPolicySearchCriteria criteria = DiscountPolicySearchCriteria.defaultCriteria();

            given(policyQueryPort.countByCriteria(criteria)).willReturn(5L);

            // when
            long result = sut.countByCriteria(criteria);

            // then
            assertThat(result).isEqualTo(5L);
            then(policyQueryPort).should().countByCriteria(criteria);
        }

        @Test
        @DisplayName("검색 결과가 없으면 0을 반환한다")
        void countByCriteria_NoResults_ReturnsZero() {
            // given
            DiscountPolicySearchCriteria criteria = DiscountPolicySearchCriteria.defaultCriteria();

            given(policyQueryPort.countByCriteria(criteria)).willReturn(0L);

            // when
            long result = sut.countByCriteria(criteria);

            // then
            assertThat(result).isZero();
        }
    }

    /**
     * 만료 기간의 정책 생성 헬퍼. DiscountFixtures.deletedPolicy()와 inactivePolicy()는 활성 기간이 기본값이라 기간 만료 시나리오를
     * 위해 별도 메서드 사용.
     */
    private DiscountPolicy createPolicyWithExpiredPeriod() {
        // expiredPeriod()는 twoDaysAgo ~ yesterday로 설정되어 현재 시점에서 만료됨
        return com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy.reconstitute(
                com.ryuqq.setof.domain.discount.id.DiscountPolicyId.of(99L),
                DiscountFixtures.defaultPolicyName(),
                "만료된 정책",
                com.ryuqq.setof.domain.discount.vo.DiscountMethod.RATE,
                DiscountFixtures.defaultDiscountRate(),
                null,
                null,
                false,
                null,
                com.ryuqq.setof.domain.discount.vo.ApplicationType.INSTANT,
                com.ryuqq.setof.domain.discount.vo.PublisherType.ADMIN,
                null,
                com.ryuqq.setof.domain.discount.vo.StackingGroup.PLATFORM_INSTANT,
                DiscountFixtures.defaultPriority(),
                DiscountFixtures.expiredPeriod(),
                DiscountFixtures.defaultBudget(),
                true,
                null,
                List.of(),
                Instant.now().minusSeconds(86400),
                Instant.now().minusSeconds(86400));
    }
}
