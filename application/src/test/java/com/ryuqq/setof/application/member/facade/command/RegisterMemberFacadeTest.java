package com.ryuqq.setof.application.member.facade.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.auth.port.in.command.IssueTokensUseCase;
import com.ryuqq.setof.application.member.dto.response.RegisterMemberResponse;
import com.ryuqq.setof.application.member.event.MemberEventDispatcher;
import com.ryuqq.setof.application.member.manager.command.MemberPersistenceManager;
import com.ryuqq.setof.domain.member.MemberFixture;
import com.ryuqq.setof.domain.member.aggregate.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("RegisterMemberFacade")
@ExtendWith(MockitoExtension.class)
class RegisterMemberFacadeTest {

    @Mock private MemberPersistenceManager memberPersistenceManager;

    @Mock private IssueTokensUseCase issueTokensUseCase;

    @Mock private MemberEventDispatcher memberEventDispatcher;

    private RegisterMemberFacade registerMemberFacade;

    @BeforeEach
    void setUp() {
        registerMemberFacade =
                new RegisterMemberFacade(
                        memberPersistenceManager, issueTokensUseCase, memberEventDispatcher);
    }

    @Nested
    @DisplayName("persistMember")
    class PersistMemberTest {

        @Test
        @DisplayName("회원 등록 성공 시 토큰 발급 및 이벤트 발행")
        void shouldRegisterMemberAndIssueTokensAndPublishEvent() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            Member member = MemberFixture.createLocalMemberWithId(memberId);
            TokenPairResponse expectedTokens =
                    new TokenPairResponse("access_token", 3600L, "refresh_token", 604800L);

            when(issueTokensUseCase.execute(memberId)).thenReturn(expectedTokens);

            // When
            RegisterMemberResponse result = registerMemberFacade.persistMember(member);

            // Then
            assertNotNull(result);
            assertEquals(memberId, result.memberId());
            assertEquals(expectedTokens, result.tokens());

            verify(memberPersistenceManager).persist(member);
            verify(issueTokensUseCase).execute(memberId);
            verify(memberEventDispatcher).publish(member);
        }

        @Test
        @DisplayName("회원 등록 시 올바른 순서로 호출됨")
        void shouldCallMethodsInCorrectOrder() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            Member member = MemberFixture.createLocalMemberWithId(memberId);
            TokenPairResponse expectedTokens =
                    new TokenPairResponse("access_token", 3600L, "refresh_token", 604800L);

            when(issueTokensUseCase.execute(memberId)).thenReturn(expectedTokens);

            InOrder inOrder =
                    inOrder(memberPersistenceManager, issueTokensUseCase, memberEventDispatcher);

            // When
            registerMemberFacade.persistMember(member);

            // Then
            inOrder.verify(memberPersistenceManager).persist(member);
            inOrder.verify(issueTokensUseCase).execute(memberId);
            inOrder.verify(memberEventDispatcher).publish(member);
        }

        @Test
        @DisplayName("카카오 회원 등록 성공")
        void shouldRegisterKakaoMember() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            Member kakaoMember = MemberFixture.createKakaoMemberWithId(memberId);
            TokenPairResponse expectedTokens =
                    new TokenPairResponse(
                            "kakao_access_token", 3600L, "kakao_refresh_token", 604800L);

            when(issueTokensUseCase.execute(memberId)).thenReturn(expectedTokens);

            // When
            RegisterMemberResponse result = registerMemberFacade.persistMember(kakaoMember);

            // Then
            assertNotNull(result);
            assertEquals(memberId, result.memberId());
            assertEquals(expectedTokens, result.tokens());

            verify(memberPersistenceManager).persist(kakaoMember);
            verify(issueTokensUseCase).execute(memberId);
            verify(memberEventDispatcher).publish(kakaoMember);
        }
    }
}
