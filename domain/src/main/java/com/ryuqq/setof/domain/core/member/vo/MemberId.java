package com.ryuqq.setof.domain.core.member.vo;

import com.ryuqq.setof.domain.core.member.exception.InvalidMemberIdException;

/**
 * 회원 식별자 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:</p>
 * <ul>
 *     <li>Lombok 금지 - Pure Java Record 사용</li>
 *     <li>불변성 보장 - Java Record 특성</li>
 *     <li>Long > 0 검증 - 양수만 허용</li>
 *     <li>Private 생성자 + Static Factory - 외부 직접 생성 금지</li>
 * </ul>
 *
 * @param value 회원 ID 값 (양수)
 */
public record MemberId(Long value) {

    /**
     * Compact Constructor - 검증 로직
     */
    public MemberId {
        validate(value);
    }

    /**
     * Static Factory Method - 신규 생성용
     *
     * @param value 회원 ID 값
     * @return MemberId 인스턴스
     * @throws InvalidMemberIdException value가 null이거나 0 이하인 경우
     */
    public static MemberId of(Long value) {
        return new MemberId(value);
    }

    private static void validate(Long value) {
        if (value == null || value <= 0) {
            throw new InvalidMemberIdException(value);
        }
    }
}
