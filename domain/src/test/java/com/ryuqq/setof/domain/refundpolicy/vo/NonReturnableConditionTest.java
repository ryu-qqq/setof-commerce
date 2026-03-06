package com.ryuqq.setof.domain.refundpolicy.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("NonReturnableCondition Enum 테스트")
class NonReturnableConditionTest {

    @Nested
    @DisplayName("displayName() 테스트")
    class DisplayNameTest {

        @Test
        @DisplayName("각 조건에 맞는 노출명을 반환한다")
        void returnsCorrectDisplayName() {
            assertThat(NonReturnableCondition.OPENED_PACKAGING.displayName()).isEqualTo("포장 개봉");
            assertThat(NonReturnableCondition.USED_PRODUCT.displayName()).isEqualTo("사용 흔적");
            assertThat(NonReturnableCondition.TIME_EXPIRED.displayName()).isEqualTo("시간 경과");
            assertThat(NonReturnableCondition.DIGITAL_CONTENT.displayName()).isEqualTo("디지털 콘텐츠");
            assertThat(NonReturnableCondition.CUSTOM_MADE.displayName()).isEqualTo("주문 제작");
            assertThat(NonReturnableCondition.HYGIENE_PRODUCT.displayName()).isEqualTo("위생 상품");
            assertThat(NonReturnableCondition.PARTIAL_SET.displayName()).isEqualTo("세트 일부");
            assertThat(NonReturnableCondition.MISSING_TAG.displayName()).isEqualTo("택/라벨 제거");
            assertThat(NonReturnableCondition.DAMAGED_BY_CUSTOMER.displayName())
                    .isEqualTo("고객 과실 파손");
        }
    }

    @Nested
    @DisplayName("description() 테스트")
    class DescriptionTest {

        @Test
        @DisplayName("OPENED_PACKAGING의 설명을 반환한다")
        void openedPackagingHasDescription() {
            assertThat(NonReturnableCondition.OPENED_PACKAGING.description())
                    .isEqualTo("고객 변심으로 인한 포장 개봉 시");
        }

        @Test
        @DisplayName("USED_PRODUCT의 설명을 반환한다")
        void usedProductHasDescription() {
            assertThat(NonReturnableCondition.USED_PRODUCT.description()).isEqualTo("사용 흔적이 있는 경우");
        }

        @Test
        @DisplayName("DIGITAL_CONTENT의 설명을 반환한다")
        void digitalContentHasDescription() {
            assertThat(NonReturnableCondition.DIGITAL_CONTENT.description())
                    .isEqualTo("복제 가능한 디지털 콘텐츠");
        }

        @Test
        @DisplayName("DAMAGED_BY_CUSTOMER의 설명을 반환한다")
        void damagedByCustomerHasDescription() {
            assertThat(NonReturnableCondition.DAMAGED_BY_CUSTOMER.description())
                    .isEqualTo("고객 과실로 인한 상품 파손");
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 반품 불가 조건 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(NonReturnableCondition.values())
                    .containsExactly(
                            NonReturnableCondition.OPENED_PACKAGING,
                            NonReturnableCondition.USED_PRODUCT,
                            NonReturnableCondition.TIME_EXPIRED,
                            NonReturnableCondition.DIGITAL_CONTENT,
                            NonReturnableCondition.CUSTOM_MADE,
                            NonReturnableCondition.HYGIENE_PRODUCT,
                            NonReturnableCondition.PARTIAL_SET,
                            NonReturnableCondition.MISSING_TAG,
                            NonReturnableCondition.DAMAGED_BY_CUSTOMER);
        }

        @Test
        @DisplayName("총 9개의 반품 불가 조건이 있다")
        void hasTotalNineValues() {
            assertThat(NonReturnableCondition.values()).hasSize(9);
        }

        @Test
        @DisplayName("valueOf로 enum 값을 조회한다")
        void valueOfReturnsCorrectEnum() {
            assertThat(NonReturnableCondition.valueOf("OPENED_PACKAGING"))
                    .isEqualTo(NonReturnableCondition.OPENED_PACKAGING);
            assertThat(NonReturnableCondition.valueOf("DIGITAL_CONTENT"))
                    .isEqualTo(NonReturnableCondition.DIGITAL_CONTENT);
            assertThat(NonReturnableCondition.valueOf("DAMAGED_BY_CUSTOMER"))
                    .isEqualTo(NonReturnableCondition.DAMAGED_BY_CUSTOMER);
        }
    }
}
