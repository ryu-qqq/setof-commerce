package com.ryuqq.setof.domain.member.vo;

/**
 * 수령인 이름 Value Object.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ReceiverName(String value) {

    private static final int MAX_LENGTH = 50;

    public ReceiverName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("수령인 이름은 필수입니다");
        }
        value = value.trim();
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("수령인 이름은 %d자를 초과할 수 없습니다: %d자", MAX_LENGTH, value.length()));
        }
    }

    public static ReceiverName of(String value) {
        return new ReceiverName(value);
    }
}
