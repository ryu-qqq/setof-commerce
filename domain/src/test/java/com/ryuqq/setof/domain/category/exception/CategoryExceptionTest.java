package com.ryuqq.setof.domain.category.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("CategoryException 테스트")
class CategoryExceptionTest {

    @Nested
    @DisplayName("기본 생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("ErrorCode로 예외를 생성한다")
        void createWithErrorCode() {
            // when
            CategoryException exception =
                    new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND);

            // then
            assertThat(exception.getMessage()).isEqualTo("카테고리를 찾을 수 없습니다");
            assertThat(exception.code()).isEqualTo("CTG-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지로 예외를 생성한다")
        void createWithErrorCodeAndMessage() {
            // when
            CategoryException exception =
                    new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND, "ID 789 카테고리 없음");

            // then
            assertThat(exception.getMessage()).isEqualTo("ID 789 카테고리 없음");
            assertThat(exception.code()).isEqualTo("CTG-001");
        }

        @Test
        @DisplayName("ErrorCode와 원인 예외로 예외를 생성한다")
        void createWithErrorCodeAndCause() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            CategoryException exception =
                    new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND, cause);

            // then
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception.code()).isEqualTo("CTG-001");
        }
    }

    @Nested
    @DisplayName("구체적 예외 클래스 테스트")
    class ConcreteExceptionTest {

        @Test
        @DisplayName("CategoryNotFoundException 기본 생성")
        void createCategoryNotFoundException() {
            // when
            CategoryNotFoundException exception = new CategoryNotFoundException();

            // then
            assertThat(exception.code()).isEqualTo("CTG-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).isEqualTo("카테고리를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("CategoryNotFoundException ID 포함 생성")
        void createCategoryNotFoundExceptionWithId() {
            // when
            CategoryNotFoundException exception = new CategoryNotFoundException(123L);

            // then
            assertThat(exception.code()).isEqualTo("CTG-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).isEqualTo("ID가 123인 카테고리를 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("CategoryException은 DomainException을 상속한다")
        void categoryExceptionExtendsDomainException() {
            // given
            CategoryException exception =
                    new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND);

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("CategoryNotFoundException은 CategoryException을 상속한다")
        void categoryNotFoundExceptionExtendsCategoryException() {
            // given
            CategoryNotFoundException exception = new CategoryNotFoundException();

            // then
            assertThat(exception).isInstanceOf(CategoryException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }
}
