package com.connectly.partnerAdmin.module.user.dto;

import com.connectly.partnerAdmin.module.user.enums.SocialLoginType;
import com.connectly.partnerAdmin.module.user.enums.UserGradeEnum;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class UserDto {
    private long userId;
    private SocialLoginType socialLoginType;
    private String phoneNumber;
    private UserGradeEnum userGrade;
    private String name;
    private BigDecimal currentMileage;
    private boolean authenticated;
    private LocalDateTime joinedDate;

    @Builder
    @QueryProjection
    public UserDto(long userId, SocialLoginType socialLoginType, String phoneNumber, String name, UserGradeEnum userGrade, BigDecimal currentMileage, LocalDateTime joinedDate) {
        this.userId = userId;
        this.socialLoginType = socialLoginType;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.userGrade = userGrade;
        this.currentMileage = currentMileage;
        this.authenticated = StringUtils.hasText(phoneNumber);
        this.joinedDate = joinedDate;
    }

    public void setCurrentMileage(BigDecimal accumulateMileage) {
        if (this.currentMileage == null) {
            this.currentMileage = BigDecimal.ZERO;
        }
        this.currentMileage = this.currentMileage.add(accumulateMileage);
    }
}
