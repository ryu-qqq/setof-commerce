package com.connectly.partnerAdmin.module.user.core;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.user.enums.SocialLoginType;
import com.connectly.partnerAdmin.module.user.enums.UserGradeEnum;

public interface UserContext {

    long getUserId();
    String getName();
    String getEmail();
    String getPhoneNumber();
    SocialLoginType getSocialLoginType();
    UserGradeEnum getUserGrade();
    Yn getDeleteYn();
}
