package com.setof.connectly.module.order.entity.snapshot.option.embedded;

import com.setof.connectly.module.product.enums.option.OptionName;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SnapShotOptionGroup {

    @Column(name = "OPTION_GROUP_ID")
    private long optionGroupId;

    @Column(name = "OPTION_NAME")
    @Enumerated(EnumType.STRING)
    private OptionName optionName;
}
