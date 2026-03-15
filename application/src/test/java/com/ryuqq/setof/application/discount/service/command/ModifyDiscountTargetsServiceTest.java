package com.ryuqq.setof.application.discount.service.command;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.discount.DiscountCommandFixtures;
import com.ryuqq.setof.application.discount.dto.command.ModifyDiscountTargetsCommand;
import com.ryuqq.setof.application.discount.factory.DiscountPolicyCommandFactory;
import com.ryuqq.setof.application.discount.internal.DiscountPolicyCommandCoordinator;
import com.ryuqq.setof.application.discount.manager.DiscountPolicyReadManager;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetDiff;
import java.time.Instant;
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
@DisplayName("ModifyDiscountTargetsService 단위 테스트")
class ModifyDiscountTargetsServiceTest {

    @InjectMocks private ModifyDiscountTargetsService sut;

    @Mock private DiscountPolicyReadManager readManager;
    @Mock private DiscountPolicyCommandFactory commandFactory;
    @Mock private DiscountPolicyCommandCoordinator coordinator;

    @Nested
    @DisplayName("execute() - 할인 적용 대상 수정")
    class ExecuteTest {

        @Test
        @DisplayName("타겟 수정 커맨드로 정책을 조회하고 타겟을 교체한 뒤 Coordinator에 위임한다")
        void execute_ValidCommand_ReplacesTargetsAndCallsCoordinator() {
            // given
            ModifyDiscountTargetsCommand command = DiscountCommandFixtures.modifyTargetsCommand(1L);
            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            StatusChangeContext<ModifyDiscountTargetsCommand> context =
                    new StatusChangeContext<>(command, now);

            DiscountPolicy policy = DiscountFixtures.activeRatePolicy(1L);

            given(commandFactory.createModifyTargetsContext(command)).willReturn(context);
            given(readManager.getById(1L)).willReturn(policy);

            // when
            sut.execute(command);

            // then
            then(commandFactory).should().createModifyTargetsContext(command);
            then(readManager).should().getById(1L);
            then(coordinator)
                    .should()
                    .persistTargetDiff(
                            org.mockito.ArgumentMatchers.eq(1L),
                            org.mockito.ArgumentMatchers.any(DiscountTargetDiff.class));
        }

        @Test
        @DisplayName("Factory → ReadManager → domain.replaceTargets() → Coordinator 순서로 처리된다")
        void execute_ProcessingOrder_IsCorrect() {
            // given
            ModifyDiscountTargetsCommand command = DiscountCommandFixtures.modifyTargetsCommand(2L);
            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            StatusChangeContext<ModifyDiscountTargetsCommand> context =
                    new StatusChangeContext<>(command, now);

            DiscountPolicy policy = DiscountFixtures.activeRatePolicy(2L);

            given(commandFactory.createModifyTargetsContext(command)).willReturn(context);
            given(readManager.getById(2L)).willReturn(policy);

            // when
            sut.execute(command);

            // then
            then(commandFactory).should().createModifyTargetsContext(command);
            then(readManager).should().getById(2L);
            then(coordinator)
                    .should()
                    .persistTargetDiff(
                            org.mockito.ArgumentMatchers.eq(2L),
                            org.mockito.ArgumentMatchers.any(DiscountTargetDiff.class));
        }
    }
}
