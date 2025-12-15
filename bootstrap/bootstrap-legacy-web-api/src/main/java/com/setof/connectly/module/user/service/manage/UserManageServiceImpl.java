package com.setof.connectly.module.user.service.manage;

import com.setof.connectly.auth.token.AuthTokenProvider;
import com.setof.connectly.module.exception.user.InvalidPasswordException;
import com.setof.connectly.module.exception.user.InvalidWithdrawalException;
import com.setof.connectly.module.exception.user.JoinedUserException;
import com.setof.connectly.module.exception.user.WithdrawalUserException;
import com.setof.connectly.module.user.dto.TokenDto;
import com.setof.connectly.module.user.dto.UserDto;
import com.setof.connectly.module.user.dto.account.WithdrawalDto;
import com.setof.connectly.module.user.dto.join.CreateUser;
import com.setof.connectly.module.user.dto.join.IsJoinedUser;
import com.setof.connectly.module.user.dto.join.JoinedUser;
import com.setof.connectly.module.user.dto.join.LoginUser;
import com.setof.connectly.module.user.entity.Users;
import com.setof.connectly.module.user.enums.UserGradeEnum;
import com.setof.connectly.module.user.mapper.UserMapper;
import com.setof.connectly.module.user.service.fetch.UserFindService;
import com.setof.connectly.module.user.service.query.UserQueryService;
import com.setof.connectly.module.user.service.query.UserRedisQueryService;
import com.setof.connectly.module.utils.CookieUtils;
import com.setof.connectly.module.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserManageServiceImpl implements UserManageService {

    private final UserFindService userFindService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserQueryService userQueryService;
    private final AuthTokenProvider tokenProvider;
    private final UserRedisQueryService userRedisQueryService;

    @Value(value = "${front.web-domain}")
    private String frontDomainUrl;

    private static final String ACCESS_TOKEN = "token";
    private static final String REFRESH_TOKEN = "refresh_token";

    @Override
    public TokenDto joinUser(CreateUser createUser, HttpServletResponse response) {
        validateUserNotJoined(createUser.getPhoneNumber());

        String encodedPassword = passwordEncoder.encode(createUser.getPasswordHash());
        createUser.setPasswordHash(encodedPassword);
        Users users = userMapper.toEntity(createUser);

        Users savedUser = userQueryService.saveUser(users);

        TokenDto token =
                tokenProvider.createToken(
                        String.valueOf(savedUser.getId()), UserGradeEnum.NORMAL_GRADE);
        tokenProvider.createRedisRefreshTokenAndSave(
                String.valueOf(savedUser.getId()),
                UserGradeEnum.NORMAL_GRADE,
                token.getRefreshToken());

        // 쿠키에 토큰 설정 (cookie.txt 스펙 준수)
        CookieUtils.setCookie(response, ACCESS_TOKEN, token.getAccessToken(), 3600);
        CookieUtils.setCookie(response, REFRESH_TOKEN, token.getRefreshToken(), 604800); // 7일

        return token;
    }

    @Override
    public TokenDto loginUser(LoginUser loginUser, HttpServletResponse response) {
        Users users = userFindService.fetchUserEntity(loginUser.getPhoneNumber());
        if (users.getWithdrawalYn().isYes()) throw new WithdrawalUserException();
        if (!passwordEncoder.matches(loginUser.getPasswordHash(), users.getPasswordHash()))
            throw new InvalidPasswordException();
        if (!users.isEmailUser()) throw new JoinedUserException();

        TokenDto token =
                tokenProvider.createToken(
                        String.valueOf(users.getId()), UserGradeEnum.NORMAL_GRADE);
        tokenProvider.createRedisRefreshTokenAndSave(
                String.valueOf(users.getId()), UserGradeEnum.NORMAL_GRADE, token.getRefreshToken());

        // 쿠키에 토큰 설정 (cookie.txt 스펙 준수)
        CookieUtils.setCookie(response, ACCESS_TOKEN, token.getAccessToken(), 3600);
        CookieUtils.setCookie(response, REFRESH_TOKEN, token.getRefreshToken(), 604800); // 7일

        return token;
    }

    @Override
    public UserDto resetPassword(LoginUser loginUser) {
        Users users = userFindService.fetchUserEntity(loginUser.getPhoneNumber());
        String encodedPassword = passwordEncoder.encode(loginUser.getPasswordHash());
        users.setPasswordHash(encodedPassword);

        return userMapper.toDto(users);
    }

    @Override
    public Users withdrawal(WithdrawalDto withdrawal, HttpServletResponse response) {
        if (withdrawal.getAgreementYn().isNo()) throw new InvalidWithdrawalException();

        long userId = SecurityUtils.currentUserId();
        Users users = userFindService.fetchUserEntity(userId);
        users.withdrawal(withdrawal.getReason());
        userRedisQueryService.deleteUser(userId, users.getPhoneNumber());

        CookieUtils.deleteCookie(response, ACCESS_TOKEN);
        CookieUtils.deleteCookie(response, REFRESH_TOKEN);

        return users;
    }

    private void validateUserNotJoined(String phoneNumber) {
        JoinedUser joinedUser = userFindService.fetchJoinedUser(new IsJoinedUser(phoneNumber));
        if (joinedUser.isJoined()) {
            throw new JoinedUserException();
        }
    }
}
