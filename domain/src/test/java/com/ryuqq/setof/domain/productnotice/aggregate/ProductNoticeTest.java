package com.ryuqq.setof.domain.productnotice.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.productnotice.ProductNoticeFixture;
import com.ryuqq.setof.domain.productnotice.exception.RequiredNoticeFieldMissingException;
import com.ryuqq.setof.domain.productnotice.vo.NoticeItem;
import com.ryuqq.setof.domain.productnotice.vo.NoticeTemplateId;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ProductNotice Aggregate 테스트")
class ProductNoticeTest {

    private static final Instant NOW = Instant.parse("2024-01-01T00:00:00Z");
    private static final Instant LATER = Instant.parse("2024-01-02T00:00:00Z");

    @Nested
    @DisplayName("create 메서드는")
    class CreateMethod {

        @Test
        @DisplayName("유효한 값으로 ProductNotice를 생성한다")
        void shouldCreateProductNotice() {
            // given
            Long productGroupId = 200L;
            NoticeTemplateId templateId = NoticeTemplateId.of(1L);
            List<NoticeItem> items = ProductNoticeFixture.createClothingItems();

            // when
            ProductNotice notice = ProductNotice.create(productGroupId, templateId, items, NOW);

            // then
            assertThat(notice.getId().isNew()).isTrue();
            assertThat(notice.getProductGroupId()).isEqualTo(200L);
            assertThat(notice.getTemplateIdValue()).isEqualTo(1L);
            assertThat(notice.getItems()).hasSize(4);
            assertThat(notice.getCreatedAt()).isEqualTo(NOW);
            assertThat(notice.getUpdatedAt()).isEqualTo(NOW);
        }

        @Test
        @DisplayName("productGroupId가 null이면 예외를 발생시킨다")
        void shouldThrowWhenProductGroupIdIsNull() {
            // given
            NoticeTemplateId templateId = NoticeTemplateId.of(1L);
            List<NoticeItem> items = ProductNoticeFixture.createClothingItems();

            // when & then
            assertThatThrownBy(() -> ProductNotice.create(null, templateId, items, NOW))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("ProductGroupId is required");
        }

        @Test
        @DisplayName("templateId가 null이면 예외를 발생시킨다")
        void shouldThrowWhenTemplateIdIsNull() {
            // given
            Long productGroupId = 200L;
            List<NoticeItem> items = ProductNoticeFixture.createClothingItems();

            // when & then
            assertThatThrownBy(() -> ProductNotice.create(productGroupId, null, items, NOW))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("TemplateId is required");
        }

        @Test
        @DisplayName("중복된 필드 키가 있으면 예외를 발생시킨다")
        void shouldThrowWhenDuplicateKeys() {
            // given
            Long productGroupId = 200L;
            NoticeTemplateId templateId = NoticeTemplateId.of(1L);
            List<NoticeItem> duplicateItems =
                    List.of(NoticeItem.of("origin", "대한민국", 1), NoticeItem.of("origin", "중국", 2));

            // when & then
            assertThatThrownBy(
                            () ->
                                    ProductNotice.create(
                                            productGroupId, templateId, duplicateItems, NOW))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("중복된 필드 키");
        }
    }

    @Nested
    @DisplayName("createWithValidation 메서드는")
    class CreateWithValidationMethod {

        @Test
        @DisplayName("필수 필드가 모두 있으면 생성에 성공한다")
        void shouldCreateWhenAllRequiredFieldsPresent() {
            // given
            Long productGroupId = 200L;
            NoticeTemplateId templateId = NoticeTemplateId.of(1L);
            List<NoticeItem> items = ProductNoticeFixture.createClothingItems();
            Set<String> requiredKeys = Set.of("origin", "material", "color", "size");

            // when
            ProductNotice notice =
                    ProductNotice.createWithValidation(
                            productGroupId, templateId, items, requiredKeys, NOW);

            // then
            assertThat(notice.getItems()).hasSize(4);
        }

        @Test
        @DisplayName("필수 필드가 누락되면 예외를 발생시킨다")
        void shouldThrowWhenRequiredFieldMissing() {
            // given
            Long productGroupId = 200L;
            NoticeTemplateId templateId = NoticeTemplateId.of(1L);
            List<NoticeItem> incompleteItems = List.of(NoticeItem.of("origin", "대한민국", 1));
            Set<String> requiredKeys = Set.of("origin", "material", "color", "size");

            // when & then
            assertThatThrownBy(
                            () ->
                                    ProductNotice.createWithValidation(
                                            productGroupId,
                                            templateId,
                                            incompleteItems,
                                            requiredKeys,
                                            NOW))
                    .isInstanceOf(RequiredNoticeFieldMissingException.class);
        }

