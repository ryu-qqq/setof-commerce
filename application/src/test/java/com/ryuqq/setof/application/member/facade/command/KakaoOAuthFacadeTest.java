package com.ryuqq.setof.application.member.facade.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.member.dto.bundle.KakaoOAuthResult;
import com.ryuqq.setof.application.member.dto.command.KakaoOAuthCommand;
import com.ryuqq.setof.application.member.event.MemberEventDispatcher;
import com.ryuqq.setof.application.member.factory.command.MemberCommandFactory;
import com.ryuqq.setof.application.member.factory.command.MemberUpdateFactory;
import com.ryuqq.setof.application.member.manager.command.MemberPersistenceManager;
import com.ryuqq.setof.application.member.manager.query.MemberReadManager;
import com.ryuqq.setof.application.member.validator.KakaoOAuthPolicyValidator;
import com.ryuqq.setof.domain.member.MemberFixture;
import com.ryuqq.setof.domain.member.aggregate.Member;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("KakaoOAuthFacade")
class KakaoOAuthFacadeTest {

    @Mock private MemberReadManager memberReadManager;

    @Mock private KakaoOAuthPolicyValidator kakaoOAuthPolicyValidator;

    @Mock private MemberCommandFactory memberCommandFactory;

    @Mock private MemberUpdateFactory memberUpdateFactory;

    @Mock private MemberPersistenceManager memberPersistenceManager;

    @Mock private MemberEventDispatcher memberEventDispatcher;

    private KakaoOAuthFacade kakaoOAuthFacade;

    @BeforeEach
    void setUp() {
        kakaoOAuthFacade =
                new KakaoOAuthFacade(
                        memberReadManager,
                        kakaoOAuthPolicyValidator,
                        memberCommandFactory,
                        memberUpdateFactory,
                        memberPersistenceManager,
                        memberEventDispatcher);
    }

    @Nested
    @DisplayName("processKakaoLogin - 기존 카카오 회원")
    class ProcessExistingKakaoMemberTest {

        @Test
        @DisplayName("카카오 ID로 기존 회원 찾으면 EXISTING_KAKAO 반환")
        void shouldReturnExistingKakaoResult() {
            // Given
            KakaoOAuthCommand command = createCommand(false);
            Member existingMember = MemberFixture.createKakaoMemberWithSocialId("kakao_12345");

            when(memberReadManager.findBySocialId("kakao_12345"))
                    .thenReturn(Optional.of(existingMember));

            // When
            KakaoOAuthResult result = kakaoOAuthFacade.processKakaoLogin(command);

            // Then
            assertNotNull(result);
            assertEquals(KakaoOAuthResult.ResultType.EXISTING_KAKAO, result.resultType());
            assertEquals(existingMember.getIdValue(), result.memberId());
            assertFalse(result.isNewMember());
            assertFalse(result.wasIntegrated());

            verify(kakaoOAuthPolicyValidator).validateCanKakaoLogin(existingMember);
            verify(memberPersistenceManager, never()).persist(any());
            verify(memberEventDispatcher, never()).publish(any());
        }
    }

    @Nested
    @DisplayName("processKakaoLogin - 핸드폰 번호로 기존 회원 (통합 없음)")
    class ProcessExistingMemberWithoutIntegrationTest {

        @Test
        @DisplayName("integration=false면 EXISTING_MEMBER 반환 (카카오 연동 없이)")
        void shouldReturnExistingMemberResult() {
            // Given
            KakaoOAuthCommand command = createCommand(false);
            Member existingMember = MemberFixture.createLocalMember();

            when(memberReadManager.findBySocialId("kakao_12345")).thenReturn(Optional.empty());
            when(memberReadManager.findByPhoneNumberOptional("01012345678"))
                    .thenReturn(Optional.of(existingMember));

            // When
            KakaoOAuthResult result = kakaoOAuthFacade.processKakaoLogin(command);

            // Then
            assertNotNull(result);
            assertEquals(KakaoOAuthResult.ResultType.EXISTING_MEMBER, result.resultType());
            assertEquals(existingMember.getIdValue(), result.memberId());
            assertFalse(result.isNewMember());
            assertFalse(result.wasIntegrated());

            verify(kakaoOAuthPolicyValidator).validateCanKakaoLogin(existingMember);
            verify(memberUpdateFactory, never()).linkKakaoWithProfile(any(), any());
            verify(memberPersistenceManager, never()).persist(any());
        }
    }

    @Nested
    @DisplayName("processKakaoLogin - 핸드폰 번호로 기존 회원 (통합)")
    class ProcessExistingMemberWithIntegrationTest {

        @Test
        @DisplayName("integration=true면 카카오 연동 후 INTEGRATED 반환")
        void shouldIntegrateAndReturnIntegratedResult() {
            // Given
            KakaoOAuthCommand command = createCommand(true);
            Member existingMember = MemberFixture.createLocalMember();

            when(memberReadManager.findBySocialId("kakao_12345")).thenReturn(Optional.empty());
            when(memberReadManager.findByPhoneNumberOptional("01012345678"))
                    .thenReturn(Optional.of(existingMember));

            // When
            KakaoOAuthResult result = kakaoOAuthFacade.processKakaoLogin(command);

            // Then
            assertNotNull(result);
            assertEquals(KakaoOAuthResult.ResultType.INTEGRATED, result.resultType());
            assertEquals(existingMember.getIdValue(), result.memberId());
            assertFalse(result.isNewMember());
            assertTrue(result.wasIntegrated());

            verify(kakaoOAuthPolicyValidator).validateCanKakaoLogin(existingMember);
            verify(memberUpdateFactory).linkKakaoWithProfile(any(), any());
            verify(memberPersistenceManager).persist(existingMember);
            verify(memberEventDispatcher, never()).publish(any());
        }
    }

    @Nested
    @DisplayName("processKakaoLogin - 신규 카카오 회원")
    class ProcessNewKakaoMemberTest {

        @Test
        @DisplayName("회원이 없으면 신규 가입 후 NEW_MEMBER 반환")
        void shouldRegisterAndReturnNewMemberResult() {
            // Given
            KakaoOAuthCommand command = createCommand(false);
            Member newMember = MemberFixture.createKakaoMemberWithSocialId("kakao_12345");

            when(memberReadManager.findBySocialId("kakao_12345")).thenReturn(Optional.empty());
            when(memberReadManager.findByPhoneNumberOptional("01012345678"))
                    .thenReturn(Optional.empty());
            when(memberCommandFactory.create(command)).thenReturn(newMember);

            // When
            KakaoOAuthResult result = kakaoOAuthFacade.processKakaoLogin(command);

            // Then
            assertNotNull(result);
            assertEquals(KakaoOAuthResult.ResultType.NEW_MEMBER, result.resultType());
            assertEquals(newMember.getIdValue(), result.memberId());
            assertTrue(result.isNewMember());
            assertFalse(result.wasIntegrated());

            verify(memberCommandFactory).create(command);
            verify(memberPersistenceManager).persist(newMember);
            verify(memberEventDispatcher).publish(newMember);
        }
    }

    private KakaoOAuthCommand createCommand(boolean integration) {
        return new KakaoOAuthCommand(
                "kakao_12345",
                "01012345678",
                "kakao@example.com",
                "카카오사용자",
                LocalDate.of(1990, 1, 1),
                "M",
                Collections.emptyList(),
                integration);
    }
}
