package com.connectly.partnerAdmin.module.common.filter;

import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;

public interface SearchFilter {

    SearchKeyword getSearchKeyword();
    String getSearchWord();
}
