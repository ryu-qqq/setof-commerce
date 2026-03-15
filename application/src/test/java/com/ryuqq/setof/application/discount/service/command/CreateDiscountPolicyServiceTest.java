package com.ryuqq.setof.application.discount.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.discount.DiscountCommandFixtures;
import com.ryuqq.setof.application.discount.dto.command.CreateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.factory.DiscountPolicyCommandFactory;
import com.ryuqq.setof.application.discount.internal.DiscountPolicyCommandCoordinator;
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
@DisplayName("CreateDiscountPolicyService 단위 테스트")
class CreateDiscountPolicyServiceTest {

    @InjectMocks private CreateDiscountPolicyService sut;

    @Mock private DiscountPolicyCommandFactory commandFactory;
    @Mock private DiscountPolicyCommandCoordinator coordinator;

    @Nested
    @DisplayName("execute() - 할인 정책 생성")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 할인 정책을 생성하고 정책 ID를 반환한다")
        void execute_ValidCommand_ReturnsPolicyId() {
            // given
            CreateDiscountPolicyCommand command = DiscountCommandFixtures.createRateCommand();
            DiscountPolicy policy = DiscountFixtures.newRateInstantPolicy();
            long expectedPolicyId = 1L;

            given(commandFactory.create(command)).willReturn(policy);
            given(coordinator.createPolicyWithTargets(policy)).willReturn(expectedPolicyId);

            // when
            long result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedPolicyId);
            then(commandFactory).should().create(command);
            then(coordinator).should().createPolicyWithTargets(policy);
        }

        @Test
        @DisplayName("Factory에서 생성된 도메인 객체가 그대로 Coordinator에 전달된다")
        void execute_DomainObjectFromFactory_PassedToCoordinator() {
            // given
            CreateDiscountPolicyCommand command = DiscountCommandFixtures.createRateCommand();
            DiscountPolicy policy = DiscountFixtures.newRateInstantPolicy();

            given(commandFactory.create(command)).willReturn(policy);
            given(coordinator.createPolicyWithTargets(policy)).willReturn(10L);

            // when
            sut.execute(command);

            // then
            then(commandFactory).should().create(command);
            then(coordinator).should().createPolicyWithTargets(policy);
            then(commandFactory).shouldHaveNoMoreInteractions();
            then(coordinator).shouldHaveNoMoreInteractions();
        }
    }
}
