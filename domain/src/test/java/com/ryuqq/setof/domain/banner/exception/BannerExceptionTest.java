package com.ryuqq.setof.domain.banner.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("BannerException 테스트")
class BannerExceptionTest {

    @Nested
    @DisplayName("기본 생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("ErrorCode로 예외를 생성한다")
        void createWithErrorCode() {
            // when
            BannerException exception = new BannerException(BannerErrorCode.BANNER_GROUP_NOT_FOUND);

            // then
            assertThat(exception.getMessage()).isEqualTo("배너 그룹을 찾을 수 없습니다");
            assertThat(exception.code()).isEqualTo("BNR-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지로 예외를 생성한다")
        void createWithErrorCodeAndMessage() {
            // when
            BannerException exception =
                    new BannerException(BannerErrorCode.BANNER_GROUP_NOT_FOUND, "ID 100 배너 그룹 없음");

            // then
            assertThat(exception.getMessage()).isEqualTo("ID 100 배너 그룹 없음");
            assertThat(exception.code()).isEqualTo("BNR-001");
        }

        @Test
        @DisplayName("ErrorCode와 원인 예외로 예외를 생성한다")
        void createWithErrorCodeAndCause() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            BannerException exception =
                    new BannerException(BannerErrorCode.BANNER_GROUP_NOT_FOUND, cause);

            // then
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception.code()).isEqualTo("BNR-001");
        }
    }

    @Nested
    @DisplayName("구체적 예외 클래스 테스트")
    class ConcreteExceptionTest {

        @Test
        @DisplayName("BannerGroupNotFoundException 기본 생성")
        void createBannerGroupNotFoundException() {
            // when
            BannerGroupNotFoundException exception = new BannerGroupNotFoundException();

            // then
            assertThat(exception.code()).isEqualTo("BNR-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).isEqualTo("배너 그룹을 찾을 수 없습니다");
        }

        @Test
        @DisplayName("BannerGroupNotFoundException 상세 메시지 포함 생성")
        void createBannerGroupNotFoundExceptionWithDetail() {
            // when
            BannerGroupNotFoundException exception = new BannerGroupNotFoundException(" (ID: 999)");

            // then
            assertThat(exception.code()).isEqualTo("BNR-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).contains("해당 배너가 존재하지 않습니다");
            assertThat(exception.getMessage()).contains("(ID: 999)");
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("BannerException은 DomainException을 상속한다")
        void bannerExceptionExtendsDomainException() {
            // given
            BannerException exception = new BannerException(BannerErrorCode.BANNER_GROUP_NOT_FOUND);

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("BannerGroupNotFoundException은 BannerException을 상속한다")
        void bannerGroupNotFoundExceptionExtendsBannerException() {
            // given
            BannerGroupNotFoundException exception = new BannerGroupNotFoundException();

            // then
            assertThat(exception).isInstanceOf(BannerException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }
}
