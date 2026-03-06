package com.ryuqq.setof.domain.member.id;

/**
 * 회원 찜 ID Value Object.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record MemberFavoriteId(Long value) {

    public static MemberFavoriteId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("MemberFavoriteId 값은 null일 수 없습니다");
        }
        return new MemberFavoriteId(value);
    }

    public static MemberFavoriteId forNew() {
        return new MemberFavoriteId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
