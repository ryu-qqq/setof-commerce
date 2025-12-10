package com.ryuqq.setof.domain.member.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.member.exception.RequiredConsentMissingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Consent Value Object")
class ConsentTest {

    @Nested
    @DisplayName("생성 테스트")
    class Creation {

        @Test
        @DisplayName("필수 동의 충족 시 Consent 생성 성공")
        void shouldCreateConsentWithRequiredConsents() {
            Consent consent = Consent.of(true, true, false);

            assertNotNull(consent);
            assertTrue(consent.privacyConsent());
            assertTrue(consent.serviceConsent());
            assertFalse(consent.marketingConsent());
        }

        @Test
        @DisplayName("모든 동의 포함하여 생성 성공")
        void shouldCreateConsentWithAllConsents() {
            Consent consent = Consent.of(true, true, true);

            assertTrue(consent.privacyConsent());
            assertTrue(consent.serviceConsent());
            assertTrue(consent.marketingConsent());
        }

        @Test
        @DisplayName("필수 동의만으로 생성 (ofRequired)")
        void shouldCreateConsentWithRequiredOnly() {
            Consent consent = Consent.ofRequired(true, true);

            assertTrue(consent.privacyConsent());
            assertTrue(consent.serviceConsent());
            assertFalse(consent.marketingConsent());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailure {

        @Test
        @DisplayName("개인정보 동의 누락 시 예외 발생")
        void shouldThrowExceptionWhenPrivacyConsentIsFalse() {
            RequiredConsentMissingException exception =
                    assertThrows(
                            RequiredConsentMissingException.class,
                            () -> Consent.of(false, true, false));

            assertTrue(exception.getMessage().contains("개인정보"));
        }

        @Test
        @DisplayName("서비스 동의 누락 시 예외 발생")
        void shouldThrowExceptionWhenServiceConsentIsFalse() {
            RequiredConsentMissingException exception =
                    assertThrows(
                            RequiredConsentMissingException.class,
                            () -> Consent.of(true, false, false));

            assertTrue(exception.getMessage().contains("서비스"));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class Equality {

        @Test
        @DisplayName("같은 동의 상태를 가진 Consent는 동등")
        void shouldBeEqualWhenSameConsents() {
            Consent consent1 = Consent.of(true, true, true);
            Consent consent2 = Consent.of(true, true, true);

            assertEquals(consent1, consent2);
            assertEquals(consent1.hashCode(), consent2.hashCode());
        }
    }
}
