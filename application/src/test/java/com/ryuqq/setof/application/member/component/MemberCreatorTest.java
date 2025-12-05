package com.ryuqq.setof.application.member.component;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.ryuqq.setof.application.member.assembler.MemberAssembler;
import com.ryuqq.setof.application.member.dto.command.KakaoOAuthCommand;
import com.ryuqq.setof.application.member.dto.command.RegisterMemberCommand;
import com.ryuqq.setof.application.member.port.out.PasswordEncoderPort;
import com.ryuqq.setof.domain.core.member.MemberFixture;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MemberCreator")
@ExtendWith(MockitoExtension.class)
class MemberCreatorTest {

    @Mock private PasswordEncoderPort passwordEncoderPort;

    @Mock private MemberAssembler memberAssembler;

    @Mock private Clock clock;

    private MemberCreator memberCreator;

    @BeforeEach
    void setUp() {
        memberCreator = new MemberCreator(passwordEncoderPort, memberAssembler, clock);
    }

    @Nested
    @DisplayName("createLocalMember")
    class CreateLocalMemberTest {

        @Test
        @DisplayName("로컬 회원 생성 성공")
        void shouldCreateLocalMember() {
            // Given
            RegisterMemberCommand command =
                    new RegisterMemberCommand(
                            "01012345678",
                            "test@example.com",
                            "rawPassword123!",
                            "홍길동",
                            LocalDate.of(1990, 1, 1),
                            "M",
                            Collections.emptyList());

            String hashedPassword = "$2a$10$hashedPassword";
            Member expectedMember = MemberFixture.createLocalMember();

            when(passwordEncoderPort.encode(command.rawPassword())).thenReturn(hashedPassword);
            when(memberAssembler.toDomain(command, hashedPassword, clock))
                    .thenReturn(expectedMember);

            // When
            Member result = memberCreator.createLocalMember(command);

            // Then
            assertNotNull(result);
            verify(passwordEncoderPort).encode(command.rawPassword());
            verify(memberAssembler).toDomain(command, hashedPassword, clock);
        }

        @Test
        @DisplayName("비밀번호가 해싱되어 전달됨")
        void shouldHashPasswordBeforeCreatingMember() {
            // Given
            String rawPassword = "plainText123!";
            String hashedPassword = "$2a$10$encryptedHash";
            RegisterMemberCommand command =
                    new RegisterMemberCommand(
                            "01012345678",
                            "test@example.com",
                            rawPassword,
                            "홍길동",
                            LocalDate.of(1990, 1, 1),
                            "M",
                            Collections.emptyList());

            Member expectedMember = MemberFixture.createLocalMember();

            when(passwordEncoderPort.encode(rawPassword)).thenReturn(hashedPassword);
            when(memberAssembler.toDomain(eq(command), eq(hashedPassword), eq(clock)))
                    .thenReturn(expectedMember);

            // When
            memberCreator.createLocalMember(command);

            // Then
            verify(passwordEncoderPort).encode(rawPassword);
            verify(memberAssembler).toDomain(command, hashedPassword, clock);
        }
    }

    @Nested
    @DisplayName("createKakaoMember")
    class CreateKakaoMemberTest {

        @Test
        @DisplayName("카카오 회원 생성 성공")
        void shouldCreateKakaoMember() {
            // Given
            KakaoOAuthCommand command =
                    new KakaoOAuthCommand(
                            "kakao_12345",
                            "01087654321",
                            "kakao@example.com",
                            "카카오사용자",
                            LocalDate.of(1995, 5, 15),
                            "W",
                            Collections.emptyList());

            Member expectedMember = MemberFixture.createKakaoMemberWithSocialId("kakao_12345");

            when(memberAssembler.toKakaoDomain(command, clock)).thenReturn(expectedMember);

            // When
            Member result = memberCreator.createKakaoMember(command);

            // Then
            assertNotNull(result);
            verify(memberAssembler).toKakaoDomain(command, clock);
            verify(passwordEncoderPort, never()).encode(anyString());
        }

        @Test
        @DisplayName("카카오 회원 생성 시 비밀번호 해싱 없음")
        void shouldNotHashPasswordForKakaoMember() {
            // Given
            KakaoOAuthCommand command =
                    new KakaoOAuthCommand(
                            "kakao_67890",
                            "01011112222",
                            null,
                            "테스트",
                            null,
                            null,
                            Collections.emptyList());

            Member expectedMember = MemberFixture.createKakaoMember();

            when(memberAssembler.toKakaoDomain(command, clock)).thenReturn(expectedMember);

            // When
            memberCreator.createKakaoMember(command);

            // Then
            verify(passwordEncoderPort, never()).encode(anyString());
        }
    }
}
