package com.ryuqq.setof.domain.core.member.vo;

/**
 * Password Value Object TestFixture
 *
 * <p>Object Mother 패턴을 적용한 테스트 픽스처</p>
 *
 * <p>사용 예시:</p>
 * <pre>{@code
 * Password password = PasswordFixture.create();
 * Password customPassword = PasswordFixture.createWithHash("$2a$10$...");
 * }</pre>
 */
public final class PasswordFixture {

    /**
     * BCrypt 해시 예시값
     * - 원본: "password123!"
     * - $2a$10$ 형식 (cost factor 10)
     */
    private static final String DEFAULT_BCRYPT_HASH =
        "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

    private PasswordFixture() {
        // Utility class - 인스턴스화 방지
    }

    /**
     * 기본 Password 생성
     *
     * @return 기본 BCrypt 해시값을 가진 Password
     */
    public static Password create() {
        return Password.of(DEFAULT_BCRYPT_HASH);
    }

    /**
     * 커스텀 해시값으로 Password 생성
     *
     * @param hash BCrypt 해시값
     * @return 지정된 해시값을 가진 Password
     */
    public static Password createWithHash(String hash) {
        return Password.of(hash);
    }
}
