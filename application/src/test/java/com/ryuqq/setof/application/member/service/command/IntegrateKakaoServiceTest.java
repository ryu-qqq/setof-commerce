package com.ryuqq.setof.application.member.service.command;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.member.dto.command.IntegrateKakaoCommand;
import com.ryuqq.setof.application.member.factory.command.MemberUpdateFactory;
import com.ryuqq.setof.application.member.manager.command.MemberPersistenceManager;
import com.ryuqq.setof.application.member.manager.query.MemberReadManager;
import com.ryuqq.setof.application.member.validator.KakaoOAuthPolicyValidator;
import com.ryuqq.setof.domain.member.MemberFixture;
import com.ryuqq.setof.domain.member.aggregate.Member;
import java.time.LocalDate;
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
@DisplayName("IntegrateKakaoService")
class IntegrateKakaoServiceTest {

    @Mock private MemberReadManager memberReadManager;

    @Mock private KakaoOAuthPolicyValidator kakaoOAuthPolicyValidator;

    @Mock private MemberUpdateFactory memberUpdateFactory;

    @Mock private MemberPersistenceManager memberPersistenceManager;

    private IntegrateKakaoService integrateKakaoService;

    @BeforeEach
    void setUp() {
        integrateKakaoService =
                new IntegrateKakaoService(
                        memberReadManager,
                        kakaoOAuthPolicyValidator,
                        memberUpdateFactory,
                        memberPersistenceManager);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("카카오 계정 통합 성공")
        void shouldIntegrateKakaoSuccessfully() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            IntegrateKakaoCommand command =
                    new IntegrateKakaoCommand(
                            memberId,
                            "kakao_123456789",
                            "kakao@example.com",
                            "홍길동",
                            LocalDate.of(1990, 1, 15),
                            "M");

            Member member = MemberFixture.createLocalMember();
            when(memberReadManager.findById(memberId)).thenReturn(member);

            // When
            integrateKakaoService.execute(command);

            // Then
            verify(kakaoOAuthPolicyValidator).validateCanIntegrateKakao(member);
            verify(memberUpdateFactory).linkKakaoWithProfile(member, command);
            verify(memberPersistenceManager).persist(member);
        }

        @Test
        @DisplayName("통합 처리 순서 검증: 조회 → 검증 → 연동 → 저장")
        void shouldExecuteInCorrectOrder() {
            // Given
            String memberId = "member-id-123";
            IntegrateKakaoCommand command =
                    IntegrateKakaoCommand.withoutProfile(memberId, "kakao_789");

            Member member = MemberFixture.createLocalMember();
            when(memberReadManager.findById(memberId)).thenReturn(member);

            // When
            integrateKakaoService.execute(command);

            // Then
            InOrder inOrder =
                    Mockito.inOrder(
                            memberReadManager,
                            kakaoOAuthPolicyValidator,
                            memberUpdateFactory,
                            memberPersistenceManager);

            inOrder.verify(memberReadManager).findById(memberId);
            inOrder.verify(kakaoOAuthPolicyValidator).validateCanIntegrateKakao(member);
            inOrder.verify(memberUpdateFactory).linkKakaoWithProfile(member, command);
            inOrder.verify(memberPersistenceManager).persist(member);
        }

        @Test
        @DisplayName("프로필 정보 없이 카카오 연동만 수행")
        void shouldIntegrateKakaoWithoutProfile() {
            // Given
            String memberId = "member-id-456";
            IntegrateKakaoCommand command =
                    IntegrateKakaoCommand.withoutProfile(memberId, "kakao_456");

            Member member = MemberFixture.createLocalMember();
            when(memberReadManager.findById(memberId)).thenReturn(member);

            // When
            integrateKakaoService.execute(command);

            // Then
            verify(memberUpdateFactory).linkKakaoWithProfile(member, command);
            verify(memberPersistenceManager).persist(member);
        }

        @Test
        @DisplayName("정책 검증 통과 후 연동 수행")
        void shouldValidatePolicyBeforeIntegration() {
            // Given
            String memberId = "member-id-789";
            IntegrateKakaoCommand command =
                    new IntegrateKakaoCommand(memberId, "kakao_999", null, null, null, null);

            Member member = MemberFixture.createLocalMember();
            when(memberReadManager.findById(memberId)).thenReturn(member);

            // When
            integrateKakaoService.execute(command);

            // Then
            verify(kakaoOAuthPolicyValidator).validateCanIntegrateKakao(member);
        }
    }
}
