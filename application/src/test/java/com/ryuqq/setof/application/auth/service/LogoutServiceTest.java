package com.ryuqq.setof.application.auth.service;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.auth.AuthCommandFixtures;
import com.ryuqq.setof.application.auth.dto.command.LogoutCommand;
import com.ryuqq.setof.application.auth.manager.TokenCommandFacade;
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
@DisplayName("LogoutService 단위 테스트")
class LogoutServiceTest {

    @InjectMocks private LogoutService sut;

    @Mock private TokenCommandFacade tokenCommandFacade;

    @Nested
    @DisplayName("execute() - 로그아웃")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 로그아웃을 수행하고 Refresh Token 캐시를 삭제한다")
        void execute_ValidCommand_RevokesRefreshToken() {
            // given
            LogoutCommand command = AuthCommandFixtures.logoutCommand();
            willDoNothing().given(tokenCommandFacade).revokeRefreshToken(command.userId());

            // when
            sut.execute(command);

            // then
            then(tokenCommandFacade).should().revokeRefreshToken(command.userId());
        }

        @Test
        @DisplayName("다른 userId로 로그아웃 시 tokenCommandFacade.revokeRefreshToken을 호출한다")
        void execute_DifferentUserId_CallsRevokeRefreshToken() {
            // given
            LogoutCommand command = AuthCommandFixtures.logoutCommand("999");
            willDoNothing().given(tokenCommandFacade).revokeRefreshToken(command.userId());

            // when
            sut.execute(command);

            // then
            then(tokenCommandFacade).should().revokeRefreshToken("999");
        }
    }
}
