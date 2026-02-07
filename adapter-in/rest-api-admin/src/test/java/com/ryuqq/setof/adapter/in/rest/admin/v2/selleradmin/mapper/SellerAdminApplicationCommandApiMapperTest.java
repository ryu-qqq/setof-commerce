package com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.selleradmin.SellerAdminApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.dto.command.ApplySellerAdminApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.dto.command.BulkApproveSellerAdminApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.dto.command.BulkRejectSellerAdminApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.dto.response.BulkApproveSellerAdminApiResponse;
import com.ryuqq.setof.application.common.dto.result.BatchProcessingResult;
import com.ryuqq.setof.application.selleradmin.dto.command.ApplySellerAdminCommand;
import com.ryuqq.setof.application.selleradmin.dto.command.ApproveSellerAdminCommand;
import com.ryuqq.setof.application.selleradmin.dto.command.BulkApproveSellerAdminCommand;
import com.ryuqq.setof.application.selleradmin.dto.command.BulkRejectSellerAdminCommand;
import com.ryuqq.setof.application.selleradmin.dto.command.RejectSellerAdminCommand;
import com.ryuqq.setof.application.selleradmin.dto.command.ResetSellerAdminPasswordCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * SellerAdminApplicationCommandApiMapper 단위 테스트.
 *
 * <p>Command API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("SellerAdminApplicationCommandApiMapper 단위 테스트")
class SellerAdminApplicationCommandApiMapperTest {

    private SellerAdminApplicationCommandApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SellerAdminApplicationCommandApiMapper();
    }

    @Nested
    @DisplayName("toCommand(ApplySellerAdminApiRequest)")
    class ToApplyCommandTest {

        @Test
        @DisplayName("가입 신청 요청을 Command로 변환한다")
        void toCommand_Apply_Success() {
            // given
            ApplySellerAdminApiRequest request = SellerAdminApiFixtures.applyRequest();

            // when
            ApplySellerAdminCommand command = mapper.toCommand(request);

            // then
            assertThat(command.sellerId()).isEqualTo(request.sellerId());
            assertThat(command.loginId()).isEqualTo(request.loginId());
            assertThat(command.name()).isEqualTo(request.name());
            assertThat(command.phoneNumber()).isEqualTo(request.phoneNumber());
            assertThat(command.password()).isEqualTo(request.password());
        }
    }

    @Nested
    @DisplayName("toApproveCommand(String)")
    class ToApproveCommandTest {

        @Test
        @DisplayName("승인 요청을 Command로 변환한다")
        void toApproveCommand_Success() {
            // given
            String sellerAdminId = SellerAdminApiFixtures.sellerAdminId();

            // when
            ApproveSellerAdminCommand command = mapper.toApproveCommand(sellerAdminId);

            // then
            assertThat(command.sellerAdminId()).isEqualTo(sellerAdminId);
        }
    }

    @Nested
    @DisplayName("toRejectCommand(String)")
    class ToRejectCommandTest {

        @Test
        @DisplayName("거절 요청을 Command로 변환한다")
        void toRejectCommand_Success() {
            // given
            String sellerAdminId = SellerAdminApiFixtures.sellerAdminId();

            // when
            RejectSellerAdminCommand command = mapper.toRejectCommand(sellerAdminId);

            // then
            assertThat(command.sellerAdminId()).isEqualTo(sellerAdminId);
        }
    }

    @Nested
    @DisplayName("toBulkApproveCommand(BulkApproveSellerAdminApiRequest)")
    class ToBulkApproveCommandTest {

        @Test
        @DisplayName("일괄 승인 요청을 Command로 변환한다")
        void toBulkApproveCommand_Success() {
            // given
            BulkApproveSellerAdminApiRequest request = SellerAdminApiFixtures.bulkApproveRequest();

            // when
            BulkApproveSellerAdminCommand command = mapper.toBulkApproveCommand(request);

            // then
            assertThat(command.sellerAdminIds()).isEqualTo(request.sellerAdminIds());
        }
    }

    @Nested
    @DisplayName("toBulkRejectCommand(BulkRejectSellerAdminApiRequest)")
    class ToBulkRejectCommandTest {

        @Test
        @DisplayName("일괄 거절 요청을 Command로 변환한다")
        void toBulkRejectCommand_Success() {
            // given
            BulkRejectSellerAdminApiRequest request = SellerAdminApiFixtures.bulkRejectRequest();

            // when
            BulkRejectSellerAdminCommand command = mapper.toBulkRejectCommand(request);

            // then
            assertThat(command.sellerAdminIds()).isEqualTo(request.sellerAdminIds());
        }
    }

    @Nested
    @DisplayName("toResetPasswordCommand(String)")
    class ToResetPasswordCommandTest {

        @Test
        @DisplayName("비밀번호 초기화 요청을 Command로 변환한다")
        void toResetPasswordCommand_Success() {
            // given
            String sellerAdminId = SellerAdminApiFixtures.sellerAdminId();

            // when
            ResetSellerAdminPasswordCommand command = mapper.toResetPasswordCommand(sellerAdminId);

            // then
            assertThat(command.sellerAdminId()).isEqualTo(sellerAdminId);
        }
    }

    @Nested
    @DisplayName("toResponse(BatchProcessingResult)")
    class ToResponseTest {

        @Test
        @DisplayName("일괄 승인 결과를 API Response로 변환한다")
        void toResponse_Success() {
            // given
            BatchProcessingResult<String> result = SellerAdminApiFixtures.batchProcessingResult();

            // when
            BulkApproveSellerAdminApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.totalCount()).isEqualTo(result.totalCount());
            assertThat(response.successCount()).isEqualTo(result.successCount());
            assertThat(response.failureCount()).isEqualTo(result.failureCount());
            assertThat(response.results()).hasSize(result.results().size());
            assertThat(response.results().getFirst().success()).isTrue();
        }
    }
}
