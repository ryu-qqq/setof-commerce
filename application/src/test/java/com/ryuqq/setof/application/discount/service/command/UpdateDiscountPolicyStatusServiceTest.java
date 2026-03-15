package com.ryuqq.setof.application.discount.service.command;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.discount.DiscountCommandFixtures;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyStatusCommand;
import com.ryuqq.setof.application.discount.factory.DiscountPolicyCommandFactory;
import com.ryuqq.setof.application.discount.internal.DiscountPolicyCommandCoordinator;
import com.ryuqq.setof.application.discount.manager.DiscountPolicyReadManager;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
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
@DisplayName("UpdateDiscountPolicyStatusService 단위 테스트")
class UpdateDiscountPolicyStatusServiceTest {

    @InjectMocks private UpdateDiscountPolicyStatusService sut;

    @Mock private DiscountPolicyReadManager readManager;
    @Mock private DiscountPolicyCommandFactory commandFactory;
    @Mock private DiscountPolicyCommandCoordinator coordinator;

    @Nested
    @DisplayName("execute() - 할인 정책 상태 일괄 변경")
    class ExecuteTest {

        @Test
        @DisplayName("활성화 커맨드로 여러 정책을 활성화하고 Coordinator를 호출한다")
        void execute_ActivateCommand_ActivatesPoliciesAndCallsCoordinator() {
            // given
            UpdateDiscountPolicyStatusCommand command =
                    DiscountCommandFixtures.activateCommand(List.of(1L, 2L));
            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            StatusChangeContext<UpdateDiscountPolicyStatusCommand> context =
                    new StatusChangeContext<>(command, now);

            DiscountPolicy policy1 = DiscountFixtures.inactivePolicy();
            DiscountPolicy policy2 = DiscountFixtures.inactivePolicy();

            given(commandFactory.createStatusChangeContext(command)).willReturn(context);
            given(readManager.getById(1L)).willReturn(policy1);
            given(readManager.getById(2L)).willReturn(policy2);

            // when
            sut.execute(command);

            // then
            then(commandFactory).should().createStatusChangeContext(command);
            then(readManager).should().getById(1L);
            then(readManager).should().getById(2L);
            then(coordinator).should().updatePolicy(policy1);
            then(coordinator).should().updatePolicy(policy2);
        }

        @Test
        @DisplayName("비활성화 커맨드로 정책을 비활성화하고 Coordinator를 호출한다")
        void execute_DeactivateCommand_DeactivatesPoliciesAndCallsCoordinator() {
            // given
            UpdateDiscountPolicyStatusCommand command =
                    DiscountCommandFixtures.deactivateCommand(List.of(1L));
            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            StatusChangeContext<UpdateDiscountPolicyStatusCommand> context =
                    new StatusChangeContext<>(command, now);

            DiscountPolicy policy = DiscountFixtures.activeRatePolicy(1L);

            given(commandFactory.createStatusChangeContext(command)).willReturn(context);
            given(readManager.getById(1L)).willReturn(policy);

            // when
            sut.execute(command);

            // then
            then(commandFactory).should().createStatusChangeContext(command);
            then(readManager).should().getById(1L);
            then(coordinator).should().updatePolicy(policy);
        }

        @Test
        @DisplayName("단일 정책 활성화 커맨드를 처리한다")
        void execute_SinglePolicyActivateCommand_ProcessesCorrectly() {
            // given
            UpdateDiscountPolicyStatusCommand command =
                    DiscountCommandFixtures.singleActivateCommand(5L);
            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            StatusChangeContext<UpdateDiscountPolicyStatusCommand> context =
                    new StatusChangeContext<>(command, now);

            DiscountPolicy policy = DiscountFixtures.inactivePolicy();

            given(commandFactory.createStatusChangeContext(command)).willReturn(context);
            given(readManager.getById(5L)).willReturn(policy);

            // when
            sut.execute(command);

            // then
            then(readManager).should().getById(5L);
            then(coordinator).should().updatePolicy(policy);
        }
    }
}
