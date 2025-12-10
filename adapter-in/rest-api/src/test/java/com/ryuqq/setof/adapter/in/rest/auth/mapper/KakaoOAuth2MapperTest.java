package com.ryuqq.setof.adapter.in.rest.auth.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.member.dto.command.KakaoOAuthCommand;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.user.OAuth2User;

@DisplayName("KakaoOAuth2ApiMapper")
class KakaoOAuth2ApiMapperTest {

    private KakaoOAuth2ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new KakaoOAuth2ApiMapper();
    }

    @Nested
    @DisplayName("toKakaoOAuthCommand")
    class ToKakaoOAuthCommandTest {

        @Test
        @DisplayName("모든 정보가 있을 때 KakaoOAuthCommand 생성 성공")
        void shouldCreateCommandWithAllInfo() {
            // Given
            OAuth2User oAuth2User = mock(OAuth2User.class);
            Map<String, Object> attributes = createFullAttributes();
            when(oAuth2User.getAttributes()).thenReturn(attributes);

            // When
            KakaoOAuthCommand result = mapper.toKakaoOAuthCommand(oAuth2User, false);

            // Then
            assertNotNull(result);
            assertEquals("123456789", result.kakaoId());
            assertEquals("hong@test.com", result.email());
            assertEquals("홍길동", result.name());
            assertEquals("male", result.gender());
            assertFalse(result.integration());
        }

        @Test
        @DisplayName("integration=true일 때 KakaoOAuthCommand 생성 성공")
        void shouldCreateCommandWithIntegrationTrue() {
            // Given
            OAuth2User oAuth2User = mock(OAuth2User.class);
            Map<String, Object> attributes = createFullAttributes();
            when(oAuth2User.getAttributes()).thenReturn(attributes);

            // When
            KakaoOAuthCommand result = mapper.toKakaoOAuthCommand(oAuth2User, true);

            // Then
            assertNotNull(result);
            assertTrue(result.integration());
        }

        @Test
        @DisplayName("최소 정보만 있을 때 KakaoOAuthCommand 생성 성공")
        void shouldCreateCommandWithMinimalInfo() {
            // Given
            OAuth2User oAuth2User = mock(OAuth2User.class);
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("id", 123456789L);
            attributes.put("kakao_account", new HashMap<>());
            when(oAuth2User.getAttributes()).thenReturn(attributes);

            // When
            KakaoOAuthCommand result = mapper.toKakaoOAuthCommand(oAuth2User, false);

            // Then
            assertNotNull(result);
            assertEquals("123456789", result.kakaoId());
            assertNull(result.email());
            assertNull(result.name());
        }

        private Map<String, Object> createFullAttributes() {
            Map<String, Object> profile = new HashMap<>();
            profile.put("nickname", "길동이");

            Map<String, Object> kakaoAccount = new HashMap<>();
            kakaoAccount.put("profile", profile);
            kakaoAccount.put("name", "홍길동");
            kakaoAccount.put("email", "hong@test.com");
            kakaoAccount.put("gender", "male");
            kakaoAccount.put("birthday", "0115");
            kakaoAccount.put("birthyear", "1990");
            kakaoAccount.put("phone_number", "+82 10-1234-5678");

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("id", 123456789L);
            attributes.put("kakao_account", kakaoAccount);
            return attributes;
        }
    }

    @Nested
    @DisplayName("extractKakaoId")
    class ExtractKakaoIdTest {

        @Test
        @DisplayName("ID가 있으면 문자열로 반환")
        void shouldReturnIdAsString() {
            // Given
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("id", 123456789L);

            // When
            String result = mapper.extractKakaoId(attributes);

            // Then
            assertEquals("123456789", result);
        }

        @Test
        @DisplayName("ID가 없으면 null 반환")
        void shouldReturnNullWhenIdNotFound() {
            // Given
            Map<String, Object> attributes = new HashMap<>();

            // When
            String result = mapper.extractKakaoId(attributes);

            // Then
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("extractEmail")
    class ExtractEmailTest {

        @Test
        @DisplayName("이메일이 있으면 반환")
        void shouldReturnEmail() {
            // Given
            Map<String, Object> kakaoAccount = new HashMap<>();
            kakaoAccount.put("email", "test@example.com");

            // When
            String result = mapper.extractEmail(kakaoAccount);

            // Then
            assertEquals("test@example.com", result);
        }

        @Test
        @DisplayName("이메일이 없으면 null 반환")
        void shouldReturnNullWhenEmailNotFound() {
            // Given
            Map<String, Object> kakaoAccount = new HashMap<>();

            // When
            String result = mapper.extractEmail(kakaoAccount);

            // Then
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("extractName")
    class ExtractNameTest {

        @Test
        @DisplayName("실명이 있으면 실명 반환")
        void shouldReturnRealName() {
            // Given
            Map<String, Object> profile = new HashMap<>();
            profile.put("nickname", "길동이");

            Map<String, Object> kakaoAccount = new HashMap<>();
            kakaoAccount.put("profile", profile);
            kakaoAccount.put("name", "홍길동");

            // When
            String result = mapper.extractName(kakaoAccount);

            // Then
            assertEquals("홍길동", result);
        }

        @Test
        @DisplayName("실명이 없으면 닉네임 반환")
        void shouldReturnNicknameWhenNameNotFound() {
            // Given
            Map<String, Object> profile = new HashMap<>();
            profile.put("nickname", "길동이");

            Map<String, Object> kakaoAccount = new HashMap<>();
            kakaoAccount.put("profile", profile);

            // When
            String result = mapper.extractName(kakaoAccount);

            // Then
            assertEquals("길동이", result);
        }

        @Test
        @DisplayName("실명이 빈 문자열이면 닉네임 반환")
        void shouldReturnNicknameWhenNameIsBlank() {
            // Given
            Map<String, Object> profile = new HashMap<>();
            profile.put("nickname", "길동이");

            Map<String, Object> kakaoAccount = new HashMap<>();
            kakaoAccount.put("profile", profile);
            kakaoAccount.put("name", "   ");

            // When
            String result = mapper.extractName(kakaoAccount);

            // Then
            assertEquals("길동이", result);
        }
    }

    @Nested
    @DisplayName("extractPhoneNumber")
    class ExtractPhoneNumberTest {

        @Test
        @DisplayName("핸드폰 번호 정규화 - +82 형식")
        void shouldNormalizePhoneNumber() {
            // Given
            Map<String, Object> kakaoAccount = new HashMap<>();
            kakaoAccount.put("phone_number", "+82 10-1234-5678");

            // When
            String result = mapper.extractPhoneNumber(kakaoAccount);

            // Then
            assertEquals("01012345678", result);
        }

        @Test
        @DisplayName("핸드폰 번호가 없으면 null 반환")
        void shouldReturnNullWhenPhoneNumberNotFound() {
            // Given
            Map<String, Object> kakaoAccount = new HashMap<>();

            // When
            String result = mapper.extractPhoneNumber(kakaoAccount);

            // Then
            assertNull(result);
        }

        @Test
        @DisplayName("핸드폰 번호가 빈 문자열이면 null 반환")
        void shouldReturnNullWhenPhoneNumberIsBlank() {
            // Given
            Map<String, Object> kakaoAccount = new HashMap<>();
            kakaoAccount.put("phone_number", "   ");

            // When
            String result = mapper.extractPhoneNumber(kakaoAccount);

            // Then
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("extractBirthdayAsLocalDate")
    class ExtractBirthdayAsLocalDateTest {

        @Test
        @DisplayName("생년월일이 있으면 LocalDate 반환")
        void shouldReturnLocalDate() {
            // Given
            Map<String, Object> kakaoAccount = new HashMap<>();
            kakaoAccount.put("birthyear", "1990");
            kakaoAccount.put("birthday", "0115");

            // When
            LocalDate result = mapper.extractBirthdayAsLocalDate(kakaoAccount);

            // Then
            assertNotNull(result);
            assertEquals(LocalDate.of(1990, 1, 15), result);
        }

        @Test
        @DisplayName("생년이 없으면 null 반환")
        void shouldReturnNullWhenBirthyearNotFound() {
            // Given
            Map<String, Object> kakaoAccount = new HashMap<>();
            kakaoAccount.put("birthday", "0115");

            // When
            LocalDate result = mapper.extractBirthdayAsLocalDate(kakaoAccount);

            // Then
            assertNull(result);
        }

        @Test
        @DisplayName("생일이 없으면 null 반환")
        void shouldReturnNullWhenBirthdayNotFound() {
            // Given
            Map<String, Object> kakaoAccount = new HashMap<>();
            kakaoAccount.put("birthyear", "1990");

            // When
            LocalDate result = mapper.extractBirthdayAsLocalDate(kakaoAccount);

            // Then
            assertNull(result);
        }

        @Test
        @DisplayName("잘못된 형식이면 null 반환")
        void shouldReturnNullWhenInvalidFormat() {
            // Given
            Map<String, Object> kakaoAccount = new HashMap<>();
            kakaoAccount.put("birthyear", "invalid");
            kakaoAccount.put("birthday", "0115");

            // When
            LocalDate result = mapper.extractBirthdayAsLocalDate(kakaoAccount);

            // Then
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("extractGender")
    class ExtractGenderTest {

        @Test
        @DisplayName("성별이 있으면 반환")
        void shouldReturnGender() {
            // Given
            Map<String, Object> kakaoAccount = new HashMap<>();
            kakaoAccount.put("gender", "female");

            // When
            String result = mapper.extractGender(kakaoAccount);

            // Then
            assertEquals("female", result);
        }
    }

    @Nested
    @DisplayName("getKakaoAccount")
    class GetKakaoAccountTest {

        @Test
        @DisplayName("kakao_account가 있으면 반환")
        void shouldReturnKakaoAccount() {
            // Given
            Map<String, Object> kakaoAccount = new HashMap<>();
            kakaoAccount.put("email", "test@test.com");

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("kakao_account", kakaoAccount);

            // When
            Map<String, Object> result = mapper.getKakaoAccount(attributes);

            // Then
            assertNotNull(result);
            assertEquals("test@test.com", result.get("email"));
        }

        @Test
        @DisplayName("kakao_account가 없으면 빈 Map 반환")
        void shouldReturnEmptyMapWhenNotFound() {
            // Given
            Map<String, Object> attributes = new HashMap<>();

            // When
            Map<String, Object> result = mapper.getKakaoAccount(attributes);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("kakao_account가 Map이 아니면 빈 Map 반환")
        void shouldReturnEmptyMapWhenNotMap() {
            // Given
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("kakao_account", "invalid");

            // When
            Map<String, Object> result = mapper.getKakaoAccount(attributes);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}
