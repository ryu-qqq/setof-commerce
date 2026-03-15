package com.ryuqq.setof.application.discount.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.discount.port.out.command.DiscountPolicyCommandPort;
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
@DisplayName("DiscountPolicyCommandManager 단위 테스트")
class DiscountPolicyCommandManagerTest {

    @InjectMocks private DiscountPolicyCommandManager sut;

    @Mock private DiscountPolicyCommandPort policyCommandPort;

    @Nested
    @DisplayName("persist() - 할인 정책 저장")
    class PersistTest {

        @Test
        @DisplayName("신규 정책을 저장하고 정책 ID를 반환한다")
        void persist_NewPolicy_ReturnsPolicyId() {
            // given
            DiscountPolicy policy = DiscountFixtures.newRateInstantPolicy();
            long expectedId = 1L;

            given(policyCommandPort.persist(policy)).willReturn(expectedId);

            // when
            long result = sut.persist(policy);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(policyCommandPort).should().persist(policy);
        }

        @Test
        @DisplayName("기존 정책 수정 후 저장하고 정책 ID를 반환한다")
        void persist_ExistingPolicy_ReturnsPolicyId() {
            // given
            DiscountPolicy policy = DiscountFixtures.activeRatePolicy(5L);
            long expectedId = 5L;

            given(policyCommandPort.persist(policy)).willReturn(expectedId);

            // when
            long result = sut.persist(policy);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(policyCommandPort).should().persist(policy);
        }

        @Test
        @DisplayName("CommandPort에 저장 처리를 위임한다")
        void persist_DelegatesToCommandPort() {
            // given
            DiscountPolicy policy = DiscountFixtures.activeRatePolicy(1L);

            given(policyCommandPort.persist(policy)).willReturn(1L);

            // when
            sut.persist(policy);

            // then
            then(policyCommandPort).should().persist(policy);
            then(policyCommandPort).shouldHaveNoMoreInteractions();
        }
    }
}
