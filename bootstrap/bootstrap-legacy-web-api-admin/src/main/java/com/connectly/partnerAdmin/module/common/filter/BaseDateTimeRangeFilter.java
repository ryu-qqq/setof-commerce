package com.connectly.partnerAdmin.module.common.filter;

import com.connectly.partnerAdmin.auth.validator.AuthorityValidate;
import com.connectly.partnerAdmin.module.common.annotation.ValidDateRange;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@ValidDateRange(start = "startDate", end = "endDate")
@AuthorityValidate
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BaseDateTimeRangeFilter extends BaseRoleFilter implements DateRangeFilter  {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
}
