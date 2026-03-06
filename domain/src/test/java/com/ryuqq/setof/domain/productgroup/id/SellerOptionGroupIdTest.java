package com.ryuqq.setof.domain.productgroup.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("SellerOptionGroupId Value Object 테스트")
class SellerOptionGroupIdTest {

    @Nested
    @DisplayName("of() - 팩토리 메서드")
    class OfTest {

        @Test
        @DisplayName("유효한 값으로 SellerOptionGroupId를 생성한다")
        void createWithValidValue() {
            // when
            SellerOptionGroupId id = SellerOptionGroupId.of(10L);

            // then
            assertThat(id.value()).isEqualTo(10L);
            assertThat(id.isNew()).isFalse();
        }

        @Test
        @DisplayName("null 값이면 예외가 발생한다")
        void throwExceptionForNull() {
            assertThatThrownBy(() -> SellerOptionGroupId.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("null");
        }
    }

    @Nested
    @DisplayName("forNew() - 신규 ID 생성")
    class ForNewTest {

        @Test
        @DisplayName("신규 ID를 생성한다 (value가 null)")
        void createNewId() {
            // when
            SellerOptionGroupId id = SellerOptionGroupId.forNew();

            // then
            assertThat(id.value()).isNull();
            assertThat(id.isNew()).isTrue();
        }
    }

    @Nested
    @DisplayName("isNew() - 신규 여부 확인")
    class IsNewTest {

        @Test
        @DisplayName("forNew()로 생성한 ID는 신규이다")
        void forNewIsNew() {
            assertThat(SellerOptionGroupId.forNew().isNew()).isTrue();
        }

        @Test
        @DisplayName("of()로 생성한 ID는 신규가 아니다")
        void ofIsNotNew() {
            assertThat(SellerOptionGroupId.of(10L).isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값의 SellerOptionGroupId는 동등하다")
        void equalIds() {
            SellerOptionGroupId id1 = SellerOptionGroupId.of(10L);
            SellerOptionGroupId id2 = SellerOptionGroupId.of(10L);
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("다른 값의 SellerOptionGroupId는 동등하지 않다")
        void notEqualIds() {
            SellerOptionGroupId id1 = SellerOptionGroupId.of(10L);
            SellerOptionGroupId id2 = SellerOptionGroupId.of(11L);
            assertThat(id1).isNotEqualTo(id2);
        }

        @Test
        @DisplayName("forNew()로 생성된 두 ID는 동등하다 (value 모두 null)")
        void twoNewIdsAreEqual() {
            SellerOptionGroupId id1 = SellerOptionGroupId.forNew();
            SellerOptionGroupId id2 = SellerOptionGroupId.forNew();
            assertThat(id1).isEqualTo(id2);
        }
    }
}
