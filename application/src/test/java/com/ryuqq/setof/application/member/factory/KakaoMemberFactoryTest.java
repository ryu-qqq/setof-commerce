package com.ryuqq.setof.application.member.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.member.MemberCommandFixtures;
import com.ryuqq.setof.application.member.dto.command.KakaoLoginCommand;
import com.ryuqq.setof.application.member.dto.command.MemberRegistrationInfo;
import com.ryuqq.setof.application.member.dto.command.SocialIntegrationContext;
import com.ryuqq.setof.domain.member.aggregate.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("KakaoMemberFactory 단위 테스트")
class KakaoMemberFactoryTest {

    private KakaoMemberFactory sut;

    @BeforeEach
    void setUp() {
        sut = new KakaoMemberFactory();
    }

    @Nested
    @DisplayName("createMember() - 카카오 신규 회원 Member 생성")
    class CreateMemberTest {

        @Test
        @DisplayName("KakaoLoginCommand로 신규 Member 도메인 객체를 생성한다")
        void createMember_ValidCommand_ReturnsMember() {
            // given
            KakaoLoginCommand command = MemberCommandFixtures.kakaoLoginCommand();

            // when
            Member member = sut.createMember(command);

            // then
            assertThat(member).isNotNull();
            assertThat(member.memberNameValue()).isEqualTo(command.name());
            assertThat(member.phoneNumberValue()).isEqualTo(command.phoneNumber());
        }

        @Test
        @DisplayName("생성된 Member의 legacyMemberId는 null이다")
        void createMember_ValidCommand_LegacyMemberIdIsNull() {
            // given
            KakaoLoginCommand command = MemberCommandFixtures.kakaoLoginCommand();

            // when
            Member member = sut.createMember(command);

            // then
            assertThat(member.legacyMemberIdValue()).isNull();
        }
    }

    @Nested
    @DisplayName("createRegistrationInfo() - 카카오 신규 회원 가입 부가 정보 생성")
    class CreateRegistrationInfoTest {

        @Test
        @DisplayName("KakaoLoginCommand로 MemberRegistrationInfo를 생성한다")
        void createRegistrationInfo_ValidCommand_ReturnsMemberRegistrationInfo() {
            // given
            KakaoLoginCommand command = MemberCommandFixtures.kakaoLoginCommand();

            // when
            MemberRegistrationInfo info = sut.createRegistrationInfo(command);

            // then
            assertThat(info).isNotNull();
            assertThat(info.encodedPassword()).isEmpty();
            assertThat(info.socialLoginType()).isEqualTo("KAKAO");
            assertThat(info.socialPkId()).isEqualTo(command.socialPkId());
            assertThat(info.privacyConsent()).isTrue();
            assertThat(info.serviceTermsConsent()).isTrue();
            assertThat(info.adConsent()).isTrue();
        }

        @Test
        @DisplayName("카카오 로그인은 비밀번호가 빈 문자열이다")
        void createRegistrationInfo_KakaoLogin_HasEmptyPassword() {
            // given
            KakaoLoginCommand command = MemberCommandFixtures.kakaoLoginCommand();

            // when
            MemberRegistrationInfo info = sut.createRegistrationInfo(command);

            // then
            assertThat(info.encodedPassword()).isEmpty();
        }
    }

    @Nested
    @DisplayName("createIntegrationContext() - 소셜 통합 Context 생성")
    class CreateIntegrationContextTest {

        @Test
        @DisplayName("userId와 KakaoLoginCommand로 SocialIntegrationContext를 생성한다")
        void createIntegrationContext_ValidInputs_ReturnsSocialIntegrationContext() {
            // given
            long userId = MemberCommandFixtures.DEFAULT_USER_ID;
            KakaoLoginCommand command = MemberCommandFixtures.kakaoLoginCommand();

            // when
            SocialIntegrationContext context = sut.createIntegrationContext(userId, command);

            // then
            assertThat(context).isNotNull();
            assertThat(context.userId()).isEqualTo(userId);
            assertThat(context.socialLoginType()).isEqualTo("KAKAO");
            assertThat(context.socialPkId()).isEqualTo(command.socialPkId());
            assertThat(context.gender()).isEqualTo(command.gender());
            assertThat(context.dateOfBirth()).isEqualTo(command.dateOfBirth());
        }

        @Test
        @DisplayName("소셜 로그인 타입은 항상 KAKAO이다")
        void createIntegrationContext_AlwaysKakaoType() {
            // given
            long userId = 9999L;
            KakaoLoginCommand command = MemberCommandFixtures.kakaoLoginCommand();

            // when
            SocialIntegrationContext context = sut.createIntegrationContext(userId, command);

            // then
            assertThat(context.socialLoginType()).isEqualTo("KAKAO");
        }
    }
}
