package com.ryuqq.setof.application.member.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.member.port.out.client.PasswordEncoderClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("PasswordManager 단위 테스트")
class PasswordManagerTest {

    @InjectMocks private PasswordManager sut;

    @Mock private PasswordEncoderClient passwordEncoderClient;

    @Nested
    @DisplayName("encode() - 비밀번호 인코딩")
    class EncodeTest {

        @Test
        @DisplayName("원본 비밀번호를 인코딩하여 반환한다")
        void encode_RawPassword_ReturnsEncodedPassword() {
            // given
            String rawPassword = "password123!";
            String encodedPassword = "$2a$10$hashedPassword";

            given(passwordEncoderClient.encode(rawPassword)).willReturn(encodedPassword);

            // when
            String result = sut.encode(rawPassword);

            // then
            assertThat(result).isEqualTo(encodedPassword);
            then(passwordEncoderClient).should().encode(rawPassword);
        }

        @Test
        @DisplayName("PasswordEncoderClient.encode()가 호출된다")
        void encode_DelegatesToPasswordEncoderClient() {
            // given
            String rawPassword = "mySecret";
            String encodedPassword = "$2a$10$encoded";

            given(passwordEncoderClient.encode(rawPassword)).willReturn(encodedPassword);

            // when
            sut.encode(rawPassword);

            // then
            then(passwordEncoderClient).should().encode(rawPassword);
        }
    }

    @Nested
    @DisplayName("matches() - 비밀번호 일치 여부 확인")
    class MatchesTest {

        @Test
        @DisplayName("원본 비밀번호와 인코딩된 비밀번호가 일치하면 true를 반환한다")
        void matches_CorrectPassword_ReturnsTrue() {
            // given
            String rawPassword = "password123!";
            String encodedPassword = "$2a$10$hashedPassword";

            given(passwordEncoderClient.matches(rawPassword, encodedPassword)).willReturn(true);

            // when
            boolean result = sut.matches(rawPassword, encodedPassword);

            // then
            assertThat(result).isTrue();
            then(passwordEncoderClient).should().matches(rawPassword, encodedPassword);
        }

        @Test
        @DisplayName("원본 비밀번호와 인코딩된 비밀번호가 다르면 false를 반환한다")
        void matches_IncorrectPassword_ReturnsFalse() {
            // given
            String rawPassword = "wrongPassword";
            String encodedPassword = "$2a$10$hashedPassword";

            given(passwordEncoderClient.matches(rawPassword, encodedPassword)).willReturn(false);

            // when
            boolean result = sut.matches(rawPassword, encodedPassword);

            // then
            assertThat(result).isFalse();
            then(passwordEncoderClient).should().matches(rawPassword, encodedPassword);
        }
    }
}
