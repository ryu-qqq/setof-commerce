package com.ryuqq.setof.domain.product.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductStatus 테스트")
class ProductStatusTest {

    @Nested
    @DisplayName("canActivate() - 활성화 가능 여부")
    class CanActivateTest {

        @Test
        @DisplayName("ACTIVE 상태에서는 활성화할 수 없다")
        void activeCannotActivate() {
            assertThat(ProductStatus.ACTIVE.canActivate()).isFalse();
        }

        @Test
        @DisplayName("INACTIVE 상태에서는 활성화할 수 있다")
        void inactiveCanActivate() {
            assertThat(ProductStatus.INACTIVE.canActivate()).isTrue();
        }

        @Test
        @DisplayName("SOLD_OUT 상태에서는 활성화할 수 있다")
        void soldOutCanActivate() {
            assertThat(ProductStatus.SOLD_OUT.canActivate()).isTrue();
        }

        @Test
        @DisplayName("DELETED 상태에서는 활성화할 수 없다")
        void deletedCannotActivate() {
            assertThat(ProductStatus.DELETED.canActivate()).isFalse();
        }
    }

    @Nested
    @DisplayName("canDeactivate() - 비활성화 가능 여부")
    class CanDeactivateTest {

        @Test
        @DisplayName("ACTIVE 상태에서는 비활성화할 수 있다")
        void activeCanDeactivate() {
            assertThat(ProductStatus.ACTIVE.canDeactivate()).isTrue();
        }

        @Test
        @DisplayName("INACTIVE 상태에서는 비활성화할 수 없다")
        void inactiveCannotDeactivate() {
            assertThat(ProductStatus.INACTIVE.canDeactivate()).isFalse();
        }

        @Test
        @DisplayName("SOLD_OUT 상태에서는 비활성화할 수 없다")
        void soldOutCannotDeactivate() {
            assertThat(ProductStatus.SOLD_OUT.canDeactivate()).isFalse();
        }

        @Test
        @DisplayName("DELETED 상태에서는 비활성화할 수 없다")
        void deletedCannotDeactivate() {
            assertThat(ProductStatus.DELETED.canDeactivate()).isFalse();
        }
    }

    @Nested
    @DisplayName("canMarkSoldOut() - 품절 처리 가능 여부")
    class CanMarkSoldOutTest {

        @Test
        @DisplayName("ACTIVE 상태에서는 품절 처리할 수 있다")
        void activeCanMarkSoldOut() {
            assertThat(ProductStatus.ACTIVE.canMarkSoldOut()).isTrue();
        }

        @Test
        @DisplayName("INACTIVE 상태에서는 품절 처리할 수 없다")
        void inactiveCannotMarkSoldOut() {
            assertThat(ProductStatus.INACTIVE.canMarkSoldOut()).isFalse();
        }

        @Test
        @DisplayName("SOLD_OUT 상태에서는 품절 처리할 수 없다")
        void soldOutCannotMarkSoldOut() {
            assertThat(ProductStatus.SOLD_OUT.canMarkSoldOut()).isFalse();
        }

        @Test
        @DisplayName("DELETED 상태에서는 품절 처리할 수 없다")
        void deletedCannotMarkSoldOut() {
            assertThat(ProductStatus.DELETED.canMarkSoldOut()).isFalse();
        }
    }

    @Nested
    @DisplayName("canDelete() - 삭제 가능 여부")
    class CanDeleteTest {

        @Test
        @DisplayName("ACTIVE 상태에서는 삭제할 수 있다")
        void activeCanDelete() {
            assertThat(ProductStatus.ACTIVE.canDelete()).isTrue();
        }

        @Test
        @DisplayName("INACTIVE 상태에서는 삭제할 수 있다")
        void inactiveCanDelete() {
            assertThat(ProductStatus.INACTIVE.canDelete()).isTrue();
        }

        @Test
        @DisplayName("SOLD_OUT 상태에서는 삭제할 수 있다")
        void soldOutCanDelete() {
            assertThat(ProductStatus.SOLD_OUT.canDelete()).isTrue();
        }

