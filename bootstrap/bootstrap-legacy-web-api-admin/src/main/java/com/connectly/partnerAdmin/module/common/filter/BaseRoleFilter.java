package com.connectly.partnerAdmin.module.common.filter;

import com.connectly.partnerAdmin.auth.validator.AuthorityValidate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AuthorityValidate
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BaseRoleFilter implements RoleFilter {
    private Long sellerId;

    @Override
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }
}
