package com.ryuqq.setof.domain.productgroup.aggregate;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionGroupId;
import com.ryuqq.setof.domain.productgroup.vo.OptionGroupName;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** 셀러 옵션 그룹 (Child Entity of ProductGroup). 셀러가 정의한 옵션 그룹 (예: "색상", "사이즈"). */
public class SellerOptionGroup {

    private final SellerOptionGroupId id;
    private final ProductGroupId productGroupId;
    private OptionGroupName optionGroupName;
    private int sortOrder;
    private final List<SellerOptionValue> optionValues;
    private DeletionStatus deletionStatus;

    private SellerOptionGroup(
            SellerOptionGroupId id,
            ProductGroupId productGroupId,
            OptionGroupName optionGroupName,
            int sortOrder,
            List<SellerOptionValue> optionValues,
            DeletionStatus deletionStatus) {
        this.id = id;
        this.productGroupId = productGroupId;
        this.optionGroupName = optionGroupName;
        this.sortOrder = sortOrder;
        this.optionValues = new ArrayList<>(optionValues);
        this.deletionStatus = deletionStatus;
    }

    /** 신규 셀러 옵션 그룹 생성. */
    public static SellerOptionGroup forNew(
            ProductGroupId productGroupId,
            OptionGroupName optionGroupName,
            int sortOrder,
            List<SellerOptionValue> optionValues) {
        return new SellerOptionGroup(
                SellerOptionGroupId.forNew(),
                productGroupId,
                optionGroupName,
                sortOrder,
                optionValues,
                DeletionStatus.active());
    }

    /** 영속성에서 복원 시 사용. */
    public static SellerOptionGroup reconstitute(
            SellerOptionGroupId id,
            ProductGroupId productGroupId,
            OptionGroupName optionGroupName,
            int sortOrder,
            List<SellerOptionValue> optionValues,
            DeletionStatus deletionStatus) {
        return new SellerOptionGroup(
                id, productGroupId, optionGroupName, sortOrder, optionValues, deletionStatus);
    }

    /** 옵션 그룹명 수정. */
    public void updateName(OptionGroupName optionGroupName) {
        this.optionGroupName = optionGroupName;
    }

    /** 옵션 값 추가. */
    public void addOptionValue(SellerOptionValue optionValue) {
        this.optionValues.add(optionValue);
    }

    /** 정렬 순서 변경. */
    public void updateSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    /** 옵션 값 수 반환. */
    public int optionValueCount() {
        return optionValues.size();
    }

    /** soft delete 처리. 하위 SellerOptionValue도 일괄 delete. */
    public void delete(Instant occurredAt) {
        this.deletionStatus = DeletionStatus.deletedAt(occurredAt);
        for (SellerOptionValue value : optionValues) {
            value.delete(occurredAt);
        }
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    public SellerOptionGroupId id() {
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

    public OptionGroupName optionGroupName() {
        return optionGroupName;
    }

    public String optionGroupNameValue() {
        return optionGroupName.value();
    }

    public int sortOrder() {
        return sortOrder;
    }

    public List<SellerOptionValue> optionValues() {
        return Collections.unmodifiableList(optionValues);
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }
}
