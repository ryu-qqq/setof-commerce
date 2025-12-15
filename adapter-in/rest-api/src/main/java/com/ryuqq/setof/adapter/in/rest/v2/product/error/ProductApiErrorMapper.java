package com.ryuqq.setof.adapter.in.rest.v2.product.error;

import com.ryuqq.setof.adapter.in.rest.common.mapper.ErrorMapper;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.net.URI;
import java.util.Locale;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Product 도메인 예외를 HTTP 응답으로 매핑하는 ErrorMapper
 *
 * <p>OCP 원칙 준수: 새로운 예외 추가 시 GlobalExceptionHandler 수정 없이 확장 가능
 *
 * <p>지원하는 예외:
 *
 * <ul>
 *   <li>PRODUCT_GROUP_NOT_FOUND - 404 Not Found
 *   <li>PRODUCT_NOT_FOUND - 404 Not Found
 *   <li>INVALID_PRODUCT_* - 400 Bad Request
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductApiErrorMapper implements ErrorMapper {

    private static final String ERROR_PREFIX = "PRODUCT";
    private static final URI ERROR_TYPE_BASE = URI.create("/errors/product");

    /** 지원하는 에러 코드 목록 */
    private static final Set<String> SUPPORTED_CODES =
            Set.of(
                    "PRODUCT_GROUP_NOT_FOUND",
                    "PRODUCT_NOT_FOUND",
                    "INVALID_PRODUCT_GROUP_ID",
                    "INVALID_PRODUCT_ID",
                    "INVALID_PRODUCT_GROUP_NAME",
                    "INVALID_PRICE",
                    "INVALID_MONEY",
                    "PRODUCT_GROUP_ALREADY_DELETED",
                    "PRODUCT_GROUP_NOT_EDITABLE",
                    "INVALID_OPTION_CONFIGURATION",
                    "PRODUCT_NOT_BELONG_TO_GROUP");

    @Override
    public boolean supports(String code) {
        return code != null
                && (SUPPORTED_CODES.contains(code) || code.startsWith(ERROR_PREFIX + "_"));
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        String code = extractCode(ex);

        return switch (code) {
            case "PRODUCT_GROUP_NOT_FOUND" ->
                    new MappedError(
                            HttpStatus.NOT_FOUND,
                            "Not Found",
                            "상품그룹을 찾을 수 없습니다",
                            URI.create(ERROR_TYPE_BASE + "/product-group-not-found"));
            case "PRODUCT_NOT_FOUND" ->
                    new MappedError(
                            HttpStatus.NOT_FOUND,
                            "Not Found",
                            "상품을 찾을 수 없습니다",
                            URI.create(ERROR_TYPE_BASE + "/product-not-found"));
            case "INVALID_PRODUCT_GROUP_ID" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "상품그룹 ID가 올바르지 않습니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-product-group-id"));
            case "INVALID_PRODUCT_ID" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "상품 ID가 올바르지 않습니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-product-id"));
            case "INVALID_PRODUCT_GROUP_NAME" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "상품그룹명이 올바르지 않습니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-product-group-name"));
            case "INVALID_PRICE" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "가격이 올바르지 않습니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-price"));
            case "INVALID_MONEY" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "금액이 올바르지 않습니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-money"));
            case "PRODUCT_GROUP_ALREADY_DELETED" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "이미 삭제된 상품그룹입니다",
                            URI.create(ERROR_TYPE_BASE + "/product-group-already-deleted"));
            case "PRODUCT_GROUP_NOT_EDITABLE" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "수정할 수 없는 상품그룹입니다",
                            URI.create(ERROR_TYPE_BASE + "/product-group-not-editable"));
            case "INVALID_OPTION_CONFIGURATION" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "옵션 구성이 올바르지 않습니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-option-configuration"));
            case "PRODUCT_NOT_BELONG_TO_GROUP" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "해당 상품그룹에 속하지 않는 상품입니다",
                            URI.create(ERROR_TYPE_BASE + "/product-not-belong-to-group"));
            default ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            ex.getMessage() != null ? ex.getMessage() : "잘못된 요청입니다",
                            ERROR_TYPE_BASE);
        };
    }

    /**
     * 예외에서 에러 코드 추출
     *
     * <p>클래스명을 UPPER_SNAKE_CASE로 변환
     */
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
