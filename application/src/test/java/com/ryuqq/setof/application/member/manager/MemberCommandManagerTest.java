package com.ryuqq.setof.application.member.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.member.MemberCommandFixtures;
import com.ryuqq.setof.application.member.dto.command.MemberRegistrationInfo;
import com.ryuqq.setof.application.member.dto.command.SocialIntegrationContext;
import com.ryuqq.setof.application.member.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.member.dto.command.UpdatePasswordContext;
import com.ryuqq.setof.application.member.port.out.command.MemberCommandPort;
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
@DisplayName("MemberCommandManager 단위 테스트")
class MemberCommandManagerTest {

    @InjectMocks private MemberCommandManager sut;

    @Mock private MemberCommandPort memberCommandPort;

    @Nested
    @DisplayName("persist(Member, MemberRegistrationInfo) - 신규 회원 저장")
    class PersistMemberTest {

        @Test
        @DisplayName("신규 회원을 저장하고 생성된 userId를 반환한다")
        void persist_NewMember_ReturnsUserId() {
            // given
            Member member = MemberFixtures.newMember();
            MemberRegistrationInfo info = MemberCommandFixtures.memberRegistrationInfo();
            Long expectedUserId = MemberCommandFixtures.DEFAULT_USER_ID;

            given(memberCommandPort.persist(member, info)).willReturn(expectedUserId);

            // when
            Long result = sut.persist(member, info);

            // then
            assertThat(result).isEqualTo(expectedUserId);
            then(memberCommandPort).should().persist(member, info);
        }

        @Test
        @DisplayName("카카오 신규 회원도 동일하게 저장된다")
        void persist_KakaoNewMember_ReturnsUserId() {
            // given
            Member member = MemberFixtures.newMember();
            MemberRegistrationInfo info = MemberCommandFixtures.kakaoMemberRegistrationInfo();
            Long expectedUserId = 2001L;

            given(memberCommandPort.persist(member, info)).willReturn(expectedUserId);

            // when
            Long result = sut.persist(member, info);

            // then
            assertThat(result).isEqualTo(expectedUserId);
            then(memberCommandPort).should().persist(member, info);
        }
    }

    @Nested
    @DisplayName("persist(StatusChangeContext) - 회원 탈퇴 상태 변경")
    class PersistStatusChangeTest {

        @Test
        @DisplayName("StatusChangeContext로 탈퇴 처리를 한다")
        void persist_StatusChangeContext_ProcessesWithdrawal() {
            // given
            StatusChangeContext context = MemberCommandFixtures.statusChangeContext();

            willDoNothing()
                    .given(memberCommandPort)
                    .persistWithdrawal(context.userId(), context.withdrawalReason());

            // when
            sut.persist(context);

            // then
            then(memberCommandPort)
                    .should()
                    .persistWithdrawal(context.userId(), context.withdrawalReason());
        }
    }

    @Nested
    @DisplayName("persist(UpdatePasswordContext) - 비밀번호 변경")
    class PersistUpdatePasswordTest {

        @Test
        @DisplayName("UpdatePasswordContext로 비밀번호 변경을 한다")
        void persist_UpdatePasswordContext_ChangesPassword() {
            // given
            UpdatePasswordContext context = MemberCommandFixtures.updatePasswordContext();

            willDoNothing()
                    .given(memberCommandPort)
                    .persistPasswordChange(context.userId(), context.encodedPassword());

            // when
            sut.persist(context);

            // then
            then(memberCommandPort)
                    .should()
                    .persistPasswordChange(context.userId(), context.encodedPassword());
        }
    }

    @Nested
    @DisplayName("persist(SocialIntegrationContext) - 소셜 로그인 통합")
    class PersistSocialIntegrationTest {

        @Test
        @DisplayName("SocialIntegrationContext로 소셜 통합을 처리한다")
        void persist_SocialIntegrationContext_IntegratesSocial() {
            // given
            SocialIntegrationContext context = MemberCommandFixtures.socialIntegrationContext();

            willDoNothing()
                    .given(memberCommandPort)
                    .persistSocialIntegration(
                            context.userId(),
                            context.socialLoginType(),
                            context.socialPkId(),
                            context.gender(),
                            context.dateOfBirth());

            // when
            sut.persist(context);

            // then
            then(memberCommandPort)
                    .should()
                    .persistSocialIntegration(
                            context.userId(),
                            context.socialLoginType(),
                            context.socialPkId(),
                            context.gender(),
                            context.dateOfBirth());
        }
    }
}
