package com.ryuqq.setof.domain.core.member.vo;

import java.util.UUID;

/**
 * MemberId TestFixture - Object Mother Pattern
 *
 * <p>테스트에서 MemberId 인스턴스 생성을 위한 팩토리 클래스
 *
 * <p>사용 예시:
 *
 * <pre>{@code
 * MemberId memberId = MemberIdFixture.create();
 * MemberId customId = MemberIdFixture.createWithSequence(1);
 * }</pre>
 */
public final class MemberIdFixture {

    private static final String DEFAULT_ID = "01234567-89ab-7cde-8000-000000000001";

    private MemberIdFixture() {
        // Utility class - 인스턴스 생성 방지
    }

    /**
     * 기본 MemberId 생성 (UUID v7 형식)
     *
     * @return MemberId 인스턴스
     */
    public static MemberId create() {
        return MemberId.of(DEFAULT_ID);
    }

    /**
     * 새 UUID v7 MemberId 생성
     *
     * @return MemberId 인스턴스 (새로 생성된 UUID v7)
     */
    public static MemberId createNew() {
        return MemberId.generate();
    }

    /**
     * 지정된 UUID 문자열로 MemberId 생성
     *
     * @param uuidString UUID 문자열
     * @return MemberId 인스턴스
     */
    public static MemberId create(String uuidString) {
        return MemberId.of(uuidString);
    }

    /**
     * 지정된 UUID로 MemberId 생성
     *
     * @param uuid UUID 값
     * @return MemberId 인스턴스
     */
    public static MemberId create(UUID uuid) {
        return MemberId.of(uuid);
    }

    /**
     * 순차적 MemberId 생성 (테스트용)
     *
     * <p>예측 가능한 UUID를 생성하여 테스트 간 비교 용이
     *
     * @param sequence 시퀀스 번호 (1부터 시작)
     * @return MemberId 인스턴스
     */
    public static MemberId createWithSequence(int sequence) {
        String formattedSequence = String.format("%012d", sequence);
        String uuid = String.format("01234567-89ab-7cde-8000-%s", formattedSequence);
        return MemberId.of(uuid);
    }
}
