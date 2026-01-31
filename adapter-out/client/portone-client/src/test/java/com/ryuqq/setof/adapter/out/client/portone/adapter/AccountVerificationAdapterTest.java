package com.ryuqq.setof.adapter.out.client.portone.adapter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.adapter.out.client.portone.client.PortOneClient;
import com.ryuqq.setof.adapter.out.client.portone.config.PortOneProperties;
import com.ryuqq.setof.adapter.out.client.portone.dto.PortOneBankHolderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("AccountVerificationAdapter")
@ExtendWith(MockitoExtension.class)
class AccountVerificationAdapterTest {

    @Mock private PortOneClient portOneClient;

    private PortOneProperties properties;
    private AccountVerificationAdapter adapter;

    @BeforeEach
    void setUp() {
        properties = new PortOneProperties();
        properties.setEnabled(true);
        adapter = new AccountVerificationAdapter(portOneClient, properties);
    }

    @Nested
    @DisplayName("verifyAccount")
    class VerifyAccountTest {

        @Test
        @DisplayName("비활성화 시 항상 true 반환")
        void shouldReturnTrueWhenDisabled() {
            // Given
            properties.setEnabled(false);

            // When
            boolean result = adapter.verifyAccount("004", "1234567890", "홍길동");

            // Then
            assertTrue(result);
            verify(portOneClient, never()).fetchBankHolder(anyString(), anyString());
        }

        @Test
        @DisplayName("예금주명 일치 시 true 반환")
        void shouldReturnTrueWhenHolderMatches() {
            // Given
            when(portOneClient.fetchBankHolder("004", "1234567890"))
                    .thenReturn(new PortOneBankHolderResponse("홍길동"));

            // When
            boolean result = adapter.verifyAccount("004", "1234567890", "홍길동");

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("예금주명 불일치 시 false 반환")
        void shouldReturnFalseWhenHolderDoesNotMatch() {
            // Given
            when(portOneClient.fetchBankHolder("004", "1234567890"))
                    .thenReturn(new PortOneBankHolderResponse("김철수"));

            // When
            boolean result = adapter.verifyAccount("004", "1234567890", "홍길동");

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("예금주 정보 없으면 false 반환")
        void shouldReturnFalseWhenNoHolder() {
            // Given
            when(portOneClient.fetchBankHolder("004", "1234567890"))
                    .thenReturn(PortOneBankHolderResponse.empty());

            // When
            boolean result = adapter.verifyAccount("004", "1234567890", "홍길동");

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("공백 포함 예금주명 정규화 후 비교")
        void shouldNormalizeHolderName() {
            // Given
            when(portOneClient.fetchBankHolder("004", "1234567890"))
                    .thenReturn(new PortOneBankHolderResponse("홍 길 동"));

            // When
            boolean result = adapter.verifyAccount("004", "1234567890", "홍길동");

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("예외 발생 시 false 반환")
        void shouldReturnFalseOnException() {
            // Given
            when(portOneClient.fetchBankHolder("004", "1234567890"))
                    .thenThrow(new RuntimeException("API error"));

            // When
            boolean result = adapter.verifyAccount("004", "1234567890", "홍길동");

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("계좌번호 마스킹 - 4자리 미만")
        void shouldMaskShortAccountNumber() {
            // Given
            when(portOneClient.fetchBankHolder("004", "123"))
                    .thenReturn(PortOneBankHolderResponse.empty());

            // When
            boolean result = adapter.verifyAccount("004", "123", "홍길동");

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("null 예금주명 정규화")
        void shouldHandleNullHolderName() {
            // Given
            when(portOneClient.fetchBankHolder("004", "1234567890"))
                    .thenReturn(new PortOneBankHolderResponse(null));

            // When
            boolean result = adapter.verifyAccount("004", "1234567890", "홍길동");

            // Then
            assertFalse(result);
        }
    }
}
