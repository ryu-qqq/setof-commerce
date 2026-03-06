package com.ryuqq.setof.domain.productnotice.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productnotice.id.NoticeFieldId;
import com.ryuqq.setof.domain.productnotice.vo.NoticeFieldValue;
import com.setof.commerce.domain.productnotice.ProductNoticeFixtures;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductNotice Aggregate 테스트")
class ProductNoticeTest {

    @Nested
    @DisplayName("forNew() - 신규 상품고시 생성")
    class ForNewTest {

        @Test
        @DisplayName("기본 엔트리로 신규 상품고시를 생성한다")
        void createNewNotice() {
            // when
            var notice = ProductNoticeFixtures.newNotice();

            // then
            assertThat(notice.isNew()).isTrue();
            assertThat(notice.productGroupIdValue())
                    .isEqualTo(ProductNoticeFixtures.DEFAULT_PRODUCT_GROUP_ID);
            assertThat(notice.entries()).hasSize(4);
            assertThat(notice.createdAt()).isNotNull();
            assertThat(notice.updatedAt()).isNotNull();
        }

        @Test
        @DisplayName("특정 ProductGroupId로 신규 상품고시를 생성한다")
        void createWithSpecificProductGroupId() {
            // given
            ProductGroupId groupId = ProductGroupId.of(999L);

            // when
            var notice = ProductNoticeFixtures.newNotice(groupId);

            // then
            assertThat(notice.isNew()).isTrue();
            assertThat(notice.productGroupIdValue()).isEqualTo(999L);
        }

        @Test
        @DisplayName("엔트리 목록을 포함하여 생성한다")
        void createWithEntries() {
            // given
            var entries =
                    List.of(
                            ProductNoticeEntry.forNew(
                                    NoticeFieldId.forNew(),
                                    NoticeFieldValue.of("소재", "면 100%"),
                                    1));

            // when
            var notice =
                    ProductNotice.forNew(ProductGroupId.of(1L), entries, CommonVoFixtures.now());

            // then
            assertThat(notice.entries()).hasSize(1);
            assertThat(notice.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("신규 생성 시 isNew()는 true를 반환한다")
        void isNewReturnsTrueForNewNotice() {
            // when
            var notice = ProductNoticeFixtures.newNotice();

            // then
            assertThat(notice.isNew()).isTrue();
            assertThat(notice.id().isNew()).isTrue();
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("엔트리를 포함한 상품고시를 복원한다")
        void reconstituteWithEntries() {
            // when
            var notice = ProductNoticeFixtures.activeNotice();

            // then
            assertThat(notice.isNew()).isFalse();
            assertThat(notice.idValue()).isEqualTo(1L);
            assertThat(notice.entries()).hasSize(4);
            assertThat(notice.createdAt()).isNotNull();
            assertThat(notice.updatedAt()).isNotNull();
        }

        @Test
        @DisplayName("특정 ID로 상품고시를 복원한다")
        void reconstituteWithSpecificId() {
            // when
            var notice = ProductNoticeFixtures.activeNotice(42L);

            // then
            assertThat(notice.idValue()).isEqualTo(42L);
            assertThat(notice.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("replaceEntries() - 고시 항목 교체")
    class ReplaceEntriesTest {

        @Test
        @DisplayName("새로운 엔트리 목록으로 교체한다")
        void replaceWithNewEntries() {
            // given
            var notice = ProductNoticeFixtures.activeNotice();
            var newEntries =
                    List.of(
                            ProductNoticeEntry.forNew(
                                    NoticeFieldId.forNew(), NoticeFieldValue.of("색상", "블랙"), 1),
                            ProductNoticeEntry.forNew(
                                    NoticeFieldId.forNew(), NoticeFieldValue.of("사이즈", "FREE"), 2));
            Instant now = CommonVoFixtures.now();

            // when
            notice.replaceEntries(newEntries, now);

            // then
            assertThat(notice.entries()).hasSize(2);
            assertThat(notice.entries().get(0).fieldName()).isEqualTo("색상");
            assertThat(notice.entries().get(1).fieldName()).isEqualTo("사이즈");
        }

        @Test
        @DisplayName("null을 전달하면 빈 엔트리 목록이 된다")
        void replaceWithNullClearsEntries() {
            // given
            var notice = ProductNoticeFixtures.activeNotice();
            Instant now = CommonVoFixtures.now();

            // when
            notice.replaceEntries(null, now);

            // then
            assertThat(notice.entries()).isEmpty();
            assertThat(notice.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("교체 시 updatedAt이 갱신된다")
        void replaceUpdatesTimestamp() {
            // given
            var notice = ProductNoticeFixtures.activeNotice();
            Instant before = notice.updatedAt();
            Instant now = CommonVoFixtures.now();

            // when
            notice.replaceEntries(List.of(), now);

            // then
            assertThat(notice.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("isEmpty() - 엔트리 비어있는지 확인")
    class IsEmptyTest {

        @Test
        @DisplayName("엔트리가 있으면 false를 반환한다")
        void returnsFalseWhenEntriesExist() {
            // given
            var notice = ProductNoticeFixtures.activeNotice();

            // then
            assertThat(notice.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("엔트리가 비어있으면 true를 반환한다")
        void returnsTrueWhenEmpty() {
            // given
            var notice = ProductNoticeFixtures.emptyNotice();

            // then
            assertThat(notice.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("entryCount() - 엔트리 수 확인")
    class EntryCountTest {

        @Test
        @DisplayName("엔트리 수를 정확히 반환한다")
        void returnsCorrectCount() {
            // given
            var notice = ProductNoticeFixtures.activeNotice();

            // then
            assertThat(notice.entryCount()).isEqualTo(4);
        }

        @Test
        @DisplayName("비어있는 고시는 0을 반환한다")
        void returnsZeroForEmpty() {
            // given
            var notice = ProductNoticeFixtures.emptyNotice();

            // then
            assertThat(notice.entryCount()).isZero();
        }
    }

    @Nested
    @DisplayName("Accessor 메서드 테스트")
    class AccessorTest {

        @Test
        @DisplayName("id()는 ProductNoticeId를 반환한다")
        void returnsId() {
            // given
            var notice = ProductNoticeFixtures.activeNotice();

            // then
            assertThat(notice.id()).isNotNull();
            assertThat(notice.idValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("productGroupId()는 ProductGroupId를 반환한다")
        void returnsProductGroupId() {
            // given
            var notice = ProductNoticeFixtures.activeNotice();

            // then
            assertThat(notice.productGroupId()).isNotNull();
            assertThat(notice.productGroupIdValue())
                    .isEqualTo(ProductNoticeFixtures.DEFAULT_PRODUCT_GROUP_ID);
        }

        @Test
        @DisplayName("entries()는 수정 불가능한 리스트를 반환한다")
        void returnsUnmodifiableEntries() {
            // given
            var notice = ProductNoticeFixtures.activeNotice();
            var entries = notice.entries();

            // when & then
            assertThat(entries).isNotNull();
            org.assertj.core.api.Assertions.assertThatThrownBy(
                            () -> entries.add(ProductNoticeFixtures.defaultNoticeEntry()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("createdAt()은 생성 시각을 반환한다")
        void returnsCreatedAt() {
            // given
            var notice = ProductNoticeFixtures.activeNotice();

            // then
            assertThat(notice.createdAt()).isNotNull();
        }

        @Test
        @DisplayName("updatedAt()은 수정 시각을 반환한다")
        void returnsUpdatedAt() {
            // given
            var notice = ProductNoticeFixtures.activeNotice();

            // then
            assertThat(notice.updatedAt()).isNotNull();
        }
    }
}
