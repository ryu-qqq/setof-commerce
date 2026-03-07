package com.ryuqq.setof.adapter.in.rest.common.auth;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * {@link AuthenticatedUserId} 어노테이션이 붙은 파라미터에 userId를 주입하는 Resolver.
 *
 * <p>SecurityContext의 Authentication에서 userId를 추출합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class AuthenticatedUserIdResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedUserId.class)
                && Long.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticatedUserId annotation =
                parameter.getParameterAnnotation(AuthenticatedUserId.class);
        boolean required = annotation != null && annotation.required();

        if (authentication == null || authentication.getPrincipal() == null) {
            if (required) {
                throw new IllegalStateException("인증 정보가 없습니다.");
            }
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails ud) {
            try {
                return Long.parseLong(ud.getUsername());
            } catch (NumberFormatException e) {
                if (required) {
                    throw new IllegalStateException("유효하지 않은 사용자 인증 정보입니다.");
                }
                return null;
            }
        }

        if (principal instanceof String name) {
            try {
                return Long.parseLong(name);
            } catch (NumberFormatException e) {
                if (required) {
                    throw new IllegalStateException("유효하지 않은 사용자 인증 정보입니다.");
                }
                return null;
            }
        }

        if (required) {
            throw new IllegalStateException("인증 정보에서 사용자 ID를 추출할 수 없습니다.");
        }
        return null;
    }
}
