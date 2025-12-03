package com.ryuqq.setof.application.member.component;

import static org.junit.jupiter.api.Assertions.*;

import com.ryuqq.setof.domain.core.member.MemberFixture;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import com.ryuqq.setof.domain.core.member.exception.AlreadyKakaoMemberException;
import com.ryuqq.setof.domain.core.member.exception.InactiveMemberException;
import com.ryuqq.setof.domain.core.member.vo.WithdrawalReason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("KakaoOAuthPolicyValidator")
class KakaoOAuthPolicyValidatorTest {

    private KakaoOAuthPolicyValidator validator;

    @BeforeEach
    void setUp() {
        validator = new KakaoOAuthPolicyValidator();
    }

    @Nested
    @DisplayName("validateCanIntegrateKakao")
    class ValidateCanIntegrateKakaoTest {

        @Test
        @DisplayName("LOCAL 활성 회원은 카카오 통합 가능")
        void shouldPassWhenLocalActiveMember() {
            // Given
            Member localMember = MemberFixture.createLocalMember();

            // When & Then
            assertDoesNotThrow(() -> validator.validateCanIntegrateKakao(localMember));
        }

        @Test
        @DisplayName("이미 카카오 회원인 경우 AlreadyKakaoMemberException 발생")
        void shouldThrowExceptionWhenAlreadyKakaoMember() {
            // Given
            Member kakaoMember = MemberFixture.createKakaoMember();

            // When & Then
            assertThrows(AlreadyKakaoMemberException.class,
                    () -> validator.validateCanIntegrateKakao(kakaoMember));
        }

        @Test
        @DisplayName("휴면 회원인 경우 InactiveMemberException 발생")
        void shouldThrowExceptionWhenDormantMember() {
            // Given
            Member dormantMember = MemberFixture.createInactiveMember("01936ddc-8d37-7c6e-8ad6-18c76adc9dfa");

            // When & Then
            assertThrows(InactiveMemberException.class,
                    () -> validator.validateCanIntegrateKakao(dormantMember));
        }

        @Test
        @DisplayName("탈퇴한 회원인 경우 InactiveMemberException 발생")
        void shouldThrowExceptionWhenWithdrawnMember() {
            // Given
            Member withdrawnMember = MemberFixture.createWithdrawnMember();

            // When & Then
            assertThrows(InactiveMemberException.class,
                    () -> validator.validateCanIntegrateKakao(withdrawnMember));
        }
    }

    @Nested
    @DisplayName("validateCanKakaoLogin")
    class ValidateCanKakaoLoginTest {

        @Test
        @DisplayName("활성 카카오 회원은 로그인 가능")
        void shouldPassWhenActiveKakaoMember() {
            // Given
            Member kakaoMember = MemberFixture.createKakaoMember();

            // When & Then
            assertDoesNotThrow(() -> validator.validateCanKakaoLogin(kakaoMember));
        }

        @Test
        @DisplayName("휴면 회원인 경우 InactiveMemberException 발생")
        void shouldThrowExceptionWhenDormantMember() {
            // Given
            Member dormantMember = MemberFixture.createInactiveMember("01936ddc-8d37-7c6e-8ad6-18c76adc9dfa");

            // When & Then
            assertThrows(InactiveMemberException.class,
                    () -> validator.validateCanKakaoLogin(dormantMember));
        }

        @Test
        @DisplayName("탈퇴한 회원인 경우 InactiveMemberException 발생")
        void shouldThrowExceptionWhenWithdrawnMember() {
            // Given
            Member withdrawnMember = MemberFixture.createWithdrawnMember();

            // When & Then
            assertThrows(InactiveMemberException.class,
                    () -> validator.validateCanKakaoLogin(withdrawnMember));
        }
    }
}
