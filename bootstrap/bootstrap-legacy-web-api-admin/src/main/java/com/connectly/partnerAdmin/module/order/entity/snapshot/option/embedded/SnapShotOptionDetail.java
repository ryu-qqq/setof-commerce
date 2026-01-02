package com.connectly.partnerAdmin.module.order.entity.snapshot.option.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnapShotOptionDetail {
    @Column(name = "OPTION_DETAIL_ID")
    private long optionDetailId;
    @Column(name = "OPTION_GROUP_ID")
    private long optionGroupId;

    @Column(name = "OPTION_VALUE")
    private String optionValue;


    public SnapShotOptionDetail(long optionDetailId, long optionGroupId, String optionValue) {
        this.optionDetailId = optionDetailId;
        this.optionGroupId = optionGroupId;
        this.optionValue = optionValue;
    }

}
