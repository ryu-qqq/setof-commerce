package com.connectly.partnerAdmin.module.common.filter;

import com.connectly.partnerAdmin.module.common.annotation.ValidDateRange;
import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@ValidDateRange(start = "startDate", end = "endDate")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class SearchAndDateFilter implements DateRangeFilter, SearchFilter {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime endDate;

    protected SearchKeyword searchKeyword;
    protected String searchWord;


}
