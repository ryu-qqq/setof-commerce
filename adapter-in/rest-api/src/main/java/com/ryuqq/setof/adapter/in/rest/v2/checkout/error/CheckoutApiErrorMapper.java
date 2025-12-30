package com.ryuqq.setof.adapter.in.rest.v2.checkout.error;

import com.ryuqq.setof.adapter.in.rest.common.mapper.ErrorMapper;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.net.URI;
import java.util.Locale;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Checkout 도메인 예외를 HTTP 응답으로 매핑하는 ErrorMapper
 *
 * <p>OCP 원칙 준수: 새로운 예외 추가 시 GlobalExceptionHandler 수정 없이 확장 가능
 *
 * <p>지원하는 예외:
 *
 * <ul>
 *   <li>CHECKOUT_NOT_FOUND - 404 Not Found
 *   <li>DUPLICATE_CHECKOUT - 409 Conflict
 *   <li>CHECKOUT_STATUS - 409 Conflict
 *   <li>INSUFFICIENT_STOCK - 409 Conflict
 *   <li>LOCK_ACQUISITION - 409 Conflict
 *   <li>INVALID_CHECKOUT_* - 400 Bad Request
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CheckoutApiErrorMapper implements ErrorMapper {

    private static final String ERROR_PREFIX = "CHECKOUT";
    private static final URI ERROR_TYPE_BASE = URI.create("/errors/checkout");

    private static final Set<String> SUPPORTED_CODES =
            Set.of(
                    "CHECKOUT_NOT_FOUND",
                    "DUPLICATE_CHECKOUT",
                    "CHECKOUT_STATUS",
                    "INVALID_CHECKOUT_ID",
                    "INVALID_CHECKOUT_MONEY",
                    "INVALID_CHECKOUT_ITEM",
                    "INVALID_SHIPPING_ADDRESS",
                    "INSUFFICIENT_STOCK",
                    "LOCK_ACQUISITION");

    @Override
    public boolean supports(String code) {
        return code != null
                && (SUPPORTED_CODES.contains(code) || code.startsWith(ERROR_PREFIX + "_"));
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        String code = extractCode(ex);

        return switch (code) {
            case "CHECKOUT_NOT_FOUND" ->
                    new MappedError(
                            HttpStatus.NOT_FOUND,
                            "Not Found",
                            "결제 세션을 찾을 수 없습니다",
                            URI.create(ERROR_TYPE_BASE + "/not-found"));
            case "DUPLICATE_CHECKOUT" ->
                    new MappedError(
                            HttpStatus.CONFLICT,
                            "Conflict",
                            "이미 처리 중인 결제 세션 요청입니다",
                            URI.create(ERROR_TYPE_BASE + "/duplicate"));
            case "CHECKOUT_STATUS" ->
                    new MappedError(
                            HttpStatus.CONFLICT,
                            "Conflict",
                            ex.getMessage() != null ? ex.getMessage() : "결제 세션 상태가 유효하지 않습니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-status"));
            case "INSUFFICIENT_STOCK" ->
                    new MappedError(
                            HttpStatus.CONFLICT,
                            "Conflict",
                            "재고가 부족합니다",
                            URI.create(ERROR_TYPE_BASE + "/insufficient-stock"));
            case "LOCK_ACQUISITION" ->
                    new MappedError(
                            HttpStatus.CONFLICT,
                            "Conflict",
                            "락 획득에 실패했습니다. 잠시 후 다시 시도해주세요",
                            URI.create(ERROR_TYPE_BASE + "/lock-failed"));
            case "INVALID_CHECKOUT_ID" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "유효하지 않은 결제 세션 ID입니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-id"));
            case "INVALID_CHECKOUT_MONEY" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "유효하지 않은 금액입니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-money"));
            case "INVALID_CHECKOUT_ITEM" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "유효하지 않은 결제 항목입니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-item"));
            case "INVALID_SHIPPING_ADDRESS" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "유효하지 않은 배송지 정보입니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-shipping-address"));
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
