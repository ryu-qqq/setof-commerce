package com.ryuqq.setof.application.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.auth.dto.response.KakaoLoginResult;
import com.ryuqq.setof.application.auth.dto.response.LoginResult;
import com.ryuqq.setof.application.member.MemberCommandFixtures;
import com.ryuqq.setof.application.member.MemberQueryFixtures;
import com.ryuqq.setof.application.member.dto.command.KakaoLoginCommand;
import com.ryuqq.setof.application.member.dto.query.MemberWithCredentials;
import com.ryuqq.setof.application.member.internal.KakaoLoginCoordinator;
import com.ryuqq.setof.application.member.validator.KakaoLoginValidator;
import com.ryuqq.setof.domain.member.exception.MemberNotActiveException;
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
@DisplayName("KakaoLoginService 단위 테스트")
class KakaoLoginServiceTest {

    @InjectMocks private KakaoLoginService sut;

    @Mock private KakaoLoginValidator kakaoLoginValidator;
    @Mock private KakaoLoginCoordinator kakaoLoginCoordinator;

    @Nested
    @DisplayName("execute() - 카카오 소셜 로그인")
    class ExecuteTest {

        @Test
        @DisplayName("신규 회원이면 Coordinator가 신규 가입 처리를 하고 KakaoLoginResult를 반환한다")
        void execute_NewMember_ReturnsNewMemberKakaoLoginResult() {
            // given
            KakaoLoginCommand command = MemberCommandFixtures.kakaoLoginCommand();
            Optional<MemberWithCredentials> emptyOpt = Optional.empty();
            LoginResult loginResult =
                    LoginResult.success("1001", "access", "refresh", 3600L, "Bearer");
            KakaoLoginResult expected = KakaoLoginResult.ofNewMember(loginResult);

            given(kakaoLoginValidator.validateAndFindExisting(command.phoneNumber()))
                    .willReturn(emptyOpt);
            given(kakaoLoginCoordinator.coordinate(emptyOpt, command)).willReturn(expected);

            // when
            KakaoLoginResult result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.joined()).isFalse();
            then(kakaoLoginValidator).should().validateAndFindExisting(command.phoneNumber());
            then(kakaoLoginCoordinator).should().coordinate(emptyOpt, command);
        }

        @Test
        @DisplayName("기존 회원이면 Coordinator가 기존 로그인 처리를 하고 KakaoLoginResult를 반환한다")
        void execute_ExistingMember_ReturnsExistingMemberKakaoLoginResult() {
            // given
            KakaoLoginCommand command = MemberCommandFixtures.kakaoLoginCommand();
            MemberWithCredentials credentials = MemberQueryFixtures.kakaoMemberWithCredentials();
            Optional<MemberWithCredentials> existingOpt = Optional.of(credentials);
            LoginResult loginResult =
                    LoginResult.success("1001", "access", "refresh", 3600L, "Bearer");
            KakaoLoginResult expected = KakaoLoginResult.ofExistingMember(loginResult);

            given(kakaoLoginValidator.validateAndFindExisting(command.phoneNumber()))
                    .willReturn(existingOpt);
            given(kakaoLoginCoordinator.coordinate(existingOpt, command)).willReturn(expected);

            // when
            KakaoLoginResult result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.joined()).isTrue();
            then(kakaoLoginValidator).should().validateAndFindExisting(command.phoneNumber());
            then(kakaoLoginCoordinator).should().coordinate(existingOpt, command);
        }

        @Test
        @DisplayName("탈퇴/정지 회원이면 MemberNotActiveException이 전파된다")
        void execute_InactiveMember_PropagatesMemberNotActiveException() {
            // given
            KakaoLoginCommand command = MemberCommandFixtures.kakaoLoginCommand();

            given(kakaoLoginValidator.validateAndFindExisting(command.phoneNumber()))
                    .willThrow(new MemberNotActiveException());

            // when & then
            assertThatThrownBy(() -> sut.execute(command))
                    .isInstanceOf(MemberNotActiveException.class);

            then(kakaoLoginCoordinator).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("Validator의 결과가 Coordinator에 그대로 전달된다")
        void execute_ValidCommand_ValidatorResultPassedToCoordinator() {
            // given
            KakaoLoginCommand command = MemberCommandFixtures.kakaoLoginCommand();
            Optional<MemberWithCredentials> emptyOpt = Optional.empty();
            LoginResult loginResult =
                    LoginResult.success("1001", "access", "refresh", 3600L, "Bearer");
            KakaoLoginResult kakaoResult = KakaoLoginResult.ofNewMember(loginResult);

            given(kakaoLoginValidator.validateAndFindExisting(command.phoneNumber()))
                    .willReturn(emptyOpt);
            given(kakaoLoginCoordinator.coordinate(emptyOpt, command)).willReturn(kakaoResult);

            // when
            sut.execute(command);

            // then
            then(kakaoLoginCoordinator).should().coordinate(emptyOpt, command);
        }
    }
}
