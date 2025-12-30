package com.connectly.partnerAdmin.module.user.filter;

import com.connectly.partnerAdmin.module.common.filter.SearchAndDateFilter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserFilter extends SearchAndDateFilter {
    private Long userId;
}
