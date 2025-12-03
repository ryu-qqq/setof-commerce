package com.ryuqq.setof.domain.core.common.util;

import java.security.SecureRandom;
import java.time.Clock;
import java.util.UUID;

/**
 * UUID v7 생성기
 *
 * <p>UUID v7 (RFC 9562) 스펙을 따르는 UUID 생성기입니다.
 *
 * <p>UUID v7 특징:
 *
 * <ul>
 *   <li>시간 기반 정렬 가능 (Time-ordered)
 *   <li>밀리초 단위 타임스탬프 (48-bit Unix timestamp)
 *   <li>랜덤 컴포넌트로 보안성 보장
 *   <li>데이터베이스 인덱스 친화적 (Sequential)
 * </ul>
 *
 * <p>포맷: xxxxxxxx-xxxx-7xxx-yxxx-xxxxxxxxxxxx
 *
 * <ul>
 *   <li>48-bit: Unix timestamp (ms)
 *   <li>4-bit: Version (7)
 *   <li>12-bit: Random
 *   <li>2-bit: Variant (10)
 *   <li>62-bit: Random
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public final class UuidV7Generator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private UuidV7Generator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * UUID v7 생성 (시스템 시계 사용)
     *
     * @return 새로운 UUID v7
     */
    public static UUID generate() {
        return generate(Clock.systemUTC());
    }

    /**
     * UUID v7 생성 (지정된 시계 사용)
     *
     * <p>테스트 시 고정된 시간을 사용할 수 있습니다.
     *
     * @param clock 시간 제공자
     * @return 새로운 UUID v7
     */
    public static UUID generate(Clock clock) {
        long timestamp = clock.millis();
        return generate(timestamp);
    }

    /**
     * UUID v7 생성 (지정된 타임스탬프 사용)
     *
     * @param timestampMillis Unix timestamp (밀리초)
     * @return 새로운 UUID v7
     */
    public static UUID generate(long timestampMillis) {
        byte[] randomBytes = new byte[10];
        RANDOM.nextBytes(randomBytes);

        long msb = buildMostSignificantBits(timestampMillis, randomBytes);
        long lsb = buildLeastSignificantBits(randomBytes);

        return new UUID(msb, lsb);
    }

    /**
     * 문자열로 UUID v7 생성
     *
     * @return UUID v7 문자열 (하이픈 포함)
     */
    public static String generateAsString() {
        return generate().toString();
    }

    /**
     * 문자열로 UUID v7 생성 (하이픈 없음)
     *
     * @return UUID v7 문자열 (하이픈 없음, 32자)
     */
    public static String generateAsCompactString() {
        return generate().toString().replace("-", "");
    }

    private static long buildMostSignificantBits(long timestamp, byte[] randomBytes) {
        long msb = 0;

        // 48-bit timestamp (상위 48비트)
        msb |= (timestamp & 0xFFFF_FFFF_FFFFL) << 16;

        // 4-bit version (7)
        msb |= 0x7000L;

        // 12-bit random (하위 12비트)
        msb |= ((randomBytes[0] & 0xFF) << 4) | ((randomBytes[1] & 0xF0) >> 4);

        return msb;
    }

    private static long buildLeastSignificantBits(byte[] randomBytes) {
        long lsb = 0;

        // 2-bit variant (10)
        lsb |= 0x8000_0000_0000_0000L;

        // 62-bit random
        lsb |= ((long) (randomBytes[1] & 0x0F) << 58);
        lsb |= ((long) (randomBytes[2] & 0xFF) << 50);
        lsb |= ((long) (randomBytes[3] & 0xFF) << 42);
        lsb |= ((long) (randomBytes[4] & 0xFF) << 34);
        lsb |= ((long) (randomBytes[5] & 0xFF) << 26);
        lsb |= ((long) (randomBytes[6] & 0xFF) << 18);
        lsb |= ((long) (randomBytes[7] & 0xFF) << 10);
        lsb |= ((long) (randomBytes[8] & 0xFF) << 2);
        lsb |= ((long) (randomBytes[9] & 0xC0) >> 6);

        return lsb;
    }
}
