package com.ryuqq.setof.adapter.out.client.portone.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.adapter.out.client.portone.client.PortOneAuthClient;
import com.ryuqq.setof.adapter.out.client.portone.config.PortOneProperties;
import com.ryuqq.setof.adapter.out.client.portone.exception.PortOneAuthException;
import com.ryuqq.setof.adapter.out.client.portone.exception.PortOneException;
import com.ryuqq.setof.adapter.out.client.portone.support.PortOneApiExecutor.CircuitState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

@DisplayName("PortOneApiExecutor")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PortOneApiExecutorTest {

    @Mock private PortOneProperties properties;
    @Mock private PortOneAuthClient authClient;

    private PortOneApiExecutor executor;

    @BeforeEach
    void setUp() {
        executor = new PortOneApiExecutor(properties, authClient);
    }

    @Nested
    @DisplayName("execute 성공 케이스")
    class ExecuteSuccessTest {

        @Test
        @DisplayName("정상 실행 시 결과 반환")
        void shouldReturnResultOnSuccess() {
            // Given
            when(properties.isEnabled()).thenReturn(true);
            when(authClient.getValidAccessToken()).thenReturn("Bearer test-token");
            String expected = "success";

            // When
            String result = executor.execute("testApi", token -> expected);

            // Then
            assertEquals(expected, result);
            assertEquals(CircuitState.CLOSED, executor.getCircuitState());
        }

        @Test
        @DisplayName("토큰이 API 호출에 전달됨")
        void shouldPassTokenToApiCall() {
            // Given
            when(properties.isEnabled()).thenReturn(true);
            when(authClient.getValidAccessToken()).thenReturn("Bearer test-token");

            // When
            String result = executor.execute("testApi", token -> token);

            // Then
            assertEquals("Bearer test-token", result);
        }
    }

    @Nested
    @DisplayName("execute 비활성화 케이스")
    class ExecuteDisabledTest {

        @Test
        @DisplayName("비활성화 시 예외 발생")
        void shouldThrowExceptionWhenDisabled() {
            // Given
            when(properties.isEnabled()).thenReturn(false);

            // When & Then
            PortOneException exception =
                    assertThrows(
                            PortOneException.class,
                            () -> executor.execute("testApi", token -> "result"));

            assertEquals(PortOneException.ErrorType.UNKNOWN, exception.getErrorType());
        }
    }

    @Nested
    @DisplayName("executeWithDefault")
    class ExecuteWithDefaultTest {

        @Test
        @DisplayName("성공 시 결과 반환")
        void shouldReturnResultOnSuccess() {
            // Given
            when(properties.isEnabled()).thenReturn(true);
            when(authClient.getValidAccessToken()).thenReturn("Bearer test-token");

            // When
            String result =
                    executor.executeWithDefault("testApi", token -> "success", () -> "default");

            // Then
            assertEquals("success", result);
        }

        @Test
        @DisplayName("실패 시 기본값 반환")
        void shouldReturnDefaultOnFailure() {
            // Given
            when(properties.isEnabled()).thenReturn(false);

            // When
            String result =
                    executor.executeWithDefault("testApi", token -> "success", () -> "default");

            // Then
            assertEquals("default", result);
        }

        @Test
        @DisplayName("예외 발생 시 기본값 반환")
        void shouldReturnDefaultOnException() {
            // Given
            when(properties.isEnabled()).thenReturn(true);
            when(authClient.getValidAccessToken()).thenReturn("Bearer test-token");

            PortOneApiExecutor.ApiCall<String> failingCall =
                    token -> {
                        throw new RestClientException("Network error");
                    };

            // When
            String result = executor.executeWithDefault("testApi", failingCall, () -> "default");

            // Then
            assertEquals("default", result);
        }
    }

    @Nested
    @DisplayName("재시도 로직")
    class RetryLogicTest {

        @Test
        @DisplayName("인증 예외 시 토큰 갱신 후 재시도")
        void shouldRetryWithNewTokenOnAuthException() {
            // Given
            when(properties.isEnabled()).thenReturn(true);
            when(authClient.getValidAccessToken())
                    .thenReturn("Bearer old-token")
                    .thenReturn("Bearer new-token");

            int[] callCount = {0};
            PortOneApiExecutor.ApiCall<String> apiCall =
                    token -> {
                        callCount[0]++;
                        if (callCount[0] == 1) {
                            throw new PortOneAuthException("Auth failed");
                        }
                        return "success";
                    };

            // When
            String result = executor.execute("testApi", apiCall);

            // Then
            assertEquals("success", result);
            assertEquals(2, callCount[0]);
            verify(authClient, atLeast(1)).clearCache();
        }

        @Test
        @DisplayName("서버 에러 시 재시도")
        void shouldRetryOnServerError() {
            // Given
            when(properties.isEnabled()).thenReturn(true);
            when(authClient.getValidAccessToken()).thenReturn("Bearer test-token");

            int[] callCount = {0};
            PortOneApiExecutor.ApiCall<String> apiCall =
                    token -> {
                        callCount[0]++;
                        if (callCount[0] < 3) {
                            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                        return "success";
                    };

            // When
            String result = executor.execute("testApi", apiCall);

            // Then
            assertEquals("success", result);
            assertEquals(3, callCount[0]);
        }

        @Test
        @DisplayName("네트워크 에러 시 재시도")
        void shouldRetryOnNetworkError() {
            // Given
            when(properties.isEnabled()).thenReturn(true);
            when(authClient.getValidAccessToken()).thenReturn("Bearer test-token");

            int[] callCount = {0};
            PortOneApiExecutor.ApiCall<String> apiCall =
                    token -> {
                        callCount[0]++;
                        if (callCount[0] < 2) {
                            throw new ResourceAccessException("Connection timeout");
                        }
                        return "success";
                    };

            // When
            String result = executor.execute("testApi", apiCall);

            // Then
            assertEquals("success", result);
            assertEquals(2, callCount[0]);
        }

        @Test
        @DisplayName("최대 재시도 횟수 초과 시 예외 발생")
        void shouldThrowExceptionAfterMaxRetries() {
            // Given
            when(properties.isEnabled()).thenReturn(true);
            when(authClient.getValidAccessToken()).thenReturn("Bearer test-token");

            PortOneApiExecutor.ApiCall<String> failingCall =
                    token -> {
                        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
                    };

            // When & Then
            PortOneException exception =
                    assertThrows(
                            PortOneException.class, () -> executor.execute("testApi", failingCall));

            assertEquals(PortOneException.ErrorType.SERVER_ERROR, exception.getErrorType());
        }
    }

    @Nested
    @DisplayName("HTTP 에러 처리")
    class HttpErrorHandlingTest {

        @BeforeEach
        void setUp() {
            when(properties.isEnabled()).thenReturn(true);
            when(authClient.getValidAccessToken()).thenReturn("Bearer test-token");
        }

        @Test
        @DisplayName("401 Unauthorized - 재시도 가능")
        void shouldHandleUnauthorized() {
            // Given
            PortOneApiExecutor.ApiCall<String> apiCall =
                    token -> {
                        throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
                    };

            // When & Then
            PortOneException exception =
                    assertThrows(
                            PortOneException.class, () -> executor.execute("testApi", apiCall));

            assertEquals(PortOneException.ErrorType.AUTHENTICATION, exception.getErrorType());
        }

        @Test
        @DisplayName("403 Forbidden - 재시도 불가")
        void shouldHandleForbidden() {
            // Given
            PortOneApiExecutor.ApiCall<String> apiCall =
                    token -> {
                        throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
                    };

            // When & Then
            PortOneException exception =
                    assertThrows(
                            PortOneException.class, () -> executor.execute("testApi", apiCall));

            assertEquals(PortOneException.ErrorType.AUTHORIZATION, exception.getErrorType());
        }

        @Test
        @DisplayName("404 Not Found - 재시도 불가")
        void shouldHandleNotFound() {
            // Given
            PortOneApiExecutor.ApiCall<String> apiCall =
                    token -> {
                        throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
                    };

            // When & Then
            PortOneException exception =
                    assertThrows(
                            PortOneException.class, () -> executor.execute("testApi", apiCall));

            assertEquals(PortOneException.ErrorType.NOT_FOUND, exception.getErrorType());
        }

        @Test
        @DisplayName("429 Too Many Requests - 재시도 가능")
        void shouldHandleRateLimit() {
            // Given
            int[] callCount = {0};
            PortOneApiExecutor.ApiCall<String> apiCall =
                    token -> {
                        callCount[0]++;
                        if (callCount[0] < 3) {
                            throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS);
                        }
                        return "success";
                    };

            // When
            String result = executor.execute("testApi", apiCall);

            // Then
            assertEquals("success", result);
            assertEquals(3, callCount[0]);
        }

        @Test
        @DisplayName("500 Server Error - 재시도 가능")
        void shouldHandleServerError() {
            // Given
            PortOneApiExecutor.ApiCall<String> apiCall =
                    token -> {
                        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
                    };

            // When & Then
            PortOneException exception =
                    assertThrows(
                            PortOneException.class, () -> executor.execute("testApi", apiCall));

            assertEquals(PortOneException.ErrorType.SERVER_ERROR, exception.getErrorType());
        }
    }

    @Nested
    @DisplayName("서킷 브레이커")
    class CircuitBreakerTest {

        @Test
        @DisplayName("초기 상태는 CLOSED")
        void shouldStartWithClosedState() {
            assertEquals(CircuitState.CLOSED, executor.getCircuitState());
        }

        @Test
        @DisplayName("연속 실패 시 서킷 OPEN")
        void shouldOpenCircuitAfterConsecutiveFailures() {
            // Given
            when(properties.isEnabled()).thenReturn(true);
            when(authClient.getValidAccessToken()).thenReturn("Bearer test-token");

            PortOneApiExecutor.ApiCall<String> failingCall =
                    token -> {
                        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
                    };

            // 5번 연속 실패 필요 (각 호출당 3회 재시도)
            for (int i = 0; i < 5; i++) {
                try {
                    executor.execute("testApi", failingCall);
                } catch (PortOneException ignored) {
                    // 예외 무시
                }
            }

            // Then
            assertEquals(CircuitState.OPEN, executor.getCircuitState());
        }

        @Test
        @DisplayName("서킷 OPEN 상태에서 즉시 예외 발생")
        void shouldThrowExceptionWhenCircuitOpen() {
            // Given - 서킷을 OPEN 상태로 만들기
            when(properties.isEnabled()).thenReturn(true);
            when(authClient.getValidAccessToken()).thenReturn("Bearer test-token");

            PortOneApiExecutor.ApiCall<String> failingCall =
                    token -> {
                        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
                    };

            for (int i = 0; i < 5; i++) {
                try {
                    executor.execute("testApi", failingCall);
                } catch (PortOneException ignored) {
                }
            }

            assertEquals(CircuitState.OPEN, executor.getCircuitState());

            // When & Then
            PortOneException exception =
                    assertThrows(
                            PortOneException.class,
                            () -> executor.execute("testApi", token -> "success"));

            assertEquals(PortOneException.ErrorType.CIRCUIT_OPEN, exception.getErrorType());
        }

        @Test
        @DisplayName("성공 시 실패 카운트 리셋")
        void shouldResetFailureCountOnSuccess() {
            // Given
            when(properties.isEnabled()).thenReturn(true);
            when(authClient.getValidAccessToken()).thenReturn("Bearer test-token");

            // 먼저 성공하는 호출
            String result = executor.execute("testApi", token -> "success");

            // Then
            assertEquals("success", result);
            assertEquals(CircuitState.CLOSED, executor.getCircuitState());
        }
    }

    @Nested
    @DisplayName("PortOneException 에러 타입")
    class ErrorTypeTest {

        @Test
        @DisplayName("AUTHENTICATION은 재시도 가능")
        void authenticationShouldBeRetryable() {
            PortOneException ex =
                    new PortOneException(PortOneException.ErrorType.AUTHENTICATION, "msg");
            assertEquals(true, ex.isRetryable());
        }

        @Test
        @DisplayName("AUTHORIZATION은 재시도 불가")
        void authorizationShouldNotBeRetryable() {
            PortOneException ex =
                    new PortOneException(PortOneException.ErrorType.AUTHORIZATION, "msg");
            assertEquals(false, ex.isRetryable());
        }

        @Test
        @DisplayName("RATE_LIMIT은 재시도 가능")
        void rateLimitShouldBeRetryable() {
            PortOneException ex =
                    new PortOneException(PortOneException.ErrorType.RATE_LIMIT, "msg");
            assertEquals(true, ex.isRetryable());
        }

        @Test
        @DisplayName("SERVER_ERROR는 재시도 가능")
        void serverErrorShouldBeRetryable() {
            PortOneException ex =
                    new PortOneException(PortOneException.ErrorType.SERVER_ERROR, "msg");
            assertEquals(true, ex.isRetryable());
        }

        @Test
        @DisplayName("NETWORK은 재시도 가능")
        void networkShouldBeRetryable() {
            PortOneException ex = new PortOneException(PortOneException.ErrorType.NETWORK, "msg");
            assertEquals(true, ex.isRetryable());
        }

        @Test
        @DisplayName("NOT_FOUND는 재시도 불가")
        void notFoundShouldNotBeRetryable() {
            PortOneException ex = new PortOneException(PortOneException.ErrorType.NOT_FOUND, "msg");
            assertEquals(false, ex.isRetryable());
        }

        @Test
        @DisplayName("BAD_REQUEST는 재시도 불가")
        void badRequestShouldNotBeRetryable() {
            PortOneException ex =
                    new PortOneException(PortOneException.ErrorType.BAD_REQUEST, "msg");
            assertEquals(false, ex.isRetryable());
        }

        @Test
        @DisplayName("CIRCUIT_OPEN은 재시도 불가")
        void circuitOpenShouldNotBeRetryable() {
            PortOneException ex =
                    new PortOneException(PortOneException.ErrorType.CIRCUIT_OPEN, "msg");
            assertEquals(false, ex.isRetryable());
        }
    }

    @Nested
    @DisplayName("토큰 갱신 로직")
    class TokenRefreshTest {

        @Test
        @DisplayName("첫 시도에서는 캐시된 토큰 사용")
        void shouldUseCachedTokenOnFirstAttempt() {
            // Given
            when(properties.isEnabled()).thenReturn(true);
            when(authClient.getValidAccessToken()).thenReturn("Bearer test-token");

            // When
            executor.execute("testApi", token -> "success");

            // Then
            verify(authClient).getValidAccessToken();
        }

        @Test
        @DisplayName("인증 실패 후 캐시 클리어")
        void shouldClearCacheAfterAuthFailure() {
            // Given
            when(properties.isEnabled()).thenReturn(true);
            when(authClient.getValidAccessToken())
                    .thenReturn("Bearer old-token")
                    .thenReturn("Bearer new-token");

            int[] callCount = {0};
            PortOneApiExecutor.ApiCall<String> apiCall =
                    token -> {
                        callCount[0]++;
                        if (callCount[0] == 1) {
                            throw new PortOneAuthException("Token expired");
                        }
                        return token;
                    };

            // When
            String result = executor.execute("testApi", apiCall);

            // Then
            assertEquals("Bearer new-token", result);
            verify(authClient, atLeast(1)).clearCache();
        }
    }
}
