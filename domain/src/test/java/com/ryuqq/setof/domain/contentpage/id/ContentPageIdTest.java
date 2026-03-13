package com.ryuqq.setof.domain.contentpage.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ContentPageId Value Object 테스트")
class ContentPageIdTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 ContentPageId를 생성한다")
        void createWithOf() {
            // when
            ContentPageId id = ContentPageId.of(123L);

            // then
            assertThat(id.value()).isEqualTo(123L);
        }

        @Test
        @DisplayName("of()에 null을 전달하면 예외를 발생시킨다")
        void ofWithNullThrowsException() {
            // when & then
            assertThatThrownBy(() -> ContentPageId.of(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("of()에 0 이하 값을 전달하면 예외를 발생시킨다")
        void ofWithZeroOrNegativeThrowsException() {
            assertThatThrownBy(() -> ContentPageId.of(0L))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> ContentPageId.of(-1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("forNew()로 신규 생성용 ContentPageId를 생성한다")
        void createWithForNew() {
            // when
            ContentPageId id = ContentPageId.forNew();

            // then
            assertThat(id.value()).isNull();
            assertThat(id.isNew()).isTrue();
        }
    }

    @Nested
    @DisplayName("상태 확인 테스트")
    class StateCheckTest {

        @Test
        @DisplayName("isNew()는 value가 null이면 true를 반환한다")
        void isNewReturnsTrueWhenValueIsNull() {
            // given
            ContentPageId id = ContentPageId.forNew();

            // then
            assertThat(id.isNew()).isTrue();
        }

        @Test
        @DisplayName("isNew()는 value가 있으면 false를 반환한다")
        void isNewReturnsFalseWhenValueExists() {
            // given
            ContentPageId id = ContentPageId.of(1L);

            // then
            assertThat(id.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 ContentPageId는 동등하다")
        void sameValueEquals() {
            // given
            ContentPageId id1 = ContentPageId.of(100L);
            ContentPageId id2 = ContentPageId.of(100L);

            // then
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 ContentPageId는 동등하지 않다")
        void differentValueNotEquals() {
            // given
            ContentPageId id1 = ContentPageId.of(100L);
            ContentPageId id2 = ContentPageId.of(200L);

            // then
            assertThat(id1).isNotEqualTo(id2);
        }
    }
}
