package com.connectly.partnerAdmin.auth.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthMenuId implements Serializable {
    private long authGroupId;
    private long menuId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthMenuId that = (AuthMenuId) o;
        return authGroupId == that.authGroupId && menuId == that.menuId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(authGroupId, menuId);
    }
}