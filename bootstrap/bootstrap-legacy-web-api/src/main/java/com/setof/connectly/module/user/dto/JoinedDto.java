package com.setof.connectly.module.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.auth.dto.OAuth2UserInfo;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.user.entity.Users;
import com.setof.connectly.module.user.enums.SocialLoginType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinedDto {

    private String name;
    private long userId;
    private SocialLoginType socialLoginType;
    private String phoneNumber;
    private String socialPkId;
    private double currentMileage;
    private LocalDateTime joinedDate;
    private Yn deleteYn;

    @Builder
    @QueryProjection
    public JoinedDto(
            String name,
            long userId,
            SocialLoginType socialLoginType,
            String phoneNumber,
            String socialPkId,
            double currentMileage,
            LocalDateTime joinedDate,
            Yn deleteYn) {
        this.name = name;
        this.userId = userId;
        this.socialLoginType = socialLoginType;
        this.phoneNumber = phoneNumber;
        this.socialPkId = socialPkId;
        this.currentMileage = currentMileage;
        this.joinedDate = joinedDate;
        this.deleteYn = deleteYn;
    }

    public JoinedDto(Users users) {
        this.userId = users.getId();
        this.socialLoginType = users.getSocialLoginType();
        this.phoneNumber = users.getPhoneNumber();
        this.socialPkId = users.getSocialPkId();
    }

    @JsonIgnore
    public boolean isEmailUser() {
        return this.socialLoginType.equals(SocialLoginType.none);
    }

    public void integrationSocial(OAuth2UserInfo oAuth2User) {
        socialLoginType = oAuth2User.getSocialLoginType();
        socialPkId = oAuth2User.getId();
    }

    @JsonIgnore
    public boolean isWithdrawalUser() {
        return deleteYn.isYes();
    }
}
