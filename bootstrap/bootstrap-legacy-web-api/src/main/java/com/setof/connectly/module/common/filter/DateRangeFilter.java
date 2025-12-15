package com.setof.connectly.module.common.filter;

import java.time.LocalDateTime;

public interface DateRangeFilter {

    LocalDateTime getStartDate();

    LocalDateTime getEndDate();
}
