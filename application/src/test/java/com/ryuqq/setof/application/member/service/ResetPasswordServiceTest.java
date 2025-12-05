package com.ryuqq.setof.application.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.ryuqq.setof.application.member.component.MemberPolicyValidator;
import com.ryuqq.setof.application.member.component.MemberReader;
import com.ryuqq.setof.application.member.component.MemberUpdater;
import com.ryuqq.setof.application.member.dto.command.ResetPasswordCommand;
import com.ryuqq.setof.application.member.manager.MemberPersistenceManager;
import com.ryuqq.setof.domain.core.member.MemberFixture;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import com.ryuqq.setof.domain.core.member.exception.AlreadyKakaoMemberException;
import com.ryuqq.setof.domain.core.member.exception.MemberNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("ResetPasswordService")
@ExtendWith(MockitoExtension.class)
class ResetPasswordServiceTest {

    @Mock private MemberReader memberReader;

    @Mock private MemberPolicyValidator memberPolicyValidator;

    @Mock private MemberUpdater memberUpdater;

    @Mock private MemberPersistenceManager memberPersistenceManager;

    private ResetPasswordService service;

    @BeforeEach
    void setUp() {
        service =
                new ResetPasswordService(
                        memberReader,
                        memberPolicyValidator,
                        memberUpdater,
                        memberPersistenceManager);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("LOCAL 회원 비밀번호 재설정 성공")
        void shouldResetPasswordSuccessfully() {
            // Given
            String phoneNumber = "01012345678";
            String newRawPassword = "newPassword123!";
            ResetPasswordCommand command = new ResetPasswordCommand(phoneNumber, newRawPassword);
            Member localMember = MemberFixture.createLocalMember();

            when(memberReader.getByPhoneNumber(phoneNumber)).thenReturn(localMember);

            // When
            service.execute(command);

            // Then
            InOrder inOrder =
                    inOrder(
                            memberReader,
                            memberPolicyValidator,
                            memberUpdater,
                            memberPersistenceManager);

            inOrder.verify(memberReader).getByPhoneNumber(phoneNumber);
            inOrder.verify(memberPolicyValidator).validateCanResetPassword(localMember);
            inOrder.verify(memberUpdater).changePassword(localMember, newRawPassword);
            inOrder.verify(memberPersistenceManager).persist(localMember);
        }

        @Test
        @DisplayName("존재하지 않는 회원 비밀번호 재설정 시 MemberNotFoundException 발생")
        void shouldThrowExceptionWhenMemberNotFound() {
            // Given
            String phoneNumber = "01099999999";
            String newRawPassword = "newPassword123!";
            ResetPasswordCommand command = new ResetPasswordCommand(phoneNumber, newRawPassword);

            when(memberReader.getByPhoneNumber(phoneNumber))
                    .thenThrow(new MemberNotFoundException());

            // When & Then
            assertThrows(MemberNotFoundException.class, () -> service.execute(command));

            verify(memberReader).getByPhoneNumber(phoneNumber);
            verify(memberPolicyValidator, never()).validateCanResetPassword(any());
            verify(memberUpdater, never()).changePassword(any(), any());
            verify(memberPersistenceManager, never()).persist(any());
        }

        @Test
        @DisplayName("카카오 회원 비밀번호 재설정 시 AlreadyKakaoMemberException 발생")
        void shouldThrowExceptionWhenKakaoMember() {
            // Given
            String phoneNumber = "01012345678";
            String newRawPassword = "newPassword123!";
            ResetPasswordCommand command = new ResetPasswordCommand(phoneNumber, newRawPassword);
            Member kakaoMember = MemberFixture.createKakaoMember();

            when(memberReader.getByPhoneNumber(phoneNumber)).thenReturn(kakaoMember);
            doThrow(new AlreadyKakaoMemberException())
                    .when(memberPolicyValidator)
                    .validateCanResetPassword(kakaoMember);

            // When & Then
            assertThrows(AlreadyKakaoMemberException.class, () -> service.execute(command));

            verify(memberReader).getByPhoneNumber(phoneNumber);
            verify(memberPolicyValidator).validateCanResetPassword(kakaoMember);
            verify(memberUpdater, never()).changePassword(any(), any());
            verify(memberPersistenceManager, never()).persist(any());
        }

        @Test
        @DisplayName("ID가 있는 LOCAL 회원 비밀번호 재설정 성공")
        void shouldResetPasswordForMemberWithId() {
            // Given
            String phoneNumber = "01012345678";
            String newRawPassword = "newPassword123!";
            ResetPasswordCommand command = new ResetPasswordCommand(phoneNumber, newRawPassword);
            Member localMemberWithId =
                    MemberFixture.createLocalMemberWithId("01936ddc-8d37-7c6e-8ad6-18c76adc9dfa");

            when(memberReader.getByPhoneNumber(phoneNumber)).thenReturn(localMemberWithId);

            // When
            service.execute(command);

            // Then
            verify(memberReader).getByPhoneNumber(phoneNumber);
            verify(memberPolicyValidator).validateCanResetPassword(localMemberWithId);
            verify(memberUpdater).changePassword(localMemberWithId, newRawPassword);
            verify(memberPersistenceManager).persist(localMemberWithId);
        }
    }
}
