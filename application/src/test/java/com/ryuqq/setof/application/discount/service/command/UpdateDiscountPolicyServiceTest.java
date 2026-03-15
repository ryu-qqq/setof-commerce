package com.ryuqq.setof.application.discount.service.command;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.discount.DiscountCommandFixtures;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.factory.DiscountPolicyCommandFactory;
import com.ryuqq.setof.application.discount.internal.DiscountPolicyCommandCoordinator;
import com.ryuqq.setof.application.discount.manager.DiscountPolicyReadManager;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicyUpdateData;
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
@DisplayName("UpdateDiscountPolicyService 단위 테스트")
class UpdateDiscountPolicyServiceTest {

    @InjectMocks private UpdateDiscountPolicyService sut;

    @Mock private DiscountPolicyReadManager readManager;
    @Mock private DiscountPolicyCommandFactory commandFactory;
    @Mock private DiscountPolicyCommandCoordinator coordinator;

    @Nested
    @DisplayName("execute() - 할인 정책 수정")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 수정 커맨드로 정책을 조회하고 수정 후 저장한다")
        void execute_ValidCommand_UpdatesAndSavesPolicy() {
            // given
            UpdateDiscountPolicyCommand command = DiscountCommandFixtures.updateCommand(1L);
            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            DiscountPolicyUpdateData updateData = DiscountFixtures.rateUpdateData();
            UpdateContext<Long, DiscountPolicyUpdateData> context =
                    new UpdateContext<>(1L, updateData, now);
            DiscountPolicy policy = DiscountFixtures.activeRatePolicy(1L);

            given(commandFactory.createUpdateContext(command)).willReturn(context);
            given(readManager.getById(1L)).willReturn(policy);

            // when
            sut.execute(command);

            // then
            then(commandFactory).should().createUpdateContext(command);
            then(readManager).should().getById(1L);
            then(coordinator).should().updatePolicy(policy);
        }

        @Test
        @DisplayName("Factory → ReadManager → domain.update() → Coordinator 순서로 호출된다")
        void execute_CallsInCorrectOrder() {
            // given
            UpdateDiscountPolicyCommand command = DiscountCommandFixtures.updateCommand(5L);
            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            DiscountPolicyUpdateData updateData = DiscountFixtures.rateUpdateData();
            UpdateContext<Long, DiscountPolicyUpdateData> context =
                    new UpdateContext<>(5L, updateData, now);
            DiscountPolicy policy = DiscountFixtures.activeRatePolicy(5L);

            given(commandFactory.createUpdateContext(command)).willReturn(context);
            given(readManager.getById(5L)).willReturn(policy);

            // when
            sut.execute(command);

            // then: 순서 검증 (InOrder 없이 각 협력 객체 호출 확인)
            then(commandFactory).should().createUpdateContext(command);
            then(readManager).should().getById(5L);
            then(coordinator).should().updatePolicy(policy);
        }
    }
}
