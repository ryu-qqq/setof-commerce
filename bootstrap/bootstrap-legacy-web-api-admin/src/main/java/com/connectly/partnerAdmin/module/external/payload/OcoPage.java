package com.connectly.partnerAdmin.module.external.payload;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OcoPage {
    private int totalCount;
    private int pageSize;
    private int page;
    private int totalPage;

    public OcoPage(int totalCount, int pageSize, int page, int totalPage) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.page = page;
        this.totalPage = totalPage;
    }
}
