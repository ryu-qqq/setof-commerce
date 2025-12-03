package com.ryuqq.setof.application.member.assembler;

import static org.junit.jupiter.api.Assertions.*;

import com.ryuqq.setof.application.member.dto.command.KakaoOAuthCommand;
import com.ryuqq.setof.application.member.dto.command.RegisterMemberCommand;
import com.ryuqq.setof.application.member.dto.response.KakaoOAuthResponse;
import com.ryuqq.setof.application.member.dto.response.LocalLoginResponse;
import com.ryuqq.setof.application.member.dto.response.MemberDetailResponse;
import com.ryuqq.setof.application.member.dto.response.TokenPairResponse;
import com.ryuqq.setof.domain.core.member.MemberFixture;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import com.ryuqq.setof.domain.core.member.vo.AuthProvider;
import com.ryuqq.setof.domain.core.member.vo.Gender;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MemberAssembler")
class MemberAssemblerTest {

    private MemberAssembler memberAssembler;
    private Clock fixedClock;

    @BeforeEach
    void setUp() {
        memberAssembler = new MemberAssembler();
        fixedClock = Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.of("Asia/Seoul"));
    }

    @Nested
    @DisplayName("toDomain")
    class ToDomainTest {

        @Test
        @DisplayName("RegisterMemberCommand를 Member로 변환 성공")
        void shouldConvertRegisterMemberCommandToMember() {
            // Given
            List<RegisterMemberCommand.ConsentItem> consents = List.of(
                    new RegisterMemberCommand.ConsentItem("PRIVACY", true),
                    new RegisterMemberCommand.ConsentItem("SERVICE", true),
                    new RegisterMemberCommand.ConsentItem("MARKETING", false));

            RegisterMemberCommand command = new RegisterMemberCommand(
                    "01012345678",
                    "test@example.com",
                    "rawPassword123!",
                    "홍길동",
                    LocalDate.of(1990, 1, 1),
                    "M",
                    consents);

            String hashedPassword = "$2a$10$hashedPassword";

            // When
            Member result = memberAssembler.toDomain(command, hashedPassword, fixedClock);

            // Then
            assertNotNull(result);
            assertEquals("01012345678", result.getPhoneNumberValue());
            assertEquals("test@example.com", result.getEmailValue());
            assertEquals(hashedPassword, result.getPasswordValue());
            assertEquals("홍길동", result.getNameValue());
            assertEquals(LocalDate.of(1990, 1, 1), result.getDateOfBirth());
            assertEquals(Gender.M, result.getGender());
            assertEquals(AuthProvider.LOCAL, result.getProvider());
        }

        @Test
        @DisplayName("이메일 없이 변환 성공")
        void shouldConvertWithoutEmail() {
            // Given
            List<RegisterMemberCommand.ConsentItem> consents = List.of(
                    new RegisterMemberCommand.ConsentItem("PRIVACY", true),
                    new RegisterMemberCommand.ConsentItem("SERVICE", true));

            RegisterMemberCommand command = new RegisterMemberCommand(
                    "01012345678",
                    null,
                    "rawPassword123!",
                    "홍길동",
                    LocalDate.of(1990, 1, 1),
                    "M",
                    consents);

            String hashedPassword = "$2a$10$hashedPassword";

            // When
            Member result = memberAssembler.toDomain(command, hashedPassword, fixedClock);

            // Then
            assertNotNull(result);
            assertNull(result.getEmailValue());
        }

        @Test
        @DisplayName("성별 null일 때 N으로 변환")
        void shouldConvertNullGenderToN() {
            // Given
            List<RegisterMemberCommand.ConsentItem> consents = List.of(
                    new RegisterMemberCommand.ConsentItem("PRIVACY", true),
                    new RegisterMemberCommand.ConsentItem("SERVICE", true));

            RegisterMemberCommand command = new RegisterMemberCommand(
                    "01012345678",
                    "test@example.com",
                    "rawPassword123!",
                    "홍길동",
                    LocalDate.of(1990, 1, 1),
                    null,
                    consents);

            String hashedPassword = "$2a$10$hashedPassword";

            // When
            Member result = memberAssembler.toDomain(command, hashedPassword, fixedClock);

            // Then
            assertEquals(Gender.N, result.getGender());
        }

        @Test
        @DisplayName("소문자 성별 대문자로 변환")
        void shouldConvertLowercaseGenderToUppercase() {
            // Given
            List<RegisterMemberCommand.ConsentItem> consents = List.of(
                    new RegisterMemberCommand.ConsentItem("PRIVACY", true),
                    new RegisterMemberCommand.ConsentItem("SERVICE", true));

            RegisterMemberCommand command = new RegisterMemberCommand(
                    "01012345678",
                    "test@example.com",
                    "rawPassword123!",
                    "홍길동",
                    LocalDate.of(1990, 1, 1),
                    "w",
                    consents);

            String hashedPassword = "$2a$10$hashedPassword";

            // When
            Member result = memberAssembler.toDomain(command, hashedPassword, fixedClock);

            // Then
            assertEquals(Gender.W, result.getGender());
        }
    }

    @Nested
    @DisplayName("toKakaoDomain")
    class ToKakaoDomainTest {

        @Test
        @DisplayName("KakaoOAuthCommand를 Member로 변환 성공")
        void shouldConvertKakaoOAuthCommandToMember() {
            // Given
            List<KakaoOAuthCommand.ConsentItem> consents = List.of(
                    new KakaoOAuthCommand.ConsentItem("PRIVACY", true),
                    new KakaoOAuthCommand.ConsentItem("SERVICE", true));

            KakaoOAuthCommand command = new KakaoOAuthCommand(
                    "kakao_12345",
                    "01087654321",
                    "kakao@example.com",
                    "카카오",
                    LocalDate.of(1995, 5, 15),
                    "W",
                    consents);

            // When
            Member result = memberAssembler.toKakaoDomain(command, fixedClock);

            // Then
            assertNotNull(result);
            assertEquals("01087654321", result.getPhoneNumberValue());
            assertEquals("kakao@example.com", result.getEmailValue());
            assertNull(result.getPassword());
            assertEquals("카카오", result.getNameValue());
            assertEquals(AuthProvider.KAKAO, result.getProvider());
            assertEquals("kakao_12345", result.getSocialIdValue());
        }

        @Test
        @DisplayName("동의 항목 없을 때 기본값 적용")
        void shouldApplyDefaultConsentsWhenEmpty() {
            // Given
            KakaoOAuthCommand command = new KakaoOAuthCommand(
                    "kakao_12345",
                    "01087654321",
                    null,
                    "카카오",
                    null,
                    null,
                    null);

            // When
            Member result = memberAssembler.toKakaoDomain(command, fixedClock);

            // Then
            assertNotNull(result);
            assertEquals(AuthProvider.KAKAO, result.getProvider());
        }
    }

    @Nested
    @DisplayName("toLocalLoginResponse")
    class ToLocalLoginResponseTest {

        @Test
        @DisplayName("LocalLoginResponse 생성 성공")
        void shouldCreateLocalLoginResponse() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            TokenPairResponse tokens = new TokenPairResponse(
                    "access_token",
                    "refresh_token",
                    3600L,
                    604800L);

            // When
            LocalLoginResponse result = memberAssembler.toLocalLoginResponse(memberId, tokens);

            // Then
            assertNotNull(result);
            assertEquals(memberId, result.memberId());
            assertEquals(tokens, result.tokens());
        }
    }

    @Nested
    @DisplayName("toMemberDetailResponse")
    class ToMemberDetailResponseTest {

        @Test
        @DisplayName("Member를 MemberDetailResponse로 변환 성공")
        void shouldConvertMemberToDetailResponse() {
            // Given
            Member member = MemberFixture.createLocalMemberWithId("01936ddc-8d37-7c6e-8ad6-18c76adc9dfa");

            // When
            MemberDetailResponse result = memberAssembler.toMemberDetailResponse(member);

            // Then
            assertNotNull(result);
            assertEquals(member.getIdValue(), result.memberId());
            assertEquals(member.getPhoneNumberValue(), result.phoneNumber());
            assertEquals(member.getEmailValue(), result.email());
            assertEquals(member.getNameValue(), result.name());
            assertEquals(member.getDateOfBirth(), result.dateOfBirth());
            assertEquals(member.getGender().name(), result.gender());
            assertEquals(member.getProvider().name(), result.provider());
            assertEquals(member.getStatus().name(), result.status());
        }
    }

    @Nested
    @DisplayName("toNewKakaoMemberResponse")
    class ToNewKakaoMemberResponseTest {

        @Test
        @DisplayName("신규 카카오 회원 응답 생성 성공")
        void shouldCreateNewKakaoMemberResponse() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            TokenPairResponse tokens = new TokenPairResponse(
                    "access_token",
                    "refresh_token",
                    3600L,
                    604800L);

            // When
            KakaoOAuthResponse result = memberAssembler.toNewKakaoMemberResponse(memberId, tokens);

            // Then
            assertNotNull(result);
            assertEquals(memberId, result.memberId());
            assertTrue(result.isNewMember());
        }
    }

    @Nested
    @DisplayName("toExistingKakaoMemberResponse")
    class ToExistingKakaoMemberResponseTest {

        @Test
        @DisplayName("기존 카카오 회원 응답 생성 성공")
        void shouldCreateExistingKakaoMemberResponse() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            TokenPairResponse tokens = new TokenPairResponse(
                    "access_token",
                    "refresh_token",
                    3600L,
                    604800L);

            // When
            KakaoOAuthResponse result = memberAssembler.toExistingKakaoMemberResponse(memberId, tokens);

            // Then
            assertNotNull(result);
            assertEquals(memberId, result.memberId());
            assertFalse(result.isNewMember());
        }
    }
}
