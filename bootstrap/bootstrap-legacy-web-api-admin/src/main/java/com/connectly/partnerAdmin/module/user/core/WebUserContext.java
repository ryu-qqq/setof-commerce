package com.connectly.partnerAdmin.module.user.core;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.user.enums.SocialLoginType;
import com.connectly.partnerAdmin.module.user.enums.UserGradeEnum;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebUserContext extends BaseUserContext{
    private String socialPkId;
    private Money currentMileage;
    private LocalDateTime joinedDate;

    @QueryProjection
    public WebUserContext(long userId, SocialLoginType socialLoginType, String name, String email, String phoneNumber, UserGradeEnum userGrade, Yn deleteYn, String socialPkId, BigDecimal currentMileage, LocalDateTime joinedDate) {
        super(userId, socialLoginType, name, email, phoneNumber, userGrade, deleteYn);
        this.socialPkId = socialPkId;
        this.currentMileage = Money.wons(currentMileage);
        this.joinedDate = joinedDate;
    }

}

