package com.ryuqq.setof.application.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.ryuqq.setof.application.member.component.MemberPolicyValidator;
import com.ryuqq.setof.application.member.component.MemberReader;
import com.ryuqq.setof.application.member.component.MemberUpdater;
import com.ryuqq.setof.application.member.dto.command.WithdrawMemberCommand;
import com.ryuqq.setof.application.member.manager.MemberPersistenceManager;
import com.ryuqq.setof.application.member.manager.TokenManager;
import com.ryuqq.setof.domain.core.member.MemberFixture;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import com.ryuqq.setof.domain.core.member.exception.AlreadyWithdrawnMemberException;
import com.ryuqq.setof.domain.core.member.exception.MemberNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("WithdrawMemberService")
@ExtendWith(MockitoExtension.class)
class WithdrawMemberServiceTest {

    @Mock
    private MemberReader memberReader;

    @Mock
    private MemberPolicyValidator memberPolicyValidator;

    @Mock
    private MemberUpdater memberUpdater;

    @Mock
    private MemberPersistenceManager memberPersistenceManager;

    @Mock
    private TokenManager tokenManager;

    private WithdrawMemberService service;

    @BeforeEach
    void setUp() {
        service = new WithdrawMemberService(
                memberReader,
                memberPolicyValidator,
                memberUpdater,
                memberPersistenceManager,
                tokenManager);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("회원 탈퇴 성공")
        void shouldWithdrawMemberSuccessfully() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            String reason = "RARELY_USED";
            WithdrawMemberCommand command = new WithdrawMemberCommand(memberId, reason);
            Member localMember = MemberFixture.createLocalMember();

            when(memberReader.getById(memberId)).thenReturn(localMember);

            // When
            service.execute(command);

            // Then
            InOrder inOrder = inOrder(
                    memberReader, memberPolicyValidator, memberUpdater,
                    memberPersistenceManager, tokenManager);

            inOrder.verify(memberReader).getById(memberId);
            inOrder.verify(memberPolicyValidator).validateCanWithdraw(localMember);
            inOrder.verify(memberUpdater).withdraw(localMember, reason);
            inOrder.verify(memberPersistenceManager).persist(localMember);
            inOrder.verify(tokenManager).revokeTokensByMemberId(memberId);
        }

        @Test
        @DisplayName("존재하지 않는 회원 탈퇴 시 MemberNotFoundException 발생")
        void shouldThrowExceptionWhenMemberNotFound() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            String reason = "RARELY_USED";
            WithdrawMemberCommand command = new WithdrawMemberCommand(memberId, reason);

            when(memberReader.getById(memberId)).thenThrow(new MemberNotFoundException());

            // When & Then
            assertThrows(MemberNotFoundException.class, () -> service.execute(command));

            verify(memberReader).getById(memberId);
            verify(memberPolicyValidator, never()).validateCanWithdraw(any());
            verify(memberUpdater, never()).withdraw(any(), any());
            verify(memberPersistenceManager, never()).persist(any());
            verify(tokenManager, never()).revokeTokensByMemberId(anyString());
        }

        @Test
        @DisplayName("이미 탈퇴한 회원 탈퇴 시 AlreadyWithdrawnMemberException 발생")
        void shouldThrowExceptionWhenAlreadyWithdrawn() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            String reason = "RARELY_USED";
            WithdrawMemberCommand command = new WithdrawMemberCommand(memberId, reason);
            Member withdrawnMember = MemberFixture.createWithdrawnMember();

            when(memberReader.getById(memberId)).thenReturn(withdrawnMember);
            doThrow(new AlreadyWithdrawnMemberException())
                    .when(memberPolicyValidator).validateCanWithdraw(withdrawnMember);

            // When & Then
            assertThrows(AlreadyWithdrawnMemberException.class, () -> service.execute(command));

            verify(memberReader).getById(memberId);
            verify(memberPolicyValidator).validateCanWithdraw(withdrawnMember);
            verify(memberUpdater, never()).withdraw(any(), any());
            verify(memberPersistenceManager, never()).persist(any());
            verify(tokenManager, never()).revokeTokensByMemberId(anyString());
        }

        @Test
        @DisplayName("카카오 회원 탈퇴 성공")
        void shouldWithdrawKakaoMemberSuccessfully() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            String reason = "OTHER";
            WithdrawMemberCommand command = new WithdrawMemberCommand(memberId, reason);
            Member kakaoMember = MemberFixture.createKakaoMember();

            when(memberReader.getById(memberId)).thenReturn(kakaoMember);

            // When
            service.execute(command);

            // Then
            verify(memberReader).getById(memberId);
            verify(memberPolicyValidator).validateCanWithdraw(kakaoMember);
            verify(memberUpdater).withdraw(kakaoMember, reason);
            verify(memberPersistenceManager).persist(kakaoMember);
            verify(tokenManager).revokeTokensByMemberId(memberId);
        }
    }
}
