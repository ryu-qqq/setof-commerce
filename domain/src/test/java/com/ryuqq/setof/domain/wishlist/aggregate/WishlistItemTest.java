package com.ryuqq.setof.domain.wishlist.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.wishlist.id.WishlistItemId;
import com.setof.commerce.domain.wishlist.WishlistFixtures;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("WishlistItem Aggregate 테스트")
class WishlistItemTest {

    @Nested
    @DisplayName("create() - 신규 찜 항목 생성")
    class CreateTest {

        @Test
        @DisplayName("LegacyMemberId와 ProductGroupId로 신규 찜 항목을 생성한다")
        void createNewWishlistItem() {
            // given
            LegacyMemberId legacyMemberId = WishlistFixtures.defaultLegacyMemberId();
            ProductGroupId productGroupId = WishlistFixtures.defaultProductGroupId();

            // when
            WishlistItem item = WishlistItem.create(legacyMemberId, productGroupId);

            // then
            assertThat(item).isNotNull();
            assertThat(item.legacyMemberId()).isEqualTo(legacyMemberId);
            assertThat(item.productGroupId()).isEqualTo(productGroupId);
        }

        @Test
        @DisplayName("신규 생성된 찜 항목은 삭제되지 않은 상태이다")
        void newWishlistItemIsNotDeleted() {
            // when
            WishlistItem item = WishlistFixtures.newWishlistItem();

            // then
            assertThat(item.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("신규 생성된 찜 항목의 ID는 신규 ID이다")
        void newWishlistItemHasNewId() {
            // when
            WishlistItem item = WishlistFixtures.newWishlistItem();

            // then
            assertThat(item.id().isNew()).isTrue();
            assertThat(item.idValue()).isNull();
        }

        @Test
        @DisplayName("신규 생성된 찜 항목의 memberId는 null이다")
        void newWishlistItemHasNullMemberId() {
            // when
            WishlistItem item = WishlistFixtures.newWishlistItem();

            // then
            assertThat(item.memberId()).isNull();
            assertThat(item.memberIdValue()).isNull();
        }

        @Test
        @DisplayName("신규 생성된 찜 항목의 legacyMemberIdValue를 반환한다")
        void newWishlistItemReturnsLegacyMemberIdValue() {
            // given
            LegacyMemberId legacyMemberId = LegacyMemberId.of(2001L);
            WishlistItem item =
                    WishlistItem.create(legacyMemberId, WishlistFixtures.defaultProductGroupId());

            // then
            assertThat(item.legacyMemberIdValue()).isEqualTo(2001L);
        }

        @Test
        @DisplayName("신규 생성된 찜 항목의 productGroupIdValue를 반환한다")
        void newWishlistItemReturnsProductGroupIdValue() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(500L);
            WishlistItem item =
                    WishlistItem.create(WishlistFixtures.defaultLegacyMemberId(), productGroupId);

            // then
            assertThat(item.productGroupIdValue()).isEqualTo(500L);
        }
    }

