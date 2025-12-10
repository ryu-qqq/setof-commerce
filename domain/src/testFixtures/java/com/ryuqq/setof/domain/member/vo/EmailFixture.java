package com.ryuqq.setof.domain.member.vo;

/**
 * Email TestFixture - Object Mother Pattern
 *
 * <p>테스트에서 Email 인스턴스 생성을 위한 팩토리 클래스
 *
 * <p>사용 예시:
 *
 * <pre>{@code
 * Email email = EmailFixture.create();
 * Email customEmail = EmailFixture.create("custom@example.com");
 * Email nullEmail = EmailFixture.createNull();
 * }</pre>
 */
public final class EmailFixture {

    private static final String DEFAULT_EMAIL = "test@example.com";

    private EmailFixture() {
        // Utility class - 인스턴스 생성 방지
    }

    /**
     * 기본 Email 생성 (test@example.com)
     *
     * @return Email 인스턴스
     */
    public static Email create() {
        return Email.of(DEFAULT_EMAIL);
    }

    /**
     * 지정된 이메일로 Email 생성
     *
     * @param email 이메일 주소
     * @return Email 인스턴스
     */
    public static Email create(String email) {
        return Email.of(email);
    }

    /**
     * null Email 생성 (nullable 필드 테스트용)
     *
     * @return 값이 null인 Email 인스턴스
     */
    public static Email createNull() {
        return Email.of(null);
    }

    /**
     * 순차적 Email 생성 (테스트용)
     *
     * @param sequence 시퀀스 번호
     * @return Email 인스턴스
     */
    public static Email createWithSequence(int sequence) {
        return Email.of(String.format("user%d@example.com", sequence));
    }
}
