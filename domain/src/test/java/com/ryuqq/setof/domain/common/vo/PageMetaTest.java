package com.ryuqq.setof.domain.common.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("PageMeta Value Object 테스트")
class PageMetaTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 PageMeta를 생성한다 (totalPages 자동 계산)")
        void createWithOf() {
            // when
            PageMeta pageMeta = PageMeta.of(0, 20, 95);

            // then
            assertThat(pageMeta.page()).isZero();
            assertThat(pageMeta.size()).isEqualTo(20);
            assertThat(pageMeta.totalElements()).isEqualTo(95);
            assertThat(pageMeta.totalPages()).isEqualTo(5);
        }

        @Test
        @DisplayName("of()로 모든 값을 직접 지정하여 생성한다")
        void createWithAllValues() {
            // when
            PageMeta pageMeta = PageMeta.of(1, 20, 100, 5);

            // then
            assertThat(pageMeta.page()).isEqualTo(1);
            assertThat(pageMeta.size()).isEqualTo(20);
            assertThat(pageMeta.totalElements()).isEqualTo(100);
            assertThat(pageMeta.totalPages()).isEqualTo(5);
        }

        @Test
        @DisplayName("empty()로 빈 결과용 PageMeta를 생성한다")
        void createEmpty() {
            // when
            PageMeta pageMeta = PageMeta.empty();

            // then
            assertThat(pageMeta.page()).isZero();
            assertThat(pageMeta.size()).isEqualTo(PageMeta.DEFAULT_SIZE);
            assertThat(pageMeta.totalElements()).isZero();
            assertThat(pageMeta.totalPages()).isZero();
        }

        @Test
        @DisplayName("empty(size)로 크기를 지정하여 빈 PageMeta를 생성한다")
        void createEmptyWithSize() {
            // when
            PageMeta pageMeta = PageMeta.empty(30);

            // then
            assertThat(pageMeta.size()).isEqualTo(30);
            assertThat(pageMeta.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("유효성 검증 테스트")
    class ValidationTest {

        @Test
        @DisplayName("음수 page는 0으로 정규화된다")
        void negativePageNormalizesToZero() {
            // when
            PageMeta pageMeta = PageMeta.of(-1, 20, 100, 5);

            // then
            assertThat(pageMeta.page()).isZero();
        }

        @Test
        @DisplayName("0 이하 size는 기본값으로 정규화된다")
        void zeroSizeNormalizesToDefault() {
            // when
            PageMeta pageMeta = PageMeta.of(0, 0, 100, 5);

            // then
            assertThat(pageMeta.size()).isEqualTo(PageMeta.DEFAULT_SIZE);
        }

        @Test
        @DisplayName("음수 totalElements는 0으로 정규화된다")
        void negativeTotalElementsNormalizesToZero() {
            // when
            PageMeta pageMeta = PageMeta.of(0, 20, -100, 5);

            // then
            assertThat(pageMeta.totalElements()).isZero();
        }

        @Test
        @DisplayName("음수 totalPages는 0으로 정규화된다")
        void negativeTotalPagesNormalizesToZero() {
            // when
            PageMeta pageMeta = PageMeta.of(0, 20, 100, -5);

            // then
            assertThat(pageMeta.totalPages()).isZero();
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드 테스트")
    class StateCheckTest {

        @Test
        @DisplayName("hasNext()는 다음 페이지가 있으면 true를 반환한다")
        void hasNextReturnsTrueWhenNextExists() {
            // given
            PageMeta pageMeta = PageMeta.of(0, 20, 100);

            // then
            assertThat(pageMeta.hasNext()).isTrue();
        }

        @Test
        @DisplayName("hasNext()는 마지막 페이지면 false를 반환한다")
        void hasNextReturnsFalseOnLastPage() {
            // given
            PageMeta pageMeta = PageMeta.of(4, 20, 100);

            // then
            assertThat(pageMeta.hasNext()).isFalse();
        }

        @Test
        @DisplayName("hasPrevious()는 첫 페이지가 아니면 true를 반환한다")
        void hasPreviousReturnsTrueWhenPreviousExists() {
            // given
            PageMeta pageMeta = PageMeta.of(2, 20, 100);

            // then
            assertThat(pageMeta.hasPrevious()).isTrue();
        }

        @Test
        @DisplayName("hasPrevious()는 첫 페이지면 false를 반환한다")
        void hasPreviousReturnsFalseOnFirstPage() {
            // given
            PageMeta pageMeta = PageMeta.of(0, 20, 100);

            // then
            assertThat(pageMeta.hasPrevious()).isFalse();
        }

        @Test
        @DisplayName("isFirst()는 첫 페이지에서 true를 반환한다")
        void isFirstReturnsTrueOnFirstPage() {
            // given
            PageMeta pageMeta = PageMeta.of(0, 20, 100);

            // then
            assertThat(pageMeta.isFirst()).isTrue();
        }

        @Test
        @DisplayName("isLast()는 마지막 페이지에서 true를 반환한다")
        void isLastReturnsTrueOnLastPage() {
            // given
            PageMeta pageMeta = PageMeta.of(4, 20, 100);

            // then
            assertThat(pageMeta.isLast()).isTrue();
        }

        @Test
        @DisplayName("isEmpty()는 totalElements가 0이면 true를 반환한다")
        void isEmptyReturnsTrueWhenNoElements() {
            // given
            PageMeta pageMeta = PageMeta.empty();

            // then
            assertThat(pageMeta.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("계산 메서드 테스트")
    class CalculationTest {

        @Test
        @DisplayName("startElement()는 시작 요소 번호를 반환한다")
        void calculatesStartElement() {
            // given
            PageMeta pageMeta = PageMeta.of(1, 20, 100);

            // when
            long startElement = pageMeta.startElement();

            // then
            assertThat(startElement).isEqualTo(21);
        }

        @Test
        @DisplayName("빈 결과에서 startElement()는 0을 반환한다")
        void startElementReturnsZeroForEmpty() {
            // given
            PageMeta pageMeta = PageMeta.empty();

            // then
            assertThat(pageMeta.startElement()).isZero();
        }

        @Test
        @DisplayName("endElement()는 끝 요소 번호를 반환한다")
        void calculatesEndElement() {
            // given
            PageMeta pageMeta = PageMeta.of(1, 20, 100);

            // when
            long endElement = pageMeta.endElement();

            // then
            assertThat(endElement).isEqualTo(40);
        }

        @Test
        @DisplayName("마지막 페이지 endElement()는 totalElements를 반환한다")
        void endElementReturnsTotalOnLastPage() {
            // given
            PageMeta pageMeta = PageMeta.of(4, 20, 95);

            // when
            long endElement = pageMeta.endElement();

            // then
            assertThat(endElement).isEqualTo(95);
        }

        @Test
        @DisplayName("빈 결과에서 endElement()는 0을 반환한다")
        void endElementReturnsZeroForEmpty() {
            // given
            PageMeta pageMeta = PageMeta.empty();

            // then
            assertThat(pageMeta.endElement()).isZero();
        }

        @Test
        @DisplayName("offset()는 SQL OFFSET 값을 반환한다")
        void calculatesOffset() {
            // given
            PageMeta pageMeta = PageMeta.of(2, 20, 100);

            // when
            long offset = pageMeta.offset();

            // then
            assertThat(offset).isEqualTo(40);
        }
    }
}
