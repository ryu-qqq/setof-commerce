package com.ryuqq.setof.adapter.in.rest.admin.v2.claim.error;

import com.ryuqq.setof.adapter.in.rest.admin.common.mapper.ErrorMapper;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.net.URI;
import java.util.Locale;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Claim 도메인 예외를 HTTP 응답으로 매핑하는 ErrorMapper
 *
 * <p>OCP 원칙 준수: 새로운 예외 추가 시 GlobalExceptionHandler 수정 없이 확장 가능
 *
 * <p>지원하는 예외:
 *
 * <ul>
 *   <li>CLAIM_NOT_FOUND - 404 Not Found
 *   <li>CLAIM_STATUS - 409 Conflict
 *   <li>CLAIM_SHIPPING - 400 Bad Request
 *   <li>기타 CLAIM_ 관련 예외 - 400 Bad Request
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ClaimAdminErrorMapper implements ErrorMapper {

    private static final String ERROR_PREFIX = "CLAIM";
    private static final URI ERROR_TYPE_BASE = URI.create("/errors/claim");

    private static final Set<String> SUPPORTED_CODES =
            Set.of("CLAIM_NOT_FOUND", "CLAIM_STATUS", "CLAIM_SHIPPING");

    @Override
    public boolean supports(String code) {
        return code != null
                && (SUPPORTED_CODES.contains(code) || code.startsWith(ERROR_PREFIX + "_"));
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        String code = extractCode(ex);

        return switch (code) {
            case "CLAIM_NOT_FOUND" ->
                    new MappedError(
                            HttpStatus.NOT_FOUND,
                            "Not Found",
                            ex.getMessage() != null ? ex.getMessage() : "클레임을 찾을 수 없습니다",
                            URI.create(ERROR_TYPE_BASE + "/not-found"));
            case "CLAIM_STATUS" ->
                    new MappedError(
                            HttpStatus.CONFLICT,
                            "Conflict",
                            ex.getMessage() != null ? ex.getMessage() : "클레임 상태 변경이 불가능합니다",
                            URI.create(ERROR_TYPE_BASE + "/status-conflict"));
            case "CLAIM_SHIPPING" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            ex.getMessage() != null ? ex.getMessage() : "배송 정보가 올바르지 않습니다",
                            URI.create(ERROR_TYPE_BASE + "/shipping-error"));
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
