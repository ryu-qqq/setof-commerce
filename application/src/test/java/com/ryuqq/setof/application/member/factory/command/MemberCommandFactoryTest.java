package com.ryuqq.setof.application.member.factory.command;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.member.assembler.MemberAssembler;
import com.ryuqq.setof.application.member.dto.command.KakaoOAuthCommand;
import com.ryuqq.setof.application.member.dto.command.RegisterMemberCommand;
import com.ryuqq.setof.application.member.port.out.client.PasswordEncoderPort;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.member.MemberFixture;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.vo.MemberId;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MemberCommandFactory")
@ExtendWith(MockitoExtension.class)
class MemberCommandFactoryTest {

    @Mock private PasswordEncoderPort passwordEncoderPort;

    @Mock private MemberAssembler memberAssembler;

    @Mock private ClockHolder clockHolder;

    private Clock fixedClock;
    private MemberCommandFactory memberCommandFactory;

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.of("UTC"));
        when(clockHolder.getClock()).thenReturn(fixedClock);
        memberCommandFactory =
                new MemberCommandFactory(passwordEncoderPort, memberAssembler, clockHolder);
    }

    @Nested
    @DisplayName("create (RegisterMemberCommand)")
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
            when(memberAssembler.toDomain(
                            any(MemberId.class), eq(command), eq(hashedPassword), eq(fixedClock)))
                    .thenReturn(expectedMember);

            // When
            Member result = memberCommandFactory.create(command);

            // Then
            assertNotNull(result);
            verify(passwordEncoderPort).encode(command.rawPassword());
            verify(memberAssembler)
                    .toDomain(any(MemberId.class), eq(command), eq(hashedPassword), eq(fixedClock));
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
            when(memberAssembler.toDomain(
                            any(MemberId.class), eq(command), eq(hashedPassword), eq(fixedClock)))
                    .thenReturn(expectedMember);

            // When
            memberCommandFactory.create(command);

            // Then
            verify(passwordEncoderPort).encode(rawPassword);
            verify(memberAssembler)
                    .toDomain(any(MemberId.class), eq(command), eq(hashedPassword), eq(fixedClock));
        }
    }

    @Nested
    @DisplayName("create (KakaoOAuthCommand)")
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
                            Collections.emptyList(),
                            false);

            Member expectedMember = MemberFixture.createKakaoMemberWithSocialId("kakao_12345");

            when(memberAssembler.toKakaoDomain(any(MemberId.class), eq(command), eq(fixedClock)))
                    .thenReturn(expectedMember);

            // When
            Member result = memberCommandFactory.create(command);

            // Then
            assertNotNull(result);
            verify(memberAssembler).toKakaoDomain(any(MemberId.class), eq(command), eq(fixedClock));
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
                            Collections.emptyList(),
                            false);

            Member expectedMember = MemberFixture.createKakaoMember();

            when(memberAssembler.toKakaoDomain(any(MemberId.class), eq(command), eq(fixedClock)))
                    .thenReturn(expectedMember);

            // When
            memberCommandFactory.create(command);

            // Then
            verify(passwordEncoderPort, never()).encode(anyString());
        }
    }
}
