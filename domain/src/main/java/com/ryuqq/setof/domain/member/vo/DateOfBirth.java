package com.ryuqq.setof.domain.member.vo;

import java.time.LocalDate;

/**
 * 생년월일 Value Object.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DateOfBirth(LocalDate value) {

    public DateOfBirth {
        if (value == null) {
            throw new IllegalArgumentException("생년월일은 필수입니다");
        }
    }

    public static DateOfBirth of(LocalDate value) {
        return new DateOfBirth(value);
    }
}
