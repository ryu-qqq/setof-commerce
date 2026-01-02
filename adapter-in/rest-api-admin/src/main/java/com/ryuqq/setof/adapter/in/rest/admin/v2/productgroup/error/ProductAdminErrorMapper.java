package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.error;

import com.ryuqq.setof.adapter.in.rest.admin.common.mapper.ErrorMapper;
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
 *   <li>PRODUCT_IMAGE_NOT_FOUND - 404 Not Found
 *   <li>PRODUCT_DESCRIPTION_NOT_FOUND - 404 Not Found
 *   <li>PRODUCT_NOTICE_NOT_FOUND - 404 Not Found
 *   <li>PRODUCT_STOCK_NOT_FOUND - 404 Not Found
 *   <li>기타 PRODUCT_ 관련 예외 - 400 Bad Request
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ProductAdminErrorMapper implements ErrorMapper {

    private static final String ERROR_PREFIX = "PRODUCT";
    private static final URI ERROR_TYPE_BASE = URI.create("/errors/product");

    private static final Set<String> NOT_FOUND_CODES =
            Set.of(
                    "PRODUCT_GROUP_NOT_FOUND",
                    "PRODUCT_NOT_FOUND",
                    "PRODUCT_IMAGE_NOT_FOUND",
                    "PRODUCT_DESCRIPTION_NOT_FOUND",
                    "PRODUCT_NOTICE_NOT_FOUND",
                    "PRODUCT_STOCK_NOT_FOUND",
                    "NOTICE_TEMPLATE_NOT_FOUND");

    private static final Set<String> SUPPORTED_CODES =
            Set.of(
                    "PRODUCT_GROUP_NOT_FOUND",
                    "PRODUCT_NOT_FOUND",
                    "PRODUCT_IMAGE_NOT_FOUND",
                    "PRODUCT_DESCRIPTION_NOT_FOUND",
                    "PRODUCT_NOTICE_NOT_FOUND",
                    "PRODUCT_STOCK_NOT_FOUND",
                    "NOTICE_TEMPLATE_NOT_FOUND",
                    "INVALID_PRODUCT_GROUP_ID",
                    "INVALID_PRODUCT_ID",
                    "INVALID_PRODUCT_PRICE",
                    "INVALID_PRODUCT_STOCK",
                    "INVALID_PRODUCT_OPTION");

    @Override
    public boolean supports(String code) {
        return code != null
                && (SUPPORTED_CODES.contains(code)
                        || code.startsWith(ERROR_PREFIX + "_")
                        || code.startsWith("NOTICE_TEMPLATE_"));
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        String code = extractCode(ex);

        if (NOT_FOUND_CODES.contains(code)) {
            return createNotFoundError(code, ex);
        }

        return createBadRequestError(code, ex);
    }

    private MappedError createNotFoundError(String code, DomainException ex) {
        String message =
                ex.getMessage() != null ? ex.getMessage() : getDefaultNotFoundMessage(code);
        String uriSuffix = code.toLowerCase().replace("_", "-");
        return new MappedError(
                HttpStatus.NOT_FOUND,
                "Not Found",
                message,
                URI.create(ERROR_TYPE_BASE + "/" + uriSuffix));
    }

    private MappedError createBadRequestError(String code, DomainException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "잘못된 요청입니다";
        String uriSuffix = code.toLowerCase().replace("_", "-");
        return new MappedError(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                message,
                URI.create(ERROR_TYPE_BASE + "/" + uriSuffix));
    }

    private String getDefaultNotFoundMessage(String code) {
        return switch (code) {
            case "PRODUCT_GROUP_NOT_FOUND" -> "상품 그룹을 찾을 수 없습니다";
            case "PRODUCT_NOT_FOUND" -> "상품을 찾을 수 없습니다";
            case "PRODUCT_IMAGE_NOT_FOUND" -> "상품 이미지를 찾을 수 없습니다";
            case "PRODUCT_DESCRIPTION_NOT_FOUND" -> "상품 설명을 찾을 수 없습니다";
            case "PRODUCT_NOTICE_NOT_FOUND" -> "상품 공지를 찾을 수 없습니다";
            case "PRODUCT_STOCK_NOT_FOUND" -> "상품 재고를 찾을 수 없습니다";
            case "NOTICE_TEMPLATE_NOT_FOUND" -> "공지 템플릿을 찾을 수 없습니다";
            default -> "리소스를 찾을 수 없습니다";
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
