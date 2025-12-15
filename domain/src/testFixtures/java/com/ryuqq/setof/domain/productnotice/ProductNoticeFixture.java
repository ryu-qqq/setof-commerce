package com.ryuqq.setof.domain.productnotice;

import com.ryuqq.setof.domain.productnotice.aggregate.NoticeTemplate;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import com.ryuqq.setof.domain.productnotice.vo.NoticeField;
import com.ryuqq.setof.domain.productnotice.vo.NoticeItem;
import com.ryuqq.setof.domain.productnotice.vo.NoticeTemplateId;
import com.ryuqq.setof.domain.productnotice.vo.ProductNoticeId;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductNotice 도메인 테스트 Fixture
 *
 * <p>테스트에서 사용되는 ProductNotice, NoticeTemplate 관련 객체 생성 유틸리티
 */
public final class ProductNoticeFixture {

    private ProductNoticeFixture() {
        // Utility class
    }

    // ========== Default Values ==========
    public static final Long DEFAULT_TEMPLATE_ID = 1L;
    public static final Long DEFAULT_NOTICE_ID = 1L;
    public static final Long DEFAULT_CATEGORY_ID = 100L;
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 200L;
    public static final String DEFAULT_TEMPLATE_NAME = "의류";
    public static final Instant DEFAULT_NOW = Instant.parse("2024-01-01T00:00:00Z");

    // ========== NoticeTemplateId ==========

    public static NoticeTemplateId createTemplateId() {
        return NoticeTemplateId.of(DEFAULT_TEMPLATE_ID);
    }

    public static NoticeTemplateId createTemplateId(Long value) {
        return NoticeTemplateId.of(value);
    }

    public static NoticeTemplateId createNewTemplateId() {
        return NoticeTemplateId.forNew();
    }

    // ========== ProductNoticeId ==========

    public static ProductNoticeId createNoticeId() {
        return ProductNoticeId.of(DEFAULT_NOTICE_ID);
    }

    public static ProductNoticeId createNoticeId(Long value) {
        return ProductNoticeId.of(value);
    }

    public static ProductNoticeId createNewNoticeId() {
        return ProductNoticeId.forNew();
    }

    // ========== NoticeField ==========

    public static NoticeField createRequiredField(String key, int displayOrder) {
        return NoticeField.required(key, key + " 필드 설명", displayOrder);
    }

    public static NoticeField createOptionalField(String key, int displayOrder) {
        return NoticeField.optional(key, key + " 필드 설명 (선택)", displayOrder);
    }

    public static List<NoticeField> createClothingRequiredFields() {
        return List.of(
                NoticeField.required("origin", "원산지", 1),
                NoticeField.required("material", "소재", 2),
                NoticeField.required("color", "색상", 3),
                NoticeField.required("size", "치수", 4));
    }

    public static List<NoticeField> createClothingOptionalFields() {
        return List.of(
                NoticeField.optional("washingMethod", "세탁 방법", 5),
                NoticeField.optional("manufacturer", "제조사", 6));
    }

    // ========== NoticeItem ==========

    public static NoticeItem createItem(String fieldKey, String value, int displayOrder) {
        return NoticeItem.of(fieldKey, value, displayOrder);
    }

    public static List<NoticeItem> createClothingItems() {
        return List.of(
                NoticeItem.of("origin", "대한민국", 1),
                NoticeItem.of("material", "면 100%", 2),
                NoticeItem.of("color", "블랙, 화이트, 네이비", 3),
                NoticeItem.of("size", "S, M, L, XL", 4));
    }

    public static List<NoticeItem> createClothingItemsWithOptional() {
        return List.of(
                NoticeItem.of("origin", "대한민국", 1),
                NoticeItem.of("material", "면 100%", 2),
                NoticeItem.of("color", "블랙, 화이트, 네이비", 3),
                NoticeItem.of("size", "S, M, L, XL", 4),
                NoticeItem.of("washingMethod", "찬물 손세탁", 5),
                NoticeItem.of("manufacturer", "(주)테스트의류", 6));
    }

    // ========== NoticeTemplate ==========

    public static NoticeTemplate createClothingTemplate() {
        return NoticeTemplate.create(
                DEFAULT_CATEGORY_ID,
                DEFAULT_TEMPLATE_NAME,
                createClothingRequiredFields(),
                createClothingOptionalFields(),
                DEFAULT_NOW);
    }

    public static NoticeTemplate createTemplateWithRequiredOnly() {
        return NoticeTemplate.createWithRequiredFieldsOnly(
                DEFAULT_CATEGORY_ID, "신발", createClothingRequiredFields(), DEFAULT_NOW);
    }

    public static NoticeTemplate reconstituteTemplate() {
        return NoticeTemplate.reconstitute(
                createTemplateId(),
                DEFAULT_CATEGORY_ID,
                DEFAULT_TEMPLATE_NAME,
                createClothingRequiredFields(),
                createClothingOptionalFields(),
                DEFAULT_NOW,
                DEFAULT_NOW);
    }

    public static NoticeTemplate reconstituteTemplate(Long id) {
        return NoticeTemplate.reconstitute(
                NoticeTemplateId.of(id),
                DEFAULT_CATEGORY_ID,
                DEFAULT_TEMPLATE_NAME,
                createClothingRequiredFields(),
                createClothingOptionalFields(),
                DEFAULT_NOW,
                DEFAULT_NOW);
    }

