package com.ryuqq.setof.domain.faq.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@Tag("unit")
@DisplayName("Faq VO 테스트")
class FaqVoTest {

    @Nested
    @DisplayName("FaqTitle 테스트")
    class FaqTitleTest {

        @Test
        @DisplayName("유효한 제목으로 생성한다")
        void createValidFaqTitle() {
            // when
            FaqTitle title = FaqTitle.of("배송은 얼마나 걸리나요?");

            // then
            assertThat(title.value()).isEqualTo("배송은 얼마나 걸리나요?");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("null 또는 빈 값이면 예외가 발생한다")
        void throwExceptionForNullOrEmpty(String value) {
            assertThatThrownBy(() -> FaqTitle.of(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("FAQ 제목");
        }

        @Test
        @DisplayName("공백만으로 구성된 값이면 예외가 발생한다")
        void throwExceptionForBlank() {
            assertThatThrownBy(() -> FaqTitle.of("   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("FAQ 제목");
        }

        @Test
        @DisplayName("50자 초과 시 예외가 발생한다")
        void throwExceptionForTooLong() {
            String longTitle = "가".repeat(51);
            assertThatThrownBy(() -> FaqTitle.of(longTitle))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("50");
        }

        @Test
        @DisplayName("정확히 50자인 제목을 생성한다")
        void createExactlyMaxLengthTitle() {
            // given
            String maxLengthTitle = "가".repeat(50);

            // when
            FaqTitle title = FaqTitle.of(maxLengthTitle);

            // then
            assertThat(title.value()).isEqualTo(maxLengthTitle);
        }

        @Test
        @DisplayName("같은 값의 FaqTitle은 동등하다")
        void sameValueEquals() {
            // given
            FaqTitle title1 = FaqTitle.of("배송 문의");
            FaqTitle title2 = FaqTitle.of("배송 문의");

            // then
            assertThat(title1).isEqualTo(title2);
            assertThat(title1.hashCode()).isEqualTo(title2.hashCode());
        }
    }

    @Nested
    @DisplayName("FaqContents 테스트")
    class FaqContentsTest {

        @Test
        @DisplayName("유효한 내용으로 생성한다")
        void createValidFaqContents() {
            // when
            FaqContents contents = FaqContents.of("일반적으로 2~3 영업일 이내에 배송됩니다.");

            // then
            assertThat(contents.value()).isEqualTo("일반적으로 2~3 영업일 이내에 배송됩니다.");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("null 또는 빈 값이면 예외가 발생한다")
        void throwExceptionForNullOrEmpty(String value) {
            assertThatThrownBy(() -> FaqContents.of(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("FAQ 내용");
        }

        @Test
        @DisplayName("공백만으로 구성된 값이면 예외가 발생한다")
        void throwExceptionForBlank() {
            assertThatThrownBy(() -> FaqContents.of("   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("FAQ 내용");
        }

        @Test
        @DisplayName("1024자 초과 시 예외가 발생한다")
        void throwExceptionForTooLong() {
            String longContents = "가".repeat(1025);
            assertThatThrownBy(() -> FaqContents.of(longContents))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("1024");
        }

        @Test
        @DisplayName("정확히 1024자인 내용을 생성한다")
        void createExactlyMaxLengthContents() {
            // given
            String maxLengthContents = "가".repeat(1024);

            // when
            FaqContents contents = FaqContents.of(maxLengthContents);

            // then
            assertThat(contents.value()).isEqualTo(maxLengthContents);
        }

        @Test
        @DisplayName("같은 값의 FaqContents는 동등하다")
        void sameValueEquals() {
            // given
            FaqContents contents1 = FaqContents.of("FAQ 내용입니다.");
            FaqContents contents2 = FaqContents.of("FAQ 내용입니다.");

            // then
            assertThat(contents1).isEqualTo(contents2);
            assertThat(contents1.hashCode()).isEqualTo(contents2.hashCode());
        }
    }

    @Nested
    @DisplayName("FaqDisplayOrder 테스트")
    class FaqDisplayOrderTest {

        @Test
        @DisplayName("유효한 표시 순서로 생성한다")
        void createValidDisplayOrder() {
            // when
            FaqDisplayOrder order = FaqDisplayOrder.of(1);

            // then
            assertThat(order.value()).isEqualTo(1);
        }

        @Test
        @DisplayName("0은 유효한 값이다")
        void zeroIsValid() {
            // when
            FaqDisplayOrder order = FaqDisplayOrder.of(0);

            // then
            assertThat(order.value()).isZero();
        }

        @Test
        @DisplayName("defaultOrder()는 0을 반환한다")
        void defaultOrderIsZero() {
            // when
            FaqDisplayOrder order = FaqDisplayOrder.defaultOrder();

            // then
            assertThat(order.value()).isZero();
        }

        @Test
        @DisplayName("9999는 유효한 최대 값이다")
        void maxValueIsValid() {
            // when
            FaqDisplayOrder order = FaqDisplayOrder.of(9999);

            // then
            assertThat(order.value()).isEqualTo(9999);
        }

        @Test
        @DisplayName("음수 값이면 예외가 발생한다")
        void throwExceptionForNegative() {
            assertThatThrownBy(() -> FaqDisplayOrder.of(-1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("FAQ 표시 순서");
        }

        @Test
        @DisplayName("10000 이상이면 예외가 발생한다")
        void throwExceptionForTooLarge() {
            assertThatThrownBy(() -> FaqDisplayOrder.of(10000))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("FAQ 표시 순서");
        }

        @Test
        @DisplayName("같은 값의 FaqDisplayOrder는 동등하다")
        void sameValueEquals() {
            // given
            FaqDisplayOrder order1 = FaqDisplayOrder.of(5);
            FaqDisplayOrder order2 = FaqDisplayOrder.of(5);

            // then
            assertThat(order1).isEqualTo(order2);
        }
    }

    @Nested
    @DisplayName("FaqType 테스트")
    class FaqTypeTest {

        @Test
        @DisplayName("TOP 유형은 isTop()이 true이다")
        void topTypeIsTop() {
            assertThat(FaqType.TOP.isTop()).isTrue();
        }

        @Test
        @DisplayName("TOP이 아닌 유형은 isTop()이 false이다")
        void nonTopTypeIsNotTop() {
            assertThat(FaqType.MEMBER_LOGIN.isTop()).isFalse();
            assertThat(FaqType.PRODUCT_SELLER.isTop()).isFalse();
            assertThat(FaqType.SHIPPING.isTop()).isFalse();
            assertThat(FaqType.ORDER_PAYMENT.isTop()).isFalse();
            assertThat(FaqType.CANCEL_REFUND.isTop()).isFalse();
            assertThat(FaqType.EXCHANGE_RETURN.isTop()).isFalse();
        }

        @Test
        @DisplayName("displayName()은 사용자 표시용 이름을 반환한다")
        void displayNameReturnsDisplayName() {
            assertThat(FaqType.MEMBER_LOGIN.displayName()).isEqualTo("회원/로그인");
            assertThat(FaqType.PRODUCT_SELLER.displayName()).isEqualTo("상품/판매자");
            assertThat(FaqType.SHIPPING.displayName()).isEqualTo("배송");
            assertThat(FaqType.ORDER_PAYMENT.displayName()).isEqualTo("주문/결제");
            assertThat(FaqType.CANCEL_REFUND.displayName()).isEqualTo("취소/환불");
            assertThat(FaqType.EXCHANGE_RETURN.displayName()).isEqualTo("교환/반품");
            assertThat(FaqType.TOP.displayName()).isEqualTo("상단 고정");
        }

        @Test
        @DisplayName("모든 FaqType 값이 존재한다")
        void allValuesExist() {
            assertThat(FaqType.values())
                    .containsExactly(
                            FaqType.MEMBER_LOGIN,
                            FaqType.PRODUCT_SELLER,
                            FaqType.SHIPPING,
                            FaqType.ORDER_PAYMENT,
                            FaqType.CANCEL_REFUND,
                            FaqType.EXCHANGE_RETURN,
                            FaqType.TOP);
        }

        @Test
        @DisplayName("valueOf로 enum 값을 조회한다")
        void valueOfReturnsCorrectEnum() {
            assertThat(FaqType.valueOf("SHIPPING")).isEqualTo(FaqType.SHIPPING);
            assertThat(FaqType.valueOf("TOP")).isEqualTo(FaqType.TOP);
            assertThat(FaqType.valueOf("MEMBER_LOGIN")).isEqualTo(FaqType.MEMBER_LOGIN);
        }
    }
}
