package com.connectly.partnerAdmin.module.display.filter;

import com.connectly.partnerAdmin.module.common.enums.PeriodType;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.common.filter.SearchAndDateFilter;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ContentFilter extends SearchAndDateFilter {

    private Yn displayYn;
    private Long lastDomainId;
    @NotNull(message = "periodType은 필수입니다.")
    private PeriodType periodType;

}