    // ========== ProductNotice ==========

    public static ProductNotice createNotice() {
        return ProductNotice.create(
                DEFAULT_PRODUCT_GROUP_ID, createTemplateId(), createClothingItems(), DEFAULT_NOW);
    }

    public static ProductNotice createNoticeWithOptional() {
        return ProductNotice.create(
                DEFAULT_PRODUCT_GROUP_ID,
                createTemplateId(),
                createClothingItemsWithOptional(),
                DEFAULT_NOW);
    }

    public static ProductNotice createNoticeWithValidation() {
        NoticeTemplate template = createClothingTemplate();
        return ProductNotice.createWithValidation(
                DEFAULT_PRODUCT_GROUP_ID,
                createTemplateId(),
                createClothingItems(),
                template.getRequiredFieldKeys(),
                DEFAULT_NOW);
    }

    public static ProductNotice reconstituteNotice() {
        return ProductNotice.reconstitute(
                createNoticeId(),
                DEFAULT_PRODUCT_GROUP_ID,
                createTemplateId(),
                createClothingItems(),
                DEFAULT_NOW,
                DEFAULT_NOW);
    }

    public static ProductNotice reconstituteNotice(Long id) {
        return ProductNotice.reconstitute(
                ProductNoticeId.of(id),
                DEFAULT_PRODUCT_GROUP_ID,
                createTemplateId(),
                createClothingItems(),
                DEFAULT_NOW,
                DEFAULT_NOW);
    }

    // ========== NoticeTemplate Builder ==========

    public static NoticeTemplateBuilder templateBuilder() {
        return new NoticeTemplateBuilder();
    }

    public static class NoticeTemplateBuilder {
        private NoticeTemplateId id = createNewTemplateId();
        private Long categoryId = DEFAULT_CATEGORY_ID;
        private String templateName = DEFAULT_TEMPLATE_NAME;
        private List<NoticeField> requiredFields = new ArrayList<>(createClothingRequiredFields());
        private List<NoticeField> optionalFields = new ArrayList<>(createClothingOptionalFields());
        private Instant createdAt = DEFAULT_NOW;
        private Instant updatedAt = DEFAULT_NOW;

        public NoticeTemplateBuilder id(Long id) {
            this.id = NoticeTemplateId.of(id);
            return this;
        }

        public NoticeTemplateBuilder categoryId(Long categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public NoticeTemplateBuilder templateName(String templateName) {
            this.templateName = templateName;
            return this;
        }

        public NoticeTemplateBuilder requiredFields(List<NoticeField> requiredFields) {
            this.requiredFields = new ArrayList<>(requiredFields);
            return this;
        }

        public NoticeTemplateBuilder optionalFields(List<NoticeField> optionalFields) {
            this.optionalFields = new ArrayList<>(optionalFields);
            return this;
        }

        public NoticeTemplateBuilder addRequiredField(NoticeField field) {
            this.requiredFields.add(field);
            return this;
        }

        public NoticeTemplateBuilder addOptionalField(NoticeField field) {
            this.optionalFields.add(field);
            return this;
        }

        public NoticeTemplateBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public NoticeTemplateBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public NoticeTemplate build() {
            return NoticeTemplate.reconstitute(
                    id,
                    categoryId,
                    templateName,
                    requiredFields,
                    optionalFields,
                    createdAt,
                    updatedAt);
        }

        public NoticeTemplate buildNew() {
            return NoticeTemplate.create(
                    categoryId, templateName, requiredFields, optionalFields, createdAt);
        }
    }

    // ========== ProductNotice Builder ==========

    public static ProductNoticeBuilder noticeBuilder() {
        return new ProductNoticeBuilder();
    }

    public static class ProductNoticeBuilder {
        private ProductNoticeId id = createNewNoticeId();
        private Long productGroupId = DEFAULT_PRODUCT_GROUP_ID;
        private NoticeTemplateId templateId = createTemplateId();
        private List<NoticeItem> items = new ArrayList<>(createClothingItems());
        private Instant createdAt = DEFAULT_NOW;
        private Instant updatedAt = DEFAULT_NOW;

        public ProductNoticeBuilder id(Long id) {
            this.id = ProductNoticeId.of(id);
            return this;
        }

        public ProductNoticeBuilder productGroupId(Long productGroupId) {
            this.productGroupId = productGroupId;
            return this;
        }

        public ProductNoticeBuilder templateId(Long templateId) {
            this.templateId = NoticeTemplateId.of(templateId);
            return this;
        }

        public ProductNoticeBuilder templateId(NoticeTemplateId templateId) {
            this.templateId = templateId;
            return this;
        }

        public ProductNoticeBuilder items(List<NoticeItem> items) {
            this.items = new ArrayList<>(items);
            return this;
        }

        public ProductNoticeBuilder addItem(NoticeItem item) {
            this.items.add(item);
            return this;
        }

        public ProductNoticeBuilder clearItems() {
            this.items.clear();
            return this;
        }

        public ProductNoticeBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ProductNoticeBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ProductNotice build() {
            return ProductNotice.reconstitute(
                    id, productGroupId, templateId, items, createdAt, updatedAt);
        }

        public ProductNotice buildNew() {
            return ProductNotice.create(productGroupId, templateId, items, createdAt);
        }
    }
}
