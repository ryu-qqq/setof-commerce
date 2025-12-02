package com.ryuqq.setof.domain.core.member.vo;

import com.ryuqq.setof.domain.core.member.exception.InvalidSocialIdException;

/**
 * 소셜 로그인 ID Value Object
 *
 * <p>카카오 등 소셜 로그인 제공자로부터 받은 고유 식별자</p>
 *
 * <p>검증 규칙:</p>
 * <ul>
 *     <li>NotBlank</li>
 * </ul>
 *
 * @param value 소셜 ID 값
 */
public record SocialId(String value) {

    public SocialId {
        validate(value);
    }

    public static SocialId of(String value) {
        return new SocialId(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidSocialIdException();
        }
    }
}
