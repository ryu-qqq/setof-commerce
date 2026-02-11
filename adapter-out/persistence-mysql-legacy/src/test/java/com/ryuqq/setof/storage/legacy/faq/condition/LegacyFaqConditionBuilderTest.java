package com.ryuqq.setof.storage.legacy.faq.condition;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.domain.faq.vo.FaqType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * LegacyFaqConditionBuilder 단위 테스트.
 *
 * <p>QueryDSL 조건 생성 로직을 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@DisplayName("LegacyFaqConditionBuilder 단위 테스트")
@ExtendWith(MockitoExtension.class)
class LegacyFaqConditionBuilderTest {

    private LegacyFaqConditionBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new LegacyFaqConditionBuilder();
    }

    @Nested
    @DisplayName("faqTypeEq 메서드 테스트")
    class FaqTypeEqTest {

        @Test
        @DisplayName("FaqType이 주어지면 BooleanExpression을 반환한다")
        void shouldReturnBooleanExpressionWhenFaqTypeIsGiven() {
            // given
            FaqType faqType = FaqType.MEMBER_LOGIN;

            // when
            BooleanExpression result = builder.faqTypeEq(faqType);

            // then
            assertThat(result).isNotNull();
            assertThat(result.toString()).contains("faqType");
            assertThat(result.toString()).contains("MEMBER_LOGIN");
        }

        @Test
        @DisplayName("FaqType이 null이면 null을 반환한다")
        void shouldReturnNullWhenFaqTypeIsNull() {
            // when
            BooleanExpression result = builder.faqTypeEq(null);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("TOP 유형은 레거시 TOP으로 변환한다")
        void shouldConvertTopFaqTypeToLegacyTop() {
            // given
            FaqType faqType = FaqType.TOP;

            // when
            BooleanExpression result = builder.faqTypeEq(faqType);

            // then
            assertThat(result).isNotNull();
            assertThat(result.toString()).contains("TOP");
        }

        @Test
        @DisplayName("모든 FaqType 값에 대해 정상적으로 조건을 생성한다")
        void shouldCreateConditionForAllFaqTypes() {
            // given
            FaqType[] allTypes =
                    new FaqType[] {
                        FaqType.MEMBER_LOGIN,
                        FaqType.PRODUCT_SELLER,
                        FaqType.SHIPPING,
                        FaqType.ORDER_PAYMENT,
                        FaqType.CANCEL_REFUND,
                        FaqType.EXCHANGE_RETURN,
                        FaqType.TOP
                    };

            // when & then
            for (FaqType type : allTypes) {
                BooleanExpression result = builder.faqTypeEq(type);
                assertThat(result).isNotNull();
                assertThat(result.toString()).contains(type.name());
            }
        }
    }
}
