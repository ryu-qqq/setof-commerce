package com.ryuqq.setof.application.discount.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.discount.DiscountCommandFixtures;
import com.ryuqq.setof.application.discount.dto.command.CreateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.ModifyDiscountTargetsCommand;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyStatusCommand;
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
@DisplayName("DiscountPolicyCommandFactory 단위 테스트")
class DiscountPolicyCommandFactoryTest {

    @InjectMocks private DiscountPolicyCommandFactory sut;

    @Mock private TimeProvider timeProvider;

    @Nested
    @DisplayName("create() - CreateDiscountPolicyCommand → DiscountPolicy 변환")
    class CreateTest {

        @Test
        @DisplayName("RATE 방식 커맨드로 할인 정책 도메인 객체를 생성한다")
        void create_RateCommand_ReturnsDiscountPolicy() {
            // given
            CreateDiscountPolicyCommand command = DiscountCommandFixtures.createRateCommand();
            Instant now = Instant.parse("2025-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            DiscountPolicy policy = sut.create(command);

            // then
            assertThat(policy).isNotNull();
            assertThat(policy.nameValue()).isEqualTo(command.name());
            assertThat(policy.description()).isEqualTo(command.description());
            assertThat(policy.discountMethod().name()).isEqualTo(command.discountMethod());
            assertThat(policy.discountRateValue()).isEqualTo(command.discountRate());
            assertThat(policy.applicationType().name()).isEqualTo(command.applicationType());
            assertThat(policy.publisherType().name()).isEqualTo(command.publisherType());
            assertThat(policy.stackingGroup().name()).isEqualTo(command.stackingGroup());
            assertThat(policy.priorityValue()).isEqualTo(command.priority());
            assertThat(policy.createdAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("타겟이 포함된 커맨드로 타겟이 있는 할인 정책을 생성한다")
        void create_CommandWithTargets_ReturnsDiscountPolicyWithTargets() {
            // given
            CreateDiscountPolicyCommand command = DiscountCommandFixtures.createRateCommand();
            Instant now = Instant.parse("2025-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            DiscountPolicy policy = sut.create(command);

            // then
            assertThat(policy.activeTargets()).isNotEmpty();
            assertThat(policy.activeTargets()).hasSize(command.targets().size());
        }

        @Test
        @DisplayName("타겟이 없는 커맨드로 타겟이 없는 할인 정책을 생성한다")
        void create_CommandWithoutTargets_ReturnsDiscountPolicyWithNoTargets() {
            // given
            CreateDiscountPolicyCommand command =
                    DiscountCommandFixtures.createCommandWithoutTargets();
            Instant now = Instant.parse("2025-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            DiscountPolicy policy = sut.create(command);

            // then
            assertThat(policy.activeTargets()).isEmpty();
        }

        @Test
        @DisplayName("다중 타겟 커맨드로 모든 타겟이 포함된 정책을 생성한다")
        void create_CommandWithMultipleTargets_ReturnsDiscountPolicyWithAllTargets() {
            // given
            CreateDiscountPolicyCommand command =
                    DiscountCommandFixtures.createCommandWithMultipleTargets();
            Instant now = Instant.parse("2025-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            DiscountPolicy policy = sut.create(command);

            // then
            assertThat(policy.activeTargets()).hasSize(3);
        }

        @Test
        @DisplayName("FIXED_AMOUNT 방식 커맨드로 정액 할인 정책을 생성한다")
        void create_FixedAmountCommand_ReturnsDiscountPolicyWithFixedAmount() {
            // given
            CreateDiscountPolicyCommand command =
                    DiscountCommandFixtures.createFixedAmountCommand();
            Instant now = Instant.parse("2025-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            DiscountPolicy policy = sut.create(command);

            // then
            assertThat(policy.discountAmountValue()).isEqualTo(command.discountAmount());
            assertThat(policy.discountRateValue()).isNull();
        }
    }

    @Nested
    @DisplayName("createUpdateContext() - UpdateDiscountPolicyCommand → UpdateContext 변환")
    class CreateUpdateContextTest {

        @Test
        @DisplayName("수정 커맨드로 UpdateContext를 생성한다")
        void createUpdateContext_ValidCommand_ReturnsUpdateContext() {
            // given
            UpdateDiscountPolicyCommand command = DiscountCommandFixtures.updateCommand(1L);
            Instant now = Instant.parse("2025-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            UpdateContext<Long, DiscountPolicyUpdateData> context =
                    sut.createUpdateContext(command);

            // then
            assertThat(context).isNotNull();
            assertThat(context.id()).isEqualTo(command.discountPolicyId());
            assertThat(context.changedAt()).isEqualTo(now);
            assertThat(context.updateData()).isNotNull();
        }

        @Test
        @DisplayName("UpdateContext의 UpdateData에 커맨드 정책명이 반영된다")
        void createUpdateContext_CommandName_ReflectedInUpdateData() {
            // given
            UpdateDiscountPolicyCommand command = DiscountCommandFixtures.updateCommand(1L);
            Instant now = Instant.parse("2025-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            UpdateContext<Long, DiscountPolicyUpdateData> context =
                    sut.createUpdateContext(command);

            // then
            assertThat(context.updateData().name().value()).isEqualTo(command.name());
        }

        @Test
        @DisplayName("TimeProvider.now()를 통해 changedAt 시간이 설정된다")
        void createUpdateContext_TimeFromProvider_SetAsChangedAt() {
            // given
            UpdateDiscountPolicyCommand command = DiscountCommandFixtures.updateCommand(1L);
            Instant fixedTime = Instant.parse("2025-06-15T12:00:00Z");

            given(timeProvider.now()).willReturn(fixedTime);

            // when
            UpdateContext<Long, DiscountPolicyUpdateData> context =
                    sut.createUpdateContext(command);

            // then
            assertThat(context.changedAt()).isEqualTo(fixedTime);
        }
    }

    @Nested
    @DisplayName(
            "createStatusChangeContext() - UpdateDiscountPolicyStatusCommand → StatusChangeContext"
                    + " 변환")
    class CreateStatusChangeContextTest {

        @Test
        @DisplayName("활성화 커맨드로 StatusChangeContext를 생성한다")
        void createStatusChangeContext_ActivateCommand_ReturnsStatusChangeContext() {
            // given
            UpdateDiscountPolicyStatusCommand command = DiscountCommandFixtures.activateCommand();
            Instant now = Instant.parse("2025-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            StatusChangeContext<UpdateDiscountPolicyStatusCommand> context =
                    sut.createStatusChangeContext(command);

            // then
            assertThat(context).isNotNull();
            assertThat(context.id()).isEqualTo(command);
            assertThat(context.id().active()).isTrue();
            assertThat(context.changedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("비활성화 커맨드로 StatusChangeContext를 생성한다")
        void createStatusChangeContext_DeactivateCommand_ReturnsStatusChangeContext() {
            // given
            UpdateDiscountPolicyStatusCommand command = DiscountCommandFixtures.deactivateCommand();
            Instant now = Instant.parse("2025-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            StatusChangeContext<UpdateDiscountPolicyStatusCommand> context =
                    sut.createStatusChangeContext(command);

            // then
            assertThat(context.id().active()).isFalse();
            assertThat(context.id().policyIds()).containsExactlyElementsOf(command.policyIds());
        }
    }

    @Nested
    @DisplayName(
            "createModifyTargetsContext() - ModifyDiscountTargetsCommand → StatusChangeContext 변환")
    class CreateModifyTargetsContextTest {

        @Test
        @DisplayName("타겟 수정 커맨드로 StatusChangeContext를 생성한다")
        void createModifyTargetsContext_ValidCommand_ReturnsStatusChangeContext() {
            // given
            ModifyDiscountTargetsCommand command = DiscountCommandFixtures.modifyTargetsCommand(1L);
            Instant now = Instant.parse("2025-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            StatusChangeContext<ModifyDiscountTargetsCommand> context =
                    sut.createModifyTargetsContext(command);

            // then
            assertThat(context).isNotNull();
            assertThat(context.id()).isEqualTo(command);
            assertThat(context.id().discountPolicyId()).isEqualTo(command.discountPolicyId());
            assertThat(context.id().targetType()).isEqualTo(command.targetType());
            assertThat(context.changedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("TimeProvider.now()를 통해 타겟 수정 시간이 설정된다")
        void createModifyTargetsContext_TimeFromProvider_SetAsChangedAt() {
            // given
            ModifyDiscountTargetsCommand command = DiscountCommandFixtures.modifyTargetsCommand(1L);
            Instant fixedTime = Instant.parse("2025-03-20T09:30:00Z");

            given(timeProvider.now()).willReturn(fixedTime);

            // when
            StatusChangeContext<ModifyDiscountTargetsCommand> context =
                    sut.createModifyTargetsContext(command);

            // then
            assertThat(context.changedAt()).isEqualTo(fixedTime);
        }
    }
}
