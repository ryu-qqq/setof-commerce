package com.ryuqq.setof.application.discount.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResult;
import com.ryuqq.setof.application.discount.manager.DiscountPolicyReadManager;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
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
@DisplayName("GetDiscountPolicyDetailService 단위 테스트")
class GetDiscountPolicyDetailServiceTest {

    @InjectMocks private GetDiscountPolicyDetailService sut;

    @Mock private DiscountPolicyReadManager readManager;

    @Nested
    @DisplayName("execute() - 할인 정책 단건 조회")
    class ExecuteTest {

        @Test
        @DisplayName("정책 ID로 할인 정책 상세를 조회하고 결과 DTO로 변환한다")
        void execute_ValidId_ReturnsPolicyResult() {
            // given
            long policyId = 1L;
            DiscountPolicy policy = DiscountFixtures.activeRatePolicy(policyId);

            given(readManager.getById(policyId)).willReturn(policy);

            // when
            DiscountPolicyResult result = sut.execute(policyId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(policyId);
            assertThat(result.name()).isEqualTo(policy.nameValue());
            assertThat(result.active()).isTrue();
            then(readManager).should().getById(policyId);
        }

        @Test
        @DisplayName("비활성 정책도 조회하여 결과 DTO로 반환한다")
        void execute_InactivePolicy_ReturnsPolicyResultWithInactiveStatus() {
            // given
            long policyId = 2L;
            DiscountPolicy policy = DiscountFixtures.inactivePolicy();

            given(readManager.getById(policyId)).willReturn(policy);

            // when
            DiscountPolicyResult result = sut.execute(policyId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.active()).isFalse();
            then(readManager).should().getById(policyId);
        }

        @Test
        @DisplayName("도메인 정책의 할인 방식이 결과 DTO에 정확히 반영된다")
        void execute_PolicyWithDiscountMethod_ReflectsMethodInResult() {
            // given
            long policyId = 1L;
            DiscountPolicy policy = DiscountFixtures.activeRatePolicy(policyId);

            given(readManager.getById(policyId)).willReturn(policy);

            // when
            DiscountPolicyResult result = sut.execute(policyId);

            // then
            assertThat(result.discountMethod()).isEqualTo(policy.discountMethod().name());
            assertThat(result.discountRate()).isEqualTo(policy.discountRateValue());
        }
    }
}