        @Test
        @DisplayName("DELETED 상태에서는 삭제할 수 없다")
        void deletedCannotDelete() {
            assertThat(ProductStatus.DELETED.canDelete()).isFalse();
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드 테스트")
    class StatusCheckTest {

        @Test
        @DisplayName("isActive()는 ACTIVE 상태에서만 true를 반환한다")
        void isActiveOnlyForActive() {
            assertThat(ProductStatus.ACTIVE.isActive()).isTrue();
            assertThat(ProductStatus.INACTIVE.isActive()).isFalse();
            assertThat(ProductStatus.SOLD_OUT.isActive()).isFalse();
            assertThat(ProductStatus.DELETED.isActive()).isFalse();
        }

        @Test
        @DisplayName("isSoldOut()은 SOLD_OUT 상태에서만 true를 반환한다")
        void isSoldOutOnlyForSoldOut() {
            assertThat(ProductStatus.SOLD_OUT.isSoldOut()).isTrue();
            assertThat(ProductStatus.ACTIVE.isSoldOut()).isFalse();
            assertThat(ProductStatus.INACTIVE.isSoldOut()).isFalse();
            assertThat(ProductStatus.DELETED.isSoldOut()).isFalse();
        }

        @Test
        @DisplayName("isDeleted()는 DELETED 상태에서만 true를 반환한다")
        void isDeletedOnlyForDeleted() {
            assertThat(ProductStatus.DELETED.isDeleted()).isTrue();
            assertThat(ProductStatus.ACTIVE.isDeleted()).isFalse();
            assertThat(ProductStatus.INACTIVE.isDeleted()).isFalse();
            assertThat(ProductStatus.SOLD_OUT.isDeleted()).isFalse();
        }
    }

    @Nested
    @DisplayName("fromLegacyFlags() - 레거시 플래그 변환")
    class FromLegacyFlagsTest {

        @Test
        @DisplayName("deleteYn=Y이면 DELETED를 반환한다")
        void deletedWhenDeleteYnIsY() {
            // when
            ProductStatus status = ProductStatus.fromLegacyFlags("Y", "N", "Y");

            // then
            assertThat(status).isEqualTo(ProductStatus.DELETED);
        }

        @Test
        @DisplayName("deleteYn=Y이면 다른 플래그와 무관하게 DELETED를 반환한다")
        void deletedRegardlessOfOtherFlags() {
            // when & then
            assertThat(ProductStatus.fromLegacyFlags("Y", "Y", "Y"))
                    .isEqualTo(ProductStatus.DELETED);
            assertThat(ProductStatus.fromLegacyFlags("Y", "Y", "N"))
                    .isEqualTo(ProductStatus.DELETED);
            assertThat(ProductStatus.fromLegacyFlags("Y", "N", "N"))
                    .isEqualTo(ProductStatus.DELETED);
        }

        @Test
        @DisplayName("soldOutYn=Y이면 SOLD_OUT을 반환한다")
        void soldOutWhenSoldOutYnIsY() {
            // when
            ProductStatus status = ProductStatus.fromLegacyFlags("N", "Y", "Y");

            // then
            assertThat(status).isEqualTo(ProductStatus.SOLD_OUT);
        }

        @Test
        @DisplayName("displayYn=N이면 INACTIVE를 반환한다")
        void inactiveWhenDisplayYnIsN() {
            // when
            ProductStatus status = ProductStatus.fromLegacyFlags("N", "N", "N");

            // then
            assertThat(status).isEqualTo(ProductStatus.INACTIVE);
        }

        @Test
        @DisplayName("모든 플래그가 기본값이면 ACTIVE를 반환한다")
        void activeWhenAllDefault() {
            // when
            ProductStatus status = ProductStatus.fromLegacyFlags("N", "N", "Y");

            // then
            assertThat(status).isEqualTo(ProductStatus.ACTIVE);
        }

        @Test
        @DisplayName("대소문자를 구분하지 않는다")
        void caseInsensitive() {
            // when & then
            assertThat(ProductStatus.fromLegacyFlags("y", "n", "y"))
                    .isEqualTo(ProductStatus.DELETED);
            assertThat(ProductStatus.fromLegacyFlags("n", "y", "y"))
                    .isEqualTo(ProductStatus.SOLD_OUT);
            assertThat(ProductStatus.fromLegacyFlags("n", "n", "n"))
                    .isEqualTo(ProductStatus.INACTIVE);
            assertThat(ProductStatus.fromLegacyFlags("n", "n", "y"))
                    .isEqualTo(ProductStatus.ACTIVE);
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 상태 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(ProductStatus.values())
                    .containsExactly(
                            ProductStatus.ACTIVE,
                            ProductStatus.INACTIVE,
                            ProductStatus.SOLD_OUT,
                            ProductStatus.DELETED);
        }
    }
}
