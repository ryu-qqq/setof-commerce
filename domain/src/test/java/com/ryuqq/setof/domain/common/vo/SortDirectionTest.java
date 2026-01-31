package com.ryuqq.setof.domain.common.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("SortDirection Value Object 테스트")
class SortDirectionTest {

    @Nested
    @DisplayName("기본 메서드 테스트")
    class BasicMethodTest {

        @Test
        @DisplayName("defaultDirection()은 DESC를 반환한다")
        void defaultDirectionReturnsDesc() {
            // when
            SortDirection direction = SortDirection.defaultDirection();

            // then
            assertThat(direction).isEqualTo(SortDirection.DESC);
        }

        @Test
        @DisplayName("ASC.isAscending()은 true를 반환한다")
        void ascIsAscendingReturnsTrue() {
            assertThat(SortDirection.ASC.isAscending()).isTrue();
        }

        @Test
        @DisplayName("DESC.isAscending()은 false를 반환한다")
        void descIsAscendingReturnsFalse() {
            assertThat(SortDirection.DESC.isAscending()).isFalse();
        }

        @Test
        @DisplayName("ASC.isDescending()은 false를 반환한다")
        void ascIsDescendingReturnsFalse() {
            assertThat(SortDirection.ASC.isDescending()).isFalse();
        }

        @Test
        @DisplayName("DESC.isDescending()은 true를 반환한다")
        void descIsDescendingReturnsTrue() {
            assertThat(SortDirection.DESC.isDescending()).isTrue();
        }
    }

    @Nested
    @DisplayName("reverse 테스트")
    class ReverseTest {

        @Test
        @DisplayName("ASC.reverse()는 DESC를 반환한다")
        void ascReverseReturnsDesc() {
            // when
            SortDirection reversed = SortDirection.ASC.reverse();

            // then
            assertThat(reversed).isEqualTo(SortDirection.DESC);
        }

        @Test
        @DisplayName("DESC.reverse()는 ASC를 반환한다")
        void descReverseReturnsAsc() {
            // when
            SortDirection reversed = SortDirection.DESC.reverse();

            // then
            assertThat(reversed).isEqualTo(SortDirection.ASC);
        }
    }

    @Nested
    @DisplayName("displayName 테스트")
    class DisplayNameTest {

        @Test
        @DisplayName("ASC.displayName()은 '오름차순'을 반환한다")
        void ascDisplayName() {
            assertThat(SortDirection.ASC.displayName()).isEqualTo("오름차순");
        }

        @Test
        @DisplayName("DESC.displayName()은 '내림차순'을 반환한다")
        void descDisplayName() {
            assertThat(SortDirection.DESC.displayName()).isEqualTo("내림차순");
        }
    }

    @Nested
    @DisplayName("fromString 테스트")
    class FromStringTest {

        @Test
        @DisplayName("'asc' 문자열을 ASC로 파싱한다")
        void parsesLowercaseAsc() {
            // when
            SortDirection direction = SortDirection.fromString("asc");

            // then
            assertThat(direction).isEqualTo(SortDirection.ASC);
        }

        @Test
        @DisplayName("'DESC' 문자열을 DESC로 파싱한다")
        void parsesUppercaseDesc() {
            // when
            SortDirection direction = SortDirection.fromString("DESC");

            // then
            assertThat(direction).isEqualTo(SortDirection.DESC);
        }

        @Test
        @DisplayName("공백이 포함된 문자열도 처리한다")
        void handlesWhitespace() {
            // when
            SortDirection direction = SortDirection.fromString("  asc  ");

            // then
            assertThat(direction).isEqualTo(SortDirection.ASC);
        }

        @Test
        @DisplayName("null 입력은 기본값(DESC)을 반환한다")
        void nullReturnsDefault() {
            // when
            SortDirection direction = SortDirection.fromString(null);

            // then
            assertThat(direction).isEqualTo(SortDirection.DESC);
        }

        @Test
        @DisplayName("빈 문자열은 기본값(DESC)을 반환한다")
        void emptyStringReturnsDefault() {
            // when
            SortDirection direction = SortDirection.fromString("");

            // then
            assertThat(direction).isEqualTo(SortDirection.DESC);
        }

        @Test
        @DisplayName("공백 문자열은 기본값(DESC)을 반환한다")
        void blankStringReturnsDefault() {
            // when
            SortDirection direction = SortDirection.fromString("   ");

            // then
            assertThat(direction).isEqualTo(SortDirection.DESC);
        }

        @Test
        @DisplayName("유효하지 않은 값은 기본값(DESC)을 반환한다")
        void invalidValueReturnsDefault() {
            // when
            SortDirection direction = SortDirection.fromString("invalid");

            // then
            assertThat(direction).isEqualTo(SortDirection.DESC);
        }
    }
}
