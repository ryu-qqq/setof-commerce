package com.ryuqq.setof.adapter.in.rest.admin.v1.brand.error;

import com.ryuqq.setof.adapter.in.rest.admin.common.mapper.ErrorMapper;
import com.ryuqq.setof.domain.brand.exception.BrandException;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.net.URI;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * BrandAdminErrorMapper - 브랜드 도메인 예외를 HTTP 응답으로 변환.
 *
 * <p>API-ERR-001: 도메인별 ErrorMapper를 구현하여 DomainException을 HTTP 응답으로 매핑.
 *
 * <p>rest-api-admin 모듈 전용 (rest-api BrandErrorMapper와 별도 구현).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class BrandAdminErrorMapper implements ErrorMapper {

    private static final String ERROR_TYPE_PREFIX = "/errors/brand";

    @Override
    public boolean supports(DomainException ex) {
        return ex instanceof BrandException;
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        HttpStatus status = HttpStatus.valueOf(ex.httpStatus());
        return new MappedError(
                status,
                "Brand Error",
                ex.getMessage(),
                URI.create(ERROR_TYPE_PREFIX + "/" + ex.code().toLowerCase()));
    }
}
