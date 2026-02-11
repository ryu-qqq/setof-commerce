package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.error;

import com.ryuqq.setof.adapter.in.rest.admin.common.mapper.ErrorMapper;
import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.seller.exception.SellerException;
import java.net.URI;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * SellerAdminErrorMapper - 셀러 도메인 예외를 HTTP 응답으로 변환.
 *
 * <p>API-ERR-001: 도메인별 ErrorMapper를 구현하여 DomainException을 HTTP 응답으로 매핑.
 *
 * <p>rest-api-admin 모듈 전용 (rest-api SellerErrorMapper와 별도 구현).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerAdminErrorMapper implements ErrorMapper {

    private static final String ERROR_TYPE_PREFIX = "/errors/seller";

    @Override
    public boolean supports(DomainException ex) {
        return ex instanceof SellerException;
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        HttpStatus status = HttpStatus.valueOf(ex.httpStatus());
        return new MappedError(
                status,
                "Seller Error",
                ex.getMessage(),
                URI.create(ERROR_TYPE_PREFIX + "/" + ex.code().toLowerCase()));
    }
}
