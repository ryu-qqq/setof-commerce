package com.ryuqq.setof.domain.core.member.vo;

/**
 * SocialId TestFixture - Object Mother Pattern
 *
 * <p>테스트에서 SocialId 인스턴스 생성을 위한 팩토리 클래스</p>
 *
 * <p>사용 예시:</p>
 * <pre>{@code
 * SocialId socialId = SocialIdFixture.create();
 * SocialId kakaoId = SocialIdFixture.createKakaoId();
 * SocialId customId = SocialIdFixture.create("custom_social_id_12345");
 * }</pre>
 */
public final class SocialIdFixture {

    private static final String DEFAULT_SOCIAL_ID = "kakao_12345678";
    private static final String KAKAO_PREFIX = "kakao_";

    private SocialIdFixture() {
        // Utility class - 인스턴스 생성 방지
    }

    /**
     * 기본 SocialId 생성 (kakao_12345678)
     *
     * @return SocialId 인스턴스
     */
    public static SocialId create() {
        return SocialId.of(DEFAULT_SOCIAL_ID);
    }

    /**
     * 지정된 값으로 SocialId 생성
     *
     * @param value 소셜 ID 값
     * @return SocialId 인스턴스
     */
    public static SocialId create(String value) {
        return SocialId.of(value);
    }

    /**
     * 카카오 소셜 ID 생성
     *
     * @param kakaoUserId 카카오 사용자 ID
     * @return SocialId 인스턴스
     */
    public static SocialId createKakaoId(long kakaoUserId) {
        return SocialId.of(KAKAO_PREFIX + kakaoUserId);
    }

    /**
     * 기본 카카오 소셜 ID 생성
     *
     * @return SocialId 인스턴스
     */
    public static SocialId createKakaoId() {
        return SocialId.of(DEFAULT_SOCIAL_ID);
    }

    /**
     * 순차적 SocialId 생성 (테스트용)
     *
     * @param sequence 시퀀스 번호
     * @return SocialId 인스턴스
     */
    public static SocialId createWithSequence(int sequence) {
        return SocialId.of(KAKAO_PREFIX + sequence);
    }
}
