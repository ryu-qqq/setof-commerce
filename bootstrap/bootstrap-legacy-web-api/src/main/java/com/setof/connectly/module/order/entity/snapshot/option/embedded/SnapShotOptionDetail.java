package com.setof.connectly.module.order.entity.snapshot.option.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SnapShotOptionDetail {
    @Column(name = "option_detail_id")
    private long optionDetailId;

    @Column(name = "option_group_id")
    private long optionGroupId;

    @Column(name = "option_value")
    private String optionValue;
}
