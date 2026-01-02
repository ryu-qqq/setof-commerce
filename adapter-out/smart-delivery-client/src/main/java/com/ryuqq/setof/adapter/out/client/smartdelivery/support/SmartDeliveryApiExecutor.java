package com.ryuqq.setof.adapter.out.client.smartdelivery.support;

import com.ryuqq.setof.adapter.out.client.smartdelivery.config.SmartDeliveryProperties;
import com.ryuqq.setof.adapter.out.client.smartdelivery.exception.SmartDeliveryException;
import com.ryuqq.setof.adapter.out.client.smartdelivery.exception.SmartDeliveryException.ErrorType;
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
 * SmartDeliveryApiExecutor - 스마트택배 API 공통 실행기
 *
 * <p>모든 스마트택배 API 호출의 공통 로직을 처리합니다.
 *
 * <p><strong>주요 기능:</strong>
 *
 * <ul>
 *   <li>재시도 로직 (Retry with Exponential Backoff)
 *   <li>서킷 브레이커 (Circuit Breaker)
 *   <li>통합 예외 처리
 *   <li>로깅 및 메트릭
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class SmartDeliveryApiExecutor {

    private static final Logger log = LoggerFactory.getLogger(SmartDeliveryApiExecutor.class);

    /** 최대 재시도 횟수 */
    private static final int MAX_RETRIES = 3;

    /** 초기 재시도 대기 시간 (ms) */
    private static final long INITIAL_BACKOFF_MS = 100;

    /** 서킷 오픈 임계값 (연속 실패 횟수) */
    private static final int CIRCUIT_FAILURE_THRESHOLD = 5;

    /** 서킷 반오픈 대기 시간 */
    private static final Duration CIRCUIT_OPEN_DURATION = Duration.ofSeconds(30);

    private final SmartDeliveryProperties properties;

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
    public SmartDeliveryApiExecutor(SmartDeliveryProperties properties) {
        this.properties = properties;
    }

    /**
     * API 실행 (기본값 반환)
     *
     * <p>API 호출 실패 시 기본값을 반환합니다. 예외를 던지지 않습니다.
     *
     * @param <T> 반환 타입
     * @param apiName API 이름 (로깅용)
     * @param apiCall API 호출 람다
     * @param defaultValue 실패 시 반환할 기본값
     * @return API 응답 또는 기본값
     */
    public <T> T executeWithDefault(String apiName, ApiCall<T> apiCall, Supplier<T> defaultValue) {
        try {
            return execute(apiName, apiCall);
        } catch (SmartDeliveryException e) {
            log.warn(
                    "Smart Delivery API [{}] failed, returning default value: {}",
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
     * @param apiCall API 호출 람다
     * @return API 응답
     * @throws SmartDeliveryException API 호출 실패 시
     */
    public <T> T execute(String apiName, ApiCall<T> apiCall) {
        if (!properties.isEnabled()) {
            log.debug("Smart Delivery is disabled. Skipping API call: {}", apiName);
            throw new SmartDeliveryException(ErrorType.UNKNOWN, "Smart Delivery is disabled");
        }

        checkCircuitBreaker(apiName);

        int attempt = 0;
        SmartDeliveryException lastException = null;

        while (attempt < MAX_RETRIES) {
            attempt++;
            try {
                T result = apiCall.call();
                onSuccess();
                return result;

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
                lastException = new SmartDeliveryException(ErrorType.UNKNOWN, e.getMessage(), e);
                sleep(calculateBackoff(attempt));
            }
        }

        onFailure();
        log.error(
                "Smart Delivery API [{}] failed after {} attempts: {}",
                apiName,
                attempt,
                lastException != null ? lastException.getMessage() : "Unknown error");
        throw lastException != null
                ? lastException
                : new SmartDeliveryException("API call failed after " + attempt + " attempts");
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

    private void checkCircuitBreaker(String apiName) {
        CircuitState state = circuitState.get();

        if (state == CircuitState.OPEN) {
            Instant openedAt = circuitOpenedAt.get();
            if (openedAt != null && Instant.now().isAfter(openedAt.plus(CIRCUIT_OPEN_DURATION))) {
                circuitState.compareAndSet(CircuitState.OPEN, CircuitState.HALF_OPEN);
                log.info("Smart Delivery circuit breaker moved to HALF_OPEN for API: {}", apiName);
            } else {
                throw new SmartDeliveryException(
                        ErrorType.CIRCUIT_OPEN,
                        "Circuit breaker is OPEN. API temporarily unavailable: " + apiName);
            }
        }
    }

    private void onSuccess() {
        consecutiveFailures.set(0);
        if (circuitState.get() == CircuitState.HALF_OPEN) {
            circuitState.set(CircuitState.CLOSED);
            log.info("Smart Delivery circuit breaker CLOSED after successful call");
        }
    }

    private void onFailure() {
        int failures = consecutiveFailures.incrementAndGet();
        if (failures >= CIRCUIT_FAILURE_THRESHOLD) {
            circuitState.set(CircuitState.OPEN);
            circuitOpenedAt.set(Instant.now());
            log.warn(
                    "Smart Delivery circuit breaker OPENED after {} consecutive failures",
                    failures);
        }
    }

    private SmartDeliveryException handleClientError(HttpClientErrorException e, String apiName) {
        HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());

        return switch (status) {
            case UNAUTHORIZED -> {
                log.warn("Smart Delivery API [{}] unauthorized: {}", apiName, e.getMessage());
                yield new SmartDeliveryException(
                        ErrorType.AUTHENTICATION, "Authentication failed", e);
            }
            case FORBIDDEN -> {
                log.warn("Smart Delivery API [{}] forbidden: {}", apiName, e.getMessage());
                yield new SmartDeliveryException(ErrorType.INVALID_API_KEY, "Invalid API key", e);
            }
            case NOT_FOUND -> {
                log.warn("Smart Delivery API [{}] not found: {}", apiName, e.getMessage());
                yield new SmartDeliveryException(
                        ErrorType.INVOICE_NOT_FOUND, "Invoice not found", e);
            }
            case TOO_MANY_REQUESTS -> {
                log.warn("Smart Delivery API [{}] rate limited: {}", apiName, e.getMessage());
                yield new SmartDeliveryException(ErrorType.RATE_LIMIT, "Rate limit exceeded", e);
            }
            default -> {
                log.warn(
                        "Smart Delivery API [{}] client error: {} - {}",
                        apiName,
                        status,
                        e.getMessage());
                yield new SmartDeliveryException(
                        ErrorType.BAD_REQUEST, "Bad request: " + status, e);
            }
        };
    }

    private SmartDeliveryException handleServerError(HttpServerErrorException e, String apiName) {
        log.error("Smart Delivery API [{}] server error: {}", apiName, e.getMessage());
        return new SmartDeliveryException(
                ErrorType.SERVER_ERROR, "Server error: " + e.getStatusCode(), e);
    }

    private SmartDeliveryException handleNetworkError(ResourceAccessException e, String apiName) {
        log.error("Smart Delivery API [{}] network error: {}", apiName, e.getMessage());
        return new SmartDeliveryException(ErrorType.NETWORK, "Network error: " + e.getMessage(), e);
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
        T call() throws RestClientException;
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
