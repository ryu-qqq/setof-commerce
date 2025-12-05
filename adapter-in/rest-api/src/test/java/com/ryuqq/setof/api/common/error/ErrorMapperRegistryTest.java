package com.ryuqq.setof.api.common.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.ryuqq.setof.api.common.mapper.ErrorMapper;
import com.ryuqq.setof.api.common.mapper.ErrorMapper.MappedError;
import com.ryuqq.setof.domain.core.exception.DomainException;

@DisplayName("ErrorMapperRegistry 단위 테스트")
class ErrorMapperRegistryTest {

    @Nested
    @DisplayName("map 메서드 테스트")
    class MapTest {

        @Test
        @DisplayName("지원하는 에러 코드를 가진 매퍼가 있으면 매핑된 에러를 반환한다")
        void map_WithSupportingMapper_ShouldReturnMappedError() {
            // given
            ErrorMapper mapper1 = mock(ErrorMapper.class);
            ErrorMapper mapper2 = mock(ErrorMapper.class);

            // 클래스명에서 추출되는 코드: TEST_DOMAIN
            TestDomainException exception = new TestDomainException("테스트 에러");
            MappedError expectedError =
                    new MappedError(
                            HttpStatus.NOT_FOUND,
                            "Test Domain Not Found",
                            "테스트 도메인을 찾을 수 없습니다",
                            URI.create("https://api.test.com/errors/test-domain"));

            when(mapper1.supports("TEST_DOMAIN")).thenReturn(false);
            when(mapper2.supports("TEST_DOMAIN")).thenReturn(true);
            when(mapper2.map(exception, Locale.KOREAN)).thenReturn(expectedError);

            ErrorMapperRegistry registry = new ErrorMapperRegistry(List.of(mapper1, mapper2));

            // when
            Optional<MappedError> result = registry.map(exception, Locale.KOREAN);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().status()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(result.get().title()).isEqualTo("Test Domain Not Found");
            assertThat(result.get().detail()).isEqualTo("테스트 도메인을 찾을 수 없습니다");
        }

        @Test
        @DisplayName("지원하는 매퍼가 없으면 빈 Optional을 반환한다")
        void map_WithNoSupportingMapper_ShouldReturnEmpty() {
            // given
            ErrorMapper mapper = mock(ErrorMapper.class);
            when(mapper.supports("UNKNOWN_DOMAIN")).thenReturn(false);

            UnknownDomainException exception = new UnknownDomainException("알 수 없는 에러");
            ErrorMapperRegistry registry = new ErrorMapperRegistry(List.of(mapper));

            // when
            Optional<MappedError> result = registry.map(exception, Locale.KOREAN);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("빈 매퍼 목록이면 빈 Optional을 반환한다")
        void map_WithEmptyMapperList_ShouldReturnEmpty() {
            // given
            TestDomainException exception = new TestDomainException("에러");
            ErrorMapperRegistry registry = new ErrorMapperRegistry(List.of());

            // when
            Optional<MappedError> result = registry.map(exception, Locale.KOREAN);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("첫 번째 지원하는 매퍼가 사용된다")
        void map_WithMultipleSupportingMappers_ShouldUseFirst() {
            // given
            ErrorMapper mapper1 = mock(ErrorMapper.class);
            ErrorMapper mapper2 = mock(ErrorMapper.class);

            TestDomainException exception = new TestDomainException("에러");
            MappedError error1 =
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Error 1",
                            "첫 번째 매퍼",
                            URI.create("about:blank"));
            MappedError error2 =
                    new MappedError(
                            HttpStatus.NOT_FOUND, "Error 2", "두 번째 매퍼", URI.create("about:blank"));

            when(mapper1.supports("TEST_DOMAIN")).thenReturn(true);
            when(mapper2.supports("TEST_DOMAIN")).thenReturn(true);
            when(mapper1.map(exception, Locale.KOREAN)).thenReturn(error1);
            when(mapper2.map(exception, Locale.KOREAN)).thenReturn(error2);

            ErrorMapperRegistry registry = new ErrorMapperRegistry(List.of(mapper1, mapper2));

            // when
            Optional<MappedError> result = registry.map(exception, Locale.KOREAN);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().detail()).isEqualTo("첫 번째 매퍼");
        }
    }

    @Nested
    @DisplayName("defaultMapping 메서드 테스트")
    class DefaultMappingTest {

