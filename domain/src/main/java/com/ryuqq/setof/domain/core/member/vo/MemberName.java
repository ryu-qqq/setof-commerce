package com.ryuqq.setof.domain.core.member.vo;

import com.ryuqq.setof.domain.core.member.exception.InvalidMemberNameException;

/**
 * 회원 이름 Value Object
 *
 * <p>검증 규칙:
 *
 * <ul>
 *   <li>2~5자 길이
 *   <li>NotBlank
 * </ul>
 *
 * @param value 회원 이름
 */
public record MemberName(String value) {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 5;

    public MemberName {
        validate(value);
    }

    public static MemberName of(String value) {
        return new MemberName(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidMemberNameException();
        }
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new InvalidMemberNameException(
                    value, String.format("이름은 %d~%d자여야 합니다.", MIN_LENGTH, MAX_LENGTH));
        }
    }
}
