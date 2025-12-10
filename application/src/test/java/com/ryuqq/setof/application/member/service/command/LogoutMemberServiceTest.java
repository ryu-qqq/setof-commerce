package com.ryuqq.setof.application.member.service.command;

import static org.mockito.Mockito.verify;

import com.ryuqq.setof.application.auth.port.in.command.RevokeTokensUseCase;
import com.ryuqq.setof.application.member.dto.command.LogoutMemberCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("LogoutMemberService")
class LogoutMemberServiceTest {

    @Mock private RevokeTokensUseCase revokeTokensUseCase;

    private LogoutMemberService logoutMemberService;

    @BeforeEach
    void setUp() {
        logoutMemberService = new LogoutMemberService(revokeTokensUseCase);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("회원 ID로 토큰 무효화")
        void shouldRevokeTokensByMemberId() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            LogoutMemberCommand command = new LogoutMemberCommand(memberId);

            // When
            logoutMemberService.execute(command);

            // Then
            verify(revokeTokensUseCase).revokeByMemberId(memberId);
        }

        @Test
        @DisplayName("RevokeTokensUseCase로 토큰 무효화 위임")
        void shouldDelegateToRevokeTokensUseCase() {
            // Given
            String memberId = "member-id-123";
            LogoutMemberCommand command = new LogoutMemberCommand(memberId);

            // When
            logoutMemberService.execute(command);

            // Then
            verify(revokeTokensUseCase).revokeByMemberId(memberId);
        }
    }
}
