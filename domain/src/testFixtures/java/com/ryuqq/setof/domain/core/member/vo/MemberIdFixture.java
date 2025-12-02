package com.ryuqq.setof.domain.core.member.vo;

/**
 * MemberId TestFixture - Object Mother Pattern
 *
 * <p>테스트에서 MemberId 인스턴스 생성을 위한 팩토리 클래스</p>
 *
 * <p>사용 예시:</p>
 * <pre>{@code
 * MemberId memberId = MemberIdFixture.create();
 * MemberId customId = MemberIdFixture.create(100L);
 * }</pre>
 */
public final class MemberIdFixture {

    private static final Long DEFAULT_ID = 1L;

    private MemberIdFixture() {
        // Utility class - 인스턴스 생성 방지
    }

    /**
     * 기본 MemberId 생성 (ID: 1)
     *
     * @return MemberId 인스턴스
     */
    public static MemberId create() {
        return MemberId.of(DEFAULT_ID);
    }

    /**
     * 지정된 ID로 MemberId 생성
     *
     * @param id 회원 ID 값
     * @return MemberId 인스턴스
     */
    public static MemberId create(Long id) {
        return MemberId.of(id);
    }

    /**
     * 순차적 MemberId 생성 (테스트용)
     *
     * @param sequence 시퀀스 번호 (1부터 시작)
     * @return MemberId 인스턴스
     */
    public static MemberId createWithSequence(int sequence) {
        return MemberId.of((long) sequence);
    }
}
