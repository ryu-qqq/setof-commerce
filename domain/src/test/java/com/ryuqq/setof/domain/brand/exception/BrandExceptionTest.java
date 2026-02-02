package com.ryuqq.setof.domain.brand.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("BrandException 테스트")
class BrandExceptionTest {

    @Nested
    @DisplayName("기본 생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("ErrorCode로 예외를 생성한다")
        void createWithErrorCode() {
            // when
            BrandException exception = new BrandException(BrandErrorCode.BRAND_NOT_FOUND);

            // then
            assertThat(exception.getMessage()).isEqualTo("브랜드를 찾을 수 없습니다");
            assertThat(exception.code()).isEqualTo("BRD-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지로 예외를 생성한다")
        void createWithErrorCodeAndMessage() {
            // when
            BrandException exception =
                    new BrandException(BrandErrorCode.BRAND_NOT_FOUND, "ID 789 브랜드 없음");

            // then
            assertThat(exception.getMessage()).isEqualTo("ID 789 브랜드 없음");
            assertThat(exception.code()).isEqualTo("BRD-001");
        }

        @Test
        @DisplayName("ErrorCode와 원인 예외로 예외를 생성한다")
        void createWithErrorCodeAndCause() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            BrandException exception = new BrandException(BrandErrorCode.BRAND_NOT_FOUND, cause);

            // then
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception.code()).isEqualTo("BRD-001");
        }
    }

    @Nested
    @DisplayName("구체적 예외 클래스 테스트")
    class ConcreteExceptionTest {

        @Test
        @DisplayName("BrandNotFoundException 기본 생성")
        void createBrandNotFoundException() {
            // when
            BrandNotFoundException exception = new BrandNotFoundException();

            // then
            assertThat(exception.code()).isEqualTo("BRD-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).isEqualTo("브랜드를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("BrandNotFoundException ID 포함 생성")
        void createBrandNotFoundExceptionWithId() {
            // when
            BrandNotFoundException exception = new BrandNotFoundException(123L);

            // then
            assertThat(exception.code()).isEqualTo("BRD-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).isEqualTo("ID가 123인 브랜드를 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("BrandException은 DomainException을 상속한다")
        void brandExceptionExtendsDomainException() {
            // given
            BrandException exception = new BrandException(BrandErrorCode.BRAND_NOT_FOUND);

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("BrandNotFoundException은 BrandException을 상속한다")
        void brandNotFoundExceptionExtendsBrandException() {
            // given
            BrandNotFoundException exception = new BrandNotFoundException();

            // then
            assertThat(exception).isInstanceOf(BrandException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }
}
