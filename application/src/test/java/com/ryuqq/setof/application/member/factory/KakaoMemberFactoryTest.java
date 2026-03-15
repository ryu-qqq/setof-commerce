package com.ryuqq.setof.application.member.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.member.MemberCommandFixtures;
import com.ryuqq.setof.application.member.dto.command.KakaoLoginCommand;
import com.ryuqq.setof.application.member.dto.command.MemberRegistrationBundle;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.aggregate.MemberAuth;
import com.ryuqq.setof.domain.member.aggregate.MemberConsent;
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
    @DisplayName("createRegistrationBundle() - 카카오 신규 회원 등록 번들 생성")
    class CreateRegistrationBundleTest {

        @Test
        @DisplayName("KakaoLoginCommand로 MemberRegistrationBundle을 생성한다")
        void createRegistrationBundle_ValidCommand_ReturnsBundle() {
            // given
            KakaoLoginCommand command = MemberCommandFixtures.kakaoLoginCommand();

            // when
            MemberRegistrationBundle bundle = sut.createRegistrationBundle(command);

            // then
            assertThat(bundle).isNotNull();

            Member member = bundle.member();
            assertThat(member).isNotNull();
            assertThat(member.memberNameValue()).isEqualTo(command.name());
            assertThat(member.phoneNumberValue()).isEqualTo(command.phoneNumber());
            assertThat(member.idValue()).isNull();

            MemberAuth auth = bundle.memberAuth();
            assertThat(auth).isNotNull();

            MemberConsent consent = bundle.memberConsent();
            assertThat(consent).isNotNull();
        }

        @Test
        @DisplayName("생성된 Member의 id는 null이다")
        void createRegistrationBundle_MemberIdIsNull() {
            // given
            KakaoLoginCommand command = MemberCommandFixtures.kakaoLoginCommand();

            // when
            MemberRegistrationBundle bundle = sut.createRegistrationBundle(command);

            // then
            assertThat(bundle.member().idValue()).isNull();
        }
    }

    @Nested
    @DisplayName("createSocialAuth() - 소셜 통합용 MemberAuth 생성")
    class CreateSocialAuthTest {

        @Test
        @DisplayName("memberId와 KakaoLoginCommand로 MemberAuth를 생성한다")
        void createSocialAuth_ValidInputs_ReturnsMemberAuth() {
            // given
            long memberId = MemberCommandFixtures.DEFAULT_USER_ID;
            KakaoLoginCommand command = MemberCommandFixtures.kakaoLoginCommand();

            // when
            MemberAuth auth = sut.createSocialAuth(memberId, command);

            // then
            assertThat(auth).isNotNull();
        }

        @Test
        @DisplayName("생성된 MemberAuth에 memberId가 할당된다")
        void createSocialAuth_AssignsMemberId() {
            // given
            long memberId = 9999L;
            KakaoLoginCommand command = MemberCommandFixtures.kakaoLoginCommand();

            // when
            MemberAuth auth = sut.createSocialAuth(memberId, command);

            // then
            assertThat(auth).isNotNull();
        }
    }
}
