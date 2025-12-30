package com.ryuqq.setof.adapter.in.rest.admin.v2.content.error;

import com.ryuqq.setof.adapter.in.rest.admin.common.mapper.ErrorMapper;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.net.URI;
import java.util.Locale;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * CMS 도메인 예외를 HTTP 응답으로 매핑하는 ErrorMapper
 *
 * <p>OCP 원칙 준수: 새로운 예외 추가 시 GlobalExceptionHandler 수정 없이 확장 가능
 *
 * <p>지원하는 예외:
 *
 * <ul>
 *   <li>*_NOT_FOUND - 404 Not Found
 *   <li>*_ALREADY_EXISTS - 409 Conflict
 *   <li>INVALID_* - 400 Bad Request
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class CmsAdminErrorMapper implements ErrorMapper {

    private static final URI ERROR_TYPE_BASE = URI.create("/errors/cms");

    private static final Set<String> NOT_FOUND_CODES =
            Set.of(
                    "CONTENT_NOT_FOUND",
                    "COMPONENT_NOT_FOUND",
                    "BANNER_NOT_FOUND",
                    "BANNER_ITEM_NOT_FOUND",
                    "GNB_NOT_FOUND");

    private static final Set<String> CONFLICT_CODES = Set.of("CONTENT_ALREADY_EXISTS");

    private static final Set<String> SUPPORTED_PREFIXES =
            Set.of(
                    "CONTENT_",
                    "COMPONENT_",
                    "BANNER_",
                    "GNB_",
                    "INVALID_CONTENT",
                    "INVALID_DISPLAY",
                    "INVALID_COMPONENT",
                    "INVALID_BANNER");

    @Override
    public boolean supports(String code) {
        if (code == null) {
            return false;
        }
        if (NOT_FOUND_CODES.contains(code) || CONFLICT_CODES.contains(code)) {
            return true;
        }
        for (String prefix : SUPPORTED_PREFIXES) {
            if (code.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        String code = extractCode(ex);

        if (NOT_FOUND_CODES.contains(code)) {
            return new MappedError(
                    HttpStatus.NOT_FOUND,
                    "Not Found",
                    ex.getMessage() != null ? ex.getMessage() : "리소스를 찾을 수 없습니다",
                    URI.create(ERROR_TYPE_BASE + "/not-found"));
        }

        if (CONFLICT_CODES.contains(code)) {
            return new MappedError(
                    HttpStatus.CONFLICT,
                    "Conflict",
                    ex.getMessage() != null ? ex.getMessage() : "리소스가 이미 존재합니다",
                    URI.create(ERROR_TYPE_BASE + "/conflict"));
        }

        return new MappedError(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                ex.getMessage() != null ? ex.getMessage() : "잘못된 요청입니다",
                ERROR_TYPE_BASE);
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
