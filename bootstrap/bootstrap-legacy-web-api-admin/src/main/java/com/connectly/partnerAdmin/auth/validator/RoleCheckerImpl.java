package com.connectly.partnerAdmin.auth.validator;

import com.connectly.partnerAdmin.auth.enums.RoleType;
import org.springframework.stereotype.Component;

@Component
public class RoleCheckerImpl implements RoleChecker {

    @Override
    public boolean checkRoleType(RoleType roleType, RoleType confirmRoleType) {
        return roleType.equals(confirmRoleType);
    }
}

