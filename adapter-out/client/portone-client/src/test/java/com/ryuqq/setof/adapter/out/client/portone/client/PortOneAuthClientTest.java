package com.ryuqq.setof.adapter.out.client.portone.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuqq.setof.adapter.out.client.portone.config.PortOneProperties;
import com.ryuqq.setof.adapter.out.client.portone.dto.auth.PortOneTokenResponse;
import com.ryuqq.setof.adapter.out.client.portone.exception.PortOneAuthException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@DisplayName("PortOneAuthClient")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PortOneAuthClientTest {

    private static final String LOGIN_PATH = "/login/api-secret";
    private static final String REFRESH_PATH = "/token/refresh";

    @Mock private RestClient restClient;
    @Mock private RestClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock private RestClient.RequestBodySpec requestBodySpec;
    @Mock private RestClient.ResponseSpec responseSpec;

    private PortOneProperties properties;
    private ObjectMapper objectMapper;
    private PortOneAuthClient authClient;

    @BeforeEach
    void setUp() {
        properties = new PortOneProperties();
        properties.setApiUrl("https://api.portone.io");
        properties.setApiSecret("test-api-secret");
        properties.setEnabled(true);

        objectMapper = new ObjectMapper();

        // Setup mock chain - RestClient fluent API stubbing
        when(restClient.post()).thenReturn(requestBodyUriSpec);

        // Use eq() matcher with exact path values
        when(requestBodyUriSpec.uri(eq(LOGIN_PATH))).thenReturn(requestBodySpec);
        when(requestBodyUriSpec.uri(eq(REFRESH_PATH))).thenReturn(requestBodySpec);

        // Use explicit any(Class) for clarity
        when(requestBodySpec.contentType(any(MediaType.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Object.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);

        authClient = new PortOneAuthClient(restClient, properties, objectMapper);
    }

    @Nested
    @DisplayName("getValidAccessToken")
    class GetValidAccessTokenTest {

        @Test
        @DisplayName("비활성화 시 빈 문자열 반환")
        void shouldReturnEmptyStringWhenDisabled() {
            // Given
            properties.setEnabled(false);

            // When
            String result = authClient.getValidAccessToken();

            // Then
            assertEquals("", result);
            verify(restClient, never()).post();
        }

        @Test
        @DisplayName("캐시 없을 때 새 토큰 발급")
        void shouldIssueNewTokenWhenNoCached() {
            // Given
            setupMockForSuccessfulTokenIssue();

            // When
            String result = authClient.getValidAccessToken();

            // Then
            assertTrue(result.startsWith("Bearer "));
            assertNotNull(authClient.getCachedToken());
        }
    }

    @Nested
    @DisplayName("issueToken")
    class IssueTokenTest {

        @Test
        @DisplayName("토큰 발급 성공")
        void shouldIssueTokenSuccessfully() {
            // Given
            setupMockForSuccessfulTokenIssue();

            // When
            PortOneTokenResponse result = authClient.issueToken();

            // Then
            assertNotNull(result);
            assertEquals("test-access-token", result.accessToken());
            assertEquals("test-refresh-token", result.refreshToken());
        }

        @Test
        @DisplayName("비활성화 시 예외 발생")
        void shouldThrowExceptionWhenDisabled() {
            // Given
            properties.setEnabled(false);

            // When & Then
            assertThrows(PortOneAuthException.class, () -> authClient.issueToken());
        }

        @Test
        @DisplayName("토큰 응답이 null일 때 예외 발생")
        void shouldThrowExceptionWhenResponseIsNull() {
            // Given
            setupMockForNullResponse();

            // When & Then
            assertThrows(PortOneAuthException.class, () -> authClient.issueToken());
        }
    }

    @Nested
    @DisplayName("refreshToken")
    class RefreshTokenTest {

        @Test
        @DisplayName("토큰 갱신 성공")
        void shouldRefreshTokenSuccessfully() {
            // Given
            setupMockForSuccessfulTokenRefresh();

            // When
            PortOneTokenResponse result = authClient.refreshToken("old-refresh-token");

            // Then
            assertNotNull(result);
            assertEquals("new-access-token", result.accessToken());
            assertEquals("new-refresh-token", result.refreshToken());
        }

        @Test
        @DisplayName("비활성화 시 예외 발생")
        void shouldThrowExceptionWhenDisabled() {
            // Given
            properties.setEnabled(false);

            // When & Then
            assertThrows(
                    PortOneAuthException.class,
                    () -> authClient.refreshToken("some-refresh-token"));
        }
    }

    @Nested
    @DisplayName("CachedToken")
    class CachedTokenTest {

        @Test
        @DisplayName("캐시 초기화")
        void shouldClearCache() {
            // Given
            setupMockForSuccessfulTokenIssue();
            authClient.issueToken();
            assertNotNull(authClient.getCachedToken());

            // When
            authClient.clearCache();

            // Then
            assertNull(authClient.getCachedToken());
        }

        @Test
        @DisplayName("Bearer 토큰 형식 확인")
        void shouldReturnBearerToken() {
            // Given
            setupMockForSuccessfulTokenIssue();
            authClient.issueToken();

            // When
            PortOneAuthClient.CachedToken cached = authClient.getCachedToken();

            // Then
            assertNotNull(cached);
            assertTrue(cached.bearerToken().startsWith("Bearer "));
            assertEquals("Bearer test-access-token", cached.bearerToken());
        }
    }

    @Nested
    @DisplayName("PortOneTokenResponse")
    class PortOneTokenResponseTest {

        @Test
        @DisplayName("유효한 토큰 응답")
        void shouldBeValidResponse() {
            // Given
            PortOneTokenResponse response =
                    new PortOneTokenResponse("access-token", "refresh-token");

            // Then
            assertTrue(response.isValid());
            assertTrue(response.hasAccessToken());
            assertTrue(response.hasRefreshToken());
        }

        @Test
        @DisplayName("액세스 토큰 없으면 유효하지 않음")
        void shouldBeInvalidWithoutAccessToken() {
            // Given
            PortOneTokenResponse response = new PortOneTokenResponse(null, "refresh-token");

            // Then
            assertFalse(response.isValid());
            assertFalse(response.hasAccessToken());
        }

        @Test
        @DisplayName("리프레시 토큰 없으면 유효하지 않음")
        void shouldBeInvalidWithoutRefreshToken() {
            // Given
            PortOneTokenResponse response = new PortOneTokenResponse("access-token", null);

            // Then
            assertFalse(response.isValid());
            assertFalse(response.hasRefreshToken());
        }

        @Test
        @DisplayName("빈 문자열도 유효하지 않음")
        void shouldBeInvalidWithEmptyStrings() {
            // Given
            PortOneTokenResponse response = new PortOneTokenResponse("", "  ");

            // Then
            assertFalse(response.isValid());
            assertFalse(response.hasAccessToken());
            assertFalse(response.hasRefreshToken());
        }
    }

    private void setupMockForSuccessfulTokenIssue() {
        PortOneTokenResponse tokenResponse =
                new PortOneTokenResponse("test-access-token", "test-refresh-token");
        when(responseSpec.body(PortOneTokenResponse.class)).thenReturn(tokenResponse);
    }

    private void setupMockForSuccessfulTokenRefresh() {
        PortOneTokenResponse tokenResponse =
                new PortOneTokenResponse("new-access-token", "new-refresh-token");
        when(responseSpec.body(PortOneTokenResponse.class)).thenReturn(tokenResponse);
    }

    private void setupMockForNullResponse() {
        when(responseSpec.body(PortOneTokenResponse.class)).thenReturn(null);
    }
}
