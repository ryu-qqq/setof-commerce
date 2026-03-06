package com.ryuqq.setof.domain.productnotice.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.productnotice.id.NoticeFieldId;
import com.ryuqq.setof.domain.productnotice.id.ProductNoticeEntryId;
import com.ryuqq.setof.domain.productnotice.id.ProductNoticeId;
import com.ryuqq.setof.domain.productnotice.vo.NoticeFieldValue;
import com.setof.commerce.domain.productnotice.ProductNoticeFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductNoticeEntry Child Entity 테스트")
class ProductNoticeEntryTest {

    @Nested
    @DisplayName("forNew() - 신규 고시 항목 생성")
    class ForNewTest {

        @Test
        @DisplayName("유효한 필드값으로 신규 항목을 생성한다")
        void createNewEntry() {
            // given
            NoticeFieldValue fieldValue = ProductNoticeFixtures.defaultFieldValue();

            // when
            var entry = ProductNoticeEntry.forNew(NoticeFieldId.forNew(), fieldValue, 1);

            // then
            assertThat(entry.isNew()).isTrue();
            assertThat(entry.fieldName()).isEqualTo(ProductNoticeFixtures.DEFAULT_FIELD_NAME);
            assertThat(entry.fieldValueText()).isEqualTo(ProductNoticeFixtures.DEFAULT_FIELD_VALUE);
            assertThat(entry.sortOrder()).isEqualTo(1);
        }

        @Test
        @DisplayName("커스텀 필드값으로 항목을 생성한다")
        void createWithCustomValues() {
            // given
            NoticeFieldValue fieldValue = NoticeFieldValue.of("세탁방법", "드라이클리닝");

            // when
            var entry = ProductNoticeEntry.forNew(NoticeFieldId.forNew(), fieldValue, 5);

            // then
            assertThat(entry.fieldName()).isEqualTo("세탁방법");
            assertThat(entry.fieldValueText()).isEqualTo("드라이클리닝");
            assertThat(entry.sortOrder()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("ID가 할당된 항목을 복원한다")
        void reconstituteEntry() {
            // given
            ProductNoticeEntryId id = ProductNoticeEntryId.of(10L);
            NoticeFieldValue fieldValue = ProductNoticeFixtures.defaultFieldValue();

            // when
            var entry =
                    ProductNoticeEntry.reconstitute(
                            id, ProductNoticeId.of(1L), NoticeFieldId.of(1L), fieldValue, 3);

            // then
            assertThat(entry.isNew()).isFalse();
            assertThat(entry.idValue()).isEqualTo(10L);
            assertThat(entry.fieldName()).isEqualTo(ProductNoticeFixtures.DEFAULT_FIELD_NAME);
            assertThat(entry.sortOrder()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("isNew() - 신규 여부 확인")
    class IsNewTest {

        @Test
        @DisplayName("forNew()로 생성된 항목은 true를 반환한다")
        void returnsTrueForNewEntry() {
            // given
            var entry = ProductNoticeFixtures.defaultNoticeEntry();

            // then
            assertThat(entry.isNew()).isTrue();
        }

        @Test
        @DisplayName("reconstitute()로 복원된 항목은 false를 반환한다")
        void returnsFalseForReconstitutedEntry() {
            // given
            var entry =
                    ProductNoticeEntry.reconstitute(
                            ProductNoticeEntryId.of(1L),
                            ProductNoticeId.of(1L),
                            NoticeFieldId.of(1L),
                            ProductNoticeFixtures.defaultFieldValue(),
                            1);

            // then
            assertThat(entry.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("Accessor 메서드 테스트")
    class AccessorTest {

        @Test
        @DisplayName("id()는 ProductNoticeEntryId를 반환한다")
        void returnsId() {
            // given
            var entry =
                    ProductNoticeEntry.reconstitute(
                            ProductNoticeEntryId.of(7L),
                            ProductNoticeId.of(1L),
                            NoticeFieldId.of(1L),
                            ProductNoticeFixtures.defaultFieldValue(),
                            1);

            // then
            assertThat(entry.id()).isNotNull();
            assertThat(entry.idValue()).isEqualTo(7L);
        }

        @Test
        @DisplayName("fieldValue()는 NoticeFieldValue를 반환한다")
        void returnsFieldValue() {
            // given
            var entry = ProductNoticeFixtures.defaultNoticeEntry();

            // then
            assertThat(entry.fieldValue()).isNotNull();
            assertThat(entry.fieldValue().fieldName())
                    .isEqualTo(ProductNoticeFixtures.DEFAULT_FIELD_NAME);
            assertThat(entry.fieldValue().fieldValue())
                    .isEqualTo(ProductNoticeFixtures.DEFAULT_FIELD_VALUE);
        }

        @Test
        @DisplayName("fieldName()은 필드명을 반환한다")
        void returnsFieldName() {
            // given
            var entry = ProductNoticeFixtures.noticeEntry("제조국", "대한민국");

            // then
            assertThat(entry.fieldName()).isEqualTo("제조국");
        }

        @Test
        @DisplayName("fieldValueText()는 필드값 텍스트를 반환한다")
        void returnsFieldValueText() {
            // given
            var entry = ProductNoticeFixtures.noticeEntry("제조국", "대한민국");

            // then
            assertThat(entry.fieldValueText()).isEqualTo("대한민국");
        }

        @Test
        @DisplayName("sortOrder()는 정렬 순서를 반환한다")
        void returnsSortOrder() {
            // given
            var entry =
                    ProductNoticeEntry.forNew(
                            NoticeFieldId.forNew(), ProductNoticeFixtures.defaultFieldValue(), 99);

            // then
            assertThat(entry.sortOrder()).isEqualTo(99);
        }
    }
}
