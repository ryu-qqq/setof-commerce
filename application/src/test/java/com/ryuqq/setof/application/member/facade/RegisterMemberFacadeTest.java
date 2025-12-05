package com.ryuqq.setof.application.member.facade;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ryuqq.setof.application.member.dto.response.RegisterMemberResponse;
import com.ryuqq.setof.application.member.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.event.MemberEventDispatcher;
import com.ryuqq.setof.application.member.manager.MemberPersistenceManager;
import com.ryuqq.setof.application.member.manager.TokenManager;
import com.ryuqq.setof.domain.core.member.MemberFixture;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
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

    @Mock private TokenManager tokenManager;

    @Mock private MemberEventDispatcher memberEventDispatcher;

    private RegisterMemberFacade registerMemberFacade;

    @BeforeEach
    void setUp() {
        registerMemberFacade =
                new RegisterMemberFacade(
                        memberPersistenceManager, tokenManager, memberEventDispatcher);
    }

    @Nested
    @DisplayName("register")
    class RegisterTest {

        @Test
        @DisplayName("회원 등록 성공 시 토큰 발급 및 이벤트 발행")
        void shouldRegisterMemberAndIssueTokensAndPublishEvent() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            Member member = MemberFixture.createLocalMemberWithId(memberId);
            TokenPairResponse expectedTokens =
                    new TokenPairResponse("access_token", "refresh_token", 3600L, 604800L);

            when(tokenManager.issueTokens(memberId)).thenReturn(expectedTokens);

            // When
            RegisterMemberResponse result = registerMemberFacade.register(member);

            // Then
            assertNotNull(result);
            assertEquals(memberId, result.memberId());
            assertEquals(expectedTokens, result.tokens());

            verify(memberPersistenceManager).persist(member);
            verify(tokenManager).issueTokens(memberId);
            verify(memberEventDispatcher).publishAndClear(member);
        }

        @Test
        @DisplayName("회원 등록 시 올바른 순서로 호출됨")
        void shouldCallMethodsInCorrectOrder() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            Member member = MemberFixture.createLocalMemberWithId(memberId);
            TokenPairResponse expectedTokens =
                    new TokenPairResponse("access_token", "refresh_token", 3600L, 604800L);

            when(tokenManager.issueTokens(memberId)).thenReturn(expectedTokens);

            InOrder inOrder =
                    inOrder(memberPersistenceManager, tokenManager, memberEventDispatcher);

            // When
            registerMemberFacade.register(member);

            // Then
            inOrder.verify(memberPersistenceManager).persist(member);
            inOrder.verify(tokenManager).issueTokens(memberId);
            inOrder.verify(memberEventDispatcher).publishAndClear(member);
        }

        @Test
        @DisplayName("카카오 회원 등록 성공")
        void shouldRegisterKakaoMember() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            Member kakaoMember = MemberFixture.createKakaoMemberWithId(memberId);
            TokenPairResponse expectedTokens =
                    new TokenPairResponse(
                            "kakao_access_token", "kakao_refresh_token", 3600L, 604800L);

            when(tokenManager.issueTokens(memberId)).thenReturn(expectedTokens);

            // When
            RegisterMemberResponse result = registerMemberFacade.register(kakaoMember);

            // Then
            assertNotNull(result);
            assertEquals(memberId, result.memberId());
            assertEquals(expectedTokens, result.tokens());

            verify(memberPersistenceManager).persist(kakaoMember);
            verify(tokenManager).issueTokens(memberId);
            verify(memberEventDispatcher).publishAndClear(kakaoMember);
        }
    }
}
