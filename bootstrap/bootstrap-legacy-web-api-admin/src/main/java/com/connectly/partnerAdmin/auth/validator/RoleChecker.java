package com.connectly.partnerAdmin.auth.validator;

import com.connectly.partnerAdmin.auth.enums.RoleType;

public interface RoleChecker {
    boolean checkRoleType(RoleType roleType, RoleType confirmRoleType);
}
