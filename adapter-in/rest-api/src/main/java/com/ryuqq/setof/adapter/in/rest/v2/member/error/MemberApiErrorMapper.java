package com.ryuqq.setof.adapter.in.rest.v2.member.error;

import com.ryuqq.setof.adapter.in.rest.common.mapper.ErrorMapper;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.net.URI;
import java.util.Locale;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Member 도메인 예외를 HTTP 응답으로 매핑하는 ErrorMapper
 *
 * <p>OCP 원칙 준수: 새로운 예외 추가 시 GlobalExceptionHandler 수정 없이 확장 가능
 *
 * <p>지원하는 예외:
 *
 * <ul>
 *   <li>MEMBER_NOT_FOUND - 404 Not Found
 *   <li>DUPLICATE_PHONE_NUMBER - 409 Conflict
 *   <li>INVALID_PASSWORD - 401 Unauthorized
 *   <li>INACTIVE_MEMBER - 403 Forbidden
 *   <li>ALREADY_WITHDRAWN_MEMBER - 403 Forbidden
 *   <li>기타 MEMBER_ 관련 예외 - 400 Bad Request
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class MemberApiErrorMapper implements ErrorMapper {

    private static final String ERROR_PREFIX = "MEMBER";
    private static final URI ERROR_TYPE_BASE = URI.create("/errors/member");

    /** 지원하는 에러 코드 목록 */
    private static final Set<String> SUPPORTED_CODES =
            Set.of(
                    "MEMBER_NOT_FOUND",
                    "DUPLICATE_PHONE_NUMBER",
                    "INVALID_PASSWORD",
                    "INVALID_EMAIL",
                    "INVALID_PHONE_NUMBER",
                    "INVALID_MEMBER_NAME",
                    "INVALID_MEMBER_ID",
                    "INVALID_SOCIAL_ID",
                    "INVALID_REFRESH_TOKEN",
                    "INVALID_WITHDRAWAL_INFO",
                    "INACTIVE_MEMBER",
                    "ALREADY_WITHDRAWN_MEMBER",
                    "ALREADY_KAKAO_MEMBER",
                    "KAKAO_MEMBER_CANNOT_CHANGE_PASSWORD",
                    "PASSWORD_POLICY_VIOLATION",
                    "REQUIRED_CONSENT_MISSING");

    @Override
    public boolean supports(String code) {
        return code != null
                && (SUPPORTED_CODES.contains(code) || code.startsWith(ERROR_PREFIX + "_"));
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        String code = extractCode(ex);

        return switch (code) {
            case "MEMBER_NOT_FOUND" ->
                    new MappedError(
                            HttpStatus.NOT_FOUND,
                            "Not Found",
                            "회원을 찾을 수 없습니다",
                            URI.create(ERROR_TYPE_BASE + "/not-found"));
            case "DUPLICATE_PHONE_NUMBER" ->
                    new MappedError(
                            HttpStatus.CONFLICT,
                            "Conflict",
                            "이미 사용 중인 핸드폰 번호입니다",
                            URI.create(ERROR_TYPE_BASE + "/duplicate-phone-number"));
            case "INVALID_PASSWORD" ->
                    new MappedError(
                            HttpStatus.UNAUTHORIZED,
                            "Unauthorized",
                            "비밀번호가 일치하지 않습니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-password"));
            case "INVALID_EMAIL" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "유효하지 않은 이메일 형식입니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-email"));
            case "INVALID_PHONE_NUMBER" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "유효하지 않은 핸드폰 번호입니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-phone-number"));
            case "INVALID_MEMBER_NAME" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "유효하지 않은 이름입니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-member-name"));
            case "INVALID_REFRESH_TOKEN" ->
                    new MappedError(
                            HttpStatus.UNAUTHORIZED,
                            "Unauthorized",
                            "유효하지 않은 리프레시 토큰입니다",
                            URI.create(ERROR_TYPE_BASE + "/invalid-refresh-token"));
            case "INACTIVE_MEMBER" ->
                    new MappedError(
                            HttpStatus.FORBIDDEN,
                            "Forbidden",
                            "비활성화된 회원입니다",
                            URI.create(ERROR_TYPE_BASE + "/inactive-member"));
            case "ALREADY_WITHDRAWN_MEMBER" ->
                    new MappedError(
                            HttpStatus.FORBIDDEN,
                            "Forbidden",
                            "이미 탈퇴한 회원입니다",
                            URI.create(ERROR_TYPE_BASE + "/already-withdrawn"));
            case "ALREADY_KAKAO_MEMBER" ->
                    new MappedError(
                            HttpStatus.CONFLICT,
                            "Conflict",
                            "이미 카카오 계정이 연동되어 있습니다",
                            URI.create(ERROR_TYPE_BASE + "/already-kakao-linked"));
            case "KAKAO_MEMBER_CANNOT_CHANGE_PASSWORD" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "카카오 로그인 회원은 비밀번호를 변경할 수 없습니다",
                            URI.create(ERROR_TYPE_BASE + "/kakao-cannot-change-password"));
            case "PASSWORD_POLICY_VIOLATION" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "비밀번호 정책을 위반했습니다",
                            URI.create(ERROR_TYPE_BASE + "/password-policy-violation"));
            case "REQUIRED_CONSENT_MISSING" ->
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            "필수 동의 항목이 누락되었습니다",
                            URI.create(ERROR_TYPE_BASE + "/required-consent-missing"));
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
