package com.ryuqq.setof.domain.discount.vo;

import java.util.regex.Pattern;

/**
 * 쿠폰 코드 Value Object.
 *
 * <p>4~20자, 영문 대소문자 + 숫자만 허용합니다.
 */
public record CouponCode(String value) {

    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 20;
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    public CouponCode {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("쿠폰 코드는 필수입니다");
        }
        value = value.trim();
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("쿠폰 코드는 %d~%d자여야 합니다", MIN_LENGTH, MAX_LENGTH));
        }
        if (!PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("쿠폰 코드는 영문 대소문자와 숫자만 허용됩니다");
        }
    }

    public static CouponCode of(String value) {
        return new CouponCode(value);
    }

    /** 대소문자 무시 비교 */
    public boolean matchesIgnoreCase(String input) {
        return value.equalsIgnoreCase(input);
    }
}
