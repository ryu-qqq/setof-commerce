package com.connectly.partnerAdmin.module.external.dto.user;

import com.connectly.partnerAdmin.module.external.core.ExMallUser;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ExMallUserInfo implements ExMallUser {

    private String userName;
    private String getPhoneNumber;

    private SiteName siteName;

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getPhoneNumber() {
        return getPhoneNumber;
    }

    @Override
    public long getSiteId() {
        return siteName.getSiteId();
    }

    @Override
    public SiteName getSiteName() {
        return siteName;
    }
}
