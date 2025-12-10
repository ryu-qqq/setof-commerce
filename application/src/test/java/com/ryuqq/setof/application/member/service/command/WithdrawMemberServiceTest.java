package com.ryuqq.setof.application.member.service.command;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.auth.port.in.command.RevokeTokensUseCase;
import com.ryuqq.setof.application.member.dto.command.WithdrawMemberCommand;
import com.ryuqq.setof.application.member.factory.command.MemberUpdateFactory;
import com.ryuqq.setof.application.member.manager.command.MemberPersistenceManager;
import com.ryuqq.setof.application.member.manager.query.MemberReadManager;
import com.ryuqq.setof.application.member.validator.MemberPolicyValidator;
import com.ryuqq.setof.domain.member.MemberFixture;
import com.ryuqq.setof.domain.member.aggregate.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("WithdrawMemberService")
class WithdrawMemberServiceTest {

    @Mock private MemberReadManager memberReadManager;

    @Mock private MemberPolicyValidator memberPolicyValidator;

    @Mock private MemberUpdateFactory memberUpdateFactory;

    @Mock private MemberPersistenceManager memberPersistenceManager;

    @Mock private RevokeTokensUseCase revokeTokensUseCase;

    private WithdrawMemberService withdrawMemberService;

    @BeforeEach
    void setUp() {
        withdrawMemberService =
                new WithdrawMemberService(
                        memberReadManager,
                        memberPolicyValidator,
                        memberUpdateFactory,
                        memberPersistenceManager,
                        revokeTokensUseCase);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("회원 탈퇴 처리 성공")
        void shouldWithdrawMemberSuccessfully() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            String reason = "서비스 불만족";
            WithdrawMemberCommand command = new WithdrawMemberCommand(memberId, reason);

            Member member = MemberFixture.createLocalMember();
            when(memberReadManager.findById(memberId)).thenReturn(member);

            // When
            withdrawMemberService.execute(command);

            // Then
            verify(memberPolicyValidator).validateCanWithdraw(member);
            verify(memberUpdateFactory).withdraw(member, reason);
            verify(memberPersistenceManager).persist(member);
            verify(revokeTokensUseCase).revokeByMemberId(memberId);
        }

        @Test
        @DisplayName("탈퇴 처리 순서 검증: 조회 → 검증 → 업데이트 → 저장 → 토큰 무효화")
        void shouldExecuteInCorrectOrder() {
            // Given
            String memberId = "member-id-123";
            String reason = "다른 서비스 이용";
            WithdrawMemberCommand command = new WithdrawMemberCommand(memberId, reason);

            Member member = MemberFixture.createLocalMember();
            when(memberReadManager.findById(memberId)).thenReturn(member);

            // When
            withdrawMemberService.execute(command);

            // Then
            InOrder inOrder =
                    Mockito.inOrder(
                            memberReadManager,
                            memberPolicyValidator,
                            memberUpdateFactory,
                            memberPersistenceManager,
                            revokeTokensUseCase);

            inOrder.verify(memberReadManager).findById(memberId);
            inOrder.verify(memberPolicyValidator).validateCanWithdraw(member);
            inOrder.verify(memberUpdateFactory).withdraw(member, reason);
            inOrder.verify(memberPersistenceManager).persist(member);
            inOrder.verify(revokeTokensUseCase).revokeByMemberId(memberId);
        }

        @Test
        @DisplayName("탈퇴 사유 없이도 탈퇴 가능")
        void shouldAllowWithdrawWithoutReason() {
            // Given
            String memberId = "member-id-456";
            String reason = null;
            WithdrawMemberCommand command = new WithdrawMemberCommand(memberId, reason);

            Member member = MemberFixture.createLocalMember();
            when(memberReadManager.findById(memberId)).thenReturn(member);

            // When
            withdrawMemberService.execute(command);

            // Then
            verify(memberUpdateFactory).withdraw(member, reason);
            verify(memberPersistenceManager).persist(member);
        }
    }
}
