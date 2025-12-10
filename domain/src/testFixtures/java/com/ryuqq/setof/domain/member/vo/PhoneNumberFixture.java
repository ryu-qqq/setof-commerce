package com.ryuqq.setof.domain.member.vo;

/**
 * PhoneNumber TestFixture - Object Mother Pattern
 *
 * <p>테스트에서 PhoneNumber 인스턴스 생성을 위한 팩토리 클래스
 *
 * <p>사용 예시:
 *
 * <pre>{@code
 * PhoneNumber phoneNumber = PhoneNumberFixture.create();
 * PhoneNumber customPhone = PhoneNumberFixture.create("01087654321");
 * }</pre>
 */
public final class PhoneNumberFixture {

    private static final String DEFAULT_PHONE_NUMBER = "01012345678";

    private PhoneNumberFixture() {
        // Utility class - 인스턴스 생성 방지
    }

    /**
     * 기본 PhoneNumber 생성 (01012345678)
     *
     * @return PhoneNumber 인스턴스
     */
    public static PhoneNumber create() {
        return PhoneNumber.of(DEFAULT_PHONE_NUMBER);
    }

    /**
     * 지정된 번호로 PhoneNumber 생성
     *
     * @param phoneNumber 핸드폰 번호 (01012345678 형식)
     * @return PhoneNumber 인스턴스
     */
    public static PhoneNumber create(String phoneNumber) {
        return PhoneNumber.of(phoneNumber);
    }

    /**
     * 순차적 PhoneNumber 생성 (테스트용)
     *
     * @param sequence 시퀀스 번호 (0-99999999)
     * @return PhoneNumber 인스턴스
     */
    public static PhoneNumber createWithSequence(int sequence) {
        String formattedSequence = String.format("%08d", Math.abs(sequence) % 100000000);
        return PhoneNumber.of("010" + formattedSequence);
    }
}
