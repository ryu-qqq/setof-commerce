package com.ryuqq.setof.domain.refundaccount.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * VerificationInfo Value Object 테스트
 *
 * <p>계좌 검증 정보를 테스트합니다.
 */
@DisplayName("VerificationInfo Value Object")
class VerificationInfoTest {

    private static final Clock FIXED_CLOCK =
            Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.of("Asia/Seoul"));

    @Nested
    @DisplayName("팩토리 메서드 테스트")
    class FactoryMethodTest {

        @Test
        @DisplayName("unverified()로 미검증 상태를 생성할 수 있다")
        void shouldCreateUnverifiedState() {
            // When
            VerificationInfo verificationInfo = VerificationInfo.unverified();

            // Then
            assertNotNull(verificationInfo);
            assertFalse(verificationInfo.verified());
            assertNull(verificationInfo.verifiedAt());
            assertTrue(verificationInfo.isUnverified());
            assertFalse(verificationInfo.isVerified());
        }

        @Test
        @DisplayName("verified()로 검증 완료 상태를 생성할 수 있다")
        void shouldCreateVerifiedState() {
            // When
            VerificationInfo verificationInfo = VerificationInfo.verified(FIXED_CLOCK);

            // Then
            assertNotNull(verificationInfo);
            assertTrue(verificationInfo.verified());
            assertNotNull(verificationInfo.verifiedAt());
            assertEquals(FIXED_CLOCK.instant(), verificationInfo.verifiedAt());
            assertTrue(verificationInfo.isVerified());
            assertFalse(verificationInfo.isUnverified());
        }

        @Test
        @DisplayName("verifiedAt()로 특정 시각으로 검증 완료 상태를 생성할 수 있다")
        void shouldCreateVerifiedStateWithSpecificTime() {
            // Given
            Instant specificTime = Instant.parse("2024-06-15T12:30:00Z");

            // When
            VerificationInfo verificationInfo = VerificationInfo.verifiedAt(specificTime);

            // Then
            assertNotNull(verificationInfo);
            assertTrue(verificationInfo.verified());
            assertEquals(specificTime, verificationInfo.verifiedAt());
        }

        @Test
        @DisplayName("reconstitute()로 Persistence에서 복원할 수 있다")
        void shouldReconstituteFromPersistence() {
            // Given
            boolean verified = true;
            Instant verifiedAt = Instant.parse("2024-06-15T12:30:00Z");

            // When
            VerificationInfo verificationInfo = VerificationInfo.reconstitute(verified, verifiedAt);

            // Then
            assertNotNull(verificationInfo);
            assertEquals(verified, verificationInfo.verified());
            assertEquals(verifiedAt, verificationInfo.verifiedAt());
        }

        @Test
        @DisplayName("reconstitute()로 미검증 상태를 복원할 수 있다")
        void shouldReconstituteUnverifiedFromPersistence() {
            // When
            VerificationInfo verificationInfo = VerificationInfo.reconstitute(false, null);

            // Then
            assertNotNull(verificationInfo);
            assertFalse(verificationInfo.verified());
            assertNull(verificationInfo.verifiedAt());
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드 테스트")
    class StatusCheckTest {

        @Test
        @DisplayName("isVerified()는 검증 완료 상태이면 true를 반환한다")
        void shouldReturnTrueWhenVerified() {
            // Given
            VerificationInfo verificationInfo = VerificationInfo.verified(FIXED_CLOCK);

            // Then
            assertTrue(verificationInfo.isVerified());
            assertFalse(verificationInfo.isUnverified());
        }

        @Test
        @DisplayName("isUnverified()는 미검증 상태이면 true를 반환한다")
        void shouldReturnTrueWhenUnverified() {
            // Given
            VerificationInfo verificationInfo = VerificationInfo.unverified();

            // Then
            assertTrue(verificationInfo.isUnverified());
            assertFalse(verificationInfo.isVerified());
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 VerificationInfo는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            Instant verifiedAt = Instant.parse("2025-01-01T00:00:00Z");
            VerificationInfo info1 = VerificationInfo.verifiedAt(verifiedAt);
            VerificationInfo info2 = VerificationInfo.verifiedAt(verifiedAt);

            // When & Then
            assertEquals(info1, info2);
            assertEquals(info1.hashCode(), info2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 VerificationInfo는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            VerificationInfo info1 = VerificationInfo.verified(FIXED_CLOCK);
            VerificationInfo info2 = VerificationInfo.unverified();

            // When & Then
            assertNotEquals(info1, info2);
        }

        @Test
        @DisplayName("두 미검증 상태는 동등하다")
        void shouldBeEqualWhenBothUnverified() {
            // Given
            VerificationInfo info1 = VerificationInfo.unverified();
            VerificationInfo info2 = VerificationInfo.unverified();

            // When & Then
            assertEquals(info1, info2);
            assertEquals(info1.hashCode(), info2.hashCode());
        }
    }
}
