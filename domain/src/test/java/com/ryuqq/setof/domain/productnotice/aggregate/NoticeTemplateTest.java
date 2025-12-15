package com.ryuqq.setof.domain.productnotice.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.productnotice.ProductNoticeFixture;
import com.ryuqq.setof.domain.productnotice.vo.NoticeField;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("NoticeTemplate Aggregate 테스트")
class NoticeTemplateTest {

    private static final Instant NOW = Instant.parse("2024-01-01T00:00:00Z");
    private static final Instant LATER = Instant.parse("2024-01-02T00:00:00Z");

    @Nested
    @DisplayName("create 메서드는")
    class CreateMethod {

        @Test
        @DisplayName("유효한 값으로 NoticeTemplate을 생성한다")
        void shouldCreateNoticeTemplate() {
            // given
            Long categoryId = 100L;
            String templateName = "의류";
            List<NoticeField> requiredFields = ProductNoticeFixture.createClothingRequiredFields();
            List<NoticeField> optionalFields = ProductNoticeFixture.createClothingOptionalFields();

            // when
            NoticeTemplate template =
                    NoticeTemplate.create(
                            categoryId, templateName, requiredFields, optionalFields, NOW);

            // then
            assertThat(template.getId().isNew()).isTrue();
            assertThat(template.getCategoryId()).isEqualTo(100L);
            assertThat(template.getTemplateName()).isEqualTo("의류");
            assertThat(template.getRequiredFields()).hasSize(4);
            assertThat(template.getOptionalFields()).hasSize(2);
            assertThat(template.getCreatedAt()).isEqualTo(NOW);
            assertThat(template.getUpdatedAt()).isEqualTo(NOW);
        }

        @Test
        @DisplayName("필수 필드만으로 템플릿을 생성할 수 있다")
        void shouldCreateWithRequiredFieldsOnly() {
            // given
            Long categoryId = 100L;
            String templateName = "신발";
            List<NoticeField> requiredFields = ProductNoticeFixture.createClothingRequiredFields();

            // when
            NoticeTemplate template =
                    NoticeTemplate.createWithRequiredFieldsOnly(
                            categoryId, templateName, requiredFields, NOW);

            // then
            assertThat(template.hasRequiredFields()).isTrue();
            assertThat(template.hasOptionalFields()).isFalse();
        }

        @Test
        @DisplayName("categoryId가 null이면 예외를 발생시킨다")
        void shouldThrowWhenCategoryIdIsNull() {
            // given
            String templateName = "의류";
            List<NoticeField> requiredFields = ProductNoticeFixture.createClothingRequiredFields();

            // when & then
            assertThatThrownBy(
                            () ->
                                    NoticeTemplate.create(
                                            null, templateName, requiredFields, List.of(), NOW))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("CategoryId is required");
        }

        @Test
        @DisplayName("categoryId가 0 이하면 예외를 발생시킨다")
        void shouldThrowWhenCategoryIdIsInvalid() {
            // given
            String templateName = "의류";
            List<NoticeField> requiredFields = ProductNoticeFixture.createClothingRequiredFields();

            // when & then
            assertThatThrownBy(
                            () ->
                                    NoticeTemplate.create(
                                            0L, templateName, requiredFields, List.of(), NOW))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("CategoryId is required");
        }

        @Test
        @DisplayName("templateName이 비어있으면 예외를 발생시킨다")
        void shouldThrowWhenTemplateNameIsEmpty() {
            // given
            Long categoryId = 100L;
            List<NoticeField> requiredFields = ProductNoticeFixture.createClothingRequiredFields();

            // when & then
            assertThatThrownBy(
                            () ->
                                    NoticeTemplate.create(
                                            categoryId, "", requiredFields, List.of(), NOW))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("TemplateName is required");
        }

        @Test
        @DisplayName("중복된 필드 키가 있으면 예외를 발생시킨다")
        void shouldThrowWhenDuplicateKeysExist() {
            // given
            Long categoryId = 100L;
            String templateName = "의류";
            List<NoticeField> duplicateFields =
                    List.of(
                            NoticeField.required("origin", "원산지", 1),
                            NoticeField.required("origin", "원산지 중복", 2));

            // when & then
            assertThatThrownBy(
                            () ->
                                    NoticeTemplate.create(
                                            categoryId,
                                            templateName,
                                            duplicateFields,
                                            List.of(),
                                            NOW))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("중복된 필드 키");
        }
    }

    @Nested
    @DisplayName("reconstitute 메서드는")
    class ReconstituteMethod {

        @Test
        @DisplayName("모든 필드를 복원한다")
        void shouldReconstituteAllFields() {
            // given & when
            NoticeTemplate template = ProductNoticeFixture.reconstituteTemplate(1L);

            // then
            assertThat(template.getIdValue()).isEqualTo(1L);
            assertThat(template.hasId()).isTrue();
        }
    }

    @Nested
    @DisplayName("rename 메서드는")
    class RenameMethod {

        @Test
        @DisplayName("템플릿명을 변경한다")
        void shouldRenamTemplate() {
            // given
            NoticeTemplate template = ProductNoticeFixture.createClothingTemplate();

            // when
            NoticeTemplate renamed = template.rename("가방", LATER);

            // then
            assertThat(renamed.getTemplateName()).isEqualTo("가방");
            assertThat(renamed.getUpdatedAt()).isEqualTo(LATER);
        }

        @Test
        @DisplayName("비어있는 이름은 예외를 발생시킨다")
        void shouldThrowWhenNameIsEmpty() {
            // given
            NoticeTemplate template = ProductNoticeFixture.createClothingTemplate();

            // when & then
            assertThatThrownBy(() -> template.rename("", LATER))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("템플릿명은 필수");
        }
    }

