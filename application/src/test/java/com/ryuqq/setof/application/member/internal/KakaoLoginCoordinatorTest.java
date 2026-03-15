package com.ryuqq.setof.application.member.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.auth.dto.response.KakaoLoginResult;
import com.ryuqq.setof.application.auth.dto.response.LoginResult;
import com.ryuqq.setof.application.auth.manager.TokenCommandFacade;
import com.ryuqq.setof.application.member.MemberCommandFixtures;
import com.ryuqq.setof.application.member.MemberQueryFixtures;
import com.ryuqq.setof.application.member.dto.command.KakaoLoginCommand;
import com.ryuqq.setof.application.member.dto.command.MemberRegistrationBundle;
import com.ryuqq.setof.application.member.dto.query.MemberWithCredentials;
import com.ryuqq.setof.application.member.factory.KakaoMemberFactory;
import com.ryuqq.setof.application.member.manager.MemberAuthCommandManager;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.aggregate.MemberAuth;
import java.util.Optional;
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
@DisplayName("KakaoLoginCoordinator 단위 테스트")
class KakaoLoginCoordinatorTest {

    @InjectMocks private KakaoLoginCoordinator sut;

    @Mock private KakaoMemberFactory kakaoMemberFactory;
    @Mock private MemberRegistrationFacade memberRegistrationFacade;
    @Mock private MemberAuthCommandManager memberAuthCommandManager;
    @Mock private TokenCommandFacade tokenCommandFacade;

    @Nested
    @DisplayName("coordinate() - 카카오 로그인 분기 처리")
    class CoordinateTest {

        @Test
        @DisplayName("신규 회원이면 가입 후 KakaoLoginResult.ofNewMember를 반환한다")
        void coordinate_NewMember_ReturnsNewMemberResult() {
            // given
            KakaoLoginCommand command = MemberCommandFixtures.kakaoLoginCommand();
            Optional<MemberWithCredentials> emptyOpt = Optional.empty();

            MemberRegistrationBundle bundle = MemberCommandFixtures.registrationBundle();
            Long userId = 2001L;
            LoginResult loginResult =
                    LoginResult.success(
                            String.valueOf(userId), "access", "refresh", 3600L, "Bearer");

            given(kakaoMemberFactory.createRegistrationBundle(command)).willReturn(bundle);
            given(memberRegistrationFacade.register(any(MemberRegistrationBundle.class)))
                    .willReturn(userId);
            given(tokenCommandFacade.issueLoginResult(userId)).willReturn(loginResult);

            // when
            KakaoLoginResult result = sut.coordinate(emptyOpt, command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.joined()).isFalse();
            then(kakaoMemberFactory).should().createRegistrationBundle(command);
            then(memberRegistrationFacade).should().register(any(MemberRegistrationBundle.class));
            then(tokenCommandFacade).should().issueLoginResult(userId);
        }

        @Test
        @DisplayName("기존 회원이고 통합 없이 로그인이면 KakaoLoginResult.ofExistingMember를 반환한다")
        void coordinate_ExistingMemberWithoutIntegration_ReturnsExistingMemberResult() {
            // given
            KakaoLoginCommand command = MemberCommandFixtures.kakaoLoginCommand();
            MemberWithCredentials credentials = MemberQueryFixtures.kakaoMemberWithCredentials();
            Optional<MemberWithCredentials> existingOpt = Optional.of(credentials);

            Member member = credentials.member();
            Long userId = member.idValue();
            LoginResult loginResult =
                    LoginResult.success(
                            String.valueOf(userId), "access", "refresh", 3600L, "Bearer");

            given(tokenCommandFacade.issueLoginResult(userId)).willReturn(loginResult);

            // when
            KakaoLoginResult result = sut.coordinate(existingOpt, command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.joined()).isTrue();
            then(tokenCommandFacade).should().issueLoginResult(userId);
            then(kakaoMemberFactory).shouldHaveNoInteractions();
            then(memberRegistrationFacade).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("기존 회원이고 통합 요청이면 소셜 통합 후 KakaoLoginResult.ofIntegrated를 반환한다")
        void coordinate_ExistingMemberWithIntegration_ReturnsIntegratedResult() {
            // given
            KakaoLoginCommand command = MemberCommandFixtures.kakaoLoginCommandWithIntegration();
            MemberWithCredentials credentials = MemberQueryFixtures.memberWithCredentials();
            Optional<MemberWithCredentials> existingOpt = Optional.of(credentials);

            Member member = credentials.member();
            Long userId = member.idValue();
            MemberAuth socialAuth = org.mockito.Mockito.mock(MemberAuth.class);
            LoginResult loginResult =
                    LoginResult.success(
                            String.valueOf(userId), "access", "refresh", 3600L, "Bearer");

            given(kakaoMemberFactory.createSocialAuth(userId, command)).willReturn(socialAuth);
            given(tokenCommandFacade.issueLoginResult(userId)).willReturn(loginResult);

            // when
            KakaoLoginResult result = sut.coordinate(existingOpt, command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.joined()).isFalse();
            then(kakaoMemberFactory).should().createSocialAuth(userId, command);
            then(memberAuthCommandManager).should().persist(socialAuth);
            then(tokenCommandFacade).should().issueLoginResult(userId);
        }
    }
}
