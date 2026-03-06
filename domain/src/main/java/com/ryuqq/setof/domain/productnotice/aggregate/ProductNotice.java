package com.ryuqq.setof.domain.productnotice.aggregate;

import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productnotice.id.ProductNoticeId;
import com.ryuqq.setof.domain.productnotice.vo.ProductNoticeUpdateData;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ProductNotice - 상품고시 Aggregate Root.
 *
 * <p>상품 고시 정보를 관리합니다. 전자상거래법에 따른 상품 정보 제공 고시입니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public class ProductNotice {

    private final ProductNoticeId id;
    private final ProductGroupId productGroupId;
    private final List<ProductNoticeEntry> entries;
    private final Instant createdAt;
    private Instant updatedAt;

    private ProductNotice(
            ProductNoticeId id,
            ProductGroupId productGroupId,
            List<ProductNoticeEntry> entries,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.productGroupId = productGroupId;
        this.entries = entries != null ? new ArrayList<>(entries) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ========== Factory Methods ==========

    /**
     * 새 상품고시 생성.
     *
     * @param productGroupId 상품그룹 ID (필수)
     * @param entries 고시 항목 목록
     * @param now 생성 시각
     * @return 새 ProductNotice 인스턴스
     */
    public static ProductNotice forNew(
            ProductGroupId productGroupId, List<ProductNoticeEntry> entries, Instant now) {
        return new ProductNotice(ProductNoticeId.forNew(), productGroupId, entries, now, now);
    }

    /**
     * 영속성 계층에서 엔티티 복원.
     *
     * @param id 식별자
     * @param productGroupId 상품그룹 ID
     * @param entries 고시 항목 목록
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @return 복원된 ProductNotice 인스턴스
     */
    public static ProductNotice reconstitute(
            ProductNoticeId id,
            ProductGroupId productGroupId,
            List<ProductNoticeEntry> entries,
            Instant createdAt,
            Instant updatedAt) {
        return new ProductNotice(id, productGroupId, entries, createdAt, updatedAt);
    }

    // ========== Business Methods ==========

    /** 신규 생성 여부 확인 */
    public boolean isNew() {
        return id.isNew();
    }

    /** 영속화 후 발급된 ID를 할당하고, 소유 entries에도 전파한다. */
    public void assignId(ProductNoticeId assignedId) {
        for (ProductNoticeEntry entry : entries) {
            entry.assignProductNoticeId(assignedId);
        }
    }

    /** 수정 데이터를 적용하여 고시정보를 갱신한다. */
    public void update(ProductNoticeUpdateData updateData) {
        this.entries.clear();
        this.entries.addAll(updateData.entries());
        this.updatedAt = updateData.updatedAt();
    }

    /**
     * 고시 항목 교체.
     *
     * @param newEntries 새 고시 항목 목록
     * @param now 수정 시각
     */
    public void replaceEntries(List<ProductNoticeEntry> newEntries, Instant now) {
        this.entries.clear();
        if (newEntries != null) {
            this.entries.addAll(newEntries);
        }
        this.updatedAt = now;
    }

    /** 고시 항목 비어있는지 확인 */
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    /** 고시 항목 수 반환 */
    public int entryCount() {
        return entries.size();
    }

    // ========== Accessor Methods ==========

    public ProductNoticeId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public ProductGroupId productGroupId() {
        return productGroupId;
    }

    public Long productGroupIdValue() {
        return productGroupId.value();
    }

    public List<ProductNoticeEntry> entries() {
        return Collections.unmodifiableList(entries);
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
