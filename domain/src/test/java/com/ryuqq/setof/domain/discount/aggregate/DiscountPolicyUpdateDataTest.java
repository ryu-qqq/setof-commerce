package com.ryuqq.setof.domain.discount.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.vo.DiscountBudget;
import com.ryuqq.setof.domain.discount.vo.DiscountMethod;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyName;
import com.ryuqq.setof.domain.discount.vo.DiscountRate;
import com.ryuqq.setof.domain.discount.vo.Priority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DiscountPolicyUpdateData Value Object 테스트")
class DiscountPolicyUpdateDataTest {

    @Nested
    @DisplayName("of() - 생성 테스트")
    class OfTest {

        @Test
        @DisplayName("RATE 방식의 수정 데이터를 생성한다")
        void createRateUpdateData() {
            // given
            DiscountPolicyName name = DiscountPolicyName.of("수정된정책명");
            String description = "수정된 설명";
            DiscountRate rate = DiscountRate.of(15.0);
            Money maxDiscountAmount = Money.of(30000);
            Money minimumOrderAmount = Money.of(20000);
            Priority priority = Priority.of(70);

            // when
            DiscountPolicyUpdateData data =
                    DiscountPolicyUpdateData.of(
                            name,
                            description,
                            DiscountMethod.RATE,
                            rate,
                            null,
                            maxDiscountAmount,
                            true,
                            minimumOrderAmount,
                            priority,
                            DiscountFixtures.defaultActivePeriod(),
                            DiscountFixtures.defaultBudget());

            // then
            assertThat(data.name()).isEqualTo(name);
            assertThat(data.description()).isEqualTo(description);
            assertThat(data.discountMethod()).isEqualTo(DiscountMethod.RATE);
            assertThat(data.discountRate()).isEqualTo(rate);
            assertThat(data.discountAmount()).isNull();
            assertThat(data.maxDiscountAmount()).isEqualTo(maxDiscountAmount);
            assertThat(data.discountCapped()).isTrue();
            assertThat(data.minimumOrderAmount()).isEqualTo(minimumOrderAmount);
            assertThat(data.priority()).isEqualTo(priority);
        }

        @Test
        @DisplayName("FIXED_AMOUNT 방식의 수정 데이터를 생성한다")
        void createFixedAmountUpdateData() {
            // given
            Money discountAmount = Money.of(5000);

            // when
            DiscountPolicyUpdateData data =
                    DiscountPolicyUpdateData.of(
                            DiscountPolicyName.of("정액할인정책"),
                            null,
                            DiscountMethod.FIXED_AMOUNT,
                            null,
                            discountAmount,
                            null,
                            false,
                            null,
                            Priority.of(30),
                            DiscountFixtures.defaultActivePeriod(),
                            DiscountFixtures.defaultBudget());

            // then
            assertThat(data.discountMethod()).isEqualTo(DiscountMethod.FIXED_AMOUNT);
            assertThat(data.discountAmount()).isEqualTo(discountAmount);
            assertThat(data.discountRate()).isNull();
            assertThat(data.description()).isNull();
            assertThat(data.discountCapped()).isFalse();
        }

        @Test
        @DisplayName("fixtures에서 제공하는 rateUpdateData()로 생성한다")
        void createFromFixtures() {
            // when
            DiscountPolicyUpdateData data = DiscountFixtures.rateUpdateData();

            // then
            assertThat(data.name()).isNotNull();
            assertThat(data.discountMethod()).isEqualTo(DiscountMethod.RATE);
            assertThat(data.discountRate()).isNotNull();
            assertThat(data.period()).isNotNull();
            assertThat(data.budget()).isNotNull();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("동일한 값을 가진 수정 데이터는 동등하다")
        void sameValuesAreEqual() {
            // given
            DiscountPolicyName name = DiscountPolicyName.of("정책명");
            DiscountRate rate = DiscountRate.of(10.0);
            Priority priority = Priority.of(50);
            DiscountBudget budget = DiscountFixtures.defaultBudget();
            com.ryuqq.setof.domain.discount.vo.DiscountPeriod period =
                    DiscountFixtures.defaultActivePeriod();

            DiscountPolicyUpdateData data1 =
                    DiscountPolicyUpdateData.of(
                            name,
                            "설명",
                            DiscountMethod.RATE,
                            rate,
                            null,
                            null,
                            false,
                            null,
                            priority,
                            period,
                            budget);

            DiscountPolicyUpdateData data2 =
                    DiscountPolicyUpdateData.of(
                            name,
                            "설명",
                            DiscountMethod.RATE,
                            rate,
                            null,
                            null,
                            false,
                            null,
                            priority,
                            period,
                            budget);

            // then
            assertThat(data1).isEqualTo(data2);
        }
    }
}
