package com.setof.connectly.module.user.dto.mypage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.order.dto.OrderCountDto;
import com.setof.connectly.module.user.enums.SocialLoginType;
import com.setof.connectly.module.user.enums.UserGradeEnum;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class MyPageResponse {

    private String name;
    private String phoneNumber;
    private String email;
    private SocialLoginType socialLoginType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationDate;

    private UserGradeEnum userGrade;
    private double currentMileage;
    private List<OrderCountDto> orderCounts;

    @QueryProjection
    public MyPageResponse(
            String name,
            String phoneNumber,
            String email,
            SocialLoginType socialLoginType,
            LocalDateTime registrationDate,
            UserGradeEnum userGrade,
            double currentMileage) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.socialLoginType = socialLoginType;
        this.registrationDate = registrationDate;
        this.userGrade = userGrade;
        this.currentMileage = currentMileage;
    }

    public void setOrderCounts(List<OrderCountDto> orderCounts) {
        this.orderCounts = orderCounts;
    }
}
