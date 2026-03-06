package com.ryuqq.setof.domain.discount.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.vo.CouponStatus;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("IssuedCoupon Aggregate 테스트")
class IssuedCouponTest {

    @Nested
    @DisplayName("issue() - 쿠폰 발급")
    class IssueTest {

        @Test
        @DisplayName("쿠폰을 발급한다")
        void issueCoupon() {
            var issued = DiscountFixtures.issuedCoupon();

            assertThat(issued.isNew()).isTrue();
            assertThat(issued.userId()).isEqualTo(DiscountFixtures.DEFAULT_USER_ID);
            assertThat(issued.status()).isEqualTo(CouponStatus.ISSUED);
            assertThat(issued.usedAt()).isNull();
            assertThat(issued.orderId()).isNull();
            assertThat(issued.issuedAt()).isNotNull();
            assertThat(issued.expireAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("ISSUED 상태로 복원한다")
        void reconstituteIssuedCoupon() {
            var issued = DiscountFixtures.activeIssuedCoupon();

            assertThat(issued.isNew()).isFalse();
            assertThat(issued.idValue()).isEqualTo(1L);
            assertThat(issued.status()).isEqualTo(CouponStatus.ISSUED);
        }

        @Test
        @DisplayName("USED 상태로 복원한다")
        void reconstituteUsedCoupon() {
            var issued = DiscountFixtures.usedIssuedCoupon();

            assertThat(issued.status()).isEqualTo(CouponStatus.USED);
            assertThat(issued.usedAt()).isNotNull();
            assertThat(issued.orderId()).isEqualTo(DiscountFixtures.DEFAULT_ORDER_ID);
        }

        @Test
        @DisplayName("EXPIRED 상태로 복원한다")
        void reconstituteExpiredCoupon() {
            var issued = DiscountFixtures.expiredIssuedCoupon();
            assertThat(issued.status()).isEqualTo(CouponStatus.EXPIRED);
        }

        @Test
        @DisplayName("CANCELLED 상태로 복원한다")
        void reconstituteCancelledCoupon() {
            var issued = DiscountFixtures.cancelledIssuedCoupon();
            assertThat(issued.status()).isEqualTo(CouponStatus.CANCELLED);
        }
    }

    @Nested
    @DisplayName("use() - 쿠폰 사용 (ISSUED → USED)")
    class UseTest {

        @Test
        @DisplayName("ISSUED 상태에서 쿠폰을 사용한다")
        void useCoupon() {
            var issued = DiscountFixtures.activeIssuedCoupon();
            Instant now = CommonVoFixtures.now();

            issued.use(DiscountFixtures.DEFAULT_ORDER_ID, now);

            assertThat(issued.status()).isEqualTo(CouponStatus.USED);
            assertThat(issued.orderId()).isEqualTo(DiscountFixtures.DEFAULT_ORDER_ID);
            assertThat(issued.usedAt()).isEqualTo(now);
            assertThat(issued.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("USED 상태에서 사용하면 예외가 발생한다")
        void throwForUsedState() {
            var issued = DiscountFixtures.usedIssuedCoupon();

            assertThatThrownBy(() -> issued.use(999L, CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("USED");
        }

        @Test
        @DisplayName("EXPIRED 상태에서 사용하면 예외가 발생한다")
        void throwForExpiredState() {
            var issued = DiscountFixtures.expiredIssuedCoupon();

            assertThatThrownBy(() -> issued.use(999L, CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("CANCELLED 상태에서 사용하면 예외가 발생한다")
        void throwForCancelledState() {
            var issued = DiscountFixtures.cancelledIssuedCoupon();

            assertThatThrownBy(() -> issued.use(999L, CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("만료된 쿠폰을 사용하면 예외가 발생한다")
        void throwForExpiredCoupon() {
            var issued = DiscountFixtures.activeIssuedCoupon();
            Instant farFuture = Instant.now().plusSeconds(864000);

            assertThatThrownBy(() -> issued.use(999L, farFuture))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("만료");
        }
    }

    @Nested
    @DisplayName("expire() - 쿠폰 만료 (ISSUED → EXPIRED)")
    class ExpireTest {

        @Test
        @DisplayName("ISSUED 상태에서 만료 처리한다")
        void expireCoupon() {
            var issued = DiscountFixtures.activeIssuedCoupon();
            Instant now = CommonVoFixtures.now();

            issued.expire(now);

            assertThat(issued.status()).isEqualTo(CouponStatus.EXPIRED);
            assertThat(issued.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("USED 상태에서 만료 처리하면 예외가 발생한다")
        void throwForUsedState() {
            var issued = DiscountFixtures.usedIssuedCoupon();

            assertThatThrownBy(() -> issued.expire(CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("USED");
        }
    }

    @Nested
    @DisplayName("cancel() - 쿠폰 취소 (ISSUED → CANCELLED)")
    class CancelTest {

        @Test
        @DisplayName("ISSUED 상태에서 취소한다")
        void cancelCoupon() {
            var issued = DiscountFixtures.activeIssuedCoupon();
            Instant now = CommonVoFixtures.now();

            issued.cancel(now);

            assertThat(issued.status()).isEqualTo(CouponStatus.CANCELLED);
            assertThat(issued.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("USED 상태에서 취소하면 예외가 발생한다")
        void throwForUsedState() {
            var issued = DiscountFixtures.usedIssuedCoupon();

            assertThatThrownBy(() -> issued.cancel(CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    @Nested
    @DisplayName("cancelUsage() - 사용 취소 (USED → ISSUED)")
    class CancelUsageTest {

        @Test
        @DisplayName("USED 상태에서 사용 취소한다")
        void cancelUsage() {
            var issued = DiscountFixtures.usedIssuedCoupon();
            Instant now = CommonVoFixtures.now();

            issued.cancelUsage(now);

            assertThat(issued.status()).isEqualTo(CouponStatus.ISSUED);
            assertThat(issued.orderId()).isNull();
            assertThat(issued.usedAt()).isNull();
            assertThat(issued.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("ISSUED 상태에서 사용 취소하면 예외가 발생한다")
        void throwForIssuedState() {
            var issued = DiscountFixtures.activeIssuedCoupon();

            assertThatThrownBy(() -> issued.cancelUsage(CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("ISSUED");
        }

        @Test
        @DisplayName("EXPIRED 상태에서 사용 취소하면 예외가 발생한다")
        void throwForExpiredState() {
            var issued = DiscountFixtures.expiredIssuedCoupon();

            assertThatThrownBy(() -> issued.cancelUsage(CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    @Nested
    @DisplayName("isUsable() - 사용 가능 여부")
    class IsUsableTest {

        @Test
        @DisplayName("ISSUED 상태 + 미만료이면 사용 가능하다")
        void usableWhenIssuedAndNotExpired() {
            var issued = DiscountFixtures.activeIssuedCoupon();
            assertThat(issued.isUsable(Instant.now())).isTrue();
        }

        @Test
        @DisplayName("USED 상태이면 사용 불가하다")
        void notUsableWhenUsed() {
            var issued = DiscountFixtures.usedIssuedCoupon();
            assertThat(issued.isUsable(Instant.now())).isFalse();
        }

        @Test
        @DisplayName("만료 시점 이후이면 사용 불가하다")
        void notUsableWhenExpired() {
            var issued = DiscountFixtures.activeIssuedCoupon();
            Instant farFuture = Instant.now().plusSeconds(864000);
            assertThat(issued.isUsable(farFuture)).isFalse();
        }
    }

    @Nested
    @DisplayName("isExpired() - 만료 확인")
    class IsExpiredTest {

        @Test
        @DisplayName("만료 시점 전이면 false를 반환한다")
        void notExpiredBeforeExpireAt() {
            var issued = DiscountFixtures.activeIssuedCoupon();
            assertThat(issued.isExpired(Instant.now())).isFalse();
        }

        @Test
        @DisplayName("만료 시점 이후이면 true를 반환한다")
        void expiredAfterExpireAt() {
            var issued = DiscountFixtures.activeIssuedCoupon();
            Instant farFuture = Instant.now().plusSeconds(864000);
            assertThat(issued.isExpired(farFuture)).isTrue();
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("couponId()를 반환한다")
        void returnsCouponId() {
            var issued = DiscountFixtures.activeIssuedCoupon();
            assertThat(issued.couponId()).isNotNull();
            assertThat(issued.couponIdValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("discountPolicyId()를 반환한다")
        void returnsDiscountPolicyId() {
            var issued = DiscountFixtures.activeIssuedCoupon();
            assertThat(issued.discountPolicyId()).isNotNull();
            assertThat(issued.discountPolicyIdValue()).isEqualTo(10L);
        }

        @Test
        @DisplayName("userId()를 반환한다")
        void returnsUserId() {
            var issued = DiscountFixtures.activeIssuedCoupon();
            assertThat(issued.userId()).isEqualTo(DiscountFixtures.DEFAULT_USER_ID);
        }

        @Test
        @DisplayName("createdAt()을 반환한다")
        void returnsCreatedAt() {
            var issued = DiscountFixtures.activeIssuedCoupon();
            assertThat(issued.createdAt()).isNotNull();
        }
    }
}
