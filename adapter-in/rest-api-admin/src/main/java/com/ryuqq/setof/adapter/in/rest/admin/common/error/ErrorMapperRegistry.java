package com.ryuqq.setof.adapter.in.rest.admin.common.error;

import com.ryuqq.setof.adapter.in.rest.admin.common.mapper.ErrorMapper;
import com.ryuqq.setof.domain.common.exception.DomainException;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * ErrorMapperRegistry - 도메인 예외를 HTTP 응답으로 매핑하는 레지스트리
 *
 * <p>등록된 ErrorMapper들을 순회하며 예외를 HTTP 응답으로 변환합니다.
 *
 * <p>동작 방식:
 *
 * <ol>
 *   <li>예외 클래스명을 에러 코드로 사용 (e.g., MemberNotFoundException → MEMBER_NOT_FOUND)
 *   <li>등록된 ErrorMapper 중 해당 코드를 지원하는 매퍼를 찾음
 *   <li>매퍼가 없으면 기본 매핑 적용 (400 Bad Request)
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ErrorMapperRegistry {

    private final List<ErrorMapper> mappers;

    public ErrorMapperRegistry(List<ErrorMapper> mappers) {
        this.mappers = mappers;
    }

    /**
     * 도메인 예외를 HTTP 응답으로 매핑
     *
     * @param ex 도메인 예외
     * @param locale 로케일 (국제화 지원)
     * @return 매핑 결과 (없으면 Optional.empty)
     */
    public Optional<ErrorMapper.MappedError> map(DomainException ex, Locale locale) {
        String code = extractErrorCode(ex);

        return mappers.stream()
                .filter(m -> m.supports(code))
                .findFirst()
                .map(m -> m.map(ex, locale));
    }

    /**
     * 기본 매핑 적용
     *
     * <p>등록된 매퍼가 없을 때 사용
     *
     * @param ex 도메인 예외
     * @return 기본 매핑 결과 (400 Bad Request)
     */
    public ErrorMapper.MappedError defaultMapping(DomainException ex) {
        return new ErrorMapper.MappedError(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                ex.getMessage() != null ? ex.getMessage() : "Invalid request",
                URI.create("about:blank"));
    }

    /**
     * 예외 클래스명에서 에러 코드 추출
     *
     * <p>클래스명을 UPPER_SNAKE_CASE로 변환
     *
     * <p>예시:
     *
     * <ul>
     *   <li>MemberNotFoundException → MEMBER_NOT_FOUND
     *   <li>InvalidEmailException → INVALID_EMAIL
     * </ul>
     *
     * @param ex 도메인 예외
     * @return 에러 코드
     */
    public String extractErrorCode(DomainException ex) {
        String className = ex.getClass().getSimpleName();

        // "Exception" 접미사 제거
        if (className.endsWith("Exception")) {
            className = className.substring(0, className.length() - "Exception".length());
        }

        // CamelCase → UPPER_SNAKE_CASE
        return camelToUpperSnake(className);
    }

    /**
     * CamelCase를 UPPER_SNAKE_CASE로 변환
     *
     * @param str CamelCase 문자열
     * @return UPPER_SNAKE_CASE 문자열
     */
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
