package com.ryuqq.setof.adapter.out.client.portone.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.adapter.out.client.portone.config.PortOneProperties;
import com.ryuqq.setof.adapter.out.client.portone.dto.PortOneApiResponse;
import com.ryuqq.setof.adapter.out.client.portone.dto.PortOneBankHolderResponse;
import com.ryuqq.setof.adapter.out.client.portone.support.PortOneApiExecutor;
import java.util.function.Function;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

@DisplayName("PortOneClient")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PortOneClientTest {

    @Mock private RestClient restClient;
    @Mock private PortOneAuthClient authClient;
    @Mock private PortOneApiExecutor apiExecutor;
    @Mock private RestClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;
    @Mock private RestClient.RequestHeadersSpec<?> requestHeadersSpec;
    @Mock private RestClient.ResponseSpec responseSpec;

    private PortOneProperties properties;
    private PortOneClient portOneClient;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        properties = new PortOneProperties();
        properties.setApiUrl("https://api.portone.io");
        properties.setApiSecret("test-api-secret");
        properties.setEnabled(true);

        // Setup mock chain for GET requests
        when(restClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class)))
                .thenReturn((RestClient.RequestHeadersSpec) requestHeadersSpec);
        when(requestHeadersSpec.header(any(String.class), any(String.class)))
                .thenReturn((RestClient.RequestHeadersSpec) requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(authClient.getValidAccessToken()).thenReturn("Bearer test-token");

        portOneClient = new PortOneClient(restClient, properties, authClient, apiExecutor);
    }

    @Nested
    @DisplayName("getAccessToken")
    class GetAccessTokenTest {

        @Test
        @DisplayName("AuthClient에서 토큰 반환")
        void shouldReturnTokenFromAuthClient() {
            // When
            String result = portOneClient.getAccessToken();

            // Then
            assertEquals("Bearer test-token", result);
            verify(authClient).getValidAccessToken();
        }
    }

    @Nested
    @DisplayName("fetchBankHolder")
    class FetchBankHolderTest {

        @Test
        @DisplayName("비활성화 시 빈 응답 반환 (Executor 호출 안함)")
        void shouldReturnEmptyWhenDisabled() {
            // Given
            properties.setEnabled(false);

            // When
            PortOneBankHolderResponse result = portOneClient.fetchBankHolder("004", "1234567890");

            // Then
            assertFalse(result.hasHolder());
            verify(apiExecutor, never()).executeWithDefault(anyString(), any(), any());
        }

        @Test
        @DisplayName("Executor를 통한 성공적인 예금주 조회")
        @SuppressWarnings("unchecked")
        void shouldFetchBankHolderThroughExecutor() {
            // Given
            PortOneBankHolderResponse expectedResponse = new PortOneBankHolderResponse("홍길동");

            when(apiExecutor.executeWithDefault(
                            eq("fetchBankHolder"),
                            any(PortOneApiExecutor.ApiCall.class),
                            any(Supplier.class)))
                    .thenReturn(expectedResponse);

            // When
            PortOneBankHolderResponse result = portOneClient.fetchBankHolder("004", "1234567890");

            // Then
            assertTrue(result.hasHolder());
            assertEquals("홍길동", result.bankHolderInfo());
            verify(apiExecutor).executeWithDefault(eq("fetchBankHolder"), any(), any());
        }

        @Test
        @DisplayName("Executor 실패 시 기본값 반환 (executeWithDefault 특성)")
        @SuppressWarnings("unchecked")
        void shouldReturnDefaultWhenExecutorFails() {
            // Given
            when(apiExecutor.executeWithDefault(
                            eq("fetchBankHolder"),
                            any(PortOneApiExecutor.ApiCall.class),
                            any(Supplier.class)))
                    .thenReturn(PortOneBankHolderResponse.empty());

            // When
            PortOneBankHolderResponse result = portOneClient.fetchBankHolder("004", "1234567890");

            // Then
            assertFalse(result.hasHolder());
        }
    }

    @Nested
    @DisplayName("callBankHolderApi 내부 로직 검증")
    class CallBankHolderApiTest {

        @Test
        @DisplayName("API 응답 성공 시 예금주 정보 반환")
        @SuppressWarnings("unchecked")
        void shouldReturnHolderOnSuccessfulApiResponse() {
            // Given
            PortOneBankHolderResponse holderResponse = new PortOneBankHolderResponse("홍길동");
            PortOneApiResponse<PortOneBankHolderResponse> apiResponse =
                    new PortOneApiResponse<>(0, "success", holderResponse);

            when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(apiResponse);

            // Executor가 실제 API 호출을 실행하도록 구성
            when(apiExecutor.executeWithDefault(
                            eq("fetchBankHolder"),
                            any(PortOneApiExecutor.ApiCall.class),
                            any(Supplier.class)))
                    .thenAnswer(
                            invocation -> {
                                PortOneApiExecutor.ApiCall<PortOneBankHolderResponse> apiCall =
                                        invocation.getArgument(1);
                                return apiCall.call("Bearer test-token");
                            });

            // When
            PortOneBankHolderResponse result = portOneClient.fetchBankHolder("004", "1234567890");

            // Then
            assertTrue(result.hasHolder());
            assertEquals("홍길동", result.bankHolderInfo());
        }

        @Test
        @DisplayName("API 응답이 null일 때 빈 응답 반환")
        @SuppressWarnings("unchecked")
        void shouldReturnEmptyWhenResponseIsNull() {
            // Given
            when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(null);

            when(apiExecutor.executeWithDefault(
                            eq("fetchBankHolder"),
                            any(PortOneApiExecutor.ApiCall.class),
                            any(Supplier.class)))
                    .thenAnswer(
                            invocation -> {
                                PortOneApiExecutor.ApiCall<PortOneBankHolderResponse> apiCall =
                                        invocation.getArgument(1);
                                return apiCall.call("Bearer test-token");
                            });

            // When
            PortOneBankHolderResponse result = portOneClient.fetchBankHolder("004", "1234567890");

            // Then
            assertFalse(result.hasHolder());
        }

        @Test
        @DisplayName("API 응답이 실패일 때 빈 응답 반환")
        @SuppressWarnings("unchecked")
        void shouldReturnEmptyWhenResponseIsNotSuccess() {
            // Given
            PortOneApiResponse<PortOneBankHolderResponse> apiResponse =
                    new PortOneApiResponse<>(-1, "error", null);

            when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(apiResponse);

            when(apiExecutor.executeWithDefault(
                            eq("fetchBankHolder"),
                            any(PortOneApiExecutor.ApiCall.class),
                            any(Supplier.class)))
                    .thenAnswer(
                            invocation -> {
                                PortOneApiExecutor.ApiCall<PortOneBankHolderResponse> apiCall =
                                        invocation.getArgument(1);
                                return apiCall.call("Bearer test-token");
                            });

            // When
            PortOneBankHolderResponse result = portOneClient.fetchBankHolder("004", "1234567890");

            // Then
            assertFalse(result.hasHolder());
        }
    }

    @Nested
    @DisplayName("PortOneBankHolderResponse")
    class PortOneBankHolderResponseTest {

        @Test
        @DisplayName("유효한 예금주 응답")
        void shouldBeValidResponse() {
            // Given
            PortOneBankHolderResponse response = new PortOneBankHolderResponse("홍길동");

            // Then
            assertTrue(response.hasHolder());
            assertEquals("홍길동", response.bankHolderInfo());
        }

        @Test
        @DisplayName("예금주명이 없으면 유효하지 않음")
        void shouldBeInvalidWithoutHolderName() {
            // Given
            PortOneBankHolderResponse response = new PortOneBankHolderResponse(null);

            // Then
            assertFalse(response.hasHolder());
        }

        @Test
        @DisplayName("빈 예금주명은 유효하지 않음")
        void shouldBeInvalidWithEmptyHolderName() {
            // Given
            PortOneBankHolderResponse response = new PortOneBankHolderResponse("  ");

            // Then
            assertFalse(response.hasHolder());
        }

        @Test
        @DisplayName("빈 응답 생성")
        void shouldCreateEmptyResponse() {
            // Given
            PortOneBankHolderResponse response = PortOneBankHolderResponse.empty();

            // Then
            assertFalse(response.hasHolder());
        }
    }

    @Nested
    @DisplayName("PortOneApiResponse")
    class PortOneApiResponseTest {

        @Test
        @DisplayName("코드가 0이면 성공")
        void shouldBeSuccessWhenCodeIsZero() {
            // Given
            PortOneApiResponse<String> response = new PortOneApiResponse<>(0, "success", "data");

            // Then
            assertTrue(response.isSuccess());
        }

        @Test
        @DisplayName("코드가 0이 아니면 실패")
        void shouldBeFailureWhenCodeIsNotZero() {
            // Given
            PortOneApiResponse<String> response = new PortOneApiResponse<>(-1, "error", null);

            // Then
            assertFalse(response.isSuccess());
        }
    }
}
