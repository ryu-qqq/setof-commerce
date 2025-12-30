package com.connectly.partnerAdmin.module.common.filter;

import java.time.LocalDateTime;


public interface DateRangeFilter {

    LocalDateTime getStartDate();
    LocalDateTime getEndDate();

}
