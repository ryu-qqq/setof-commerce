package com.ryuqq.setof.storage.legacy.common;

/**
 * Yn - 레거시 Y/N 구분 Enum.
 *
 * <p>레거시 DB의 Y/N 컬럼 매핑에 공통으로 사용됩니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum Yn {
    Y,
    N;

    public boolean toBoolean() {
        return this == Y;
    }

    public static Yn fromBoolean(boolean value) {
        return value ? Y : N;
    }
}
