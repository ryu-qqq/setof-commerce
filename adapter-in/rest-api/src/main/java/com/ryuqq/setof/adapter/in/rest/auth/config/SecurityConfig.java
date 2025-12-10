package com.ryuqq.setof.adapter.in.rest.auth.config;

import com.ryuqq.setof.adapter.in.rest.auth.component.MdcContextHolder;
import com.ryuqq.setof.adapter.in.rest.auth.component.SecurityContextAuthenticator;
import com.ryuqq.setof.adapter.in.rest.auth.component.TokenCookieWriter;
import com.ryuqq.setof.adapter.in.rest.auth.filter.JwtAuthenticationFilter;
import com.ryuqq.setof.adapter.in.rest.auth.handler.AuthenticationErrorHandler;
import com.ryuqq.setof.adapter.in.rest.auth.handler.OAuth2FailureHandler;
import com.ryuqq.setof.adapter.in.rest.auth.handler.OAuth2SuccessHandler;
import com.ryuqq.setof.adapter.in.rest.auth.repository.OAuth2CookieAuthorizationRequestRepository;
import com.ryuqq.setof.application.auth.port.out.client.TokenProviderPort;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Security Configuration
 *
 * <p>Spring Security + OAuth2 + JWT 설정
 *
 * <p>인증 방식:
 *
 * <ul>
 *   <li>Local 로그인: Controller → UseCase → JWT 발급
 *   <li>카카오 로그인: Spring Security OAuth2 → SuccessHandler → UseCase → JWT 발급
 *   <li>인증 확인: JwtAuthenticationFilter → SecurityContext
 * </ul>
 *
 * <p>에러 처리:
 *
 * <ul>
 *   <li>401 Unauthorized: AuthenticationErrorHandler (인증 실패)
 *   <li>403 Forbidden: AuthenticationErrorHandler (인가 실패)
 *   <li>RFC 7807 ProblemDetail 형식으로 응답
 * </ul>
 *
 * <p>설정은 {@link SecurityProperties}를 통해 외부에서 관리
 *
 * @author development-team
 * @since 1.0.0
 * @see SecurityProperties
 * @see AuthenticationErrorHandler
 */
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

    private final TokenProviderPort tokenProviderPort;
    private final TokenCookieWriter tokenCookieWriter;
    private final SecurityContextAuthenticator securityContextAuthenticator;
    private final MdcContextHolder mdcContextHolder;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final AuthenticationErrorHandler authenticationErrorHandler;
    private final OAuth2CookieAuthorizationRequestRepository authorizationRequestRepository;
    private final SecurityProperties securityProperties;

    public SecurityConfig(
            TokenProviderPort tokenProviderPort,
            TokenCookieWriter tokenCookieWriter,
            SecurityContextAuthenticator securityContextAuthenticator,
            MdcContextHolder mdcContextHolder,
            OAuth2SuccessHandler oAuth2SuccessHandler,
            OAuth2FailureHandler oAuth2FailureHandler,
            AuthenticationErrorHandler authenticationErrorHandler,
            OAuth2CookieAuthorizationRequestRepository authorizationRequestRepository,
            SecurityProperties securityProperties) {
        this.tokenProviderPort = tokenProviderPort;
        this.tokenCookieWriter = tokenCookieWriter;
        this.securityContextAuthenticator = securityContextAuthenticator;
        this.mdcContextHolder = mdcContextHolder;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.oAuth2FailureHandler = oAuth2FailureHandler;
        this.authenticationErrorHandler = authenticationErrorHandler;
        this.authorizationRequestRepository = authorizationRequestRepository;
        this.securityProperties = securityProperties;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화 (JWT 사용)
                .csrf(AbstractHttpConfigurer::disable)

                // 세션 사용 안함 (Stateless)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // CORS 설정
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 인가 설정
                .authorizeHttpRequests(this::configurePublicEndpoints)

                // OAuth2 로그인 설정 (카카오)
                .oauth2Login(
                        oauth2 ->
                                oauth2.authorizationEndpoint(
                                                authorization ->
                                                        authorization
                                                                .authorizationRequestRepository(
                                                                        authorizationRequestRepository))
                                        .successHandler(oAuth2SuccessHandler)
                                        .failureHandler(oAuth2FailureHandler))

                // 에러 핸들러 설정 (401/403 응답을 RFC 7807 형식으로)
                .exceptionHandling(
                        exception ->
                                exception
                                        .authenticationEntryPoint(authenticationErrorHandler)
                                        .accessDeniedHandler(authenticationErrorHandler))

                // JWT 필터 추가
                .addFilterBefore(
                        jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 공개 엔드포인트 설정
     *
     * <p>SecurityProperties에서 정의된 공개 엔드포인트를 등록
     *
     * @param auth AuthorizeHttpRequestsConfigurer
     */
    private void configurePublicEndpoints(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
                    auth) {

        for (SecurityProperties.PublicEndpoint endpoint : securityProperties.getPublicEndpoints()) {
            if (endpoint.hasMethod()) {
                HttpMethod httpMethod =
                        HttpMethod.valueOf(endpoint.getMethod().toUpperCase(java.util.Locale.ROOT));
                auth.requestMatchers(httpMethod, endpoint.getPattern()).permitAll();
            } else {
                auth.requestMatchers(endpoint.getPattern()).permitAll();
            }
        }

        // 그 외 모든 요청은 인증 필요
        auth.anyRequest().authenticated();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(
                tokenProviderPort,
                tokenCookieWriter,
                securityContextAuthenticator,
                mdcContextHolder);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        SecurityProperties.CorsProperties corsProps = securityProperties.getCors();

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsProps.getAllowedOrigins());
        configuration.setAllowedMethods(corsProps.getAllowedMethods());
        configuration.setAllowedHeaders(corsProps.getAllowedHeaders());
        configuration.setExposedHeaders(corsProps.getExposedHeaders());
        configuration.setAllowCredentials(corsProps.isAllowCredentials());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
