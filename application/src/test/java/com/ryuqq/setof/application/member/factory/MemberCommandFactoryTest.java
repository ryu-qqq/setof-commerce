package com.ryuqq.setof.application.member.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.member.MemberCommandFixtures;
import com.ryuqq.setof.application.member.dto.command.JoinCommand;
import com.ryuqq.setof.application.member.dto.command.MemberRegistrationBundle;
import com.ryuqq.setof.application.member.manager.PasswordManager;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.aggregate.MemberAuth;
import com.ryuqq.setof.domain.member.aggregate.MemberConsent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("MemberCommandFactory 단위 테스트")
class MemberCommandFactoryTest {

    @InjectMocks private MemberCommandFactory sut;

    @Mock private PasswordManager passwordManager;

    @Nested
    @DisplayName("createRegistrationBundle() - 회원 등록 번들 생성")
    class CreateRegistrationBundleTest {

        @Test
        @DisplayName("JoinCommand로 MemberRegistrationBundle을 생성한다")
        void createRegistrationBundle_ValidCommand_ReturnsBundle() {
            // given
            JoinCommand command = MemberCommandFixtures.joinCommand();
            String encodedPassword = MemberCommandFixtures.DEFAULT_ENCODED_PASSWORD;

            given(passwordManager.encode(command.password())).willReturn(encodedPassword);

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

            then(passwordManager).should().encode(command.password());
        }

        @Test
        @DisplayName("비밀번호는 PasswordManager를 통해 인코딩된다")
        void createRegistrationBundle_PasswordEncoded_ViaPasswordManager() {
            // given
            JoinCommand command = MemberCommandFixtures.joinCommand();
            String encodedPw = "$2a$10$encoded";

            given(passwordManager.encode(command.password())).willReturn(encodedPw);

            // when
            MemberRegistrationBundle bundle = sut.createRegistrationBundle(command);

            // then
            assertThat(bundle).isNotNull();
            then(passwordManager).should().encode(command.password());
        }

        @Test
        @DisplayName("생성된 Member의 id는 null이다 (auto-increment 대기)")
        void createRegistrationBundle_MemberIdIsNull() {
            // given
            JoinCommand command = MemberCommandFixtures.joinCommand();
            given(passwordManager.encode(command.password()))
                    .willReturn(MemberCommandFixtures.DEFAULT_ENCODED_PASSWORD);

            // when
            MemberRegistrationBundle bundle = sut.createRegistrationBundle(command);

            // then
            assertThat(bundle.member().idValue()).isNull();
        }
    }
}
