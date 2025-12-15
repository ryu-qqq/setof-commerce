package com.ryuqq.setof.adapter.out.client.portone.support;

import com.ryuqq.setof.adapter.out.client.portone.client.PortOneAuthClient;
import com.ryuqq.setof.adapter.out.client.portone.config.PortOneProperties;
import com.ryuqq.setof.adapter.out.client.portone.exception.PortOneAuthException;
import com.ryuqq.setof.adapter.out.client.portone.exception.PortOneException;
import com.ryuqq.setof.adapter.out.client.portone.exception.PortOneException.ErrorType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

/**
 * PortOne API 공통 실행기
 *
 * <p>모든 PortOne API 호출의 공통 로직을 처리합니다.
 *
 * <p><strong>주요 기능:</strong>
 *
 * <ul>
 *   <li>토큰 자동 주입 및 갱신
 *   <li>재시도 로직 (Retry with Exponential Backoff)
 *   <li>서킷 브레이커 (Circuit Breaker)
 *   <li>통합 예외 처리
 *   <li>로깅 및 메트릭
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class PortOneApiExecutor {

    private static final Logger log = LoggerFactory.getLogger(PortOneApiExecutor.class);

    /** 최대 재시도 횟수 */
    private static final int MAX_RETRIES = 3;

    /** 초기 재시도 대기 시간 (ms) */
    private static final long INITIAL_BACKOFF_MS = 100;

    /** 서킷 오픈 임계값 (연속 실패 횟수) */
    private static final int CIRCUIT_FAILURE_THRESHOLD = 5;

    /** 서킷 반오픈 대기 시간 */
    private static final Duration CIRCUIT_OPEN_DURATION = Duration.ofSeconds(30);

    private final PortOneProperties properties;
    private final PortOneAuthClient authClient;

    /** 서킷 상태 */
    private final AtomicReference<CircuitState> circuitState =
            new AtomicReference<>(CircuitState.CLOSED);

    /** 연속 실패 횟수 */
    private final AtomicInteger consecutiveFailures = new AtomicInteger(0);

    /** 서킷 오픈 시각 */
    private final AtomicReference<Instant> circuitOpenedAt = new AtomicReference<>();

    @SuppressFBWarnings(
            value = "EI_EXPOSE_REP2",
            justification = "Spring-managed beans, immutable after injection")
    public PortOneApiExecutor(PortOneProperties properties, PortOneAuthClient authClient) {
        this.properties = properties;
        this.authClient = authClient;
    }

    /**
     * API 실행 (기본값 반환)
     *
     * <p>API 호출 실패 시 기본값을 반환합니다. 예외를 던지지 않습니다.
     *
     * @param <T> 반환 타입
     * @param apiName API 이름 (로깅용)
     * @param apiCall API 호출 람다 (토큰을 파라미터로 받음)
     * @param defaultValue 실패 시 반환할 기본값
     * @return API 응답 또는 기본값
     */
    public <T> T executeWithDefault(String apiName, ApiCall<T> apiCall, Supplier<T> defaultValue) {
        try {
            return execute(apiName, apiCall);
        } catch (PortOneException e) {
            log.warn(
                    "PortOne API [{}] failed, returning default value: {}",
                    apiName,
                    e.getMessage());
            return defaultValue.get();
        }
    }

    /**
     * API 실행 (예외 발생)
     *
     * <p>API 호출 실패 시 예외를 던집니다.
     *
     * @param <T> 반환 타입
     * @param apiName API 이름 (로깅용)
     * @param apiCall API 호출 람다 (토큰을 파라미터로 받음)
     * @return API 응답
     * @throws PortOneException API 호출 실패 시
     */
    public <T> T execute(String apiName, ApiCall<T> apiCall) {
        if (!properties.isEnabled()) {
            log.debug("PortOne is disabled. Skipping API call: {}", apiName);
            throw new PortOneException(ErrorType.UNKNOWN, "PortOne is disabled");
        }

        checkCircuitBreaker(apiName);

        int attempt = 0;
        PortOneException lastException = null;

        while (attempt < MAX_RETRIES) {
            attempt++;
            try {
                String token = getToken(attempt > 1);
                T result = apiCall.call(token);
                onSuccess();
                return result;

            } catch (PortOneAuthException e) {
                lastException = convertException(e);
                if (attempt < MAX_RETRIES) {
                    log.info(
                            "PortOne API [{}] auth failed (attempt {}/{}), retrying with new token",
                            apiName,
                            attempt,
                            MAX_RETRIES);
                    authClient.clearCache();
                    sleep(calculateBackoff(attempt));
                }
            } catch (HttpClientErrorException e) {
                lastException = handleClientError(e, apiName);
                if (!lastException.isRetryable()) {
                    break;
                }
                sleep(calculateBackoff(attempt));
            } catch (HttpServerErrorException e) {
                lastException = handleServerError(e, apiName);
                sleep(calculateBackoff(attempt));
            } catch (ResourceAccessException e) {
                lastException = handleNetworkError(e, apiName);
                sleep(calculateBackoff(attempt));
            } catch (RestClientException e) {
                lastException = new PortOneException(ErrorType.UNKNOWN, e.getMessage(), e);
                sleep(calculateBackoff(attempt));
            }
        }

        onFailure();
        log.error(
                "PortOne API [{}] failed after {} attempts: {}",
                apiName,
                attempt,
                lastException != null ? lastException.getMessage() : "Unknown error");
        throw lastException != null
                ? lastException
                : new PortOneException("API call failed after " + attempt + " attempts");
    }

    /**
     * 현재 서킷 상태 조회
     *
     * @return 서킷 상태
     */
    public CircuitState getCircuitState() {
        return circuitState.get();
    }

    /** 서킷 강제 리셋 (테스트용) */
    void resetCircuit() {
        circuitState.set(CircuitState.CLOSED);
        consecutiveFailures.set(0);
        circuitOpenedAt.set(null);
    }

    private String getToken(boolean forceRefresh) {
        if (forceRefresh) {
            authClient.clearCache();
        }
        return authClient.getValidAccessToken();
    }

    private void checkCircuitBreaker(String apiName) {
        CircuitState state = circuitState.get();

        if (state == CircuitState.OPEN) {
            Instant openedAt = circuitOpenedAt.get();
            if (openedAt != null && Instant.now().isAfter(openedAt.plus(CIRCUIT_OPEN_DURATION))) {
                circuitState.compareAndSet(CircuitState.OPEN, CircuitState.HALF_OPEN);
                log.info("PortOne circuit breaker moved to HALF_OPEN for API: {}", apiName);
            } else {
                throw new PortOneException(
                        ErrorType.CIRCUIT_OPEN,
                        "Circuit breaker is OPEN. API temporarily unavailable: " + apiName);
            }
        }
    }

    private void onSuccess() {
        consecutiveFailures.set(0);
        if (circuitState.get() == CircuitState.HALF_OPEN) {
            circuitState.set(CircuitState.CLOSED);
            log.info("PortOne circuit breaker CLOSED after successful call");
        }
    }

    private void onFailure() {
        int failures = consecutiveFailures.incrementAndGet();
        if (failures >= CIRCUIT_FAILURE_THRESHOLD) {
            circuitState.set(CircuitState.OPEN);
            circuitOpenedAt.set(Instant.now());
            log.warn("PortOne circuit breaker OPENED after {} consecutive failures", failures);
        }
    }

    private PortOneException handleClientError(HttpClientErrorException e, String apiName) {
        HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());

        return switch (status) {
            case UNAUTHORIZED -> {
                log.warn("PortOne API [{}] unauthorized: {}", apiName, e.getMessage());
                yield new PortOneException(ErrorType.AUTHENTICATION, "Authentication failed", e);
            }
            case FORBIDDEN -> {
                log.warn("PortOne API [{}] forbidden: {}", apiName, e.getMessage());
                yield new PortOneException(ErrorType.AUTHORIZATION, "Access denied", e);
            }
            case NOT_FOUND -> {
                log.warn("PortOne API [{}] not found: {}", apiName, e.getMessage());
                yield new PortOneException(ErrorType.NOT_FOUND, "Resource not found", e);
            }
            case TOO_MANY_REQUESTS -> {
                log.warn("PortOne API [{}] rate limited: {}", apiName, e.getMessage());
                yield new PortOneException(ErrorType.RATE_LIMIT, "Rate limit exceeded", e);
            }
            default -> {
                log.warn("PortOne API [{}] client error: {} - {}", apiName, status, e.getMessage());
                yield new PortOneException(ErrorType.BAD_REQUEST, "Bad request: " + status, e);
            }
        };
    }

    private PortOneException handleServerError(HttpServerErrorException e, String apiName) {
        log.error("PortOne API [{}] server error: {}", apiName, e.getMessage());
        return new PortOneException(
                ErrorType.SERVER_ERROR, "Server error: " + e.getStatusCode(), e);
    }

    private PortOneException handleNetworkError(ResourceAccessException e, String apiName) {
        log.error("PortOne API [{}] network error: {}", apiName, e.getMessage());
        return new PortOneException(ErrorType.NETWORK, "Network error: " + e.getMessage(), e);
    }

    private PortOneException convertException(PortOneAuthException e) {
        return new PortOneException(ErrorType.AUTHENTICATION, e.getMessage(), e);
    }

    private long calculateBackoff(int attempt) {
        return INITIAL_BACKOFF_MS * (1L << (attempt - 1));
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * API 호출 함수형 인터페이스
     *
     * @param <T> 반환 타입
     */
    @FunctionalInterface
    public interface ApiCall<T> {
        T call(String token) throws RestClientException;
    }

    /** 서킷 브레이커 상태 */
    public enum CircuitState {
        /** 정상 상태 */
        CLOSED,
        /** 오픈 상태 (요청 차단) */
        OPEN,
        /** 반오픈 상태 (테스트 요청 허용) */
        HALF_OPEN
    }
}
