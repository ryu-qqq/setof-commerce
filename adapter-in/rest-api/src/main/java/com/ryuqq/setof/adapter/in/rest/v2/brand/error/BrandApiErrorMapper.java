package com.ryuqq.setof.adapter.in.rest.v2.brand.error;

import com.ryuqq.setof.adapter.in.rest.common.mapper.ErrorMapper;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.net.URI;
import java.util.Locale;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Brand 도메인 예외를 HTTP 응답으로 매핑하는 ErrorMapper
 *
 * <p>OCP 원칙 준수: 새로운 예외 추가 시 GlobalExceptionHandler 수정 없이 확장 가능
 *
 * <p>지원하는 예외:
 *
 * <ul>
 *   <li>BRAND_NOT_FOUND - 404 Not Found
 *   <li>INVALID_BRAND_* - 400 Bad Request
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BrandApiErrorMapper implements ErrorMapper {

    private static final String ERROR_PREFIX = "BRAND";
    private static final URI ERROR_TYPE_BASE = URI.create("/errors/brand");

    /** 지원하는 에러 코드 목록 */
    private static final Set<String> SUPPORTED_CODES =
            Set.of(
                    "BRAND_NOT_FOUND",
                    "INVALID_BRAND_ID",
                    "INVALID_BRAND_CODE",
                    "INVALID_BRAND_NAME_KO",
                    "INVALID_BRAND_NAME_EN",
                    "INVALID_BRAND_LOGO_URL");

    @Override
    public boolean supports(String code) {
        return code != null
                && (SUPPORTED_CODES.contains(code) || code.startsWith(ERROR_PREFIX + "_"));
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        String code = extractCode(ex);

        return switch (code) {
            case "BRAND_NOT_FOUND" ->
                    new MappedError(
                            HttpStatus.NOT_FOUND,
                            "Not Found",
                            "브랜드를 찾을 수 없습니다",
                            URI.create(ERROR_TYPE_BASE + "/not-found"));
            case "INVALID_BRAND_ID" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "브랜드 ID는 null이 아닌 양수여야 합니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-brand-id"));
            case "INVALID_BRAND_CODE" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "브랜드 코드가 올바르지 않습니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-brand-code"));
            case "INVALID_BRAND_NAME_KO" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "한글 브랜드명이 올바르지 않습니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-brand-name-ko"));
            case "INVALID_BRAND_NAME_EN" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "영문 브랜드명이 올바르지 않습니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-brand-name-en"));
            case "INVALID_BRAND_LOGO_URL" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "브랜드 로고 URL이 올바르지 않습니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-brand-logo-url"));
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
