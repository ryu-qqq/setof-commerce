package com.setof.connectly.module.common.filter;

import com.setof.connectly.module.common.annotation.ValidDateRange;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ValidDateRange(start = "startDate", end = "endDate")
public abstract class SearchAndDateFilter implements DateRangeFilter {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime endDate;
}
