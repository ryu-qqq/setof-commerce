package com.setof.connectly.module.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.user.enums.SocialLoginType;
import com.setof.connectly.module.user.enums.UserGradeEnum;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {
    private long userId;
    private SocialLoginType socialLoginType;
    private String phoneNumber;
    private UserGradeEnum userGrade;
    private String name;
    private double currentMileage;
    private boolean authenticated;
    private LocalDateTime joinedDate;

    @Builder
    @QueryProjection
    public UserDto(
            long userId,
            SocialLoginType socialLoginType,
            String phoneNumber,
            String name,
            UserGradeEnum userGrade,
            double currentMileage,
            LocalDateTime joinedDate) {
        this.userId = userId;
        this.socialLoginType = socialLoginType;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.userGrade = userGrade;
        this.currentMileage = currentMileage;
        this.authenticated = StringUtils.hasText(phoneNumber);
        this.joinedDate = joinedDate;
    }

    public JoinedDto joinedDto() {
        return new JoinedDto(
                name, userId, socialLoginType, phoneNumber, "", currentMileage, joinedDate, Yn.N);
    }

    public void setCurrentMileage(double accumulateMileage) {
        this.currentMileage = this.currentMileage + accumulateMileage;
    }
}
