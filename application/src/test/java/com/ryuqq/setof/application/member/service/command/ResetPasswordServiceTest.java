package com.ryuqq.setof.application.member.service.command;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.member.dto.command.ResetPasswordCommand;
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
@DisplayName("ResetPasswordService")
class ResetPasswordServiceTest {

    @Mock private MemberReadManager memberReadManager;

    @Mock private MemberPolicyValidator memberPolicyValidator;

    @Mock private MemberUpdateFactory memberUpdateFactory;

    @Mock private MemberPersistenceManager memberPersistenceManager;

    private ResetPasswordService resetPasswordService;

    @BeforeEach
    void setUp() {
        resetPasswordService =
                new ResetPasswordService(
                        memberReadManager,
                        memberPolicyValidator,
                        memberUpdateFactory,
                        memberPersistenceManager);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("비밀번호 재설정 성공")
        void shouldResetPasswordSuccessfully() {
            // Given
            String phoneNumber = "01012345678";
            String newPassword = "newPassword123!";
            ResetPasswordCommand command = new ResetPasswordCommand(phoneNumber, newPassword);

            Member member = MemberFixture.createLocalMember();
            when(memberReadManager.findByPhoneNumber(phoneNumber)).thenReturn(member);

            // When
            resetPasswordService.execute(command);

            // Then
            verify(memberPolicyValidator).validateCanResetPassword(member);
            verify(memberUpdateFactory).changePassword(member, newPassword);
            verify(memberPersistenceManager).persist(member);
        }

        @Test
        @DisplayName("비밀번호 재설정 순서 검증: 조회 → 검증 → 변경 → 저장")
        void shouldExecuteInCorrectOrder() {
            // Given
            String phoneNumber = "01098765432";
            String newPassword = "securePass456!";
            ResetPasswordCommand command = new ResetPasswordCommand(phoneNumber, newPassword);

            Member member = MemberFixture.createLocalMember();
            when(memberReadManager.findByPhoneNumber(phoneNumber)).thenReturn(member);

            // When
            resetPasswordService.execute(command);

            // Then
            InOrder inOrder =
                    Mockito.inOrder(
                            memberReadManager,
                            memberPolicyValidator,
                            memberUpdateFactory,
                            memberPersistenceManager);

            inOrder.verify(memberReadManager).findByPhoneNumber(phoneNumber);
            inOrder.verify(memberPolicyValidator).validateCanResetPassword(member);
            inOrder.verify(memberUpdateFactory).changePassword(member, newPassword);
            inOrder.verify(memberPersistenceManager).persist(member);
        }

        @Test
        @DisplayName("정책 검증 통과 후 비밀번호 변경")
        void shouldValidatePolicyBeforeChangingPassword() {
            // Given
            String phoneNumber = "01011112222";
            String newPassword = "myNewPass789!";
            ResetPasswordCommand command = new ResetPasswordCommand(phoneNumber, newPassword);

            Member member = MemberFixture.createLocalMember();
            when(memberReadManager.findByPhoneNumber(phoneNumber)).thenReturn(member);

            // When
            resetPasswordService.execute(command);

            // Then
            verify(memberPolicyValidator).validateCanResetPassword(member);
        }
    }
}
