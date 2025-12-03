package com.ryuqq.setof.domain.core.member.vo;

import com.ryuqq.setof.domain.core.common.util.UuidV7Generator;
import com.ryuqq.setof.domain.core.member.exception.InvalidMemberIdException;
import java.util.UUID;

/**
 * 회원 식별자 Value Object (UUID v7)
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>UUID v7 사용 - 시간 순서 보장 + 보안성
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 * </ul>
 *
 * <p>UUID v7 장점:
 *
 * <ul>
 *   <li>DB 저장 전에 ID 생성 가능
 *   <li>시간 기반 정렬 가능 (인덱스 친화적)
 *   <li>예측 불가능 (보안성)
 *   <li>분산 환경에서 충돌 없음
 * </ul>
 *
 * @param value 회원 ID 값 (UUID)
 */
public record MemberId(UUID value) {

    /** Compact Constructor - 검증 로직 */
    public MemberId {
        validate(value);
    }

    /**
     * Static Factory Method - 신규 생성용 (UUID v7 자동 생성)
     *
     * @return 새로운 UUID v7 기반 MemberId
     */
    public static MemberId generate() {
        return new MemberId(UuidV7Generator.generate());
    }

    /**
     * Static Factory Method - 기존 UUID로 생성
     *
     * @param value UUID 값
     * @return MemberId 인스턴스
     * @throws InvalidMemberIdException value가 null인 경우
     */
    public static MemberId of(UUID value) {
        return new MemberId(value);
    }

    /**
     * Static Factory Method - 문자열에서 생성
     *
     * @param value UUID 문자열 (하이픈 포함 또는 미포함)
     * @return MemberId 인스턴스
     * @throws InvalidMemberIdException value가 null이거나 잘못된 형식인 경우
     */
    public static MemberId of(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidMemberIdException(value);
        }
        try {
            String normalized = normalizeUuidString(value);
            return new MemberId(UUID.fromString(normalized));
        } catch (IllegalArgumentException e) {
            throw new InvalidMemberIdException(value);
        }
    }

    /**
     * UUID 문자열 반환
     *
     * @return UUID 문자열 (하이픈 포함)
     */
    public String asString() {
        return value.toString();
    }

    /**
     * UUID 문자열 반환 (하이픈 없음)
     *
     * @return UUID 문자열 (32자)
     */
    public String asCompactString() {
        return value.toString().replace("-", "");
    }

    private static void validate(UUID value) {
        if (value == null) {
            throw new InvalidMemberIdException((String) null);
        }
    }

    private static String normalizeUuidString(String value) {
        String trimmed = value.trim();
        if (trimmed.length() == 32) {
            return trimmed.substring(0, 8)
                    + "-"
                    + trimmed.substring(8, 12)
                    + "-"
                    + trimmed.substring(12, 16)
                    + "-"
                    + trimmed.substring(16, 20)
                    + "-"
                    + trimmed.substring(20);
        }
        return trimmed;
    }
}
