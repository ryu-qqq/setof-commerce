package com.ryuqq.setof.application.member.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.member.MemberCommandFixtures;
import com.ryuqq.setof.application.member.dto.command.JoinCommand;
import com.ryuqq.setof.application.member.dto.command.MemberRegistrationInfo;
import com.ryuqq.setof.application.member.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.member.dto.command.UpdatePasswordContext;
import com.ryuqq.setof.application.member.manager.PasswordManager;
import com.ryuqq.setof.domain.member.MemberFixtures;
import com.ryuqq.setof.domain.member.aggregate.Member;
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
    @DisplayName("createMember() - Member 도메인 객체 생성")
    class CreateMemberTest {

        @Test
        @DisplayName("JoinCommand로 Member 도메인 객체를 생성한다")
        void createMember_ValidCommand_ReturnsMember() {
            // given
            JoinCommand command = MemberCommandFixtures.joinCommand();

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
            JoinCommand command = MemberCommandFixtures.joinCommand();

            // when
            Member member = sut.createMember(command);

            // then
            assertThat(member.legacyMemberIdValue()).isNull();
        }
    }

    @Nested
    @DisplayName("createRegistrationInfo() - 가입 부가 정보 생성")
    class CreateRegistrationInfoTest {

        @Test
        @DisplayName("JoinCommand로 MemberRegistrationInfo를 생성한다")
        void createRegistrationInfo_ValidCommand_ReturnsMemberRegistrationInfo() {
            // given
            JoinCommand command = MemberCommandFixtures.joinCommand();
            String encodedPassword = MemberCommandFixtures.DEFAULT_ENCODED_PASSWORD;

            given(passwordManager.encode(command.password())).willReturn(encodedPassword);

            // when
            MemberRegistrationInfo info = sut.createRegistrationInfo(command);

            // then
            assertThat(info).isNotNull();
            assertThat(info.encodedPassword()).isEqualTo(encodedPassword);
            assertThat(info.socialLoginType()).isEqualTo(command.socialLoginType());
            assertThat(info.privacyConsent()).isEqualTo(command.privacyConsent());
            assertThat(info.serviceTermsConsent()).isEqualTo(command.serviceTermsConsent());
            assertThat(info.adConsent()).isEqualTo(command.adConsent());
            then(passwordManager).should().encode(command.password());
        }

        @Test
        @DisplayName("비밀번호는 PasswordManager를 통해 인코딩된다")
        void createRegistrationInfo_PasswordEncoded_ViaPasswordManager() {
            // given
            JoinCommand command = MemberCommandFixtures.joinCommand();
            String encodedPw = "$2a$10$encoded";

            given(passwordManager.encode(command.password())).willReturn(encodedPw);

            // when
            MemberRegistrationInfo info = sut.createRegistrationInfo(command);

            // then
            assertThat(info.encodedPassword()).isEqualTo(encodedPw);
            then(passwordManager).should().encode(command.password());
        }
    }

    @Nested
    @DisplayName("createUpdatePasswordContext() - 비밀번호 변경 Context 생성")
    class CreateUpdatePasswordContextTest {

        @Test
        @DisplayName("Member와 평문 비밀번호로 UpdatePasswordContext를 생성한다")
        void createUpdatePasswordContext_ValidInputs_ReturnsUpdatePasswordContext() {
            // given
            Member member = MemberFixtures.activeMigratedMember();
            String rawPassword = "newPassword123!";
            String encodedPassword = "$2a$10$newEncodedPassword";

            given(passwordManager.encode(rawPassword)).willReturn(encodedPassword);

            // when
            UpdatePasswordContext context = sut.createUpdatePasswordContext(member, rawPassword);

            // then
            assertThat(context).isNotNull();
            assertThat(context.userId()).isEqualTo(member.legacyMemberIdValue());
            assertThat(context.encodedPassword()).isEqualTo(encodedPassword);
            then(passwordManager).should().encode(rawPassword);
        }
    }

    @Nested
    @DisplayName("createStatusChangeContext() - 상태 변경 Context 생성 (탈퇴)")
    class CreateStatusChangeContextTest {

        @Test
        @DisplayName("Member와 탈퇴 사유로 StatusChangeContext를 생성한다")
        void createStatusChangeContext_ValidInputs_ReturnsStatusChangeContext() {
            // given
            Member member = MemberFixtures.activeMigratedMember();
            String reason = MemberCommandFixtures.DEFAULT_WITHDRAWAL_REASON;

            // when
            StatusChangeContext context = sut.createStatusChangeContext(member, reason);

            // then
            assertThat(context).isNotNull();
            assertThat(context.userId()).isEqualTo(member.legacyMemberIdValue());
            assertThat(context.withdrawalReason()).isEqualTo(reason);
        }

        @Test
        @DisplayName("StatusChangeContext에는 PasswordManager가 호출되지 않는다")
        void createStatusChangeContext_NoPasswordManagerInvolved() {
            // given
            Member member = MemberFixtures.activeMigratedMember();
            String reason = "단순 변심";

            // when
            sut.createStatusChangeContext(member, reason);

            // then
            then(passwordManager).shouldHaveNoInteractions();
        }
    }
}
