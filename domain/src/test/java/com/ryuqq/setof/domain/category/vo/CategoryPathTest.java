package com.ryuqq.setof.domain.category.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.category.exception.InvalidCategoryPathException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** CategoryPath Value Object 테스트 (Path Enumeration) */
@DisplayName("CategoryPath Value Object")
class CategoryPathTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("최상위 카테고리 경로 생성")
        void shouldCreateRootCategoryPath() {
            // Given
            String validPath = "/1/";

            // When
            CategoryPath categoryPath = CategoryPath.of(validPath);

            // Then
            assertNotNull(categoryPath);
            assertEquals(validPath, categoryPath.value());
        }

        @ParameterizedTest
        @DisplayName("다양한 깊이의 경로 생성")
        @ValueSource(strings = {"/1/", "/1/5/", "/1/5/23/", "/1/5/23/100/"})
        void shouldCreateCategoryPathWithVariousDepths(String validPath) {
            // When
            CategoryPath categoryPath = CategoryPath.of(validPath);

            // Then
            assertNotNull(categoryPath);
            assertEquals(validPath, categoryPath.value());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("null 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenPathIsNull() {
            // Given
            String nullPath = null;

            // When & Then
            assertThrows(InvalidCategoryPathException.class, () -> CategoryPath.of(nullPath));
        }

        @Test
        @DisplayName("빈 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenPathIsEmpty() {
            // Given
            String emptyPath = "";

            // When & Then
            assertThrows(InvalidCategoryPathException.class, () -> CategoryPath.of(emptyPath));
        }

        @Test
        @DisplayName("/로 시작하지 않는 경로로 생성 시 예외 발생")
        void shouldThrowExceptionWhenPathDoesNotStartWithSlash() {
            // Given
            String invalidPath = "1/5/";

            // When & Then
            assertThrows(InvalidCategoryPathException.class, () -> CategoryPath.of(invalidPath));
        }

        @Test
        @DisplayName("/로 끝나지 않는 경로로 생성 시 예외 발생")
        void shouldThrowExceptionWhenPathDoesNotEndWithSlash() {
            // Given
            String invalidPath = "/1/5";

            // When & Then
            assertThrows(InvalidCategoryPathException.class, () -> CategoryPath.of(invalidPath));
        }

        @Test
        @DisplayName("숫자가 아닌 값이 포함된 경로로 생성 시 예외 발생")
        void shouldThrowExceptionWhenPathContainsNonNumeric() {
            // Given
            String invalidPath = "/1/abc/5/";

            // When & Then
            assertThrows(InvalidCategoryPathException.class, () -> CategoryPath.of(invalidPath));
        }

        @Test
        @DisplayName("500자 초과 경로로 생성 시 예외 발생")
        void shouldThrowExceptionWhenPathExceedsMaxLength() {
            // Given
            StringBuilder sb = new StringBuilder("/");
            for (int i = 0; i < 200; i++) {
                sb.append(i).append("/");
            }
            String tooLongPath = sb.toString();

            // When & Then
            assertThrows(InvalidCategoryPathException.class, () -> CategoryPath.of(tooLongPath));
        }
    }

    @Nested
    @DisplayName("extractIds() 메서드")
    class ExtractIdsTest {

        @Test
        @DisplayName("경로에서 ID 목록 추출")
        void shouldExtractIdsFromPath() {
            // Given
            CategoryPath categoryPath = CategoryPath.of("/1/5/23/");

            // When
            List<Long> ids = categoryPath.extractIds();

            // Then
            assertEquals(3, ids.size());
            assertEquals(1L, ids.get(0));
            assertEquals(5L, ids.get(1));
            assertEquals(23L, ids.get(2));
        }

        @Test
        @DisplayName("단일 ID 경로에서 추출")
        void shouldExtractSingleIdFromPath() {
            // Given
            CategoryPath categoryPath = CategoryPath.of("/1/");

            // When
            List<Long> ids = categoryPath.extractIds();

            // Then
            assertEquals(1, ids.size());
            assertEquals(1L, ids.get(0));
        }
    }

    @Nested
    @DisplayName("depth() 메서드")
    class DepthTest {

        @Test
        @DisplayName("최상위 카테고리 깊이는 1")
        void shouldReturnDepthOneForRootCategory() {
            // Given
            CategoryPath categoryPath = CategoryPath.of("/1/");

            // When
            int depth = categoryPath.depth();

            // Then
            assertEquals(1, depth);
        }

        @Test
        @DisplayName("3단계 카테고리 깊이는 3")
        void shouldReturnDepthThreeForThirdLevelCategory() {
            // Given
            CategoryPath categoryPath = CategoryPath.of("/1/5/23/");

            // When
            int depth = categoryPath.depth();

            // Then
            assertEquals(3, depth);
        }
    }

    @Nested
    @DisplayName("contains() 메서드")
    class ContainsTest {

        @Test
        @DisplayName("경로에 포함된 ID는 true 반환")
        void shouldReturnTrueWhenIdExists() {
            // Given
            CategoryPath categoryPath = CategoryPath.of("/1/5/23/");

            // When & Then
            assertTrue(categoryPath.contains(1L));
            assertTrue(categoryPath.contains(5L));
            assertTrue(categoryPath.contains(23L));
        }

        @Test
        @DisplayName("경로에 포함되지 않은 ID는 false 반환")
        void shouldReturnFalseWhenIdNotExists() {
            // Given
            CategoryPath categoryPath = CategoryPath.of("/1/5/23/");

            // When & Then
            assertFalse(categoryPath.contains(99L));
            assertFalse(categoryPath.contains(100L));
        }
    }

    @Nested
    @DisplayName("isChildOf() 메서드")
    class IsChildOfTest {

        @Test
        @DisplayName("하위 경로이면 true 반환")
        void shouldReturnTrueWhenChildOf() {
            // Given
            CategoryPath parentPath = CategoryPath.of("/1/");
            CategoryPath childPath = CategoryPath.of("/1/5/");

            // When & Then
            assertTrue(childPath.isChildOf(parentPath));
        }

        @Test
        @DisplayName("같은 경로이면 false 반환")
        void shouldReturnFalseWhenSamePath() {
            // Given
            CategoryPath path1 = CategoryPath.of("/1/5/");
            CategoryPath path2 = CategoryPath.of("/1/5/");

            // When & Then
            assertFalse(path1.isChildOf(path2));
        }

        @Test
        @DisplayName("하위 경로가 아니면 false 반환")
        void shouldReturnFalseWhenNotChildOf() {
            // Given
            CategoryPath parentPath = CategoryPath.of("/2/");
            CategoryPath childPath = CategoryPath.of("/1/5/");

            // When & Then
            assertFalse(childPath.isChildOf(parentPath));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 CategoryPath는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            CategoryPath path1 = CategoryPath.of("/1/5/");
            CategoryPath path2 = CategoryPath.of("/1/5/");

            // When & Then
            assertEquals(path1, path2);
            assertEquals(path1.hashCode(), path2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 CategoryPath는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            CategoryPath path1 = CategoryPath.of("/1/5/");
            CategoryPath path2 = CategoryPath.of("/1/6/");

            // When & Then
            assertNotEquals(path1, path2);
        }
    }
}
