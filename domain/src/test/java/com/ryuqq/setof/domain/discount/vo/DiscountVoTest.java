package com.ryuqq.setof.domain.discount.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.discount.id.DiscountPolicyId;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@Tag("unit")
@DisplayName("Discount VO 테스트")
class DiscountVoTest {

    @Nested
    @DisplayName("DiscountPolicyName 테스트")
    class DiscountPolicyNameTest {

        @Test
        @DisplayName("유효한 정책명을 생성한다")
        void createValidName() {
            var name = DiscountPolicyName.of("테스트정책");
            assertThat(name.value()).isEqualTo("테스트정책");
        }

        @Test
        @DisplayName("공백은 트림된다")
        void trimWhitespace() {
            var name = DiscountPolicyName.of("  테스트정책  ");
            assertThat(name.value()).isEqualTo("테스트정책");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("null 또는 빈 값이면 예외가 발생한다")
        void throwExceptionForNullOrEmpty(String value) {
            assertThatThrownBy(() -> DiscountPolicyName.of(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("할인 정책명");
        }

        @Test
        @DisplayName("50자 초과 시 예외가 발생한다")
        void throwExceptionForTooLong() {
            String longName = "가".repeat(51);
            assertThatThrownBy(() -> DiscountPolicyName.of(longName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("50");
        }
    }

    @Nested
    @DisplayName("CouponName 테스트")
    class CouponNameTest {

        @Test
        @DisplayName("유효한 쿠폰명을 생성한다")
        void createValidName() {
            var name = CouponName.of("할인쿠폰");
            assertThat(name.value()).isEqualTo("할인쿠폰");
        }

        @Test
        @DisplayName("공백은 트림된다")
        void trimWhitespace() {
            var name = CouponName.of("  할인쿠폰  ");
            assertThat(name.value()).isEqualTo("할인쿠폰");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("null 또는 빈 값이면 예외가 발생한다")
        void throwExceptionForNullOrEmpty(String value) {
            assertThatThrownBy(() -> CouponName.of(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("쿠폰명");
        }

        @Test
        @DisplayName("50자 초과 시 예외가 발생한다")
        void throwExceptionForTooLong() {
            String longName = "가".repeat(51);
            assertThatThrownBy(() -> CouponName.of(longName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("50");
        }
    }

    @Nested
    @DisplayName("CouponCode 테스트")
    class CouponCodeTest {

        @Test
        @DisplayName("유효한 쿠폰 코드를 생성한다")
        void createValidCode() {
            var code = CouponCode.of("ABCD1234");
            assertThat(code.value()).isEqualTo("ABCD1234");
        }

        @Test
        @DisplayName("공백은 트림된다")
        void trimWhitespace() {
            var code = CouponCode.of("  ABCD1234  ");
            assertThat(code.value()).isEqualTo("ABCD1234");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("null 또는 빈 값이면 예외가 발생한다")
        void throwExceptionForNullOrEmpty(String value) {
            assertThatThrownBy(() -> CouponCode.of(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("쿠폰 코드");
        }

        @Test
        @DisplayName("4자 미만이면 예외가 발생한다")
        void throwExceptionForTooShort() {
            assertThatThrownBy(() -> CouponCode.of("ABC"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("4");
        }

        @Test
        @DisplayName("20자 초과 시 예외가 발생한다")
        void throwExceptionForTooLong() {
            String longCode = "A".repeat(21);
            assertThatThrownBy(() -> CouponCode.of(longCode))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("20");
        }

        @Test
        @DisplayName("특수문자 포함 시 예외가 발생한다")
        void throwExceptionForSpecialChars() {
            assertThatThrownBy(() -> CouponCode.of("ABC!@#1234"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("영문 대소문자와 숫자");
        }

        @Test
        @DisplayName("대소문자 무시 매칭이 동작한다")
        void matchesIgnoreCase() {
            var code = CouponCode.of("ABCD1234");
            assertThat(code.matchesIgnoreCase("abcd1234")).isTrue();
            assertThat(code.matchesIgnoreCase("ABCD1234")).isTrue();
            assertThat(code.matchesIgnoreCase("other")).isFalse();
        }
    }

    @Nested
    @DisplayName("DiscountRate 테스트")
    class DiscountRateTest {

        @Test
        @DisplayName("유효한 할인율을 생성한다")
        void createValidRate() {
            var rate = DiscountRate.of(10.0);
            assertThat(rate.value()).isEqualTo(10.0);
        }

        @Test
        @DisplayName("0%는 유효한 값이다")
        void zeroIsValid() {
            var rate = DiscountRate.of(0.0);
            assertThat(rate.value()).isZero();
        }

        @Test
        @DisplayName("100%는 유효한 값이다")
        void hundredIsValid() {
            var rate = DiscountRate.of(100.0);
            assertThat(rate.value()).isEqualTo(100.0);
        }

        @Test
        @DisplayName("음수 값이면 예외가 발생한다")
        void throwExceptionForNegative() {
            assertThatThrownBy(() -> DiscountRate.of(-1.0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("할인율");
        }

        @Test
        @DisplayName("100 초과 시 예외가 발생한다")
        void throwExceptionForOverHundred() {
            assertThatThrownBy(() -> DiscountRate.of(100.1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("할인율");
        }

        @Test
        @DisplayName("toFraction()은 소수점 비율을 반환한다")
        void toFractionReturnsDecimal() {
            var rate = DiscountRate.of(10.0);
            assertThat(rate.toFraction()).isEqualTo(0.1);
        }
    }

    @Nested
    @DisplayName("Priority 테스트")
    class PriorityTest {

        @Test
        @DisplayName("유효한 우선순위를 생성한다")
        void createValidPriority() {
            var priority = Priority.of(50);
            assertThat(priority.value()).isEqualTo(50);
        }

        @Test
        @DisplayName("0은 유효한 값이다")
        void zeroIsValid() {
            var priority = Priority.of(0);
            assertThat(priority.value()).isZero();
        }

        @Test
        @DisplayName("100은 유효한 값이다")
        void hundredIsValid() {
            var priority = Priority.of(100);
            assertThat(priority.value()).isEqualTo(100);
        }

        @Test
        @DisplayName("음수 값이면 예외가 발생한다")
        void throwExceptionForNegative() {
            assertThatThrownBy(() -> Priority.of(-1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("우선순위");
        }

        @Test
        @DisplayName("100 초과 시 예외가 발생한다")
        void throwExceptionForOverHundred() {
            assertThatThrownBy(() -> Priority.of(101))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("우선순위");
        }

        @Test
        @DisplayName("기본 우선순위는 0이다")
        void defaultPriorityIsZero() {
            var priority = Priority.defaultPriority();
            assertThat(priority.value()).isZero();
        }

        @Test
        @DisplayName("isHigherThan()으로 우선순위를 비교한다")
        void isHigherThanComparison() {
            var high = Priority.of(80);
            var low = Priority.of(20);
            assertThat(high.isHigherThan(low)).isTrue();
            assertThat(low.isHigherThan(high)).isFalse();
        }
    }

    @Nested
    @DisplayName("DiscountPeriod 테스트")
    class DiscountPeriodTest {

        @Test
        @DisplayName("유효한 기간을 생성한다")
        void createValidPeriod() {
            Instant start = Instant.now().minusSeconds(3600);
            Instant end = Instant.now().plusSeconds(3600);
            var period = DiscountPeriod.of(start, end);
            assertThat(period.startAt()).isEqualTo(start);
            assertThat(period.endAt()).isEqualTo(end);
        }

        @Test
        @DisplayName("startAt이 null이면 예외가 발생한다")
        void throwExceptionForNullStart() {
            assertThatThrownBy(() -> DiscountPeriod.of(null, Instant.now()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("시작 시점");
        }

        @Test
        @DisplayName("endAt이 null이면 예외가 발생한다")
        void throwExceptionForNullEnd() {
            assertThatThrownBy(() -> DiscountPeriod.of(Instant.now(), null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("종료 시점");
        }

        @Test
        @DisplayName("startAt >= endAt이면 예외가 발생한다")
        void throwExceptionForInvalidRange() {
            Instant now = Instant.now();
            assertThatThrownBy(() -> DiscountPeriod.of(now, now))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이전");
        }

        @Test
        @DisplayName("isActiveAt()은 기간 내 시점에서 true를 반환한다")
        void isActiveAtReturnsTrueForActiveTime() {
            Instant start = Instant.now().minusSeconds(3600);
            Instant end = Instant.now().plusSeconds(3600);
            var period = DiscountPeriod.of(start, end);
            assertThat(period.isActiveAt(Instant.now())).isTrue();
        }

        @Test
        @DisplayName("isActiveAt()은 기간 후 시점에서 false를 반환한다")
        void isActiveAtReturnsFalseForExpiredTime() {
            Instant start = Instant.now().minusSeconds(7200);
            Instant end = Instant.now().minusSeconds(3600);
            var period = DiscountPeriod.of(start, end);
            assertThat(period.isActiveAt(Instant.now())).isFalse();
        }

        @Test
        @DisplayName("isNotStarted()은 시작 전 시점에서 true를 반환한다")
        void isNotStartedReturnsTrueBeforeStart() {
            Instant start = Instant.now().plusSeconds(3600);
            Instant end = Instant.now().plusSeconds(7200);
            var period = DiscountPeriod.of(start, end);
            assertThat(period.isNotStarted(Instant.now())).isTrue();
        }

        @Test
        @DisplayName("isExpired()은 종료 후 시점에서 true를 반환한다")
        void isExpiredReturnsTrueAfterEnd() {
            Instant start = Instant.now().minusSeconds(7200);
            Instant end = Instant.now().minusSeconds(3600);
            var period = DiscountPeriod.of(start, end);
            assertThat(period.isExpired(Instant.now())).isTrue();
        }
    }

    @Nested
    @DisplayName("DiscountBudget 테스트")
    class DiscountBudgetTest {

        @Test
        @DisplayName("총 예산만으로 생성하면 사용 예산은 0이다")
        void createWithTotalOnly() {
            var budget = DiscountBudget.of(Money.of(100000));
            assertThat(budget.totalBudget()).isEqualTo(Money.of(100000));
            assertThat(budget.usedBudget()).isEqualTo(Money.zero());
        }

        @Test
        @DisplayName("totalBudget이 null이면 예외가 발생한다")
        void throwExceptionForNullTotal() {
            assertThatThrownBy(() -> DiscountBudget.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("총 예산");
        }

        @Test
        @DisplayName("사용 예산이 총 예산을 초과하면 예외가 발생한다")
        void throwExceptionForExcessiveUsed() {
            assertThatThrownBy(() -> DiscountBudget.of(Money.of(100), Money.of(200)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("초과");
        }

        @Test
        @DisplayName("consume()은 사용 예산을 증가시킨다")
        void consumeIncreaseUsedBudget() {
            var budget = DiscountBudget.of(Money.of(100000));
            var consumed = budget.consume(Money.of(30000));
            assertThat(consumed.usedBudget()).isEqualTo(Money.of(30000));
            assertThat(consumed.remaining()).isEqualTo(Money.of(70000));
        }

        @Test
        @DisplayName("consume()으로 예산 초과 시 예외가 발생한다")
        void consumeThrowsForExcessive() {
            var budget = DiscountBudget.of(Money.of(100));
            assertThatThrownBy(() -> budget.consume(Money.of(200)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("부족");
        }

        @Test
        @DisplayName("hasSufficient()는 잔여 예산이 충분하면 true를 반환한다")
        void hasSufficientReturnsTrueForSufficientBudget() {
            var budget = DiscountBudget.of(Money.of(100000));
            assertThat(budget.hasSufficient(Money.of(50000))).isTrue();
        }

        @Test
        @DisplayName("hasSufficient()는 잔여 예산이 부족하면 false를 반환한다")
        void hasSufficientReturnsFalseForInsufficientBudget() {
            var budget = DiscountBudget.of(Money.of(100), Money.of(90));
            assertThat(budget.hasSufficient(Money.of(20))).isFalse();
        }

        @Test
        @DisplayName("isExhausted()는 예산 소진 시 true를 반환한다")
        void isExhaustedReturnsTrueWhenFullyUsed() {
            var budget = DiscountBudget.of(Money.of(100), Money.of(100));
            assertThat(budget.isExhausted()).isTrue();
        }
    }

    @Nested
    @DisplayName("IssuanceLimit 테스트")
    class IssuanceLimitTest {

        @Test
        @DisplayName("유효한 발급 제한을 생성한다")
        void createValidLimit() {
            var limit = IssuanceLimit.of(1000, 3);
            assertThat(limit.totalCount()).isEqualTo(1000);
            assertThat(limit.perUserCount()).isEqualTo(3);
        }

        @Test
        @DisplayName("totalCount가 0 이하면 예외가 발생한다")
        void throwExceptionForZeroTotal() {
            assertThatThrownBy(() -> IssuanceLimit.of(0, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("총 발급 수량");
        }

        @Test
        @DisplayName("perUserCount가 0 이하면 예외가 발생한다")
        void throwExceptionForZeroPerUser() {
            assertThatThrownBy(() -> IssuanceLimit.of(100, 0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("인당 발급 수량");
        }

        @Test
        @DisplayName("perUserCount > totalCount이면 예외가 발생한다")
        void throwExceptionForPerUserExceedsTotal() {
            assertThatThrownBy(() -> IssuanceLimit.of(5, 10))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("초과");
        }

        @Test
        @DisplayName("canIssue()는 발급 가능 시 true를 반환한다")
        void canIssueReturnsTrueWhenAvailable() {
            var limit = IssuanceLimit.of(100, 1);
            assertThat(limit.canIssue(99)).isTrue();
        }

        @Test
        @DisplayName("canIssue()는 총 수량 도달 시 false를 반환한다")
        void canIssueReturnsFalseWhenFull() {
            var limit = IssuanceLimit.of(100, 1);
            assertThat(limit.canIssue(100)).isFalse();
        }

        @Test
        @DisplayName("canIssueToUser()는 인당 수량 미달 시 true를 반환한다")
        void canIssueToUserReturnsTrueWhenAvailable() {
            var limit = IssuanceLimit.of(100, 3);
            assertThat(limit.canIssueToUser(2)).isTrue();
        }

        @Test
        @DisplayName("canIssueToUser()는 인당 수량 도달 시 false를 반환한다")
        void canIssueToUserReturnsFalseWhenLimitReached() {
            var limit = IssuanceLimit.of(100, 3);
            assertThat(limit.canIssueToUser(3)).isFalse();
        }
    }

    @Nested
    @DisplayName("AppliedDiscount 테스트")
    class AppliedDiscountTest {

        @Test
        @DisplayName("유효한 할인 내역을 생성한다")
        void createValidAppliedDiscount() {
            var applied =
                    AppliedDiscount.of(
                            DiscountPolicyId.of(1L),
                            StackingGroup.PLATFORM_INSTANT,
                            Money.of(5000),
                            0.5);
            assertThat(applied.discountPolicyId()).isEqualTo(DiscountPolicyId.of(1L));
            assertThat(applied.stackingGroup()).isEqualTo(StackingGroup.PLATFORM_INSTANT);
            assertThat(applied.amount()).isEqualTo(Money.of(5000));
            assertThat(applied.shareRatio()).isEqualTo(0.5);
        }

        @Test
        @DisplayName("discountPolicyId가 null이면 예외가 발생한다")
        void throwExceptionForNullPolicyId() {
            assertThatThrownBy(
                            () ->
                                    AppliedDiscount.of(
                                            null,
                                            StackingGroup.PLATFORM_INSTANT,
                                            Money.of(5000),
                                            0.5))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("할인 정책 ID");
        }

        @Test
        @DisplayName("shareRatio가 1.0 초과 시 예외가 발생한다")
        void throwExceptionForInvalidShareRatio() {
            assertThatThrownBy(
                            () ->
                                    AppliedDiscount.of(
                                            DiscountPolicyId.of(1L),
                                            StackingGroup.PLATFORM_INSTANT,
                                            Money.of(5000),
                                            1.1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("할인 비율");
        }
    }

    @Nested
    @DisplayName("DiscountedPrice 테스트")
    class DiscountedPriceTest {

        @Test
        @DisplayName("noDiscount()는 할인 없는 결과를 생성한다")
        void noDiscountCreatesEmptyResult() {
            var result = DiscountedPrice.noDiscount(Money.of(10000));
            assertThat(result.salePrice()).isEqualTo(Money.of(10000));
            assertThat(result.totalDiscountRate()).isZero();
            assertThat(result.hasDiscount()).isFalse();
            assertThat(result.appliedDiscounts()).isEmpty();
        }

        @Test
        @DisplayName("salePrice가 null이면 예외가 발생한다")
        void throwExceptionForNullSalePrice() {
            assertThatThrownBy(() -> DiscountedPrice.of(null, 0, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("할인 가격");
        }

        @Test
        @DisplayName("totalDiscountRate가 범위 외이면 예외가 발생한다")
        void throwExceptionForInvalidRate() {
            assertThatThrownBy(() -> DiscountedPrice.of(Money.of(10000), 101, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("할인율");
        }

        @Test
        @DisplayName("totalDiscountAmount()는 적용된 할인 합계를 반환한다")
        void totalDiscountAmountReturnsSum() {
            var discounts =
                    java.util.List.of(
                            AppliedDiscount.of(
                                    DiscountPolicyId.of(1L),
                                    StackingGroup.SELLER_INSTANT,
                                    Money.of(1000),
                                    0.4),
                            AppliedDiscount.of(
                                    DiscountPolicyId.of(2L),
                                    StackingGroup.PLATFORM_INSTANT,
                                    Money.of(1500),
                                    0.6));
            var result = DiscountedPrice.of(Money.of(7500), 25, discounts);
            assertThat(result.totalDiscountAmount()).isEqualTo(Money.of(2500));
            assertThat(result.hasDiscount()).isTrue();
        }
    }

    @Nested
    @DisplayName("StackingGroup 테스트")
    class StackingGroupTest {

        @Test
        @DisplayName("SELLER_INSTANT의 applicationOrder는 1이다")
        void sellerInstantOrderIs1() {
            assertThat(StackingGroup.SELLER_INSTANT.applicationOrder()).isEqualTo(1);
        }

        @Test
        @DisplayName("PLATFORM_INSTANT의 applicationOrder는 2이다")
        void platformInstantOrderIs2() {
            assertThat(StackingGroup.PLATFORM_INSTANT.applicationOrder()).isEqualTo(2);
        }

        @Test
        @DisplayName("COUPON의 applicationOrder는 3이다")
        void couponOrderIs3() {
            assertThat(StackingGroup.COUPON.applicationOrder()).isEqualTo(3);
        }
    }
}
