package com.ryuqq.setof.domain.productnotice.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("NoticeFieldId Value Object 테스트")
class NoticeFieldIdTest {

    @Nested
    @DisplayName("of() - 생성")
    class OfTest {

        @Test
        @DisplayName("유효한 값으로 NoticeFieldId를 생성한다")
        void createWithValidValue() {
            // when
            NoticeFieldId id = NoticeFieldId.of(1L);

            // then
            assertThat(id.value()).isEqualTo(1L);
        }

        @Test
        @DisplayName("of()에 null을 전달하면 예외가 발생한다")
        void ofWithNullThrowsException() {
            // when & then
            assertThatThrownBy(() -> NoticeFieldId.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("null");
        }
    }

    @Nested
    @DisplayName("forNew() - 신규 ID 생성")
    class ForNewTest {

        @Test
        @DisplayName("forNew()는 value가 null인 ID를 생성한다")
        void forNewCreatesIdWithNullValue() {
            // when
            NoticeFieldId id = NoticeFieldId.forNew();

            // then
            assertThat(id.value()).isNull();
        }

        @Test
        @DisplayName("forNew()로 생성한 ID는 isNew()가 true를 반환한다")
        void forNewIdIsNew() {
            // when
            NoticeFieldId id = NoticeFieldId.forNew();

            // then
            assertThat(id.isNew()).isTrue();
        }
    }

    @Nested
    @DisplayName("isNew() - 신규 여부 확인")
    class IsNewTest {

        @Test
        @DisplayName("value가 null이면 isNew()는 true를 반환한다")
        void isNewReturnsTrueWhenValueIsNull() {
            // given
            NoticeFieldId id = NoticeFieldId.forNew();

            // then
            assertThat(id.isNew()).isTrue();
        }

        @Test
        @DisplayName("value가 있으면 isNew()는 false를 반환한다")
        void isNewReturnsFalseWhenValueExists() {
            // given
            NoticeFieldId id = NoticeFieldId.of(1L);

            // then
            assertThat(id.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 NoticeFieldId는 동등하다")
        void sameValueEquals() {
            // given
            NoticeFieldId id1 = NoticeFieldId.of(100L);
            NoticeFieldId id2 = NoticeFieldId.of(100L);

            // then
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 NoticeFieldId는 동등하지 않다")
        void differentValueNotEquals() {
            // given
            NoticeFieldId id1 = NoticeFieldId.of(100L);
            NoticeFieldId id2 = NoticeFieldId.of(200L);

            // then
            assertThat(id1).isNotEqualTo(id2);
        }

        @Test
        @DisplayName("forNew()로 생성한 두 ID는 동등하다")
        void twoForNewIdsAreEqual() {
            // given
            NoticeFieldId id1 = NoticeFieldId.forNew();
            NoticeFieldId id2 = NoticeFieldId.forNew();

            // then
            assertThat(id1).isEqualTo(id2);
        }
    }
}
