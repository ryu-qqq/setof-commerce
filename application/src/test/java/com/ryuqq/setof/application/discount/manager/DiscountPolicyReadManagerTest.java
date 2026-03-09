package com.ryuqq.setof.application.discount.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.discount.port.out.query.DiscountPolicyQueryPort;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
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
