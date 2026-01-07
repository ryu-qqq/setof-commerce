package com.setof.connectly.auth.handler;

import com.setof.connectly.auth.dto.OAuth2UserInfo;
import com.setof.connectly.auth.repository.CustomAuthorizationRequestRepository;
import com.setof.connectly.auth.repository.CustomAuthorizedClientRepository;
import com.setof.connectly.auth.token.AuthTokenProvider;
import com.setof.connectly.module.exception.user.DuplicatedUserException;
import com.setof.connectly.module.exception.user.UnauthorizedException;
import com.setof.connectly.module.exception.user.WithdrawalUserException;
import com.setof.connectly.module.user.dto.JoinedDto;
import com.setof.connectly.module.user.dto.TokenDto;
import com.setof.connectly.module.user.dto.join.JoinedUser;
import com.setof.connectly.module.user.entity.Users;
import com.setof.connectly.module.user.mapper.UserMapper;
import com.setof.connectly.module.user.service.fetch.UserFindService;
import com.setof.connectly.module.user.service.query.UserQueryService;
import com.setof.connectly.module.utils.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Transactional
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value(value = "${front.web-domain}")
    private String frontDomainUrl;

    @Value(value = "${front.stage-domain:}")
    private String stageDomainUrl;

    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    public static final String REFERER_URI_PARAM_COOKIE_NAME = "referer";
    public static final String USER_INTEGRATION_PARAM_COOKIE_NAME = "integration";
    public static final String TOKEN = "token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String JOINED = "joined";
    public static final String REFERER = "&referer=";
    public static final String EXISTING_SOCIAL = "&existingSocial=true&name=";
    public static final String TRUE = "true";
    public static final String AUTH_ERROR_MSG =
            "Authentication 객체의 principal을 OAuth2UserInfo 로 형변환 할 수 없습니다.";

    private final CustomAuthorizedClientRepository customAuthorizedClientRepository;
    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;
    private final UserQueryService userQueryService;
    private final UserFindService userFindService;

    private final AuthTokenProvider authTokenProvider;
    private final UserMapper userMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authentication)
            throws IOException, ServletException {
        this.handle(request, response, authentication);
    }

    @Override
    protected void handle(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        if (response.isCommitted()) {
            super.logger.debug(
                    "Response has already been committed. Unable to redirect to " + targetUrl);
        } else {
            clearAuthenticationAttributes(request, response);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }

    protected void clearAuthenticationAttributes(
            HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        customAuthorizedClientRepository.removeAuthorizationRequestCookies(response);
        customAuthorizationRequestRepository.removeAuthorizationRequestCookies(response);
    }

    private OAuth2UserInfo isOAuth2UserInfoInstance(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof OAuth2UserInfo) return (OAuth2UserInfo) principal;
        else throw new ClassCastException(AUTH_ERROR_MSG);
    }

    private String determineRedirectUrl(
            String targetUrl, String token, String refreshToken, boolean isJoined) {
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam(TOKEN, token)
                .queryParam(REFRESH_TOKEN, refreshToken)
                .queryParam(JOINED, isJoined)
                .build()
                .toUriString();
    }

    protected String determineTargetUrl(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {

        OAuth2UserInfo oAuth2User = isOAuth2UserInfoInstance(authentication);

        JoinedUser joinedMember = isJoinedMember(request, oAuth2User);
        JoinedDto joinedUser = joinedMember.getJoinedUser();
        long userId = joinedMember.getJoinedUser().getUserId();

        TokenDto token =
                authTokenProvider.createToken(String.valueOf(userId), oAuth2User.getUserGrade());
        authTokenProvider.createRedisRefreshTokenAndSave(
                String.valueOf(userId), oAuth2User.getUserGrade(), token.getRefreshToken());

        // 쿠키에 토큰 설정 (cookie.txt 스펙 준수)
        CookieUtils.setCookie(response, TOKEN, token.getAccessToken(), 3600);
        CookieUtils.setCookie(response, REFRESH_TOKEN, token.getRefreshToken(), 604800); // 7일

        Optional<Cookie> redirectCookieOpt =
                CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME);
        Optional<Cookie> refererCookieOpt =
                CookieUtils.getCookie(request, REFERER_URI_PARAM_COOKIE_NAME);

        String targetUrl =
                redirectCookieOpt.isPresent() ? redirectCookieOpt.get().getValue() : frontDomainUrl;
        String refererUrl = refererCookieOpt.isPresent() ? refererCookieOpt.get().getValue() : "";

        // 쿼리 파라미터 없이 joined만 전달
        String redirectUrl =
                UriComponentsBuilder.fromUriString(targetUrl)
                        .queryParam(JOINED, joinedMember.isJoined())
                        .build()
                        .toUriString();

        if (StringUtils.hasText(redirectUrl) && !isAuthorizedRedirectUri(redirectUrl)) {
            throw new UnauthorizedException();
        }

        if (StringUtils.hasText(refererUrl) && !isAuthorizedRedirectUri(redirectUrl)) {
            throw new UnauthorizedException();
        } else if (StringUtils.hasText(refererUrl)) {
            redirectUrl += REFERER + refererUrl;
        } else if (oAuth2User.getSocialLoginType() != joinedUser.getSocialLoginType()
                && joinedUser.isEmailUser()) {
            String encodedName = null;
            try {
                encodedName =
                        URLEncoder.encode(joinedUser.getName(), StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            redirectUrl += EXISTING_SOCIAL + encodedName;
        }

        return StringUtils.hasText(redirectUrl) ? redirectUrl : getDefaultTargetUrl();
    }

    private JoinedUser isJoinedMember(HttpServletRequest request, OAuth2UserInfo oAuth2User) {
        try {
            Optional<JoinedDto> joinedUser =
                    userFindService.isJoinedUser(oAuth2User.getPhoneNumber());
            if (joinedUser.isPresent()) {
                if (joinedUser.get().isWithdrawalUser()) throw new WithdrawalUserException();
                boolean integrationMember = isIntegrationMember(request);
                if (integrationMember) {
                    JoinedDto joinedDto =
                            userQueryService.integrationUser(joinedUser.get(), oAuth2User);
                    return new JoinedUser(false, joinedDto);
                }
                JoinedDto joinedDto = joinedUser.get();
                return new JoinedUser(true, joinedDto);
            }

            Users users = userMapper.toEntity(oAuth2User);
            Users savedUser = userQueryService.saveUser(users);

            return new JoinedUser(false, savedUser);

        } catch (DataIntegrityViolationException e) {
            Optional<JoinedDto> joinedUserByEmail =
                    userFindService.isJoinedUserByEmail(oAuth2User.getEmail());
            if (joinedUserByEmail.isPresent()) return new JoinedUser(true, joinedUserByEmail.get());
            throw new DuplicatedUserException(oAuth2User.getEmail());
        }
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        boolean matchesFront = isMatchingDomain(clientRedirectUri, frontDomainUrl);
        boolean matchesStage =
                StringUtils.hasText(stageDomainUrl)
                        && isMatchingDomain(clientRedirectUri, stageDomainUrl);

        return matchesFront || matchesStage;
    }

    private boolean isMatchingDomain(URI clientRedirectUri, String allowedDomainUrl) {
        if (!StringUtils.hasText(allowedDomainUrl)) {
            return false;
        }

        URI allowedDomainUri = URI.create(allowedDomainUrl);

        boolean isHostSame =
                allowedDomainUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost());
        boolean isPortSame = allowedDomainUri.getPort() == clientRedirectUri.getPort();

        return isHostSame && isPortSame;
    }

    private boolean isIntegrationMember(HttpServletRequest request) {
        Optional<Cookie> integrationCookieOpt =
                CookieUtils.getCookie(request, USER_INTEGRATION_PARAM_COOKIE_NAME);
        return integrationCookieOpt.map(cookie -> cookie.getValue().equals(TRUE)).orElse(false);
    }
}
