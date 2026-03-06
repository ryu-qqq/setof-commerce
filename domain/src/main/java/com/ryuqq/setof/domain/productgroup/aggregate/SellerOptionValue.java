package com.ryuqq.setof.domain.productgroup.aggregate;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import com.ryuqq.setof.domain.productgroup.vo.OptionValueName;
import java.time.Instant;

/** 셀러 옵션 값 (Child Entity of SellerOptionGroup). 셀러가 자유 입력한 옵션 값. */
public class SellerOptionValue {

    private final SellerOptionValueId id;
    private final SellerOptionGroupId sellerOptionGroupId;
    private OptionValueName optionValueName;
    private int sortOrder;
    private DeletionStatus deletionStatus;

    private SellerOptionValue(
            SellerOptionValueId id,
            SellerOptionGroupId sellerOptionGroupId,
            OptionValueName optionValueName,
            int sortOrder,
            DeletionStatus deletionStatus) {
        this.id = id;
        this.sellerOptionGroupId = sellerOptionGroupId;
        this.optionValueName = optionValueName;
        this.sortOrder = sortOrder;
        this.deletionStatus = deletionStatus;
    }

    /** 신규 셀러 옵션 값 생성. */
    public static SellerOptionValue forNew(
            SellerOptionGroupId sellerOptionGroupId,
            OptionValueName optionValueName,
            int sortOrder) {
        return new SellerOptionValue(
                SellerOptionValueId.forNew(),
                sellerOptionGroupId,
                optionValueName,
                sortOrder,
                DeletionStatus.active());
    }

    /** 영속성에서 복원 시 사용. */
    public static SellerOptionValue reconstitute(
            SellerOptionValueId id,
            SellerOptionGroupId sellerOptionGroupId,
            OptionValueName optionValueName,
            int sortOrder,
            DeletionStatus deletionStatus) {
        return new SellerOptionValue(
                id, sellerOptionGroupId, optionValueName, sortOrder, deletionStatus);
    }

    /** 옵션 값 이름 수정. */
    public void updateName(OptionValueName optionValueName) {
        this.optionValueName = optionValueName;
    }

    /** 정렬 순서 변경. */
    public void updateSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    /** soft delete 처리. */
    public void delete(Instant occurredAt) {
        this.deletionStatus = DeletionStatus.deletedAt(occurredAt);
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    public SellerOptionValueId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public SellerOptionGroupId sellerOptionGroupId() {
        return sellerOptionGroupId;
    }

    public Long sellerOptionGroupIdValue() {
        return sellerOptionGroupId.value();
    }

    public OptionValueName optionValueName() {
        return optionValueName;
    }

    public String optionValueNameValue() {
        return optionValueName.value();
    }

    public int sortOrder() {
        return sortOrder;
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }
}
