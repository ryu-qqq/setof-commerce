package com.ryuqq.setof.domain.common.util;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * UUID 검증 유틸리티 (Domain Layer 공통)
 *
 * <p>UUID v7 문자열 형식을 검증하는 정적 유틸리티 클래스입니다.
 *
 * <p>지원 형식:
 *
 * <ul>
 *   <li>하이픈 포함: 550e8400-e29b-41d4-a716-446655440000 (36자)
 *   <li>하이픈 미포함: 550e8400e29b41d4a716446655440000 (32자)
 * </ul>
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>외부 라이브러리 의존 금지
 *   <li>정적 유틸리티 - 인스턴스 생성 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public final class UuidValidator {

    /** UUID 정규식 (하이픈 포함, 36자) */
    private static final Pattern UUID_WITH_HYPHENS =
            Pattern.compile(
                    "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    /** UUID 정규식 (하이픈 미포함, 32자) */
    private static final Pattern UUID_WITHOUT_HYPHENS = Pattern.compile("^[0-9a-fA-F]{32}$");

    /** 유틸리티 클래스 - 인스턴스 생성 금지 */
    private UuidValidator() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다");
    }

    /**
     * UUID 문자열 유효성 검사
     *
     * <p>null, blank, 잘못된 UUID 형식을 검사합니다.
     *
     * @param value 검증할 UUID 문자열
     * @return 유효한 UUID이면 true
     */
    public static boolean isValid(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }

        String trimmed = value.trim();

        // 정규식으로 먼저 빠르게 형식 체크
        if (!UUID_WITH_HYPHENS.matcher(trimmed).matches()
                && !UUID_WITHOUT_HYPHENS.matcher(trimmed).matches()) {
            return false;
        }

        // UUID.fromString()으로 최종 검증 (하이픈 없는 경우 정규화 필요)
        try {
            String normalized = normalizeUuidString(trimmed);
            UUID.fromString(normalized);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * UUID 문자열이 유효하지 않으면 예외 발생
     *
     * @param value 검증할 UUID 문자열
     * @param fieldName 필드명 (예외 메시지용)
     * @throws IllegalArgumentException UUID가 유효하지 않은 경우
     */
    public static void requireValid(String value, String fieldName) {
        if (!isValid(value)) {
            throw new IllegalArgumentException(
                    String.format(
                            "%s는 유효한 UUID 형식이어야 합니다. 입력값: %s",
                            fieldName, value != null ? value : "null"));
        }
    }

    /**
     * UUID 문자열 정규화 (하이픈 없는 형식 → 하이픈 포함 형식)
     *
     * @param value UUID 문자열 (32자 또는 36자)
     * @return 하이픈 포함된 36자 UUID 문자열
     */
    public static String normalizeUuidString(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        if (trimmed.length() == 32) {
            // 32자 → 8-4-4-4-12 형식으로 변환
            return trimmed.replaceFirst(
                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{12})",
                    "$1-$2-$3-$4-$5");
        }
        return trimmed;
    }
}
