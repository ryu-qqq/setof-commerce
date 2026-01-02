package com.connectly.partnerAdmin.module.common.filter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class AbstractRoleFilter implements RoleFilter{

    protected Long sellerId;

    @Override
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

}