        @Test
        @DisplayName("필수 필드가 비어있으면 예외를 발생시킨다")
        void shouldThrowWhenRequiredFieldEmpty() {
            // given
            Long productGroupId = 200L;
            NoticeTemplateId templateId = NoticeTemplateId.of(1L);
            List<NoticeItem> itemsWithEmpty =
                    List.of(
                            NoticeItem.of("origin", "대한민국", 1),
                            NoticeItem.of("material", "", 2), // 빈 값
                            NoticeItem.of("color", "블랙", 3),
                            NoticeItem.of("size", "M", 4));
            Set<String> requiredKeys = Set.of("origin", "material", "color", "size");

            // when & then
            assertThatThrownBy(
                            () ->
                                    ProductNotice.createWithValidation(
                                            productGroupId,
                                            templateId,
                                            itemsWithEmpty,
                                            requiredKeys,
                                            NOW))
                    .isInstanceOf(RequiredNoticeFieldMissingException.class);
        }
    }

    @Nested
    @DisplayName("reconstitute 메서드는")
    class ReconstituteMethod {

        @Test
        @DisplayName("모든 필드를 복원한다")
        void shouldReconstituteAllFields() {
            // given & when
            ProductNotice notice = ProductNoticeFixture.reconstituteNotice(1L);

            // then
            assertThat(notice.getIdValue()).isEqualTo(1L);
            assertThat(notice.hasId()).isTrue();
        }
    }

    @Nested
    @DisplayName("replaceItems 메서드는")
    class ReplaceItemsMethod {

        @Test
        @DisplayName("고시 항목을 전체 교체한다")
        void shouldReplaceAllItems() {
            // given
            ProductNotice notice = ProductNoticeFixture.createNotice();
            List<NoticeItem> newItems =
                    List.of(
                            NoticeItem.of("newKey1", "새 값1", 1),
                            NoticeItem.of("newKey2", "새 값2", 2));

            // when
            ProductNotice updated = notice.replaceItems(newItems, LATER);

            // then
            assertThat(updated.getItems()).hasSize(2);
            assertThat(updated.containsItem("newKey1")).isTrue();
            assertThat(updated.containsItem("origin")).isFalse();
            assertThat(updated.getUpdatedAt()).isEqualTo(LATER);
        }
    }

    @Nested
    @DisplayName("replaceItemsWithValidation 메서드는")
    class ReplaceItemsWithValidationMethod {

        @Test
        @DisplayName("필수 필드 검증 후 항목을 교체한다")
        void shouldReplaceAfterValidation() {
            // given
            ProductNotice notice = ProductNoticeFixture.createNotice();
            List<NoticeItem> newItems =
                    List.of(
                            NoticeItem.of("origin", "중국", 1),
                            NoticeItem.of("material", "폴리에스터 100%", 2));
            Set<String> requiredKeys = Set.of("origin", "material");

            // when
            ProductNotice updated =
                    notice.replaceItemsWithValidation(newItems, requiredKeys, LATER);

            // then
            assertThat(updated.getItemValue("origin")).hasValue("중국");
            assertThat(updated.getItemValue("material")).hasValue("폴리에스터 100%");
        }

        @Test
        @DisplayName("필수 필드 누락 시 예외를 발생시킨다")
        void shouldThrowWhenRequiredFieldMissing() {
            // given
            ProductNotice notice = ProductNoticeFixture.createNotice();
            List<NoticeItem> incompleteItems = List.of(NoticeItem.of("origin", "중국", 1));
            Set<String> requiredKeys = Set.of("origin", "material");

            // when & then
            assertThatThrownBy(
                            () ->
                                    notice.replaceItemsWithValidation(
                                            incompleteItems, requiredKeys, LATER))
                    .isInstanceOf(RequiredNoticeFieldMissingException.class);
        }
    }

    @Nested
    @DisplayName("updateItemValue 메서드는")
    class UpdateItemValueMethod {

        @Test
        @DisplayName("특정 필드 값을 업데이트한다")
        void shouldUpdateItemValue() {
            // given
            ProductNotice notice = ProductNoticeFixture.createNotice();

            // when
            ProductNotice updated = notice.updateItemValue("origin", "중국", LATER);

            // then
            assertThat(updated.getItemValue("origin")).hasValue("중국");
            assertThat(updated.getUpdatedAt()).isEqualTo(LATER);
        }

        @Test
        @DisplayName("존재하지 않는 필드 키는 예외를 발생시킨다")
        void shouldThrowWhenKeyNotFound() {
            // given
            ProductNotice notice = ProductNoticeFixture.createNotice();

            // when & then
            assertThatThrownBy(() -> notice.updateItemValue("nonexistent", "값", LATER))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("존재하지 않는 필드 키");
        }
    }

    @Nested
    @DisplayName("addItem 메서드는")
    class AddItemMethod {

        @Test
        @DisplayName("새 항목을 추가한다")
        void shouldAddItem() {
            // given
            ProductNotice notice =
                    ProductNoticeFixture.noticeBuilder()
                            .items(List.of(NoticeItem.of("origin", "대한민국", 1)))
                            .buildNew();
            NoticeItem newItem = NoticeItem.of("material", "면 100%", 2);

            // when
            ProductNotice updated = notice.addItem(newItem, LATER);

            // then
            assertThat(updated.getItemCount()).isEqualTo(2);
            assertThat(updated.containsItem("material")).isTrue();
        }

