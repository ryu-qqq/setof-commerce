package com.connectly.partnerAdmin.module.order.entity.snapshot.option.embedded;

import com.connectly.partnerAdmin.module.product.enums.option.OptionName;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnapShotOptionGroup {

    @Column(name = "OPTION_GROUP_ID")
    private long optionGroupId;

    @Column(name = "OPTION_NAME")
    @Enumerated(EnumType.STRING)
    private OptionName optionName;

    public SnapShotOptionGroup(long optionGroupId, OptionName optionName) {
        this.optionGroupId = optionGroupId;
        this.optionName = optionName;
    }

}