    @Nested
    @DisplayName("reconstitute() - 기존 데이터 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("모든 필드로 찜 항목을 복원한다")
        void reconstituteWishlistItemWithAllFields() {
            // given
            WishlistItemId id = WishlistItemId.of(10L);
            LegacyMemberId legacyMemberId = LegacyMemberId.of(1001L);
            MemberId memberId = MemberId.of("member-uuid-0001");
            ProductGroupId productGroupId = ProductGroupId.of(100L);
            DeletionStatus deletionStatus = DeletionStatus.active();
            Instant createdAt = CommonVoFixtures.yesterday();

            // when
            WishlistItem item =
                    WishlistItem.reconstitute(
                            id,
                            legacyMemberId,
                            memberId,
                            productGroupId,
                            deletionStatus,
                            createdAt);

            // then
            assertThat(item.id()).isEqualTo(id);
            assertThat(item.idValue()).isEqualTo(10L);
            assertThat(item.legacyMemberId()).isEqualTo(legacyMemberId);
            assertThat(item.legacyMemberIdValue()).isEqualTo(1001L);
            assertThat(item.memberId()).isEqualTo(memberId);
            assertThat(item.memberIdValue()).isEqualTo("member-uuid-0001");
            assertThat(item.productGroupId()).isEqualTo(productGroupId);
            assertThat(item.productGroupIdValue()).isEqualTo(100L);
            assertThat(item.deletionStatus()).isEqualTo(deletionStatus);
            assertThat(item.createdAt()).isEqualTo(createdAt);
        }

        @Test
        @DisplayName("memberId가 null인 상태로 복원한다")
        void reconstituteWishlistItemWithNullMemberId() {
            // given
            WishlistItem item = WishlistFixtures.activeWishlistItemWithoutMemberId();

            // then
            assertThat(item.memberId()).isNull();
            assertThat(item.memberIdValue()).isNull();
        }

        @Test
        @DisplayName("삭제된 상태로 복원한다")
        void reconstituteDeletedWishlistItem() {
            // given
            WishlistItem item = WishlistFixtures.deletedWishlistItem();

            // then
            assertThat(item.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("활성 상태로 복원한다")
        void reconstituteActiveWishlistItem() {
            // given
            WishlistItem item = WishlistFixtures.activeWishlistItem();

            // then
            assertThat(item.isDeleted()).isFalse();
        }
    }

    @Nested
    @DisplayName("delete() - 삭제 상태 변경")
    class DeleteTest {

        @Test
        @DisplayName("활성 상태의 찜 항목을 삭제한다")
        void deleteActiveWishlistItem() {
            // given
            WishlistItem item = WishlistFixtures.activeWishlistItem();
            Instant occurredAt = CommonVoFixtures.now();

            // when
            item.delete(occurredAt);

            // then
            assertThat(item.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("삭제 후 deletionStatus가 삭제 상태로 변경된다")
        void deletionStatusChangesToDeletedAfterDelete() {
            // given
            WishlistItem item = WishlistFixtures.activeWishlistItem();
            assertThat(item.isDeleted()).isFalse();

            // when
            item.delete(CommonVoFixtures.now());

            // then
            assertThat(item.deletionStatus().isDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("isDeleted() - 삭제 상태 확인")
    class IsDeletedTest {

        @Test
        @DisplayName("활성 상태의 찜 항목은 삭제되지 않은 상태이다")
        void activeWishlistItemIsNotDeleted() {
            // given
            WishlistItem item = WishlistFixtures.activeWishlistItem();

            // then
            assertThat(item.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("삭제된 찜 항목은 삭제 상태이다")
        void deletedWishlistItemIsDeleted() {
            // given
            WishlistItem item = WishlistFixtures.deletedWishlistItem();

            // then
            assertThat(item.isDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("값 접근 메서드 테스트")
    class ValueAccessTest {

        @Test
        @DisplayName("idValue()는 ID의 Long 값을 반환한다")
        void idValueReturnsLongValue() {
            // given
            WishlistItem item = WishlistFixtures.activeWishlistItem(42L);

            // then
            assertThat(item.idValue()).isEqualTo(42L);
        }

        @Test
        @DisplayName("legacyMemberIdValue()는 LegacyMemberId의 long 값을 반환한다")
        void legacyMemberIdValueReturnsLongValue() {
            // given
            WishlistItem item = WishlistFixtures.activeWishlistItem();

            // then
            assertThat(item.legacyMemberIdValue()).isEqualTo(1001L);
        }

        @Test
        @DisplayName("productGroupIdValue()는 ProductGroupId의 Long 값을 반환한다")
        void productGroupIdValueReturnsLongValue() {
            // given
            WishlistItem item = WishlistFixtures.activeWishlistItem();

            // then
            assertThat(item.productGroupIdValue()).isEqualTo(100L);
        }

        @Test
        @DisplayName("memberIdValue()는 MemberId가 있을 때 String 값을 반환한다")
        void memberIdValueReturnsStringValueWhenPresent() {
            // given
            WishlistItem item = WishlistFixtures.activeWishlistItem();

            // then
            assertThat(item.memberIdValue()).isEqualTo("member-uuid-0001");
        }

        @Test
        @DisplayName("memberIdValue()는 MemberId가 null일 때 null을 반환한다")
        void memberIdValueReturnsNullWhenAbsent() {
            // given
            WishlistItem item = WishlistFixtures.activeWishlistItemWithoutMemberId();

            // then
            assertThat(item.memberIdValue()).isNull();
        }
    }
}
