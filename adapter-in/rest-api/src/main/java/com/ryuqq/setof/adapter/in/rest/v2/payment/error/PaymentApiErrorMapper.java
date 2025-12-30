package com.ryuqq.setof.adapter.in.rest.v2.payment.error;

import com.ryuqq.setof.adapter.in.rest.common.mapper.ErrorMapper;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.net.URI;
import java.util.Locale;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Payment 도메인 예외를 HTTP 응답으로 매핑하는 ErrorMapper
 *
 * <p>OCP 원칙 준수: 새로운 예외 추가 시 GlobalExceptionHandler 수정 없이 확장 가능
 *
 * <p>지원하는 예외:
 *
 * <ul>
 *   <li>PAYMENT_NOT_FOUND - 404 Not Found
 *   <li>PAYMENT_STATUS - 409 Conflict
 *   <li>PAYMENT_COMPLETION_IN_PROGRESS - 409 Conflict
 *   <li>REFUND_AMOUNT - 400 Bad Request
 *   <li>INVALID_PAYMENT_* - 400 Bad Request
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class PaymentApiErrorMapper implements ErrorMapper {

    private static final String ERROR_PREFIX = "PAYMENT";
    private static final URI ERROR_TYPE_BASE = URI.create("/errors/payment");

    private static final Set<String> SUPPORTED_CODES =
            Set.of(
                    "PAYMENT_NOT_FOUND",
                    "PAYMENT_STATUS",
                    "PAYMENT_COMPLETION_IN_PROGRESS",
                    "INVALID_PAYMENT_ID",
                    "INVALID_PAYMENT_MONEY",
                    "REFUND_AMOUNT");

    @Override
    public boolean supports(String code) {
        return code != null
                && (SUPPORTED_CODES.contains(code) || code.startsWith(ERROR_PREFIX + "_"));
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        String code = extractCode(ex);

        return switch (code) {
            case "PAYMENT_NOT_FOUND" ->
                    new MappedError(
                            HttpStatus.NOT_FOUND,
                            "Not Found",
                            "결제 정보를 찾을 수 없습니다",
                            URI.create(ERROR_TYPE_BASE + "/not-found"));
            case "PAYMENT_STATUS" ->
                    new MappedError(
                            HttpStatus.CONFLICT,
                            "Conflict",
                            ex.getMessage() != null ? ex.getMessage() : "결제 상태가 유효하지 않습니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-status"));
            case "PAYMENT_COMPLETION_IN_PROGRESS" ->
                    new MappedError(
                            HttpStatus.CONFLICT,
                            "Conflict",
                            "결제 완료 처리가 이미 진행 중입니다",
                            URI.create(ERROR_TYPE_BASE + "/completion-in-progress"));
            case "REFUND_AMOUNT" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            ex.getMessage() != null ? ex.getMessage() : "환불 금액이 유효하지 않습니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-refund-amount"));
            case "INVALID_PAYMENT_ID" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "유효하지 않은 결제 ID입니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-id"));
            case "INVALID_PAYMENT_MONEY" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "유효하지 않은 결제 금액입니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-money"));
            default ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            ex.getMessage() != null ? ex.getMessage() : "잘못된 요청입니다",
                            ERROR_TYPE_BASE);
        };
    }

    private String extractCode(DomainException ex) {
        String className = ex.getClass().getSimpleName();
        if (className.endsWith("Exception")) {
            className = className.substring(0, className.length() - "Exception".length());
        }
        return camelToUpperSnake(className);
    }

    private String camelToUpperSnake(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                result.append('_');
            }
            result.append(Character.toUpperCase(c));
        }
        return result.toString();
    }
}
