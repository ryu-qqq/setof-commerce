package com.setof.connectly.module.user.mapper;

import com.setof.connectly.auth.dto.OAuth2UserInfo;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.order.dto.OrderCountDto;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.order.mapper.OrderMapper;
import com.setof.connectly.module.user.dto.UserDto;
import com.setof.connectly.module.user.dto.join.CreateUser;
import com.setof.connectly.module.user.dto.mypage.MyPageResponse;
import com.setof.connectly.module.user.entity.Users;
import com.setof.connectly.module.user.enums.Gender;
import com.setof.connectly.module.user.enums.SocialLoginType;
import com.setof.connectly.module.user.enums.UserGradeEnum;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {
    private final OrderMapper orderMapper;
    private static final long DEFAULT_USER_GRATE = 1L;

    @Override
    public Users toEntity(OAuth2UserInfo oAuth2UserInfo) {
        return Users.builder()
                .socialPkId(oAuth2UserInfo.getId())
                .userGradeId(DEFAULT_USER_GRATE)
                .socialLoginType(oAuth2UserInfo.getSocialLoginType())
                .phoneNumber(oAuth2UserInfo.getPhoneNumber())
                .email(oAuth2UserInfo.getEmail())
                .name(oAuth2UserInfo.getName())
                .dateOfBirth(oAuth2UserInfo.getDateOfBirth())
                .gender(oAuth2UserInfo.getGender())
                .privacyConsent(Yn.Y)
                .serviceTermsConsent(Yn.Y)
                .adConsent(Yn.Y)
                .withdrawalYn(Yn.N)
                .build();
    }

    @Override
    public Users toEntity(CreateUser createUser) {
        return Users.builder()
                .userGradeId(DEFAULT_USER_GRATE)
                .socialLoginType(SocialLoginType.none)
                .name(createUser.getName())
                .phoneNumber(createUser.getPhoneNumber())
                .passwordHash(createUser.getPasswordHash())
                .gender(Gender.N)
                .privacyConsent(createUser.getPrivacyConsent())
                .serviceTermsConsent(createUser.getServiceTermsConsent())
                .adConsent(createUser.getAdConsent())
                .withdrawalYn(Yn.N)
                .build();
    }

    @Override
    public MyPageResponse toMyPageResponse(
            MyPageResponse myPageResponse, Map<OrderStatus, Long> orderStatusLongMap) {
        List<OrderCountDto> orderCounts = orderMapper.setOrderCount(orderStatusLongMap);
        myPageResponse.setOrderCounts(orderCounts);
        return myPageResponse;
    }

    @Override
    public UserDto toDto(Users users) {
        return UserDto.builder()
                .userId(users.getId())
                .phoneNumber(users.getPhoneNumber())
                .name(users.getName())
                .socialLoginType(users.getSocialLoginType())
                .userGrade(UserGradeEnum.NORMAL_GRADE)
                .currentMileage(0)
                .build();
    }
}
