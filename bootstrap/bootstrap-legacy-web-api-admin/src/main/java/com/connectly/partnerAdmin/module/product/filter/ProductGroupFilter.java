package com.connectly.partnerAdmin.module.product.filter;

import com.connectly.partnerAdmin.auth.validator.AuthorityValidate;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.common.filter.LastDomainIdProvider;
import com.connectly.partnerAdmin.module.common.filter.RoleFilter;
import com.connectly.partnerAdmin.module.common.filter.SearchAndDateFilter;
import com.connectly.partnerAdmin.module.product.enums.group.ManagementType;
import lombok.*;

import java.util.Set;

@AuthorityValidate
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductGroupFilter extends SearchAndDateFilter implements RoleFilter, LastDomainIdProvider {

    private Long lastDomainId;
    private ManagementType managementType;
    private Long categoryId;
    private Long brandId;
    private Yn soldOutYn;
    private Yn displayYn;
    private Long minSalePrice;
    private Long maxSalePrice;
    private Long minDiscountRate;
    private Long maxDiscountRate;

    @Setter
    private Long sellerId;

    @Setter
    private Set<Long> categoryIds;

    @Override
    public Long getId() {
        return lastDomainId;
    }

    @Override
    public boolean isNoOffsetFetch() {
        return lastDomainId != null;
    }

}
