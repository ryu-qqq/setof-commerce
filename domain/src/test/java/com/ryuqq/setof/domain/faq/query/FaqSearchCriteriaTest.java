package com.ryuqq.setof.domain.faq.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.faq.vo.FaqType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("FaqSearchCriteria 테스트")
class FaqSearchCriteriaTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 검색 조건을 생성한다")
        void createWithOf() {
            // when
            FaqSearchCriteria criteria = FaqSearchCriteria.of(FaqType.SHIPPING);

            // then
            assertThat(criteria.faqType()).isEqualTo(FaqType.SHIPPING);
        }

        @Test
        @DisplayName("ofTop()으로 TOP FAQ 검색 조건을 생성한다")
        void createWithOfTop() {
            // when
            FaqSearchCriteria criteria = FaqSearchCriteria.ofTop();

            // then
            assertThat(criteria.faqType()).isEqualTo(FaqType.TOP);
            assertThat(criteria.isTop()).isTrue();
        }

        @Test
        @DisplayName("모든 FaqType에 대해 검색 조건을 생성할 수 있다")
        void createForAllFaqTypes() {
            for (FaqType type : FaqType.values()) {
                FaqSearchCriteria criteria = FaqSearchCriteria.of(type);
                assertThat(criteria.faqType()).isEqualTo(type);
            }
        }
    }

    @Nested
    @DisplayName("유효성 검증 테스트")
    class ValidationTest {

        @Test
        @DisplayName("null faqType으로 생성하면 예외가 발생한다")
        void nullFaqTypeThrowsException() {
            assertThatThrownBy(() -> FaqSearchCriteria.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("faqType");
        }
    }

    @Nested
    @DisplayName("isTop() 편의 메서드 테스트")
    class IsTopTest {

        @Test
        @DisplayName("TOP 유형이면 isTop()이 true이다")
        void topTypeReturnsTrue() {
            // when
            FaqSearchCriteria criteria = FaqSearchCriteria.ofTop();

            // then
            assertThat(criteria.isTop()).isTrue();
        }

        @Test
        @DisplayName("TOP 유형이 아니면 isTop()이 false이다")
        void nonTopTypeReturnsFalse() {
            // when
            FaqSearchCriteria criteria = FaqSearchCriteria.of(FaqType.SHIPPING);

            // then
            assertThat(criteria.isTop()).isFalse();
        }

        @Test
        @DisplayName("MEMBER_LOGIN 유형이면 isTop()이 false이다")
        void memberLoginTypeReturnsFalse() {
            // when
            FaqSearchCriteria criteria = FaqSearchCriteria.of(FaqType.MEMBER_LOGIN);

            // then
            assertThat(criteria.isTop()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 faqType의 검색 조건은 동등하다")
        void sameFaqTypeEquals() {
            // given
            FaqSearchCriteria criteria1 = FaqSearchCriteria.of(FaqType.SHIPPING);
            FaqSearchCriteria criteria2 = FaqSearchCriteria.of(FaqType.SHIPPING);

            // then
            assertThat(criteria1).isEqualTo(criteria2);
            assertThat(criteria1.hashCode()).isEqualTo(criteria2.hashCode());
        }

        @Test
        @DisplayName("다른 faqType의 검색 조건은 동등하지 않다")
        void differentFaqTypeNotEquals() {
            // given
            FaqSearchCriteria criteria1 = FaqSearchCriteria.of(FaqType.SHIPPING);
            FaqSearchCriteria criteria2 = FaqSearchCriteria.of(FaqType.ORDER_PAYMENT);

            // then
            assertThat(criteria1).isNotEqualTo(criteria2);
        }
    }
}
