package com.ryuqq.setof.application.member.service;

import static org.mockito.Mockito.*;

import com.ryuqq.setof.application.member.dto.command.LogoutMemberCommand;
import com.ryuqq.setof.application.member.manager.TokenManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("LogoutMemberService")
@ExtendWith(MockitoExtension.class)
class LogoutMemberServiceTest {

    @Mock private TokenManager tokenManager;

    private LogoutMemberService service;

    @BeforeEach
    void setUp() {
        service = new LogoutMemberService(tokenManager);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("회원 ID로 토큰 무효화 성공")
        void shouldRevokeTokensByMemberId() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            LogoutMemberCommand command = new LogoutMemberCommand(memberId);

            // When
            service.execute(command);

            // Then
            verify(tokenManager).revokeTokensByMemberId(memberId);
        }

        @Test
        @DisplayName("다른 회원 ID로 토큰 무효화 시 해당 회원만 영향받음")
        void shouldRevokeTokensOnlyForGivenMemberId() {
            // Given
            String memberId1 = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            String memberId2 = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfb";
            LogoutMemberCommand command1 = new LogoutMemberCommand(memberId1);
            LogoutMemberCommand command2 = new LogoutMemberCommand(memberId2);

            // When
            service.execute(command1);
            service.execute(command2);

            // Then
            verify(tokenManager).revokeTokensByMemberId(memberId1);
            verify(tokenManager).revokeTokensByMemberId(memberId2);
            verifyNoMoreInteractions(tokenManager);
        }
    }
}
