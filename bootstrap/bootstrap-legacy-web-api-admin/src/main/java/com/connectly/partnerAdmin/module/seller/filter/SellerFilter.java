package com.connectly.partnerAdmin.module.seller.filter;

import com.connectly.partnerAdmin.auth.enums.ApprovalStatus;
import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.connectly.partnerAdmin.module.common.filter.AbstractSearchFilter;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerFilter extends AbstractSearchFilter{

    private List<Long> siteIds;
    private ApprovalStatus status;


    public SellerFilter(SearchKeyword searchKeyword, String searchWord, List<Long> siteIds, ApprovalStatus status) {
        super(searchKeyword, searchWord);
        this.siteIds = siteIds;
        this.status = status;
    }

}
