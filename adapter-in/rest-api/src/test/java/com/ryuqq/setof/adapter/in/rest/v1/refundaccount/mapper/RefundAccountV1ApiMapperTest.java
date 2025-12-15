package com.ryuqq.setof.adapter.in.rest.v1.refundaccount.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.command.RefundAccountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.response.RefundAccountV1ApiResponse;
import com.ryuqq.setof.application.refundaccount.dto.command.DeleteRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountByBankNameCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountByBankNameCommand;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResponse;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("RefundAccountV1ApiMapper")
class RefundAccountV1ApiMapperTest {

    private RefundAccountV1ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RefundAccountV1ApiMapper();
    }

    @Nested
    @DisplayName("toRegisterCommand")
    class ToRegisterCommandTest {

        @Test
        @DisplayName("V1 API 요청을 RegisterRefundAccountByBankNameCommand로 변환 성공")
        void shouldConvertToRegisterCommand() {
            // Given
            UUID memberId = UUID.randomUUID();
            RefundAccountV1ApiRequest request =
                    new RefundAccountV1ApiRequest("신한은행", "110123456789", "홍길동");

            // When
            RegisterRefundAccountByBankNameCommand result =
                    mapper.toRegisterCommand(memberId, request);

            // Then
            assertNotNull(result);
            assertEquals(memberId, result.memberId());
            assertEquals("신한은행", result.bankName());
            assertEquals("110123456789", result.accountNumber());
            assertEquals("홍길동", result.accountHolderName());
        }

        @Test
        @DisplayName("다른 은행으로 변환 성공")
        void shouldConvertWithDifferentBank() {
            // Given
            UUID memberId = UUID.randomUUID();
            RefundAccountV1ApiRequest request =
                    new RefundAccountV1ApiRequest("KB국민은행", "9876543210", "김철수");

            // When
            RegisterRefundAccountByBankNameCommand result =
                    mapper.toRegisterCommand(memberId, request);

            // Then
            assertNotNull(result);
            assertEquals("KB국민은행", result.bankName());
            assertEquals("9876543210", result.accountNumber());
            assertEquals("김철수", result.accountHolderName());
        }
    }

    @Nested
    @DisplayName("toUpdateCommand")
    class ToUpdateCommandTest {

        @Test
        @DisplayName("V1 API 요청을 UpdateRefundAccountByBankNameCommand로 변환 성공")
        void shouldConvertToUpdateCommand() {
            // Given
            UUID memberId = UUID.randomUUID();
            Long refundAccountId = 1L;
            RefundAccountV1ApiRequest request =
                    new RefundAccountV1ApiRequest("우리은행", "1234567890123", "박영희");

            // When
            UpdateRefundAccountByBankNameCommand result =
                    mapper.toUpdateCommand(memberId, refundAccountId, request);

            // Then
            assertNotNull(result);
            assertEquals(memberId, result.memberId());
            assertEquals(refundAccountId, result.refundAccountId());
            assertEquals("우리은행", result.bankName());
            assertEquals("1234567890123", result.accountNumber());
            assertEquals("박영희", result.accountHolderName());
        }

        @Test
        @DisplayName("다른 환불계좌 ID로 변환 성공")
        void shouldConvertWithDifferentRefundAccountId() {
            // Given
            UUID memberId = UUID.randomUUID();
            Long refundAccountId = 999L;
            RefundAccountV1ApiRequest request =
                    new RefundAccountV1ApiRequest("하나은행", "1111222233334444", "이철수");

            // When
            UpdateRefundAccountByBankNameCommand result =
                    mapper.toUpdateCommand(memberId, refundAccountId, request);

            // Then
            assertNotNull(result);
            assertEquals(999L, result.refundAccountId());
            assertEquals("하나은행", result.bankName());
        }
    }

    @Nested
    @DisplayName("toDeleteCommand")
    class ToDeleteCommandTest {

        @Test
        @DisplayName("삭제 커맨드 생성 성공")
        void shouldConvertToDeleteCommand() {
            // Given
            UUID memberId = UUID.randomUUID();
            Long refundAccountId = 1L;

            // When
            DeleteRefundAccountCommand result = mapper.toDeleteCommand(memberId, refundAccountId);

            // Then
            assertNotNull(result);
            assertEquals(memberId, result.memberId());
            assertEquals(refundAccountId, result.refundAccountId());
        }

        @Test
        @DisplayName("다른 환불계좌 ID로 삭제 커맨드 생성 성공")
        void shouldConvertToDeleteCommandWithDifferentId() {
            // Given
            UUID memberId = UUID.randomUUID();
            Long refundAccountId = 12345L;

            // When
            DeleteRefundAccountCommand result = mapper.toDeleteCommand(memberId, refundAccountId);

            // Then
            assertNotNull(result);
            assertEquals(12345L, result.refundAccountId());
        }
    }

    @Nested
    @DisplayName("toV1Response")
    class ToV1ResponseTest {

        private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");

        @Test
        @DisplayName("Application Response를 V1 API Response로 변환 성공")
        void shouldConvertToV1Response() {
            // Given
            RefundAccountResponse response =
                    new RefundAccountResponse(
                            1L, 1L, "신한은행", "088", "110***789", "홍길동", true, NOW, NOW, NOW);

            // When
            RefundAccountV1ApiResponse result = mapper.toV1Response(response);

            // Then
            assertNotNull(result);
            assertEquals(1L, result.refundAccountId());
            assertEquals("신한은행", result.bankName());
            assertEquals("110***789", result.accountNumber());
            assertEquals("홍길동", result.accountHolderName());
        }

        @Test
        @DisplayName("다른 계좌 정보로 변환 성공")
        void shouldConvertToV1ResponseWithDifferentAccount() {
            // Given
            RefundAccountResponse response =
                    new RefundAccountResponse(
                            999L, 2L, "KB국민은행", "004", "987***321", "김철수", true, NOW, NOW, NOW);

            // When
            RefundAccountV1ApiResponse result = mapper.toV1Response(response);

            // Then
            assertNotNull(result);
            assertEquals(999L, result.refundAccountId());
            assertEquals("KB국민은행", result.bankName());
            assertEquals("987***321", result.accountNumber());
            assertEquals("김철수", result.accountHolderName());
        }
    }
}
