package com.connectly.partnerAdmin.module.qna.dto.fetch;

import com.connectly.partnerAdmin.module.user.enums.Gender;
import com.connectly.partnerAdmin.module.user.enums.UserType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class UserInfoQnaDto {

    private UserType userType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long userId;

    private String userName;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String phoneNumber;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Gender gender;


    @QueryProjection
    public UserInfoQnaDto(Long userId, String userName, String phoneNumber, String email, Gender gender) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        setUserType();
    }


    private void setUserType() {
        if(userId != 1L) userType = UserType.MEMBERS;
        else userType = UserType.GUEST;
    }

}
