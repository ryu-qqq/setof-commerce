package com.ryuqq.setof.domain.common.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("PageRequest Value Object 테스트")
class PageRequestTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 PageRequest를 생성한다")
        void createWithOf() {
            // when
            PageRequest pageRequest = PageRequest.of(2, 30);

            // then
            assertThat(pageRequest.page()).isEqualTo(2);
            assertThat(pageRequest.size()).isEqualTo(30);
        }

        @Test
        @DisplayName("first()로 첫 페이지 요청을 생성한다")
        void createFirstPage() {
            // when
            PageRequest pageRequest = PageRequest.first(25);

            // then
            assertThat(pageRequest.page()).isZero();
            assertThat(pageRequest.size()).isEqualTo(25);
        }

        @Test
        @DisplayName("defaultPage()로 기본 설정 요청을 생성한다")
        void createDefaultPage() {
            // when
            PageRequest pageRequest = PageRequest.defaultPage();

            // then
            assertThat(pageRequest.page()).isZero();
            assertThat(pageRequest.size()).isEqualTo(PageRequest.DEFAULT_SIZE);
        }
    }

    @Nested
    @DisplayName("유효성 검증 테스트")
    class ValidationTest {

        @Test
        @DisplayName("음수 페이지는 0으로 정규화된다")
        void negativePageNormalizesToZero() {
            // when
            PageRequest pageRequest = PageRequest.of(-5, 20);

            // then
            assertThat(pageRequest.page()).isZero();
        }

        @Test
        @DisplayName("0 이하 size는 기본값으로 정규화된다")
        void zeroSizeNormalizesToDefault() {
            // when
            PageRequest pageRequest = PageRequest.of(0, 0);

            // then
            assertThat(pageRequest.size()).isEqualTo(PageRequest.DEFAULT_SIZE);
        }

        @Test
        @DisplayName("음수 size는 기본값으로 정규화된다")
        void negativeSizeNormalizesToDefault() {
            // when
            PageRequest pageRequest = PageRequest.of(0, -10);

            // then
            assertThat(pageRequest.size()).isEqualTo(PageRequest.DEFAULT_SIZE);
        }

        @Test
        @DisplayName("MAX_SIZE 초과 size는 MAX_SIZE로 정규화된다")
        void exceedingMaxSizeNormalizesToMax() {
            // when
            PageRequest pageRequest = PageRequest.of(0, 200);

            // then
            assertThat(pageRequest.size()).isEqualTo(PageRequest.MAX_SIZE);
        }
    }

    @Nested
    @DisplayName("오프셋 계산 테스트")
    class OffsetTest {

        @Test
        @DisplayName("오프셋을 올바르게 계산한다")
        void calculatesOffset() {
            // given
            PageRequest pageRequest = PageRequest.of(3, 20);

            // when
            long offset = pageRequest.offset();

            // then
            assertThat(offset).isEqualTo(60L);
        }

        @Test
        @DisplayName("첫 페이지 오프셋은 0이다")
        void firstPageOffsetIsZero() {
            // given
            PageRequest pageRequest = PageRequest.first(20);

            // when
            long offset = pageRequest.offset();

            // then
            assertThat(offset).isZero();
        }
    }

    @Nested
    @DisplayName("네비게이션 테스트")
    class NavigationTest {

        @Test
        @DisplayName("next()로 다음 페이지 요청을 생성한다")
        void createsNextPage() {
            // given
            PageRequest current = PageRequest.of(2, 20);

            // when
            PageRequest next = current.next();

            // then
            assertThat(next.page()).isEqualTo(3);
            assertThat(next.size()).isEqualTo(20);
        }

        @Test
        @DisplayName("previous()로 이전 페이지 요청을 생성한다")
        void createsPreviousPage() {
            // given
            PageRequest current = PageRequest.of(3, 20);

            // when
            PageRequest previous = current.previous();

            // then
            assertThat(previous.page()).isEqualTo(2);
            assertThat(previous.size()).isEqualTo(20);
        }

        @Test
        @DisplayName("첫 페이지에서 previous()는 그대로 반환한다")
        void previousOnFirstPageReturnsSame() {
            // given
            PageRequest firstPage = PageRequest.first(20);

            // when
            PageRequest previous = firstPage.previous();

            // then
            assertThat(previous).isSameAs(firstPage);
        }
    }

    @Nested
    @DisplayName("상태 확인 테스트")
    class StateCheckTest {

        @Test
        @DisplayName("isFirst()는 첫 페이지에서 true를 반환한다")
        void isFirstReturnsTrueForFirstPage() {
            // given
            PageRequest firstPage = PageRequest.first(20);

            // then
            assertThat(firstPage.isFirst()).isTrue();
        }

        @Test
        @DisplayName("isFirst()는 첫 페이지가 아니면 false를 반환한다")
        void isFirstReturnsFalseForNonFirstPage() {
            // given
            PageRequest page = PageRequest.of(2, 20);

            // then
            assertThat(page.isFirst()).isFalse();
        }

        @Test
        @DisplayName("totalPages()로 전체 페이지 수를 계산한다")
        void calculatesTotalPages() {
            // given
            PageRequest pageRequest = PageRequest.of(0, 20);

            // when
            int totalPages = pageRequest.totalPages(95);

            // then
            assertThat(totalPages).isEqualTo(5);
        }

        @Test
        @DisplayName("isLast()는 마지막 페이지에서 true를 반환한다")
        void isLastReturnsTrueForLastPage() {
            // given
            PageRequest pageRequest = PageRequest.of(4, 20);

            // then
            assertThat(pageRequest.isLast(100)).isTrue();
        }

        @Test
        @DisplayName("isLast()는 마지막 페이지가 아니면 false를 반환한다")
        void isLastReturnsFalseForNonLastPage() {
            // given
            PageRequest pageRequest = PageRequest.of(2, 20);

            // then
            assertThat(pageRequest.isLast(100)).isFalse();
        }
    }
}
