package com.ryuqq.setof.adapter.in.rest.common.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 인증된 사용자의 userId를 Controller 파라미터로 주입하는 어노테이션.
 *
 * <p>SecurityContext에서 Authentication을 추출하여 userId(Long)로 변환합니다.
 *
 * <p>인증 정보가 없으면 required=true일 때 예외를 발생시킵니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticatedUserId {

    boolean required() default true;
}
