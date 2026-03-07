package com.ryuqq.setof.adapter.in.rest.v1.refundaccount.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.RefundAccountApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.request.RegisterRefundAccountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.request.UpdateRefundAccountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.response.RefundAccountV1ApiResponse;
import com.ryuqq.setof.application.refundaccount.dto.command.DeleteRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * RefundAccountV1ApiMapper 단위 테스트.
 *
 * <p>환불 계좌 V1 API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("RefundAccountV1ApiMapper 단위 테스트")
class RefundAccountV1ApiMapperTest {

    private RefundAccountV1ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RefundAccountV1ApiMapper();
    }

    @Nested
    @DisplayName("toResponse 메서드 테스트")
    class ToResponseTest {

        @Test
        @DisplayName("RefundAccountResult를 RefundAccountV1ApiResponse로 변환한다")
        void toResponse_Success() {
            // given
            RefundAccountResult result = RefundAccountApiFixtures.refundAccountResult(1L);

            // when
            RefundAccountV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.refundAccountId()).isEqualTo(1L);
            assertThat(response.bankName()).isEqualTo("국민은행");
            assertThat(response.accountNumber()).isEqualTo("123456789012");
            assertThat(response.accountHolderName()).isEqualTo("홍길동");
        }

        @Test
        @DisplayName("refundAccountId가 올바르게 매핑된다")
        void toResponse_IdMapping() {
            // given
            long expectedId = 99L;
            RefundAccountResult result = RefundAccountApiFixtures.refundAccountResult(expectedId);

            // when
            RefundAccountV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.refundAccountId()).isEqualTo(expectedId);
        }
    }

    @Nested
    @DisplayName("toRegisterCommand 메서드 테스트")
    class ToRegisterCommandTest {

        @Test
        @DisplayName("userId와 RegisterRequest를 RegisterRefundAccountCommand로 변환한다")
        void toRegisterCommand_Success() {
            // given
            Long userId = 10L;
            RegisterRefundAccountV1ApiRequest request = RefundAccountApiFixtures.registerRequest();

            // when
            RegisterRefundAccountCommand command = mapper.toRegisterCommand(userId, request);

            // then
            assertThat(command.userId()).isEqualTo(userId);
            assertThat(command.bankName()).isEqualTo("국민은행");
            assertThat(command.accountNumber()).isEqualTo("123456789012");
            assertThat(command.accountHolderName()).isEqualTo("홍길동");
        }
    }

    @Nested
    @DisplayName("toUpdateCommand 메서드 테스트")
    class ToUpdateCommandTest {

        @Test
        @DisplayName("userId, refundAccountId, UpdateRequest를 UpdateRefundAccountCommand로 변환한다")
        void toUpdateCommand_Success() {
            // given
            Long userId = 10L;
            Long refundAccountId = 5L;
            UpdateRefundAccountV1ApiRequest request = RefundAccountApiFixtures.updateRequest();

            // when
            UpdateRefundAccountCommand command =
                    mapper.toUpdateCommand(userId, refundAccountId, request);

            // then
            assertThat(command.userId()).isEqualTo(userId);
            assertThat(command.refundAccountId()).isEqualTo(refundAccountId);
            assertThat(command.bankName()).isEqualTo("신한은행");
            assertThat(command.accountNumber()).isEqualTo("110123456789");
            assertThat(command.accountHolderName()).isEqualTo("홍길동");
        }
    }

    @Nested
    @DisplayName("toDeleteCommand 메서드 테스트")
    class ToDeleteCommandTest {

        @Test
        @DisplayName("userId와 refundAccountId를 DeleteRefundAccountCommand로 변환한다")
        void toDeleteCommand_Success() {
            // given
            Long userId = 10L;
            Long refundAccountId = 5L;

            // when
            DeleteRefundAccountCommand command = mapper.toDeleteCommand(userId, refundAccountId);

            // then
            assertThat(command.userId()).isEqualTo(userId);
            assertThat(command.refundAccountId()).isEqualTo(refundAccountId);
        }
    }
}