        @Test
        @DisplayName("중복된 키 추가시 예외를 발생시킨다")
        void shouldThrowWhenDuplicateKey() {
            // given
            ProductNotice notice = ProductNoticeFixture.createNotice();
            NoticeItem duplicateItem = NoticeItem.of("origin", "중복 원산지", 10);

            // when & then
            assertThatThrownBy(() -> notice.addItem(duplicateItem, LATER))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이미 존재하는 필드 키");
        }
    }

    @Nested
    @DisplayName("removeItem 메서드는")
    class RemoveItemMethod {

        @Test
        @DisplayName("항목을 제거한다")
        void shouldRemoveItem() {
            // given
            ProductNotice notice = ProductNoticeFixture.createNotice();
            int originalCount = notice.getItemCount();

            // when
            ProductNotice updated = notice.removeItem("origin", LATER);

            // then
            assertThat(updated.getItemCount()).isEqualTo(originalCount - 1);
            assertThat(updated.containsItem("origin")).isFalse();
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드는")
    class StateCheckMethods {

        @Test
        @DisplayName("findItemByKey는 존재하는 키의 항목을 반환한다")
        void findItemByKeyShouldReturnItem() {
            // given
            ProductNotice notice = ProductNoticeFixture.createNotice();

            // when
            Optional<NoticeItem> item = notice.findItemByKey("origin");

            // then
            assertThat(item).isPresent();
            assertThat(item.get().fieldValue()).isEqualTo("대한민국");
        }

        @Test
        @DisplayName("findItemByKey는 존재하지 않는 키에 대해 빈 Optional을 반환한다")
        void findItemByKeyShouldReturnEmpty() {
            // given
            ProductNotice notice = ProductNoticeFixture.createNotice();

            // when
            Optional<NoticeItem> item = notice.findItemByKey("nonexistent");

            // then
            assertThat(item).isEmpty();
        }

        @Test
        @DisplayName("getItemValue는 필드 값을 반환한다")
        void getItemValueShouldReturnValue() {
            // given
            ProductNotice notice = ProductNoticeFixture.createNotice();

            // then
            assertThat(notice.getItemValue("origin")).hasValue("대한민국");
            assertThat(notice.getItemValue("nonexistent")).isEmpty();
        }

        @Test
        @DisplayName("containsItem은 항목 존재 여부를 반환한다")
        void containsItemShouldReturnCorrectResult() {
            // given
            ProductNotice notice = ProductNoticeFixture.createNotice();

            // then
            assertThat(notice.containsItem("origin")).isTrue();
            assertThat(notice.containsItem("nonexistent")).isFalse();
        }

        @Test
        @DisplayName("getItemKeys는 모든 필드 키 목록을 반환한다")
        void getItemKeysShouldReturnAllKeys() {
            // given
            ProductNotice notice = ProductNoticeFixture.createNotice();

            // when
            Set<String> keys = notice.getItemKeys();

            // then
            assertThat(keys).containsExactlyInAnyOrder("origin", "material", "color", "size");
        }

        @Test
        @DisplayName("getItemsWithValue는 값이 있는 항목만 반환한다")
        void getItemsWithValueShouldReturnFiltered() {
            // given
            ProductNotice notice =
                    ProductNoticeFixture.noticeBuilder()
                            .items(
                                    List.of(
                                            NoticeItem.of("key1", "값있음", 1),
                                            NoticeItem.of("key2", "", 2),
                                            NoticeItem.of("key3", "값있음2", 3)))
                            .buildNew();

            // when
            List<NoticeItem> itemsWithValue = notice.getItemsWithValue();

            // then
            assertThat(itemsWithValue).hasSize(2);
        }

        @Test
        @DisplayName("getMissingRequiredFields는 누락된 필수 필드를 반환한다")
        void getMissingRequiredFieldsShouldReturnMissing() {
            // given
            ProductNotice notice =
                    ProductNoticeFixture.noticeBuilder()
                            .items(
                                    List.of(
                                            NoticeItem.of("origin", "대한민국", 1),
                                            NoticeItem.of("material", "면", 2)))
                            .buildNew();
            Set<String> requiredKeys = Set.of("origin", "material", "color", "size");

            // when
            List<String> missing = notice.getMissingRequiredFields(requiredKeys);

            // then
            assertThat(missing).containsExactlyInAnyOrder("color", "size");
        }

        @Test
        @DisplayName("hasItems는 항목 존재 여부를 반환한다")
        void hasItemsShouldReturnCorrectResult() {
            // given
            ProductNotice withItems = ProductNoticeFixture.createNotice();
            ProductNotice empty = ProductNoticeFixture.noticeBuilder().clearItems().buildNew();

            // then
            assertThat(withItems.hasItems()).isTrue();
            assertThat(empty.hasItems()).isFalse();
        }
    }
}
