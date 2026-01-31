package com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.shippingpolicy.ShippingPolicyApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command.ChangeShippingPolicyStatusApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command.RegisterShippingPolicyApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command.UpdateShippingPolicyApiRequest;
import com.ryuqq.setof.application.shippingpolicy.dto.command.ChangeShippingPolicyStatusCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.command.RegisterShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.command.UpdateShippingPolicyCommand;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ShippingPolicyCommandApiMapper 단위 테스트.
 *
 * <p>Command API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("ShippingPolicyCommandApiMapper 단위 테스트")
class ShippingPolicyCommandApiMapperTest {

    private ShippingPolicyCommandApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ShippingPolicyCommandApiMapper();
    }

    @Nested
    @DisplayName("toCommand(RegisterShippingPolicyApiRequest)")
    class ToRegisterCommandTest {

        @Test
        @DisplayName("등록 요청을 Command로 변환한다")
        void toCommand_Register_Success() {
            // given
            Long sellerId = 1L;
            RegisterShippingPolicyApiRequest request = ShippingPolicyApiFixtures.registerRequest();

            // when
            RegisterShippingPolicyCommand command = mapper.toCommand(sellerId, request);

            // then
            assertThat(command.sellerId()).isEqualTo(sellerId);
            assertThat(command.policyName()).isEqualTo(request.policyName());
            assertThat(command.defaultPolicy()).isEqualTo(request.defaultPolicy());
            assertThat(command.shippingFeeType()).isEqualTo(request.shippingFeeType());
            assertThat(command.baseFee()).isEqualTo(request.baseFee());
            assertThat(command.freeThreshold()).isEqualTo(request.freeThreshold());
            assertThat(command.jejuExtraFee()).isEqualTo(request.jejuExtraFee());
            assertThat(command.islandExtraFee()).isEqualTo(request.islandExtraFee());
            assertThat(command.returnFee()).isEqualTo(request.returnFee());
            assertThat(command.exchangeFee()).isEqualTo(request.exchangeFee());
            assertThat(command.leadTime()).isNotNull();
            assertThat(command.leadTime().minDays()).isEqualTo(request.leadTime().minDays());
            assertThat(command.leadTime().maxDays()).isEqualTo(request.leadTime().maxDays());
            assertThat(command.leadTime().cutoffTime()).isEqualTo(request.leadTime().cutoffTime());
        }

        @Test
        @DisplayName("null leadTime을 null로 변환한다")
        void toCommand_NullLeadTime_ReturnsNull() {
            // given
            Long sellerId = 1L;
            RegisterShippingPolicyApiRequest request =
                    new RegisterShippingPolicyApiRequest(
                            "정책명", true, "PAID", 3000L, 0L, 3000L, 5000L, 3000L, 6000L, null);

            // when
            RegisterShippingPolicyCommand command = mapper.toCommand(sellerId, request);

            // then
            assertThat(command.leadTime()).isNull();
        }
    }

    @Nested
    @DisplayName("toCommand(UpdateShippingPolicyApiRequest)")
    class ToUpdateCommandTest {

        @Test
        @DisplayName("수정 요청을 Command로 변환한다")
        void toCommand_Update_Success() {
            // given
            Long sellerId = 1L;
            Long policyId = 200L;
            UpdateShippingPolicyApiRequest request = ShippingPolicyApiFixtures.updateRequest();

            // when
            UpdateShippingPolicyCommand command = mapper.toCommand(sellerId, policyId, request);

            // then
            assertThat(command.sellerId()).isEqualTo(sellerId);
            assertThat(command.policyId()).isEqualTo(policyId);
            assertThat(command.policyName()).isEqualTo(request.policyName());
            assertThat(command.defaultPolicy()).isEqualTo(request.defaultPolicy());
            assertThat(command.shippingFeeType()).isEqualTo(request.shippingFeeType());
            assertThat(command.baseFee()).isEqualTo(request.baseFee());
            assertThat(command.freeThreshold()).isEqualTo(request.freeThreshold());
            assertThat(command.jejuExtraFee()).isEqualTo(request.jejuExtraFee());
            assertThat(command.islandExtraFee()).isEqualTo(request.islandExtraFee());
            assertThat(command.returnFee()).isEqualTo(request.returnFee());
            assertThat(command.exchangeFee()).isEqualTo(request.exchangeFee());
        }

        @Test
        @DisplayName("null leadTime을 null로 변환한다")
        void toCommand_NullLeadTime_ReturnsNull() {
            // given
            Long sellerId = 1L;
            Long policyId = 200L;
            UpdateShippingPolicyApiRequest request =
                    ShippingPolicyApiFixtures.updateRequest("수정된 정책명");

            // when
            UpdateShippingPolicyCommand command = mapper.toCommand(sellerId, policyId, request);

            // then
            assertThat(command.leadTime()).isNull();
        }
    }

    @Nested
    @DisplayName("toCommand(ChangeShippingPolicyStatusApiRequest)")
    class ToChangeStatusCommandTest {

        @Test
        @DisplayName("상태 변경 요청을 Command로 변환한다")
        void toCommand_ChangeStatus_Success() {
            // given
            Long sellerId = 1L;
            List<Long> policyIds = List.of(1L, 2L, 3L);
            ChangeShippingPolicyStatusApiRequest request =
                    ShippingPolicyApiFixtures.activateRequest(policyIds);

            // when
            ChangeShippingPolicyStatusCommand command = mapper.toCommand(sellerId, request);

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
            ChangeShippingPolicyStatusApiRequest request =
                    ShippingPolicyApiFixtures.deactivateRequest(policyIds);

            // when
            ChangeShippingPolicyStatusCommand command = mapper.toCommand(sellerId, request);

            // then
            assertThat(command.active()).isFalse();
        }
    }
}
