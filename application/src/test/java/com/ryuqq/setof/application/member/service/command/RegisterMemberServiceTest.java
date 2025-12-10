package com.ryuqq.setof.application.member.service.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.dto.bundle.ConsentItem;
import com.ryuqq.setof.application.member.dto.command.RegisterMemberCommand;
import com.ryuqq.setof.application.member.dto.response.RegisterMemberResponse;
import com.ryuqq.setof.application.member.facade.command.RegisterMemberFacade;
import com.ryuqq.setof.application.member.factory.command.MemberCommandFactory;
import com.ryuqq.setof.application.member.manager.query.MemberReadManager;
import com.ryuqq.setof.application.member.validator.MemberPolicyValidator;
import com.ryuqq.setof.domain.member.MemberFixture;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.exception.DuplicatePhoneNumberException;
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

    @Mock private MemberReadManager memberReadManager;

    @Mock private MemberPolicyValidator memberPolicyValidator;

    @Mock private MemberCommandFactory memberCommandFactory;

    @Mock private RegisterMemberFacade registerMemberFacade;

    private RegisterMemberService service;

    @BeforeEach
    void setUp() {
        service =
                new RegisterMemberService(
                        memberReadManager,
                        memberPolicyValidator,
                        memberCommandFactory,
                        registerMemberFacade);
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

            when(memberReadManager.existsByPhoneNumber(command.phoneNumber())).thenReturn(false);
            when(memberCommandFactory.create(command)).thenReturn(newMember);
            when(registerMemberFacade.persistMember(newMember)).thenReturn(expectedResponse);

            // When
            RegisterMemberResponse result = service.execute(command);

            // Then
            assertNotNull(result);
            assertEquals(memberId, result.memberId());
            assertNotNull(result.tokens());

            verify(memberReadManager).existsByPhoneNumber(command.phoneNumber());
            verify(memberPolicyValidator)
                    .validatePhoneNumberNotDuplicate(command.phoneNumber(), false);
            verify(memberCommandFactory).create(command);
            verify(registerMemberFacade).persistMember(newMember);
        }

        @Test
        @DisplayName("중복된 핸드폰 번호로 회원가입 시 DuplicatePhoneNumberException 발생")
        void shouldThrowExceptionWhenPhoneNumberDuplicate() {
            // Given
            RegisterMemberCommand command = createRegisterCommand();

            when(memberReadManager.existsByPhoneNumber(command.phoneNumber())).thenReturn(true);
            doThrow(new DuplicatePhoneNumberException(command.phoneNumber()))
                    .when(memberPolicyValidator)
                    .validatePhoneNumberNotDuplicate(command.phoneNumber(), true);

            // When & Then
            assertThrows(DuplicatePhoneNumberException.class, () -> service.execute(command));

            verify(memberReadManager).existsByPhoneNumber(command.phoneNumber());
            verify(memberPolicyValidator)
                    .validatePhoneNumberNotDuplicate(command.phoneNumber(), true);
            verify(memberCommandFactory, never()).create(any(RegisterMemberCommand.class));
            verify(registerMemberFacade, never()).persistMember(any());
        }

        @Test
        @DisplayName("동의 항목과 함께 회원가입 성공")
        void shouldRegisterMemberWithConsents() {
            // Given
            List<ConsentItem> consents =
                    List.of(
                            new ConsentItem("TERMS", true),
                            new ConsentItem("PRIVACY", true),
                            new ConsentItem("MARKETING", false));
            RegisterMemberCommand command =
                    new RegisterMemberCommand(
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

            when(memberReadManager.existsByPhoneNumber(command.phoneNumber())).thenReturn(false);
            when(memberCommandFactory.create(command)).thenReturn(newMember);
            when(registerMemberFacade.persistMember(newMember)).thenReturn(expectedResponse);

            // When
            RegisterMemberResponse result = service.execute(command);

            // Then
            assertNotNull(result);
            verify(memberCommandFactory).create(command);
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
        return new TokenPairResponse("access_token_123", 3600L, "refresh_token_456", 604800L);
    }
}
