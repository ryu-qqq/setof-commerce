package com.ryuqq.setof.application.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.ryuqq.setof.application.member.component.KakaoOAuthPolicyValidator;
import com.ryuqq.setof.application.member.component.MemberReader;
import com.ryuqq.setof.application.member.component.MemberUpdater;
import com.ryuqq.setof.application.member.dto.command.IntegrateKakaoCommand;
import com.ryuqq.setof.application.member.manager.MemberPersistenceManager;
import com.ryuqq.setof.domain.core.member.MemberFixture;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import com.ryuqq.setof.domain.core.member.exception.AlreadyKakaoMemberException;
import com.ryuqq.setof.domain.core.member.exception.InactiveMemberException;
import com.ryuqq.setof.domain.core.member.exception.MemberNotFoundException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("IntegrateKakaoService")
@ExtendWith(MockitoExtension.class)
class IntegrateKakaoServiceTest {

    @Mock private MemberReader memberReader;

    @Mock private KakaoOAuthPolicyValidator kakaoOAuthPolicyValidator;

    @Mock private MemberUpdater memberUpdater;

    @Mock private MemberPersistenceManager memberPersistenceManager;

    private IntegrateKakaoService service;

    @BeforeEach
    void setUp() {
        service =
                new IntegrateKakaoService(
                        memberReader, kakaoOAuthPolicyValidator,
                        memberUpdater, memberPersistenceManager);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("LOCAL 회원에 카카오 계정 연동 및 프로필 업데이트 성공")
        void shouldIntegrateKakaoAndUpdateProfile() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            String kakaoId = "kakao_12345";
            IntegrateKakaoCommand command =
                    new IntegrateKakaoCommand(
                            memberId,
                            kakaoId,
                            "kakao@example.com",
                            "카카오이름",
                            LocalDate.of(1995, 5, 15),
                            "F");
            Member localMember = MemberFixture.createLocalMember();

            when(memberReader.getById(memberId)).thenReturn(localMember);

            // When
            service.execute(command);

            // Then
            verify(memberReader).getById(memberId);
            verify(kakaoOAuthPolicyValidator).validateCanIntegrateKakao(localMember);
            verify(memberUpdater).linkKakaoWithProfile(localMember, command);
            verify(memberPersistenceManager).persist(localMember);
        }

        @Test
        @DisplayName("프로필 정보 없이 카카오 연동만 성공")
        void shouldIntegrateKakaoWithoutProfile() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            String kakaoId = "kakao_12345";
            IntegrateKakaoCommand command = IntegrateKakaoCommand.withoutProfile(memberId, kakaoId);
            Member localMember = MemberFixture.createLocalMember();

            when(memberReader.getById(memberId)).thenReturn(localMember);

            // When
            service.execute(command);

            // Then
            verify(memberReader).getById(memberId);
            verify(kakaoOAuthPolicyValidator).validateCanIntegrateKakao(localMember);
            verify(memberUpdater).linkKakaoWithProfile(localMember, command);
            verify(memberPersistenceManager).persist(localMember);
        }

        @Test
        @DisplayName("회원 조회 실패 시 MemberNotFoundException 발생")
        void shouldThrowExceptionWhenMemberNotFound() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            String kakaoId = "kakao_12345";
            IntegrateKakaoCommand command = IntegrateKakaoCommand.withoutProfile(memberId, kakaoId);

            when(memberReader.getById(memberId)).thenThrow(new MemberNotFoundException());

            // When & Then
            assertThrows(MemberNotFoundException.class, () -> service.execute(command));

            verify(memberReader).getById(memberId);
            verify(kakaoOAuthPolicyValidator, never()).validateCanIntegrateKakao(any());
            verify(memberUpdater, never()).linkKakaoWithProfile(any(), any());
            verify(memberPersistenceManager, never()).persist(any());
        }

        @Test
        @DisplayName("이미 카카오 회원인 경우 AlreadyKakaoMemberException 발생")
        void shouldThrowExceptionWhenAlreadyKakaoMember() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            String kakaoId = "kakao_12345";
            IntegrateKakaoCommand command = IntegrateKakaoCommand.withoutProfile(memberId, kakaoId);
            Member kakaoMember = MemberFixture.createKakaoMember();

            when(memberReader.getById(memberId)).thenReturn(kakaoMember);
            doThrow(new AlreadyKakaoMemberException())
                    .when(kakaoOAuthPolicyValidator)
                    .validateCanIntegrateKakao(kakaoMember);

            // When & Then
            assertThrows(AlreadyKakaoMemberException.class, () -> service.execute(command));

            verify(memberReader).getById(memberId);
            verify(kakaoOAuthPolicyValidator).validateCanIntegrateKakao(kakaoMember);
            verify(memberUpdater, never()).linkKakaoWithProfile(any(), any());
            verify(memberPersistenceManager, never()).persist(any());
        }

        @Test
        @DisplayName("비활성 회원인 경우 InactiveMemberException 발생")
        void shouldThrowExceptionWhenInactiveMember() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            String kakaoId = "kakao_12345";
            IntegrateKakaoCommand command = IntegrateKakaoCommand.withoutProfile(memberId, kakaoId);
            Member localMember = MemberFixture.createLocalMember();

            when(memberReader.getById(memberId)).thenReturn(localMember);
            doThrow(new InactiveMemberException())
                    .when(kakaoOAuthPolicyValidator)
                    .validateCanIntegrateKakao(localMember);

            // When & Then
            assertThrows(InactiveMemberException.class, () -> service.execute(command));

            verify(memberReader).getById(memberId);
            verify(kakaoOAuthPolicyValidator).validateCanIntegrateKakao(localMember);
            verify(memberUpdater, never()).linkKakaoWithProfile(any(), any());
            verify(memberPersistenceManager, never()).persist(any());
        }
    }
}
