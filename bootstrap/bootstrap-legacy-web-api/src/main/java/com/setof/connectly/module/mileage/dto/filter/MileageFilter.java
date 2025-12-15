package com.setof.connectly.module.mileage.dto.filter;

import com.setof.connectly.module.mileage.enums.Reason;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MileageFilter {
    private Long lastDomainId;
    private List<Reason> reasons;
}
