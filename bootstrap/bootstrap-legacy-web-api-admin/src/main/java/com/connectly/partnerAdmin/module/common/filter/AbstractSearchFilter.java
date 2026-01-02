package com.connectly.partnerAdmin.module.common.filter;

import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class AbstractSearchFilter implements SearchFilter {

    protected SearchKeyword searchKeyword;
    protected String searchWord;

}
