package com.ryuqq.setof.application.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.ryuqq.setof.application.member.component.MemberCreator;
import com.ryuqq.setof.application.member.component.MemberPolicyValidator;
import com.ryuqq.setof.application.member.component.MemberReader;
import com.ryuqq.setof.application.member.dto.command.RegisterMemberCommand;
import com.ryuqq.setof.application.member.dto.response.RegisterMemberResponse;
import com.ryuqq.setof.application.member.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.facade.RegisterMemberFacade;
import com.ryuqq.setof.domain.core.member.MemberFixture;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import com.ryuqq.setof.domain.core.member.exception.DuplicatePhoneNumberException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("RegisterMemberService")
@ExtendWith(MockitoExtension.class)
class RegisterMemberServiceTest {

    @Mock
    private MemberReader memberReader;

    @Mock
    private MemberPolicyValidator memberPolicyValidator;

    @Mock
    private MemberCreator memberCreator;

    @Mock
    private RegisterMemberFacade registerMemberFacade;

    private RegisterMemberService service;

    @BeforeEach
    void setUp() {
        service = new RegisterMemberService(
                memberReader, memberPolicyValidator, memberCreator, registerMemberFacade);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("회원가입 성공 시 회원 ID와 토큰 반환")
        void shouldRegisterMemberSuccessfully() {
            // Given
            RegisterMemberCommand command = createRegisterCommand();
            Member newMember = MemberFixture.createLocalMember();
            String memberId = newMember.getIdValue();
            TokenPairResponse tokens = createTokenPair();
            RegisterMemberResponse expectedResponse = new RegisterMemberResponse(memberId, tokens);

            when(memberReader.existsByPhoneNumber(command.phoneNumber())).thenReturn(false);
            when(memberCreator.createLocalMember(command)).thenReturn(newMember);
            when(registerMemberFacade.register(newMember)).thenReturn(expectedResponse);

            // When
            RegisterMemberResponse result = service.execute(command);

            // Then
            assertNotNull(result);
            assertEquals(memberId, result.memberId());
            assertNotNull(result.tokens());

            verify(memberReader).existsByPhoneNumber(command.phoneNumber());
            verify(memberPolicyValidator).validatePhoneNumberNotDuplicate(false);
            verify(memberCreator).createLocalMember(command);
            verify(registerMemberFacade).register(newMember);
        }

        @Test
        @DisplayName("중복된 핸드폰 번호로 회원가입 시 DuplicatePhoneNumberException 발생")
        void shouldThrowExceptionWhenPhoneNumberDuplicate() {
            // Given
            RegisterMemberCommand command = createRegisterCommand();

            when(memberReader.existsByPhoneNumber(command.phoneNumber())).thenReturn(true);
            doThrow(new DuplicatePhoneNumberException())
                    .when(memberPolicyValidator).validatePhoneNumberNotDuplicate(true);

            // When & Then
            assertThrows(DuplicatePhoneNumberException.class, () -> service.execute(command));

            verify(memberReader).existsByPhoneNumber(command.phoneNumber());
            verify(memberPolicyValidator).validatePhoneNumberNotDuplicate(true);
            verify(memberCreator, never()).createLocalMember(any());
            verify(registerMemberFacade, never()).register(any());
        }

        @Test
        @DisplayName("동의 항목과 함께 회원가입 성공")
        void shouldRegisterMemberWithConsents() {
            // Given
            List<RegisterMemberCommand.ConsentItem> consents = List.of(
                    new RegisterMemberCommand.ConsentItem("TERMS", true),
                    new RegisterMemberCommand.ConsentItem("PRIVACY", true),
                    new RegisterMemberCommand.ConsentItem("MARKETING", false));
            RegisterMemberCommand command = new RegisterMemberCommand(
                    "01012345678",
                    "test@example.com",
                    "password123!",
                    "홍길동",
                    LocalDate.of(1990, 1, 1),
                    "M",
                    consents);

            Member newMember = MemberFixture.createLocalMember();
            String memberId = newMember.getIdValue();
            TokenPairResponse tokens = createTokenPair();
            RegisterMemberResponse expectedResponse = new RegisterMemberResponse(memberId, tokens);

            when(memberReader.existsByPhoneNumber(command.phoneNumber())).thenReturn(false);
            when(memberCreator.createLocalMember(command)).thenReturn(newMember);
            when(registerMemberFacade.register(newMember)).thenReturn(expectedResponse);

            // When
            RegisterMemberResponse result = service.execute(command);

            // Then
            assertNotNull(result);
            verify(memberCreator).createLocalMember(command);
        }
    }

    private RegisterMemberCommand createRegisterCommand() {
        return new RegisterMemberCommand(
                "01012345678",
                "test@example.com",
                "password123!",
                "홍길동",
                LocalDate.of(1990, 1, 1),
                "M",
                Collections.emptyList());
    }

    private TokenPairResponse createTokenPair() {
        return new TokenPairResponse(
                "access_token_123",
                "refresh_token_456",
                3600L,
                604800L);
    }
}
