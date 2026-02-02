package com.ryuqq.setof.domain.category.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("CategoryErrorCode 테스트")
class CategoryErrorCodeTest {

    @Nested
    @DisplayName("ErrorCode 인터페이스 구현 테스트")
    class ErrorCodeInterfaceTest {

        @Test
        @DisplayName("ErrorCode 인터페이스를 구현한다")
        void implementsErrorCode() {
            // then
            assertThat(CategoryErrorCode.CATEGORY_NOT_FOUND).isInstanceOf(ErrorCode.class);
        }
    }

    @Nested
    @DisplayName("CATEGORY_NOT_FOUND 테스트")
    class CategoryNotFoundTest {

        @Test
        @DisplayName("에러 코드는 'CTG-001'이다")
        void hasCorrectCode() {
            assertThat(CategoryErrorCode.CATEGORY_NOT_FOUND.getCode()).isEqualTo("CTG-001");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 404이다")
        void hasCorrectHttpStatus() {
            assertThat(CategoryErrorCode.CATEGORY_NOT_FOUND.getHttpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("메시지는 '카테고리를 찾을 수 없습니다'이다")
        void hasCorrectMessage() {
            assertThat(CategoryErrorCode.CATEGORY_NOT_FOUND.getMessage())
                    .isEqualTo("카테고리를 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("CATEGORY_DUPLICATE 테스트")
    class CategoryDuplicateTest {

        @Test
        @DisplayName("에러 코드는 'CTG-002'이다")
        void hasCorrectCode() {
            assertThat(CategoryErrorCode.CATEGORY_DUPLICATE.getCode()).isEqualTo("CTG-002");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 409이다")
        void hasCorrectHttpStatus() {
            assertThat(CategoryErrorCode.CATEGORY_DUPLICATE.getHttpStatus()).isEqualTo(409);
        }

        @Test
        @DisplayName("메시지는 '동일한 카테고리가 이미 존재합니다'이다")
        void hasCorrectMessage() {
            assertThat(CategoryErrorCode.CATEGORY_DUPLICATE.getMessage())
                    .isEqualTo("동일한 카테고리가 이미 존재합니다");
        }
    }

    @Nested
    @DisplayName("CATEGORY_INACTIVE 테스트")
    class CategoryInactiveTest {

        @Test
        @DisplayName("에러 코드는 'CTG-003'이다")
        void hasCorrectCode() {
            assertThat(CategoryErrorCode.CATEGORY_INACTIVE.getCode()).isEqualTo("CTG-003");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 400이다")
        void hasCorrectHttpStatus() {
            assertThat(CategoryErrorCode.CATEGORY_INACTIVE.getHttpStatus()).isEqualTo(400);
        }

        @Test
        @DisplayName("메시지는 '비활성화된 카테고리입니다'이다")
        void hasCorrectMessage() {
            assertThat(CategoryErrorCode.CATEGORY_INACTIVE.getMessage()).isEqualTo("비활성화된 카테고리입니다");
        }
    }

    @Nested
    @DisplayName("PARENT_CATEGORY_NOT_FOUND 테스트")
    class ParentCategoryNotFoundTest {

        @Test
        @DisplayName("에러 코드는 'CTG-004'이다")
        void hasCorrectCode() {
            assertThat(CategoryErrorCode.PARENT_CATEGORY_NOT_FOUND.getCode()).isEqualTo("CTG-004");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 404이다")
        void hasCorrectHttpStatus() {
            assertThat(CategoryErrorCode.PARENT_CATEGORY_NOT_FOUND.getHttpStatus()).isEqualTo(404);
        }
    }

    @Nested
    @DisplayName("INVALID_CATEGORY_DEPTH 테스트")
    class InvalidCategoryDepthTest {

        @Test
        @DisplayName("에러 코드는 'CTG-005'이다")
        void hasCorrectCode() {
            assertThat(CategoryErrorCode.INVALID_CATEGORY_DEPTH.getCode()).isEqualTo("CTG-005");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 400이다")
        void hasCorrectHttpStatus() {
            assertThat(CategoryErrorCode.INVALID_CATEGORY_DEPTH.getHttpStatus()).isEqualTo(400);
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 에러 코드 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(CategoryErrorCode.values())
                    .containsExactly(
                            CategoryErrorCode.CATEGORY_NOT_FOUND,
                            CategoryErrorCode.CATEGORY_DUPLICATE,
                            CategoryErrorCode.CATEGORY_INACTIVE,
                            CategoryErrorCode.PARENT_CATEGORY_NOT_FOUND,
                            CategoryErrorCode.INVALID_CATEGORY_DEPTH);
        }
    }
}
