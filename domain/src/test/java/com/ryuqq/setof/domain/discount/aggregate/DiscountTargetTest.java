package com.ryuqq.setof.domain.discount.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.id.DiscountTargetId;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DiscountTarget 자식 엔티티 테스트")
class DiscountTargetTest {

    @Nested
    @DisplayName("forNew() - 신규 대상 생성")
    class ForNewTest {

        @Test
        @DisplayName("유효한 대상을 생성한다")
        void createValidTarget() {
            var target = DiscountTarget.forNew(DiscountTargetType.PRODUCT, 100L);

            assertThat(target.isNew()).isTrue();
            assertThat(target.targetType()).isEqualTo(DiscountTargetType.PRODUCT);
            assertThat(target.targetId()).isEqualTo(100L);
            assertThat(target.isActive()).isTrue();
        }

        @Test
        @DisplayName("targetType이 null이면 예외가 발생한다")
        void throwForNullTargetType() {
            assertThatThrownBy(() -> DiscountTarget.forNew(null, 100L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("대상 유형");
        }

        @Test
        @DisplayName("targetId가 0 이하면 예외가 발생한다")
        void throwForInvalidTargetId() {
            assertThatThrownBy(() -> DiscountTarget.forNew(DiscountTargetType.PRODUCT, 0L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("0보다");
        }

        @Test
        @DisplayName("targetId가 음수이면 예외가 발생한다")
        void throwForNegativeTargetId() {
            assertThatThrownBy(() -> DiscountTarget.forNew(DiscountTargetType.BRAND, -1L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("0보다");
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("활성 대상을 복원한다")
        void reconstituteActiveTarget() {
            var target = DiscountFixtures.activeTarget(1L);

            assertThat(target.isNew()).isFalse();
            assertThat(target.idValue()).isEqualTo(1L);
            assertThat(target.isActive()).isTrue();
        }

        @Test
        @DisplayName("비활성 대상을 복원한다")
        void reconstituteInactiveTarget() {
            var target = DiscountFixtures.inactiveTarget(2L);

            assertThat(target.isActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("deactivate() - 비활성화")
    class DeactivateTest {

        @Test
        @DisplayName("대상을 비활성화한다")
        void deactivateTarget() {
            var target = DiscountFixtures.activeTarget(1L);

            target.deactivate();

            assertThat(target.isActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("matches() - 매칭 확인")
    class MatchesTest {

        @Test
        @DisplayName("동일한 유형과 ID면 true를 반환한다")
        void returnsTrueForMatch() {
            var target = DiscountFixtures.activeTarget(1L);

            assertThat(
                            target.matches(
                                    DiscountTargetType.PRODUCT, DiscountFixtures.DEFAULT_TARGET_ID))
                    .isTrue();
        }

        @Test
        @DisplayName("유형이 다르면 false를 반환한다")
        void returnsFalseForDifferentType() {
            var target = DiscountFixtures.activeTarget(1L);

            assertThat(
                            target.matches(
                                    DiscountTargetType.CATEGORY,
                                    DiscountFixtures.DEFAULT_TARGET_ID))
                    .isFalse();
        }

        @Test
        @DisplayName("ID가 다르면 false를 반환한다")
        void returnsFalseForDifferentId() {
            var target = DiscountFixtures.activeTarget(1L);

            assertThat(target.matches(DiscountTargetType.PRODUCT, 999L)).isFalse();
        }

        @Test
        @DisplayName("비활성 대상은 매칭되지 않는다")
        void returnsFalseForInactiveTarget() {
            var target = DiscountFixtures.inactiveTarget(1L);

            assertThat(
                            target.matches(
                                    DiscountTargetType.PRODUCT, DiscountFixtures.DEFAULT_TARGET_ID))
                    .isFalse();
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("id()는 DiscountTargetId를 반환한다")
        void returnsId() {
            var target = DiscountFixtures.activeTarget(1L);
            assertThat(target.id()).isEqualTo(DiscountTargetId.of(1L));
            assertThat(target.idValue()).isEqualTo(1L);
        }
    }
}
