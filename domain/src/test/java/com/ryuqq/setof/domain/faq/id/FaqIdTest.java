package com.ryuqq.setof.domain.faq.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("FaqId Value Object 테스트")
class FaqIdTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 FaqId를 생성한다")
        void createWithOf() {
            // when
            FaqId faqId = FaqId.of(123L);

            // then
            assertThat(faqId.value()).isEqualTo(123L);
        }

        @Test
        @DisplayName("of()에 null을 전달하면 예외를 발생시킨다")
        void ofWithNullThrowsException() {
            // when & then
            assertThatThrownBy(() -> FaqId.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("null");
        }

        @Test
        @DisplayName("forNew()로 신규 FAQ용 ID를 생성한다")
        void createWithForNew() {
            // when
            FaqId faqId = FaqId.forNew();

            // then
            assertThat(faqId.value()).isNull();
            assertThat(faqId.isNew()).isTrue();
        }
    }

    @Nested
    @DisplayName("상태 확인 테스트")
    class StateCheckTest {

        @Test
        @DisplayName("isNew()는 value가 null이면 true를 반환한다")
        void isNewReturnsTrueWhenValueIsNull() {
            // given
            FaqId faqId = FaqId.forNew();

            // then
            assertThat(faqId.isNew()).isTrue();
        }

        @Test
        @DisplayName("isNew()는 value가 있으면 false를 반환한다")
        void isNewReturnsFalseWhenValueExists() {
            // given
            FaqId faqId = FaqId.of(1L);

            // then
            assertThat(faqId.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 FaqId는 동등하다")
        void sameValueEquals() {
            // given
            FaqId faqId1 = FaqId.of(100L);
            FaqId faqId2 = FaqId.of(100L);

            // then
            assertThat(faqId1).isEqualTo(faqId2);
            assertThat(faqId1.hashCode()).isEqualTo(faqId2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 FaqId는 동등하지 않다")
        void differentValueNotEquals() {
            // given
            FaqId faqId1 = FaqId.of(100L);
            FaqId faqId2 = FaqId.of(200L);

            // then
            assertThat(faqId1).isNotEqualTo(faqId2);
        }
    }
}
