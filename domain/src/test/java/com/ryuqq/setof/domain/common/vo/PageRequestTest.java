package com.ryuqq.setof.domain.common.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("PageRequest")
class PageRequestTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("정상 값으로 생성")
        void shouldCreateWithValidValues() {
            // When
            PageRequest request = PageRequest.of(2, 30);

            // Then
            assertEquals(2, request.page());
            assertEquals(30, request.size());
        }

        @Test
        @DisplayName("음수 페이지는 0으로 정규화")
        void shouldNormalizeNegativePage() {
            // When
            PageRequest request = PageRequest.of(-1, 20);

            // Then
            assertEquals(0, request.page());
        }

        @Test
        @DisplayName("0 이하 size는 기본값으로 정규화")
        void shouldNormalizeZeroSize() {
            // When
            PageRequest request = PageRequest.of(0, 0);

            // Then
            assertEquals(PageRequest.DEFAULT_SIZE, request.size());
        }

        @Test
        @DisplayName("음수 size는 기본값으로 정규화")
        void shouldNormalizeNegativeSize() {
            // When
            PageRequest request = PageRequest.of(0, -10);

            // Then
            assertEquals(PageRequest.DEFAULT_SIZE, request.size());
        }

        @Test
        @DisplayName("MAX_SIZE 초과 size는 MAX_SIZE로 정규화")
        void shouldNormalizeSizeExceedingMax() {
            // When
            PageRequest request = PageRequest.of(0, 200);

            // Then
            assertEquals(PageRequest.MAX_SIZE, request.size());
        }
    }

    @Nested
    @DisplayName("팩토리 메서드 테스트")
    class FactoryMethodTest {

        @Test
        @DisplayName("first는 page=0으로 생성")
        void shouldCreateFirstPage() {
            // When
            PageRequest request = PageRequest.first(25);

            // Then
            assertEquals(0, request.page());
            assertEquals(25, request.size());
        }

        @Test
        @DisplayName("defaultPage는 page=0, size=DEFAULT_SIZE로 생성")
        void shouldCreateDefaultPage() {
            // When
            PageRequest request = PageRequest.defaultPage();

            // Then
            assertEquals(0, request.page());
            assertEquals(PageRequest.DEFAULT_SIZE, request.size());
        }
    }

    @Nested
    @DisplayName("offset 계산 테스트")
    class OffsetTest {

        @Test
        @DisplayName("page=0이면 offset=0")
        void shouldReturnZeroOffsetForFirstPage() {
            // When
            PageRequest request = PageRequest.of(0, 20);

            // Then
            assertEquals(0L, request.offset());
        }

        @Test
        @DisplayName("page=2, size=20이면 offset=40")
        void shouldCalculateOffsetCorrectly() {
            // When
            PageRequest request = PageRequest.of(2, 20);

            // Then
            assertEquals(40L, request.offset());
        }

        @Test
        @DisplayName("큰 페이지 번호에서도 정확히 계산")
        void shouldCalculateOffsetForLargePage() {
            // When
            PageRequest request = PageRequest.of(1000, 100);

            // Then
            assertEquals(100000L, request.offset());
        }
    }

    @Nested
    @DisplayName("페이지 네비게이션 테스트")
    class NavigationTest {

        @Test
        @DisplayName("next는 다음 페이지 반환")
        void shouldReturnNextPage() {
            // Given
            PageRequest request = PageRequest.of(3, 20);

            // When
            PageRequest next = request.next();

            // Then
            assertEquals(4, next.page());
            assertEquals(20, next.size());
        }

        @Test
        @DisplayName("previous는 이전 페이지 반환")
        void shouldReturnPreviousPage() {
            // Given
            PageRequest request = PageRequest.of(3, 20);

            // When
            PageRequest previous = request.previous();

            // Then
            assertEquals(2, previous.page());
            assertEquals(20, previous.size());
        }

        @Test
        @DisplayName("첫 페이지에서 previous는 자기 자신 반환")
        void shouldReturnSameWhenPreviousOnFirstPage() {
            // Given
            PageRequest request = PageRequest.of(0, 20);

            // When
            PageRequest previous = request.previous();

            // Then
            assertSame(request, previous);
        }
    }

    @Nested
    @DisplayName("isFirst 테스트")
    class IsFirstTest {

        @Test
        @DisplayName("page=0이면 true")
        void shouldReturnTrueForFirstPage() {
            // When
            PageRequest request = PageRequest.of(0, 20);

            // Then
            assertTrue(request.isFirst());
        }

        @Test
        @DisplayName("page>0이면 false")
        void shouldReturnFalseForNonFirstPage() {
            // When
            PageRequest request = PageRequest.of(1, 20);

            // Then
            assertFalse(request.isFirst());
        }
    }

    @Nested
    @DisplayName("totalPages 계산 테스트")
    class TotalPagesTest {

        @Test
        @DisplayName("전체 항목이 size와 같으면 1페이지")
        void shouldReturnOnePageForExactSize() {
            // Given
            PageRequest request = PageRequest.of(0, 20);

            // Then
            assertEquals(1, request.totalPages(20));
        }

        @Test
        @DisplayName("전체 항목이 size보다 작으면 1페이지")
        void shouldReturnOnePageForLessThanSize() {
            // Given
            PageRequest request = PageRequest.of(0, 20);

            // Then
            assertEquals(1, request.totalPages(10));
        }

        @Test
        @DisplayName("전체 항목이 size보다 크면 올림 처리")
        void shouldCeilTotalPages() {
            // Given
            PageRequest request = PageRequest.of(0, 20);

            // Then
            assertEquals(3, request.totalPages(41));
        }

        @Test
        @DisplayName("전체 항목이 0이면 0페이지")
        void shouldReturnZeroPagesForZeroElements() {
            // Given
            PageRequest request = PageRequest.of(0, 20);

            // Then
            assertEquals(0, request.totalPages(0));
        }
    }

    @Nested
    @DisplayName("isLast 테스트")
    class IsLastTest {

        @Test
        @DisplayName("마지막 페이지면 true")
        void shouldReturnTrueForLastPage() {
            // Given
            PageRequest request = PageRequest.of(2, 20);

            // Then - 50 items = 3 pages (0,1,2), page 2 is last
            assertTrue(request.isLast(50));
        }

        @Test
        @DisplayName("마지막 페이지가 아니면 false")
        void shouldReturnFalseForNonLastPage() {
            // Given
            PageRequest request = PageRequest.of(1, 20);

            // Then
            assertFalse(request.isLast(50));
        }

        @Test
        @DisplayName("현재 페이지가 totalPages를 초과해도 true")
        void shouldReturnTrueWhenPageExceedsTotalPages() {
            // Given
            PageRequest request = PageRequest.of(10, 20);

            // Then - 50 items = 3 pages, page 10 > 2
            assertTrue(request.isLast(50));
        }
    }

    @Nested
    @DisplayName("상수 테스트")
    class ConstantsTest {

        @Test
        @DisplayName("DEFAULT_SIZE는 20")
        void shouldHaveDefaultSize20() {
            assertEquals(20, PageRequest.DEFAULT_SIZE);
        }

        @Test
        @DisplayName("MAX_SIZE는 100")
        void shouldHaveMaxSize100() {
            assertEquals(100, PageRequest.MAX_SIZE);
        }
    }
}
