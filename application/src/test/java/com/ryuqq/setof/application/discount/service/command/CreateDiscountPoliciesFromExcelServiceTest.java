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
@DisplayName("CreateDiscountPoliciesFromExcelService 단위 테스트")
class CreateDiscountPoliciesFromExcelServiceTest {

    @InjectMocks private CreateDiscountPoliciesFromExcelService sut;

    @Mock private DiscountPolicyCommandFactory commandFactory;
    @Mock private DiscountPolicyCommandCoordinator coordinator;

    @Nested
    @DisplayName("execute() - 엑셀 업로드 기반 할인 정책 일괄 생성")
    class ExecuteTest {

        @Test
        @DisplayName("커맨드 목록으로 각 정책을 생성하고 모든 정책 ID를 반환한다")
        void execute_ValidCommands_ReturnsAllPolicyIds() {
            // given
            CreateDiscountPolicyCommand command1 = DiscountCommandFixtures.createRateCommand();
            CreateDiscountPolicyCommand command2 =
                    DiscountCommandFixtures.createFixedAmountCommand();
            List<CreateDiscountPolicyCommand> commands = List.of(command1, command2);

            DiscountPolicy policy1 = DiscountFixtures.newRateInstantPolicy();
            DiscountPolicy policy2 = DiscountFixtures.newFixedInstantPolicy();

            given(commandFactory.create(command1)).willReturn(policy1);
            given(commandFactory.create(command2)).willReturn(policy2);
            given(coordinator.createPolicyWithTargets(policy1)).willReturn(1L);
            given(coordinator.createPolicyWithTargets(policy2)).willReturn(2L);

            // when
            List<Long> result = sut.execute(commands);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(1L, 2L);
            then(commandFactory).should().create(command1);
            then(commandFactory).should().create(command2);
            then(coordinator).should().createPolicyWithTargets(policy1);
            then(coordinator).should().createPolicyWithTargets(policy2);
        }

        @Test
        @DisplayName("단일 커맨드 목록으로 정책 하나를 생성하고 ID를 반환한다")
        void execute_SingleCommand_ReturnsSinglePolicyId() {
            // given
            CreateDiscountPolicyCommand command = DiscountCommandFixtures.createRateCommand();
            List<CreateDiscountPolicyCommand> commands = List.of(command);

            DiscountPolicy policy = DiscountFixtures.newRateInstantPolicy();

            given(commandFactory.create(command)).willReturn(policy);
            given(coordinator.createPolicyWithTargets(policy)).willReturn(10L);

            // when
            List<Long> result = sut.execute(commands);

            // then
            assertThat(result).hasSize(1);
            assertThat(result).containsExactly(10L);
        }

        @Test
        @DisplayName("빈 커맨드 목록으로 빈 ID 목록을 반환한다")
        void execute_EmptyCommands_ReturnsEmptyList() {
            // given
            List<CreateDiscountPolicyCommand> commands = List.of();

            // when
            List<Long> result = sut.execute(commands);

            // then
            assertThat(result).isEmpty();
            then(commandFactory).shouldHaveNoInteractions();
            then(coordinator).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("커맨드 순서에 맞게 정책 ID 순서가 유지된다")
        void execute_MultipleCommands_PreservesIdOrder() {
            // given
            CreateDiscountPolicyCommand command1 = DiscountCommandFixtures.createRateCommand();
            CreateDiscountPolicyCommand command2 =
                    DiscountCommandFixtures.createFixedAmountCommand();
            CreateDiscountPolicyCommand command3 =
                    DiscountCommandFixtures.createCommandWithoutTargets();
            List<CreateDiscountPolicyCommand> commands = List.of(command1, command2, command3);

            DiscountPolicy policy1 = DiscountFixtures.newRateInstantPolicy();
            DiscountPolicy policy2 = DiscountFixtures.newFixedInstantPolicy();
            DiscountPolicy policy3 = DiscountFixtures.newCouponPolicy();

            given(commandFactory.create(command1)).willReturn(policy1);
            given(commandFactory.create(command2)).willReturn(policy2);
            given(commandFactory.create(command3)).willReturn(policy3);
            given(coordinator.createPolicyWithTargets(policy1)).willReturn(100L);
            given(coordinator.createPolicyWithTargets(policy2)).willReturn(200L);
            given(coordinator.createPolicyWithTargets(policy3)).willReturn(300L);

            // when
            List<Long> result = sut.execute(commands);

            // then
            assertThat(result).containsExactly(100L, 200L, 300L);
        }
    }
}
