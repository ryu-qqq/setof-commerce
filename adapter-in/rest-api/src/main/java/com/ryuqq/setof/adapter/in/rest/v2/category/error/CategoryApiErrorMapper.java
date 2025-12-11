package com.ryuqq.setof.adapter.in.rest.v2.category.error;

import com.ryuqq.setof.adapter.in.rest.common.mapper.ErrorMapper;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.net.URI;
import java.util.Locale;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Category 도메인 예외를 HTTP 응답으로 매핑하는 ErrorMapper
 *
 * <p>OCP 원칙 준수: 새로운 예외 추가 시 GlobalExceptionHandler 수정 없이 확장 가능
 *
 * <p>지원하는 예외:
 *
 * <ul>
 *   <li>CATEGORY_NOT_FOUND - 404 Not Found
 *   <li>INVALID_CATEGORY_* - 400 Bad Request
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CategoryApiErrorMapper implements ErrorMapper {

    private static final String ERROR_PREFIX = "CATEGORY";
    private static final URI ERROR_TYPE_BASE = URI.create("/errors/category");

    /** 지원하는 에러 코드 목록 */
    private static final Set<String> SUPPORTED_CODES =
            Set.of(
                    "CATEGORY_NOT_FOUND",
                    "INVALID_CATEGORY_ID",
                    "INVALID_CATEGORY_CODE",
                    "INVALID_CATEGORY_NAME",
                    "INVALID_CATEGORY_PATH",
                    "INVALID_CATEGORY_DEPTH");

    @Override
    public boolean supports(String code) {
        return code != null
                && (SUPPORTED_CODES.contains(code) || code.startsWith(ERROR_PREFIX + "_"));
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        String code = extractCode(ex);

        return switch (code) {
            case "CATEGORY_NOT_FOUND" ->
                    new MappedError(
                            HttpStatus.NOT_FOUND,
                            "Not Found",
                            "카테고리를 찾을 수 없습니다",
                            URI.create(ERROR_TYPE_BASE + "/not-found"));
            case "INVALID_CATEGORY_ID" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "카테고리 ID는 null이 아닌 양수여야 합니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-category-id"));
            case "INVALID_CATEGORY_CODE" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "카테고리 코드가 올바르지 않습니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-category-code"));
            case "INVALID_CATEGORY_NAME" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "카테고리명이 올바르지 않습니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-category-name"));
            case "INVALID_CATEGORY_PATH" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "카테고리 경로가 올바르지 않습니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-category-path"));
            case "INVALID_CATEGORY_DEPTH" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "카테고리 깊이가 올바르지 않습니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-category-depth"));
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
