package com.connectly.partnerAdmin.module.user.core;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.user.enums.SocialLoginType;
import com.connectly.partnerAdmin.module.user.enums.UserGradeEnum;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseUserContext implements UserContext {

    private long userId;
    private SocialLoginType socialLoginType;
    private String name;
    private String email;
    private String phoneNumber;
    private UserGradeEnum userGrade;
    private Yn deleteYn;

    @QueryProjection
    public BaseUserContext(long userId, SocialLoginType socialLoginType, String name, String email, String phoneNumber, UserGradeEnum userGrade, Yn deleteYn) {
        this.userId = userId;
        this.socialLoginType = socialLoginType;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userGrade = userGrade;
        this.deleteYn = deleteYn;
    }

}
