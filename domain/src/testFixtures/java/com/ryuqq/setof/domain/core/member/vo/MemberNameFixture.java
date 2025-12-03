package com.ryuqq.setof.domain.core.member.vo;

/**
 * MemberName TestFixture - Object Mother Pattern
 *
 * <p>테스트에서 MemberName 인스턴스 생성을 위한 팩토리 클래스
 *
 * <p>사용 예시:
 *
 * <pre>{@code
 * MemberName name = MemberNameFixture.create();
 * MemberName customName = MemberNameFixture.create("홍길동");
 * MemberName minLengthName = MemberNameFixture.createMinLength();
 * }</pre>
 */
public final class MemberNameFixture {

    private static final String DEFAULT_NAME = "홍길동";
    private static final String MIN_LENGTH_NAME = "홍길";
    private static final String MAX_LENGTH_NAME = "홍길동이가";

    private MemberNameFixture() {
        // Utility class - 인스턴스 생성 방지
    }

    /**
     * 기본 MemberName 생성 (홍길동)
     *
     * @return MemberName 인스턴스
     */
    public static MemberName create() {
        return MemberName.of(DEFAULT_NAME);
    }

    /**
     * 지정된 이름으로 MemberName 생성
     *
     * @param name 회원 이름
     * @return MemberName 인스턴스
     */
    public static MemberName create(String name) {
        return MemberName.of(name);
    }

    /**
     * 최소 길이(2자) MemberName 생성
     *
     * @return MemberName 인스턴스
     */
    public static MemberName createMinLength() {
        return MemberName.of(MIN_LENGTH_NAME);
    }

    /**
     * 최대 길이(5자) MemberName 생성
     *
     * @return MemberName 인스턴스
     */
    public static MemberName createMaxLength() {
        return MemberName.of(MAX_LENGTH_NAME);
    }

    /**
     * 순차적 MemberName 생성 (테스트용)
     *
     * @param sequence 시퀀스 번호
     * @return MemberName 인스턴스
     */
    public static MemberName createWithSequence(int sequence) {
        return MemberName.of("회원" + sequence);
    }
}
