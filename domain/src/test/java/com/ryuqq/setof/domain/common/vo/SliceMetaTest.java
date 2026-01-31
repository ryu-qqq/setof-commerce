package com.ryuqq.setof.domain.common.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("SliceMeta Value Object 테스트")
class SliceMetaTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 커서 없이 SliceMeta를 생성한다")
        void createWithoutCursor() {
            // when
            SliceMeta sliceMeta = SliceMeta.of(20, true);

            // then
            assertThat(sliceMeta.size()).isEqualTo(20);
            assertThat(sliceMeta.hasNext()).isTrue();
            assertThat(sliceMeta.cursor()).isNull();
            assertThat(sliceMeta.count()).isZero();
        }

        @Test
        @DisplayName("of()로 count 포함하여 생성한다")
        void createWithCount() {
            // when
            SliceMeta sliceMeta = SliceMeta.of(20, true, 15);

            // then
            assertThat(sliceMeta.count()).isEqualTo(15);
        }

        @Test
        @DisplayName("withCursor()로 String 커서를 포함하여 생성한다")
        void createWithStringCursor() {
            // when
            SliceMeta sliceMeta = SliceMeta.withCursor("cursor-xyz", 20, true);

            // then
            assertThat(sliceMeta.cursor()).isEqualTo("cursor-xyz");
            assertThat(sliceMeta.hasCursor()).isTrue();
        }

        @Test
        @DisplayName("withCursor()로 Long 커서를 포함하여 생성한다")
        void createWithLongCursor() {
            // when
            SliceMeta sliceMeta = SliceMeta.withCursor(123L, 20, true);

            // then
            assertThat(sliceMeta.cursor()).isEqualTo("123");
            assertThat(sliceMeta.cursorAsLong()).isEqualTo(123L);
        }

        @Test
        @DisplayName("withCursor()로 null Long 커서를 처리한다")
        void createWithNullLongCursor() {
            // when
            SliceMeta sliceMeta = SliceMeta.withCursor((Long) null, 20, false);

            // then
            assertThat(sliceMeta.cursor()).isNull();
            assertThat(sliceMeta.hasCursor()).isFalse();
        }

        @Test
        @DisplayName("empty()로 빈 SliceMeta를 생성한다")
        void createEmpty() {
            // when
            SliceMeta sliceMeta = SliceMeta.empty();

            // then
            assertThat(sliceMeta.size()).isEqualTo(SliceMeta.DEFAULT_SIZE);
            assertThat(sliceMeta.hasNext()).isFalse();
            assertThat(sliceMeta.cursor()).isNull();
            assertThat(sliceMeta.count()).isZero();
        }

        @Test
        @DisplayName("empty(size)로 크기 지정하여 빈 SliceMeta를 생성한다")
        void createEmptyWithSize() {
            // when
            SliceMeta sliceMeta = SliceMeta.empty(30);

            // then
            assertThat(sliceMeta.size()).isEqualTo(30);
            assertThat(sliceMeta.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("유효성 검증 테스트")
    class ValidationTest {

        @Test
        @DisplayName("0 이하 size는 기본값으로 정규화된다")
        void zeroSizeNormalizesToDefault() {
            // when
            SliceMeta sliceMeta = SliceMeta.of(0, false);

            // then
            assertThat(sliceMeta.size()).isEqualTo(SliceMeta.DEFAULT_SIZE);
        }

        @Test
        @DisplayName("음수 count는 0으로 정규화된다")
        void negativeCountNormalizesToZero() {
            // when
            SliceMeta sliceMeta = SliceMeta.of(20, false, -5);

            // then
            assertThat(sliceMeta.count()).isZero();
        }
    }

    @Nested
    @DisplayName("상태 확인 테스트")
    class StateCheckTest {

        @Test
        @DisplayName("hasCursor()는 커서가 있으면 true를 반환한다")
        void hasCursorReturnsTrueWhenCursorExists() {
            // given
            SliceMeta sliceMeta = SliceMeta.withCursor("cursor", 20, true);

            // then
            assertThat(sliceMeta.hasCursor()).isTrue();
        }

        @Test
        @DisplayName("hasCursor()는 빈 문자열 커서에서 false를 반환한다")
        void hasCursorReturnsFalseForEmptyString() {
            // given
            SliceMeta sliceMeta = SliceMeta.withCursor("", 20, true);

            // then
            assertThat(sliceMeta.hasCursor()).isFalse();
        }

        @Test
        @DisplayName("isLast()는 hasNext가 false면 true를 반환한다")
        void isLastReturnsTrueWhenNoNext() {
            // given
            SliceMeta sliceMeta = SliceMeta.of(20, false);

            // then
            assertThat(sliceMeta.isLast()).isTrue();
        }

        @Test
        @DisplayName("isEmpty()는 count가 0이면 true를 반환한다")
        void isEmptyReturnsTrueWhenCountZero() {
            // given
            SliceMeta sliceMeta = SliceMeta.of(20, false, 0);

            // then
            assertThat(sliceMeta.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("변환 메서드 테스트")
    class ConversionTest {

        @Test
        @DisplayName("cursorAsLong()은 숫자 커서를 Long으로 변환한다")
        void cursorAsLongConvertsNumericCursor() {
            // given
            SliceMeta sliceMeta = SliceMeta.withCursor("456", 20, true);

            // when
            Long cursor = sliceMeta.cursorAsLong();

            // then
            assertThat(cursor).isEqualTo(456L);
        }

        @Test
        @DisplayName("cursorAsLong()은 숫자가 아닌 커서에서 null을 반환한다")
        void cursorAsLongReturnsNullForNonNumeric() {
            // given
            SliceMeta sliceMeta = SliceMeta.withCursor("not-a-number", 20, true);

            // then
            assertThat(sliceMeta.cursorAsLong()).isNull();
        }

        @Test
        @DisplayName("cursorAsLong()은 커서가 없으면 null을 반환한다")
        void cursorAsLongReturnsNullForNoCursor() {
            // given
            SliceMeta sliceMeta = SliceMeta.of(20, true);

            // then
            assertThat(sliceMeta.cursorAsLong()).isNull();
        }

        @Test
        @DisplayName("next()로 다음 SliceMeta를 생성한다")
        void createsNextSliceMeta() {
            // given
            SliceMeta current = SliceMeta.withCursor("100", 20, true);

            // when
            SliceMeta next = current.next("200", false);

            // then
            assertThat(next.cursor()).isEqualTo("200");
            assertThat(next.hasNext()).isFalse();
            assertThat(next.size()).isEqualTo(20);
        }

        @Test
        @DisplayName("next()로 Long 커서를 사용하여 다음 SliceMeta를 생성한다")
        void createsNextSliceMetaWithLongCursor() {
            // given
            SliceMeta current = SliceMeta.withCursor(100L, 20, true);

            // when
            SliceMeta next = current.next(200L, true);

            // then
            assertThat(next.cursor()).isEqualTo("200");
            assertThat(next.cursorAsLong()).isEqualTo(200L);
        }
    }
}