    @Nested
    @DisplayName("addRequiredField 메서드는")
    class AddRequiredFieldMethod {

        @Test
        @DisplayName("필수 필드를 추가한다")
        void shouldAddRequiredField() {
            // given
            NoticeTemplate template =
                    ProductNoticeFixture.templateBuilder()
                            .requiredFields(List.of(NoticeField.required("origin", "원산지", 1)))
                            .optionalFields(List.of())
                            .buildNew();
            NoticeField newField = NoticeField.required("material", "소재", 2);

            // when
            NoticeTemplate updated = template.addRequiredField(newField, LATER);

            // then
            assertThat(updated.getRequiredFields()).hasSize(2);
            assertThat(updated.isRequiredField("material")).isTrue();
        }

        @Test
        @DisplayName("중복된 키 추가시 예외를 발생시킨다")
        void shouldThrowWhenDuplicateKey() {
            // given
            NoticeTemplate template = ProductNoticeFixture.createClothingTemplate();
            NoticeField duplicateField = NoticeField.required("origin", "원산지 중복", 10);

            // when & then
            assertThatThrownBy(() -> template.addRequiredField(duplicateField, LATER))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이미 존재하는 필드 키");
        }
    }

    @Nested
    @DisplayName("addOptionalField 메서드는")
    class AddOptionalFieldMethod {

        @Test
        @DisplayName("선택 필드를 추가한다")
        void shouldAddOptionalField() {
            // given
            NoticeTemplate template =
                    ProductNoticeFixture.templateBuilder()
                            .requiredFields(List.of(NoticeField.required("origin", "원산지", 1)))
                            .optionalFields(List.of())
                            .buildNew();
            NoticeField newField = NoticeField.optional("caution", "주의사항", 5);

            // when
            NoticeTemplate updated = template.addOptionalField(newField, LATER);

            // then
            assertThat(updated.getOptionalFields()).hasSize(1);
            assertThat(updated.isOptionalField("caution")).isTrue();
        }
    }

    @Nested
    @DisplayName("removeField 메서드는")
    class RemoveFieldMethod {

        @Test
        @DisplayName("필드를 제거한다")
        void shouldRemoveField() {
            // given
            NoticeTemplate template = ProductNoticeFixture.createClothingTemplate();
            int originalCount = template.getTotalFieldCount();

            // when
            NoticeTemplate updated = template.removeField("origin", LATER);

            // then
            assertThat(updated.getTotalFieldCount()).isEqualTo(originalCount - 1);
            assertThat(updated.containsField("origin")).isFalse();
        }
    }

    @Nested
    @DisplayName("replaceAllFields 메서드는")
    class ReplaceAllFieldsMethod {

        @Test
        @DisplayName("모든 필드를 교체한다")
        void shouldReplaceAllFields() {
            // given
            NoticeTemplate template = ProductNoticeFixture.createClothingTemplate();
            List<NoticeField> newRequired = List.of(NoticeField.required("newKey1", "새 필드1", 1));
            List<NoticeField> newOptional = List.of(NoticeField.optional("newKey2", "새 필드2", 2));

            // when
            NoticeTemplate updated = template.replaceAllFields(newRequired, newOptional, LATER);

            // then
            assertThat(updated.getRequiredFields()).hasSize(1);
            assertThat(updated.getOptionalFields()).hasSize(1);
            assertThat(updated.containsField("newKey1")).isTrue();
            assertThat(updated.containsField("origin")).isFalse();
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드는")
    class StateCheckMethods {

        @Test
        @DisplayName("isRequiredField는 필수 필드일 때 true를 반환한다")
        void isRequiredFieldShouldReturnTrue() {
            // given
            NoticeTemplate template = ProductNoticeFixture.createClothingTemplate();

            // then
            assertThat(template.isRequiredField("origin")).isTrue();
            assertThat(template.isRequiredField("washingMethod")).isFalse();
        }

        @Test
        @DisplayName("isOptionalField는 선택 필드일 때 true를 반환한다")
        void isOptionalFieldShouldReturnTrue() {
            // given
            NoticeTemplate template = ProductNoticeFixture.createClothingTemplate();

            // then
            assertThat(template.isOptionalField("washingMethod")).isTrue();
            assertThat(template.isOptionalField("origin")).isFalse();
        }

        @Test
        @DisplayName("containsField는 필드가 존재하면 true를 반환한다")
        void containsFieldShouldReturnTrue() {
            // given
            NoticeTemplate template = ProductNoticeFixture.createClothingTemplate();

            // then
            assertThat(template.containsField("origin")).isTrue();
            assertThat(template.containsField("washingMethod")).isTrue();
            assertThat(template.containsField("nonexistent")).isFalse();
        }

        @Test
        @DisplayName("getRequiredFieldKeys는 필수 필드 키 목록을 반환한다")
        void getRequiredFieldKeysShouldReturnKeys() {
            // given
            NoticeTemplate template = ProductNoticeFixture.createClothingTemplate();

            // when
            Set<String> keys = template.getRequiredFieldKeys();

            // then
            assertThat(keys).containsExactlyInAnyOrder("origin", "material", "color", "size");
        }

        @Test
        @DisplayName("getTotalFieldCount는 전체 필드 개수를 반환한다")
        void getTotalFieldCountShouldReturnCount() {
            // given
            NoticeTemplate template = ProductNoticeFixture.createClothingTemplate();

            // then
            assertThat(template.getTotalFieldCount()).isEqualTo(6);
        }
    }
}