        @Test
        @DisplayName("기본 매핑은 BAD_REQUEST 상태를 반환한다")
        void defaultMapping_ShouldReturnBadRequest() {
            // given
            TestDomainException exception = new TestDomainException("에러 메시지");
            ErrorMapperRegistry registry = new ErrorMapperRegistry(List.of());

            // when
            MappedError result = registry.defaultMapping(exception);

            // then
            assertThat(result.status()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(result.title()).isEqualTo("Bad Request");
            assertThat(result.detail()).isEqualTo("에러 메시지");
            assertThat(result.type()).isEqualTo(URI.create("about:blank"));
        }

        @Test
        @DisplayName("예외 메시지가 null이면 기본 메시지를 사용한다")
        void defaultMapping_WithNullMessage_ShouldUseDefaultMessage() {
            // given
            NullMessageException exception = new NullMessageException();
            ErrorMapperRegistry registry = new ErrorMapperRegistry(List.of());

            // when
            MappedError result = registry.defaultMapping(exception);

            // then
            assertThat(result.detail()).isEqualTo("Invalid request");
        }
    }

    @Nested
    @DisplayName("extractErrorCode 메서드 테스트")
    class ExtractErrorCodeTest {

        @Test
        @DisplayName("클래스명에서 Exception 접미사를 제거하고 UPPER_SNAKE_CASE로 변환한다")
        void extractErrorCode_ShouldConvertToUpperSnakeCase() {
            // given
            ErrorMapperRegistry registry = new ErrorMapperRegistry(List.of());
            TestDomainException exception = new TestDomainException("테스트");

            // when
            String code = registry.extractErrorCode(exception);

            // then
            assertThat(code).isEqualTo("TEST_DOMAIN");
        }

        @Test
        @DisplayName("MemberNotFoundException은 MEMBER_NOT_FOUND로 변환된다")
        void extractErrorCode_WithMemberNotFound_ShouldConvertCorrectly() {
            // given
            ErrorMapperRegistry registry = new ErrorMapperRegistry(List.of());
            MemberNotFoundException exception = new MemberNotFoundException("회원을 찾을 수 없습니다");

            // when
            String code = registry.extractErrorCode(exception);

            // then
            assertThat(code).isEqualTo("MEMBER_NOT_FOUND");
        }

        @Test
        @DisplayName("InvalidEmailException은 INVALID_EMAIL로 변환된다")
        void extractErrorCode_WithInvalidEmail_ShouldConvertCorrectly() {
            // given
            ErrorMapperRegistry registry = new ErrorMapperRegistry(List.of());
            InvalidEmailException exception = new InvalidEmailException("잘못된 이메일 형식");

            // when
            String code = registry.extractErrorCode(exception);

            // then
            assertThat(code).isEqualTo("INVALID_EMAIL");
        }
    }

    @Nested
    @DisplayName("Locale 테스트")
    class LocaleTest {

        @Test
        @DisplayName("영어 로케일로 매핑할 수 있다")
        void map_WithEnglishLocale_ShouldWork() {
            // given
            ErrorMapper mapper = mock(ErrorMapper.class);
            TestDomainException exception = new TestDomainException("error");
            MappedError englishError =
                    new MappedError(
                            HttpStatus.BAD_REQUEST,
                            "Error",
                            "English message",
                            URI.create("about:blank"));

            when(mapper.supports("TEST_DOMAIN")).thenReturn(true);
            when(mapper.map(exception, Locale.ENGLISH)).thenReturn(englishError);

            ErrorMapperRegistry registry = new ErrorMapperRegistry(List.of(mapper));

            // when
            Optional<MappedError> result = registry.map(exception, Locale.ENGLISH);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().detail()).isEqualTo("English message");
        }
    }

    // ==================== Test Helper Classes ====================

    /**
     * 테스트용 예외 - TEST_DOMAIN 코드로 추출됨
     */
    private static class TestDomainException extends DomainException {
        TestDomainException(String message) {
            super(message);
        }
    }

    /**
     * 테스트용 예외 - UNKNOWN_DOMAIN 코드로 추출됨
     */
    private static class UnknownDomainException extends DomainException {
        UnknownDomainException(String message) {
            super(message);
        }
    }

    /**
     * 테스트용 예외 - null 메시지 테스트
     */
    private static class NullMessageException extends DomainException {
        NullMessageException() {
            super(null);
        }
    }

    /**
     * 테스트용 예외 - MEMBER_NOT_FOUND 코드로 추출됨
     */
    private static class MemberNotFoundException extends DomainException {
        MemberNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * 테스트용 예외 - INVALID_EMAIL 코드로 추출됨
     */
    private static class InvalidEmailException extends DomainException {
        InvalidEmailException(String message) {
            super(message);
        }
    }
}
