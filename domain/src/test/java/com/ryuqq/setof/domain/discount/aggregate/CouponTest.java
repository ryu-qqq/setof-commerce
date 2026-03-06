package com.ryuqq.setof.domain.discount.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.id.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.vo.CouponType;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("Coupon Aggregate 테스트")
class CouponTest {

    @Nested
    @DisplayName("forNew() - 신규 쿠폰 생성")
    class ForNewTest {

        @Test
        @DisplayName("DOWNLOAD 타입 쿠폰을 생성한다")
        void createDownloadCoupon() {
            var coupon = DiscountFixtures.newDownloadCoupon();

            assertThat(coupon.isNew()).isTrue();
            assertThat(coupon.couponNameValue()).isEqualTo(DiscountFixtures.DEFAULT_COUPON_NAME);
            assertThat(coupon.couponType()).isEqualTo(CouponType.DOWNLOAD);
            assertThat(coupon.couponCodeValue()).isNull();
            assertThat(coupon.issuedCount()).isZero();
            assertThat(coupon.isActive()).isTrue();
            assertThat(coupon.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("CODE 타입 쿠폰을 코드와 함께 생성한다")
        void createCodeCoupon() {
            var coupon = DiscountFixtures.newCodeCoupon();

            assertThat(coupon.couponType()).isEqualTo(CouponType.CODE);
            assertThat(coupon.couponCodeValue()).isEqualTo(DiscountFixtures.DEFAULT_COUPON_CODE);
        }

        @Test
        @DisplayName("CODE 타입에 couponCode 없으면 예외가 발생한다")
        void throwForCodeWithoutCode() {
            assertThatThrownBy(
                            () ->
                                    Coupon.forNew(
                                            DiscountPolicyId.of(10L),
                                            DiscountFixtures.defaultCouponName(),
                                            null,
                                            CouponType.CODE,
                                            null,
                                            DiscountFixtures.defaultIssuanceLimit(),
                                            DiscountFixtures.defaultActivePeriod(),
                                            CommonVoFixtures.now()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("코드가 필수");
        }

        @Test
        @DisplayName("DOWNLOAD 타입에 couponCode null은 허용된다")
        void downloadWithoutCodeIsAllowed() {
            var coupon = DiscountFixtures.newDownloadCoupon();
            assertThat(coupon.couponCode()).isNull();
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("활성 쿠폰을 복원한다")
        void reconstituteActiveCoupon() {
            var coupon = DiscountFixtures.activeDownloadCoupon();

            assertThat(coupon.isNew()).isFalse();
            assertThat(coupon.idValue()).isEqualTo(1L);
            assertThat(coupon.isActive()).isTrue();
            assertThat(coupon.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("코드 쿠폰을 복원한다")
        void reconstituteCodeCoupon() {
            var coupon = DiscountFixtures.activeCodeCoupon();

            assertThat(coupon.couponType()).isEqualTo(CouponType.CODE);
            assertThat(coupon.couponCodeValue()).isEqualTo(DiscountFixtures.DEFAULT_COUPON_CODE);
        }
    }

    @Nested
    @DisplayName("update() - 쿠폰 수정")
    class UpdateTest {

        @Test
        @DisplayName("쿠폰 정보를 수정한다")
        void updateCoupon() {
            var coupon = DiscountFixtures.activeDownloadCoupon();
            var updateData = DiscountFixtures.couponUpdateData();
            Instant now = CommonVoFixtures.now();

            coupon.update(updateData, now);

            assertThat(coupon.couponNameValue()).isEqualTo("수정된쿠폰명");
            assertThat(coupon.description()).isEqualTo("수정된 쿠폰 설명");
            assertThat(coupon.totalCount()).isEqualTo(2000);
            assertThat(coupon.perUserCount()).isEqualTo(2);
            assertThat(coupon.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("발급 관리 테스트")
    class IssuanceTest {

        @Test
        @DisplayName("canIssue()는 발급 가능 시 true를 반환한다")
        void canIssueReturnsTrueWhenAvailable() {
            var coupon = DiscountFixtures.activeDownloadCoupon();
            assertThat(coupon.canIssue()).isTrue();
        }

        @Test
        @DisplayName("canIssue()는 총 수량 소진 시 false를 반환한다")
        void canIssueReturnsFalseWhenFull() {
            var coupon = DiscountFixtures.fullIssuedCoupon();
            assertThat(coupon.canIssue()).isFalse();
        }

        @Test
        @DisplayName("canIssueToUser()는 인당 수량 미달 시 true를 반환한다")
        void canIssueToUserReturnsTrueWhenAvailable() {
            var coupon = DiscountFixtures.activeDownloadCoupon();
            assertThat(coupon.canIssueToUser(0)).isTrue();
        }

        @Test
        @DisplayName("canIssueToUser()는 인당 수량 도달 시 false를 반환한다")
        void canIssueToUserReturnsFalseWhenLimitReached() {
            var coupon = DiscountFixtures.activeDownloadCoupon();
            assertThat(coupon.canIssueToUser(DiscountFixtures.DEFAULT_PER_USER_COUNT)).isFalse();
        }

        @Test
        @DisplayName("incrementIssuedCount()는 발급 카운트를 증가시킨다")
        void incrementIssuedCount() {
            var coupon = DiscountFixtures.activeDownloadCoupon();
            Instant now = CommonVoFixtures.now();

            coupon.incrementIssuedCount(now);

            assertThat(coupon.issuedCount()).isEqualTo(1);
            assertThat(coupon.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("matchesCode() - 코드 매칭")
    class MatchesCodeTest {

        @Test
        @DisplayName("CODE 타입에서 대소문자 무시 매칭된다")
        void matchesCodeIgnoreCase() {
            var coupon = DiscountFixtures.activeCodeCoupon();
            assertThat(coupon.matchesCode("test1234")).isTrue();
            assertThat(coupon.matchesCode("TEST1234")).isTrue();
        }

        @Test
        @DisplayName("CODE 타입에서 다른 코드는 매칭되지 않는다")
        void doesNotMatchDifferentCode() {
            var coupon = DiscountFixtures.activeCodeCoupon();
            assertThat(coupon.matchesCode("OTHER123")).isFalse();
        }

        @Test
        @DisplayName("DOWNLOAD 타입에서는 항상 false를 반환한다")
        void downloadTypeReturnsFalse() {
            var coupon = DiscountFixtures.activeDownloadCoupon();
            assertThat(coupon.matchesCode("TEST1234")).isFalse();
        }
    }

    @Nested
    @DisplayName("activate() / deactivate() / delete() - 생명주기")
    class LifecycleTest {

        @Test
        @DisplayName("쿠폰을 활성화한다")
        void activateCoupon() {
            var coupon = DiscountFixtures.activeDownloadCoupon();
            coupon.deactivate(CommonVoFixtures.now());
            Instant now = CommonVoFixtures.now();

            coupon.activate(now);

            assertThat(coupon.isActive()).isTrue();
            assertThat(coupon.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("쿠폰을 비활성화한다")
        void deactivateCoupon() {
            var coupon = DiscountFixtures.activeDownloadCoupon();
            Instant now = CommonVoFixtures.now();

            coupon.deactivate(now);

            assertThat(coupon.isActive()).isFalse();
            assertThat(coupon.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("쿠폰을 삭제한다")
        void deleteCoupon() {
            var coupon = DiscountFixtures.activeDownloadCoupon();
            Instant now = CommonVoFixtures.now();

            coupon.delete(now);

            assertThat(coupon.isDeleted()).isTrue();
            assertThat(coupon.deletedAt()).isEqualTo(now);
            assertThat(coupon.isActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("discountPolicyId()를 반환한다")
        void returnsDiscountPolicyId() {
            var coupon = DiscountFixtures.activeDownloadCoupon();
            assertThat(coupon.discountPolicyId()).isNotNull();
            assertThat(coupon.discountPolicyIdValue()).isEqualTo(10L);
        }

        @Test
        @DisplayName("issuanceLimit()를 반환한다")
        void returnsIssuanceLimit() {
            var coupon = DiscountFixtures.activeDownloadCoupon();
            assertThat(coupon.issuanceLimit()).isNotNull();
            assertThat(coupon.totalCount()).isEqualTo(DiscountFixtures.DEFAULT_TOTAL_COUNT);
            assertThat(coupon.perUserCount()).isEqualTo(DiscountFixtures.DEFAULT_PER_USER_COUNT);
        }

        @Test
        @DisplayName("usagePeriod()를 반환한다")
        void returnsUsagePeriod() {
            var coupon = DiscountFixtures.activeDownloadCoupon();
            assertThat(coupon.usagePeriod()).isNotNull();
        }

        @Test
        @DisplayName("createdAt()을 반환한다")
        void returnsCreatedAt() {
            var coupon = DiscountFixtures.activeDownloadCoupon();
            assertThat(coupon.createdAt()).isNotNull();
        }
    }
}
