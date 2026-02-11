package com.ryuqq.setof.domain.productnotice.aggregate;

import com.ryuqq.setof.domain.productnotice.id.ProductNoticeEntryId;
import com.ryuqq.setof.domain.productnotice.vo.NoticeFieldValue;

/**
 * ProductNoticeEntry - 상품고시 항목 Child Entity.
 *
 * <p>ProductNotice Aggregate 내부에서 관리되는 개별 고시 항목입니다.
 */
public class ProductNoticeEntry {

    private final ProductNoticeEntryId id;
    private final NoticeFieldValue fieldValue;
    private final int sortOrder;

    private ProductNoticeEntry(
            ProductNoticeEntryId id, NoticeFieldValue fieldValue, int sortOrder) {
        this.id = id;
        this.fieldValue = fieldValue;
        this.sortOrder = sortOrder;
    }

    /**
     * 새 고시 항목 생성.
     *
     * @param fieldValue 필드값 (필드명 + 필드값)
     * @param sortOrder 정렬 순서
     * @return 새 ProductNoticeEntry 인스턴스
     */
    public static ProductNoticeEntry forNew(NoticeFieldValue fieldValue, int sortOrder) {
        return new ProductNoticeEntry(ProductNoticeEntryId.forNew(), fieldValue, sortOrder);
    }

    /**
     * 영속성 계층에서 복원.
     *
     * @param id 식별자
     * @param fieldValue 필드값
     * @param sortOrder 정렬 순서
     * @return 복원된 ProductNoticeEntry 인스턴스
     */
    public static ProductNoticeEntry reconstitute(
            ProductNoticeEntryId id, NoticeFieldValue fieldValue, int sortOrder) {
        return new ProductNoticeEntry(id, fieldValue, sortOrder);
    }

    public boolean isNew() {
        return id.isNew();
    }

    // ========== Accessor Methods ==========

    public ProductNoticeEntryId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public NoticeFieldValue fieldValue() {
        return fieldValue;
    }

    public String fieldName() {
        return fieldValue.fieldName();
    }

    public String fieldValueText() {
        return fieldValue.fieldValue();
    }

    public int sortOrder() {
        return sortOrder;
    }
}
