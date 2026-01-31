package com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.refundpolicy.RefundPolicyApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.command.ChangeRefundPolicyStatusApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.command.RegisterRefundPolicyApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.command.UpdateRefundPolicyApiRequest;
import com.ryuqq.setof.application.refundpolicy.dto.command.ChangeRefundPolicyStatusCommand;
import com.ryuqq.setof.application.refundpolicy.dto.command.RegisterRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.command.UpdateRefundPolicyCommand;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * RefundPolicyCommandApiMapper 단위 테스트.
 *
 * <p>Command API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("RefundPolicyCommandApiMapper 단위 테스트")
class RefundPolicyCommandApiMapperTest {

    private RefundPolicyCommandApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RefundPolicyCommandApiMapper();
    }

    @Nested
    @DisplayName("toCommand(RegisterRefundPolicyApiRequest)")
    class ToRegisterCommandTest {

        @Test
        @DisplayName("등록 요청을 Command로 변환한다")
        void toCommand_Register_Success() {
            // given
            Long sellerId = 1L;
            RegisterRefundPolicyApiRequest request = RefundPolicyApiFixtures.registerRequest();

            // when
            RegisterRefundPolicyCommand command = mapper.toCommand(sellerId, request);

            // then
            assertThat(command.sellerId()).isEqualTo(sellerId);
            assertThat(command.policyName()).isEqualTo(request.policyName());
            assertThat(command.defaultPolicy()).isEqualTo(request.defaultPolicy());
            assertThat(command.returnPeriodDays()).isEqualTo(request.returnPeriodDays());
            assertThat(command.exchangePeriodDays()).isEqualTo(request.exchangePeriodDays());
            assertThat(command.nonReturnableConditions())
                    .containsExactlyElementsOf(request.nonReturnableConditions());
            assertThat(command.partialRefundEnabled()).isEqualTo(request.partialRefundEnabled());
            assertThat(command.inspectionRequired()).isEqualTo(request.inspectionRequired());
            assertThat(command.inspectionPeriodDays()).isEqualTo(request.inspectionPeriodDays());
            assertThat(command.additionalInfo()).isEqualTo(request.additionalInfo());
        }

        @Test
        @DisplayName("null nonReturnableConditions를 빈 리스트로 변환한다")
        void toCommand_NullConditions_ReturnsEmptyList() {
            // given
            Long sellerId = 1L;
            RegisterRefundPolicyApiRequest request =
                    new RegisterRefundPolicyApiRequest(
                            "정책명", true, 7, 14, null, true, false, 0, null);

            // when
            RegisterRefundPolicyCommand command = mapper.toCommand(sellerId, request);

            // then
            assertThat(command.nonReturnableConditions()).isEmpty();
        }
    }

    @Nested
    @DisplayName("toCommand(UpdateRefundPolicyApiRequest)")
    class ToUpdateCommandTest {

        @Test
        @DisplayName("수정 요청을 Command로 변환한다")
        void toCommand_Update_Success() {
            // given
            Long sellerId = 1L;
            Long policyId = 100L;
            UpdateRefundPolicyApiRequest request = RefundPolicyApiFixtures.updateRequest();

            // when
            UpdateRefundPolicyCommand command = mapper.toCommand(sellerId, policyId, request);

            // then
            assertThat(command.sellerId()).isEqualTo(sellerId);
            assertThat(command.policyId()).isEqualTo(policyId);
            assertThat(command.policyName()).isEqualTo(request.policyName());
            assertThat(command.defaultPolicy()).isEqualTo(request.defaultPolicy());
            assertThat(command.returnPeriodDays()).isEqualTo(request.returnPeriodDays());
            assertThat(command.exchangePeriodDays()).isEqualTo(request.exchangePeriodDays());
        }

        @Test
        @DisplayName("null nonReturnableConditions를 빈 리스트로 변환한다")
        void toCommand_NullConditions_ReturnsEmptyList() {
            // given
            Long sellerId = 1L;
            Long policyId = 100L;
            UpdateRefundPolicyApiRequest request =
                    new UpdateRefundPolicyApiRequest(
                            "수정된 정책명", false, 7, 7, null, false, false, 0, null);

            // when
            UpdateRefundPolicyCommand command = mapper.toCommand(sellerId, policyId, request);

            // then
            assertThat(command.nonReturnableConditions()).isEmpty();
        }
    }

    @Nested
    @DisplayName("toCommand(ChangeRefundPolicyStatusApiRequest)")
    class ToChangeStatusCommandTest {

        @Test
        @DisplayName("상태 변경 요청을 Command로 변환한다")
        void toCommand_ChangeStatus_Success() {
            // given
            Long sellerId = 1L;
            List<Long> policyIds = List.of(1L, 2L, 3L);
            ChangeRefundPolicyStatusApiRequest request =
                    RefundPolicyApiFixtures.activateRequest(policyIds);

            // when
            ChangeRefundPolicyStatusCommand command = mapper.toCommand(sellerId, request);

            // then
            assertThat(command.sellerId()).isEqualTo(sellerId);
            assertThat(command.policyIds()).containsExactlyElementsOf(policyIds);
            assertThat(command.active()).isTrue();
        }

        @Test
        @DisplayName("비활성화 요청을 Command로 변환한다")
        void toCommand_Deactivate_Success() {
            // given
            Long sellerId = 1L;
            List<Long> policyIds = List.of(1L, 2L);
            ChangeRefundPolicyStatusApiRequest request =
                    RefundPolicyApiFixtures.deactivateRequest(policyIds);

            // when
            ChangeRefundPolicyStatusCommand command = mapper.toCommand(sellerId, request);

            // then
            assertThat(command.active()).isFalse();
        }
    }
}
