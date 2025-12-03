package com.ryuqq.setof.domain.core.member.vo;

/**
 * Password Value Object TestFixture
 *
 * <p>Object Mother 패턴을 적용한 테스트 픽스처
 *
 * <p>사용 예시:
 *
 * <pre>{@code
 * Password password = PasswordFixture.create();
 * Password customPassword = PasswordFixture.createWithHash("$2a$10$...");
 * }</pre>
 */
public final class PasswordFixture {

    /** BCrypt 해시 예시값 - 원본: "password123!" - $2a$10$ 형식 (cost factor 10) */
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

    /**
     * 정책을 준수하는 유효한 원본 비밀번호 반환
     *
     * <p>정책: 8자 이상, 영문 대문자/소문자/숫자/특수문자 포함
     *
     * @return 유효한 원본 비밀번호 문자열
     */
    public static String validRawPassword() {
        return "Password1!";
    }

    /**
     * 정책을 위반하는 원본 비밀번호 반환 (대문자 누락)
     *
     * @return 정책 위반 비밀번호 문자열
     */
    public static String invalidRawPasswordNoUppercase() {
        return "password1!";
    }

    /**
     * 정책을 위반하는 원본 비밀번호 반환 (소문자 누락)
     *
     * @return 정책 위반 비밀번호 문자열
     */
    public static String invalidRawPasswordNoLowercase() {
        return "PASSWORD1!";
    }

    /**
     * 정책을 위반하는 원본 비밀번호 반환 (숫자 누락)
     *
     * @return 정책 위반 비밀번호 문자열
     */
    public static String invalidRawPasswordNoDigit() {
        return "Password!!";
    }

    /**
     * 정책을 위반하는 원본 비밀번호 반환 (특수문자 누락)
     *
     * @return 정책 위반 비밀번호 문자열
     */
    public static String invalidRawPasswordNoSpecialChar() {
        return "Password12";
    }

    /**
     * 정책을 위반하는 원본 비밀번호 반환 (8자 미만)
     *
     * @return 정책 위반 비밀번호 문자열 (6자)
     */
    public static String invalidRawPasswordTooShort() {
        return "Pass1!";
    }
}
