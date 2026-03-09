package com.ryuqq.setof.adapter.in.rest.v1.auth.error;

import com.ryuqq.setof.adapter.in.rest.common.mapper.ErrorMapper;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.net.URI;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * AuthErrorMapper - 인증/회원 도메인 예외를 HTTP 응답으로 변환.
 *
 * <p>API-ERR-001: 도메인별 ErrorMapper를 구현하여 DomainException을 HTTP 응답으로 매핑.
 *
 * <p>에러 코드 prefix 기반 매칭 (MBR-).
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class AuthErrorMapper implements ErrorMapper {

    private static final String ERROR_TYPE_PREFIX = "/errors/auth";

    @Override
    public boolean supports(DomainException ex) {
        String code = ex.code();
        return code != null && code.startsWith("MBR-");
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        HttpStatus status = HttpStatus.valueOf(ex.httpStatus());
        return new MappedError(
                status,
                "Auth Error",
                ex.getMessage(),
                URI.create(ERROR_TYPE_PREFIX + "/" + ex.code().toLowerCase()));
    }
}
