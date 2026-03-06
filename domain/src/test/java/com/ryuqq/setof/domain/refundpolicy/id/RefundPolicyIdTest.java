package com.ryuqq.setof.domain.refundpolicy.id;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("RefundPolicyId Value Object 테스트")
class RefundPolicyIdTest {

    @Nested
    @DisplayName("of() - 일반 생성")
    class OfTest {

        @Test
        @DisplayName("유효한 Long 값으로 RefundPolicyId를 생성한다")
        void createWithValidValue() {
            // when
            RefundPolicyId id = RefundPolicyId.of(1L);

            // then
            assertThat(id.value()).isEqualTo(1L);
        }

        @Test
        @DisplayName("null 값으로 생성하면 예외가 발생한다")
        void createWithNullThrowsException() {
            // when & then
            assertThatThrownBy(() -> RefundPolicyId.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("RefundPolicyId 값은 null일 수 없습니다");
        }

        @Test
        @DisplayName("큰 Long 값으로도 생성할 수 있다")
        void createWithLargeValue() {
            // when
            RefundPolicyId id = RefundPolicyId.of(Long.MAX_VALUE);

            // then
            assertThat(id.value()).isEqualTo(Long.MAX_VALUE);
        }
    }

    @Nested
    @DisplayName("forNew() - 신규 ID 생성")
    class ForNewTest {

        @Test
        @DisplayName("신규 RefundPolicyId는 null 값을 가진다")
        void forNewHasNullValue() {
            // when
            RefundPolicyId id = RefundPolicyId.forNew();

            // then
            assertThat(id.value()).isNull();
        }

        @Test
        @DisplayName("신규 RefundPolicyId는 isNew()가 true이다")
        void forNewIsNew() {
            // when
            RefundPolicyId id = RefundPolicyId.forNew();

            // then
            assertThat(id.isNew()).isTrue();
        }
    }

    @Nested
    @DisplayName("isNew() - 신규 여부 확인")
    class IsNewTest {

        @Test
        @DisplayName("value가 null이면 isNew()는 true이다")
        void isNewReturnsTrueWhenValueIsNull() {
            // given
            RefundPolicyId id = RefundPolicyId.forNew();

            // then
            assertThat(id.isNew()).isTrue();
        }

        @Test
        @DisplayName("value가 존재하면 isNew()는 false이다")
        void isNewReturnsFalseWhenValueExists() {
            // given
            RefundPolicyId id = RefundPolicyId.of(1L);

            // then
            assertThat(id.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값의 RefundPolicyId는 동등하다")
        void sameValueIsEqual() {
            // given
            RefundPolicyId id1 = RefundPolicyId.of(1L);
            RefundPolicyId id2 = RefundPolicyId.of(1L);

            // then
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("다른 값의 RefundPolicyId는 동등하지 않다")
        void differentValueIsNotEqual() {
            // given
            RefundPolicyId id1 = RefundPolicyId.of(1L);
            RefundPolicyId id2 = RefundPolicyId.of(2L);

            // then
            assertThat(id1).isNotEqualTo(id2);
        }

        @Test
        @DisplayName("신규 ID 두 개는 동등하다")
        void twoNewIdsAreEqual() {
            // given
            RefundPolicyId id1 = RefundPolicyId.forNew();
            RefundPolicyId id2 = RefundPolicyId.forNew();

            // then
            assertThat(id1).isEqualTo(id2);
        }
    }
}
