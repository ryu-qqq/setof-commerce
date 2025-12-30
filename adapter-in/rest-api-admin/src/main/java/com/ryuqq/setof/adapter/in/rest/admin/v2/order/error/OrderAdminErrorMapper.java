package com.ryuqq.setof.adapter.in.rest.admin.v2.order.error;

import com.ryuqq.setof.adapter.in.rest.admin.common.mapper.ErrorMapper;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.net.URI;
import java.util.Locale;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Order 도메인 예외를 HTTP 응답으로 매핑하는 ErrorMapper
 *
 * <p>OCP 원칙 준수: 새로운 예외 추가 시 GlobalExceptionHandler 수정 없이 확장 가능
 *
 * <p>지원하는 예외:
 *
 * <ul>
 *   <li>ORDER_NOT_FOUND - 404 Not Found
 *   <li>ORDER_STATUS - 409 Conflict
 *   <li>기타 ORDER_ 관련 예외 - 400 Bad Request
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class OrderAdminErrorMapper implements ErrorMapper {

    private static final String ERROR_PREFIX = "ORDER";
    private static final URI ERROR_TYPE_BASE = URI.create("/errors/order");

    private static final Set<String> SUPPORTED_CODES =
            Set.of(
                    "ORDER_NOT_FOUND",
                    "ORDER_STATUS",
                    "INVALID_ORDER_ID",
                    "INVALID_ORDER_NUMBER",
                    "INVALID_ORDER_MONEY",
                    "INVALID_ORDER_ITEM_ID",
                    "INVALID_SHIPPING_INFO",
                    "INVALID_PRODUCT_SNAPSHOT",
                    "ORDER_ITEM_QUANTITY");

    @Override
    public boolean supports(String code) {
        return code != null
                && (SUPPORTED_CODES.contains(code) || code.startsWith(ERROR_PREFIX + "_"));
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        String code = extractCode(ex);

        return switch (code) {
            case "ORDER_NOT_FOUND" ->
                    new MappedError(
                            HttpStatus.NOT_FOUND,
                            "Not Found",
                            ex.getMessage() != null ? ex.getMessage() : "주문을 찾을 수 없습니다",
                            URI.create(ERROR_TYPE_BASE + "/not-found"));
            case "ORDER_STATUS" ->
                    new MappedError(
                            HttpStatus.CONFLICT,
                            "Conflict",
                            ex.getMessage() != null ? ex.getMessage() : "주문 상태 변경이 불가능합니다",
                            URI.create(ERROR_TYPE_BASE + "/status-conflict"));
            case "INVALID_ORDER_ID" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "유효하지 않은 주문 ID입니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-order-id"));
            case "INVALID_ORDER_NUMBER" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "유효하지 않은 주문번호입니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-order-number"));
            case "INVALID_ORDER_MONEY" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "유효하지 않은 주문 금액입니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-order-money"));
            case "INVALID_ORDER_ITEM_ID" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "유효하지 않은 주문 상품 ID입니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-order-item-id"));
            case "INVALID_SHIPPING_INFO" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "유효하지 않은 배송 정보입니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-shipping-info"));
            case "ORDER_ITEM_QUANTITY" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "유효하지 않은 주문 수량입니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-quantity"));
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
