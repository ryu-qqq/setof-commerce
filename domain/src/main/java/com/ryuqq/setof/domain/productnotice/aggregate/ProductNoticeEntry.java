package com.ryuqq.setof.domain.productnotice.aggregate;

import com.ryuqq.setof.domain.productnotice.id.NoticeFieldId;
import com.ryuqq.setof.domain.productnotice.id.ProductNoticeEntryId;
import com.ryuqq.setof.domain.productnotice.id.ProductNoticeId;
import com.ryuqq.setof.domain.productnotice.vo.NoticeFieldValue;

/**
 * ProductNoticeEntry - 상품고시 항목 Child Entity.
 *
 * <p>ProductNotice Aggregate 내부에서 관리되는 개별 고시 항목입니다.
 */
public class ProductNoticeEntry {

    private final ProductNoticeEntryId id;
    private ProductNoticeId productNoticeId;
    private final NoticeFieldId noticeFieldId;
    private NoticeFieldValue fieldValue;
    private final int sortOrder;

    private ProductNoticeEntry(
            ProductNoticeEntryId id,
            ProductNoticeId productNoticeId,
            NoticeFieldId noticeFieldId,
            NoticeFieldValue fieldValue,
            int sortOrder) {
        this.id = id;
        this.productNoticeId = productNoticeId;
        this.noticeFieldId = noticeFieldId;
        this.fieldValue = fieldValue;
        this.sortOrder = sortOrder;
    }

    /**
     * 새 고시 항목 생성.
     *
     * @param noticeFieldId 고시 필드 ID
     * @param fieldValue 필드값 (필드명 + 필드값)
     * @param sortOrder 정렬 순서
     * @return 새 ProductNoticeEntry 인스턴스
     */
    public static ProductNoticeEntry forNew(
            NoticeFieldId noticeFieldId, NoticeFieldValue fieldValue, int sortOrder) {
        return new ProductNoticeEntry(
                ProductNoticeEntryId.forNew(),
                ProductNoticeId.forNew(),
                noticeFieldId,
                fieldValue,
                sortOrder);
    }

    /**
     * 영속성 계층에서 복원.
     *
     * @param id 식별자
     * @param productNoticeId 부모 Notice ID
     * @param noticeFieldId 고시 필드 ID
     * @param fieldValue 필드값
     * @param sortOrder 정렬 순서
     * @return 복원된 ProductNoticeEntry 인스턴스
     */
    public static ProductNoticeEntry reconstitute(
            ProductNoticeEntryId id,
            ProductNoticeId productNoticeId,
            NoticeFieldId noticeFieldId,
            NoticeFieldValue fieldValue,
            int sortOrder) {
        return new ProductNoticeEntry(id, productNoticeId, noticeFieldId, fieldValue, sortOrder);
    }

    /** 부모 Notice 저장 후 ID 할당. */
    public void assignProductNoticeId(ProductNoticeId productNoticeId) {
        this.productNoticeId = productNoticeId;
    }

    /** 값 수정. */
    public void updateValue(NoticeFieldValue fieldValue) {
        this.fieldValue = fieldValue;
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

    public ProductNoticeId productNoticeId() {
        return productNoticeId;
    }

    public Long productNoticeIdValue() {
        return productNoticeId.value();
    }

    public NoticeFieldId noticeFieldId() {
        return noticeFieldId;
    }

    public Long noticeFieldIdValue() {
        return noticeFieldId.value();
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
